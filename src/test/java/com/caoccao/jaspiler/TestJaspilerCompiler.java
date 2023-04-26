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

import com.caoccao.jaspiler.contexts.JaspilerTransformContext;
import com.caoccao.jaspiler.mock.MockIgnorePublicClass;
import com.caoccao.jaspiler.trees.JTFieldAccess;
import com.caoccao.jaspiler.trees.JTName;
import com.caoccao.jaspiler.trees.JTPackageDecl;
import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.caoccao.jaspiler.utils.MockUtils;
import com.caoccao.jaspiler.visiters.JaspilerTransformScanner;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.PackageTree;
import com.sun.source.util.TreePathScanner;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJaspilerCompiler extends BaseLoggingObject {
    @Test
    public void testTransform() throws IOException {
        class TestScanner extends TreePathScanner<TestScanner, JaspilerTransformContext> {
            @Override
            public TestScanner visitImport(ImportTree node, JaspilerTransformContext jaspilerTransformContext) {
                return super.visitImport(node, jaspilerTransformContext);
            }

            @Override
            public TestScanner visitPackage(PackageTree node, JaspilerTransformContext jaspilerTransformContext) {
                var packageTree = (JTPackageDecl) node;
                var jtFieldAccess1 = new JTFieldAccess();
                packageTree.setPackageName(jtFieldAccess1);
                jtFieldAccess1.setIdentifier(new JTName("a2"));
                var jtFieldAccess2 = new JTFieldAccess();
                jtFieldAccess1.setExpression(jtFieldAccess2);
                jtFieldAccess2.setIdentifier(new JTName("a1"));
                return super.visitPackage(node, jaspilerTransformContext);
            }
        }
        Path sourceFilePath = MockUtils.getSourcePath(MockIgnorePublicClass.class);
        var compiler = new JaspilerCompiler();
        compiler.addJavaFileObjects(sourceFilePath);
        try (StringWriter writer = new StringWriter()) {
            compiler.transform(new JaspilerTransformScanner(), writer, JaspilerOptions.Default);
            String code = writer.toString();
            logger.debug(code);
            assertTrue(code.contains("package/* test */com./*1*/caoccao/*2*/.jaspiler.mock;"));
            assertTrue(code.contains("import com.caoccao/*1*/./*2*/jaspiler.JaspilerContract;"));
        }
        logger.debug("=====================================");
        try (StringWriter writer = new StringWriter()) {
            compiler.transform(new TestScanner(), writer, JaspilerOptions.Default);
            String code = writer.toString();
            logger.debug(code);
            assertTrue(code.contains("package a1.a2;"));
            assertTrue(code.contains("import com.caoccao/*1*/./*2*/jaspiler.JaspilerContract;"));
        }
    }
}
