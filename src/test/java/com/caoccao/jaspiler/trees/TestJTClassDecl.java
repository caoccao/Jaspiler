/*
 * Copyright (c) 2023-2025. caoccao.com Sam Cao
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

package com.caoccao.jaspiler.trees;

import com.caoccao.jaspiler.BaseTestSuite;
import com.caoccao.jaspiler.JaspilerContract;
import com.caoccao.jaspiler.contexts.JaspilerTransformContext;
import com.caoccao.jaspiler.mock.MockAllInOnePublicClass;
import com.caoccao.jaspiler.mock.MockIgnorePublicClass;
import com.caoccao.jaspiler.mock.MockPublicAnnotation;
import com.caoccao.jaspiler.visiters.BaseJaspilerTransformScanner;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJTClassDecl extends BaseTestSuite {
    @Test
    public void testIgnore() throws Exception {
        class TestIgnoreTransformScanner extends BaseJaspilerTransformScanner<TestIgnoreTransformScanner> {
            @Override
            public TestIgnoreTransformScanner visitCompilationUnit(
                    CompilationUnitTree node, JaspilerTransformContext jaspilerTransformContext) {
                var jtCompilationUnit = (JTCompilationUnit) node;
                if (jtCompilationUnit.getTypeDecls().stream()
                        .anyMatch(typeDecl -> {
                            if (typeDecl instanceof JTClassDecl jtClassDecl) {
                                return Optional.ofNullable(jtClassDecl.getModifiers())
                                        .filter(modifiers -> modifiers.getFlags().contains(Modifier.PUBLIC))
                                        .map(IJTAnnotatable::containsIgnore)
                                        .orElse(false);
                            }
                            return false;
                        })) {
                    jtCompilationUnit.setActionIgnore();
                }
                return super.visitCompilationUnit(node, jaspilerTransformContext);
            }

            @Override
            public TestIgnoreTransformScanner visitMethod(
                    MethodTree node, JaspilerTransformContext jaspilerTransformContext) {
                var jtMethodDecl = (JTMethodDecl) node;
                if (Optional.ofNullable(jtMethodDecl.getModifiers())
                        .map(IJTAnnotatable::containsIgnore)
                        .orElse(false)) {
                    jtMethodDecl.setActionIgnore();
                }
                return super.visitMethod(node, jaspilerTransformContext);
            }

            @Override
            public TestIgnoreTransformScanner visitVariable(
                    VariableTree node, JaspilerTransformContext jaspilerTransformContext) {
                var jtVariableDecl = (JTVariableDecl) node;
                if (Optional.ofNullable(jtVariableDecl.getModifiers())
                        .map(IJTAnnotatable::containsIgnore)
                        .orElse(false)) {
                    jtVariableDecl.setActionIgnore();
                }
                return super.visitVariable(node, jaspilerTransformContext);
            }
        }
        String code = transform(new TestIgnoreTransformScanner(), MockIgnorePublicClass.class);
        assertTrue(StringUtils.isEmpty(code));
        class TestNoChangeTransformScanner extends BaseJaspilerTransformScanner<TestNoChangeTransformScanner> {
            @Override
            public TestNoChangeTransformScanner visitClass(ClassTree node, JaspilerTransformContext jaspilerTransformContext) {
                ((JTClassDecl) node).setActionNoChange();
                return super.visitClass(node, jaspilerTransformContext);
            }
        }
        code = transform(new TestNoChangeTransformScanner(), MockIgnorePublicClass.class);
        assertTrue(StringUtils.isNotEmpty(code));
        assertTrue(code.contains(JaspilerContract.ANNOTATION_IGNORE));
        code = transform(new TestIgnoreTransformScanner(), MockAllInOnePublicClass.class);
        assertTrue(StringUtils.isNotEmpty(code));
        assertFalse(code.contains(JaspilerContract.ANNOTATION_IGNORE));
        assertFalse(code.contains("public int b;"));
        assertFalse(code.contains("public void close() throws Exception"));
    }

    @Test
    public void testReorderMembers() throws Exception {
        class TestTransformScanner extends BaseJaspilerTransformScanner<TestTransformScanner> {
            @Override
            public TestTransformScanner visitClass(ClassTree node, JaspilerTransformContext jaspilerTransformContext) {
                var jtClassDecl = (JTClassDecl) node;
                if (MockAllInOnePublicClass.class.getSimpleName().equals(jtClassDecl.getSimpleName().getValue())) {
                    Collections.swap(jtClassDecl.getMembers(), 1, 2);
                    jtClassDecl.setActionChange();
                }
                return super.visitClass(node, jaspilerTransformContext);
            }
        }
        String code = transform(new TestTransformScanner(), MockAllInOnePublicClass.class);
        assertTrue(code.contains("    private List<Object> list;\n" +
                "\n" +
                "    private String a;"));
    }

    @Test
    public void testUpdateAnnotationSimpleName() throws Exception {
        String newAnnotationName = "ANewAnnotationName";
        class TestTransformScanner extends BaseJaspilerTransformScanner<TestTransformScanner> {
            @Override
            public TestTransformScanner visitClass(ClassTree node, JaspilerTransformContext jaspilerTransformContext) {
                var jtClassDecl = (JTClassDecl) node;
                if (MockPublicAnnotation.class.getSimpleName().equals(jtClassDecl.getSimpleName().getValue())) {
                    jtClassDecl.setSimpleName(new JTName(newAnnotationName));
                    jtClassDecl.getModifiers().getFlags().add(Modifier.SEALED);
                    jtClassDecl.getModifiers().setActionChange();
                }
                return super.visitClass(node, jaspilerTransformContext);
            }
        }
        String code = transform(new TestTransformScanner(), MockPublicAnnotation.class);
        assertTrue(code.contains("@Documented\n" +
                "@Inherited\n" +
                "public sealed @interface ANewAnnotationName {"));
    }

    @Test
    public void testUpdateClassSimpleName() throws Exception {
        String newClassName = "ANewClassName";
        String newAnnotationName = "ANewAnnotationName";
        class TestTransformScanner extends BaseJaspilerTransformScanner<TestTransformScanner> {
            @Override
            public TestTransformScanner visitClass(ClassTree node, JaspilerTransformContext jaspilerTransformContext) {
                var jtClassDecl = (JTClassDecl) node;
                if (MockAllInOnePublicClass.class.getSimpleName().equals(jtClassDecl.getSimpleName().getValue())) {
                    jtClassDecl.setSimpleName(new JTName(newClassName));
                } else if ("MockAnnotation".equals(jtClassDecl.getSimpleName().getValue())) {
                    jtClassDecl.setSimpleName(new JTName(newAnnotationName));
                }
                return super.visitClass(node, jaspilerTransformContext);
            }
        }
        String code = transform(new TestTransformScanner(), MockAllInOnePublicClass.class);
        var texts = List.of(
                "\npublic abstract sealed class " + newClassName,
                "\n@interface " + newAnnotationName + " {\n");
        texts.forEach(text -> assertTrue(code.contains(text), text));
    }
}
