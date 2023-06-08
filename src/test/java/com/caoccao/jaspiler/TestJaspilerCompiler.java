/*
 * Copyright (c) 2023-2023. caoccao.com Sam Cao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.caoccao.jaspiler;

import com.caoccao.jaspiler.contexts.BaseJaspilerContext;
import com.caoccao.jaspiler.contexts.JaspilerDocContext;
import com.caoccao.jaspiler.contexts.JaspilerTransformContext;
import com.caoccao.jaspiler.mock.MockAllInOnePublicClass;
import com.caoccao.jaspiler.trees.JTCompilationUnit;
import com.caoccao.jaspiler.trees.JTImport;
import com.caoccao.jaspiler.trees.JTPackageDecl;
import com.caoccao.jaspiler.trees.JTTreeFactory;
import com.caoccao.jaspiler.utils.SystemUtils;
import com.caoccao.jaspiler.visiters.DummyDocScanner;
import com.caoccao.jaspiler.visiters.DummyTransformScanner;
import com.caoccao.jaspiler.visiters.JaspilerDocScanner;
import com.caoccao.jaspiler.visiters.JaspilerTransformScanner;
import com.sun.source.doctree.DocTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.PackageTree;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestJaspilerCompiler extends BaseTestSuite {
    @Test
    public void testTransform() throws IOException {
        class TestDocScanner extends JaspilerDocScanner<TestDocScanner> {
            @Override
            public TestDocScanner scan(DocTree node, JaspilerDocContext jaspilerDocContext) {
                var position = jaspilerDocContext.getCompilationUnitTree().getOriginalDocPosition(node);
                return super.scan(node, jaspilerDocContext);
            }
        }
        class TestTransformScanner extends JaspilerTransformScanner<TestTransformScanner> {
            @Override
            public TestTransformScanner visitCompilationUnit(CompilationUnitTree node, JaspilerTransformContext jaspilerTransformContext) {
                var jtCompilationUnit = (JTCompilationUnit) node;
                jtCompilationUnit.getImports().add(
                        new JTImport().setQualifiedIdentifier(JTTreeFactory.createFieldAccess("i4", "i5")));
                return super.visitCompilationUnit(node, jaspilerTransformContext);
            }

            @Override
            public TestTransformScanner visitImport(ImportTree node, JaspilerTransformContext jaspilerTransformContext) {
                if (node.toString().contains("import java.util.List;")) {
                    var jtImport = (JTImport) node;
                    jtImport.setStaticImport(true);
                    jtImport.setQualifiedIdentifier(JTTreeFactory.createFieldAccess("i1", "i2", "i3"));
                }
                return super.visitImport(node, jaspilerTransformContext);
            }

            @Override
            public TestTransformScanner visitPackage(PackageTree node, JaspilerTransformContext jaspilerTransformContext) {
                var packageTree = (JTPackageDecl) node;
                packageTree.setPackageName(JTTreeFactory.createFieldAccess("a1", "a2"));
                return super.visitPackage(node, jaspilerTransformContext);
            }
        }
        {
            String code = transform(new DummyTransformScanner(), new DummyDocScanner(), MockAllInOnePublicClass.class);
            var texts = List.of(
                    "* Copyright (c)",
                    "package/* test */com./*1*/caoccao/*2*/.jaspiler.mock;",
                    "import java.util.ArrayList;");
            texts.forEach(text -> assertTrue(code.contains(text), text));
        }
        {
            String code = transform(new TestTransformScanner(), new TestDocScanner(), MockAllInOnePublicClass.class);
            var texts = List.of(
                    "* Copyright (c)",
                    "package a1.a2;",
                    "import java.util.ArrayList;",
                    "import static i1.i2.i3;",
                    "import i4.i5;");
            texts.forEach(text -> assertTrue(code.contains(text), text));
        }
    }

    @Test
    @Tag("manual")
    public void testUnsupported() {
        var dummyTransformScanner = new DummyTransformScanner();
        var dummyDocScanner = new DummyDocScanner();
        List<String> unsupportedParseFileNames = new ArrayList<>();
        List<String> unsupportedTransformFileNames = new ArrayList<>();
        var path = Path.of("");
        path = SystemUtils.WORKING_DIRECTORY.resolve("src");
        try (var stream = Files.walk(path)) {
            stream.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".java"))
                    .forEach(file -> {
                        try {
                            compiler.clearJavaFileObject();
                            compiler.addJavaFileObjects(file);
                            try (StringWriter writer = new StringWriter()) {
                                compiler.transform(
                                        dummyTransformScanner,
                                        dummyDocScanner,
                                        writer,
                                        JaspilerOptions.Default);
                            } finally {
                                compiler.getParseContexts().stream()
                                        .map(context -> (JTCompilationUnit) context.getCompilationUnitTree())
                                        .filter(compilationUnit -> compilationUnit.getUnsupportedTreeCount() > 0)
                                        .map(compilationUnit -> compilationUnit.getSourceFile().getName())
                                        .forEach(unsupportedParseFileNames::add);
                                compiler.getTransformContexts().stream()
                                        .map(BaseJaspilerContext::getCompilationUnitTree)
                                        .filter(compilationUnit -> compilationUnit.getUnsupportedTreeCount() > 0)
                                        .map(compilationUnit -> compilationUnit.getSourceFile().getName())
                                        .forEach(unsupportedTransformFileNames::add);
                            }
                        } catch (IOException e) {
                            fail(e);
                        }
                    });
        } catch (Throwable e) {
            fail(e);
        }
        assertEquals(0, unsupportedParseFileNames.size(), "There shouldn't be any errors during the parsing.");
        assertEquals(0, unsupportedTransformFileNames.size(), "There shouldn't be any errors during the transforming.");
    }
}
