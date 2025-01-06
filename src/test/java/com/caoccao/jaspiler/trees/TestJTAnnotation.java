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
import com.caoccao.jaspiler.contexts.JaspilerTransformContext;
import com.caoccao.jaspiler.mock.MockAllInOnePublicClass;
import com.caoccao.jaspiler.visiters.BaseJaspilerTransformScanner;
import com.sun.source.tree.AnnotationTree;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJTAnnotation extends BaseTestSuite {
    protected static final String RETENTION_POLICY_RUNTIME = "@Retention(RetentionPolicy.RUNTIME)";

    @Test
    public void testIgnore() throws Exception {
        class TestTransformScanner extends BaseJaspilerTransformScanner<TestTransformScanner> {
            @Override
            public TestTransformScanner visitAnnotation(AnnotationTree node, JaspilerTransformContext jaspilerTransformContext) {
                if (node.toString().startsWith(RETENTION_POLICY_RUNTIME)) {
                    ((JTAnnotation) node).setActionIgnore();
                }
                return super.visitAnnotation(node, jaspilerTransformContext);
            }
        }
        String code = transform(new TestTransformScanner(), MockAllInOnePublicClass.class);
        assertFalse(code.contains(RETENTION_POLICY_RUNTIME));
        var texts = List.of(
                "* Copyright (c)",
                "public abstract sealed class MockAllInOnePublicClass");
        texts.forEach(text -> assertTrue(code.contains(text), text));
    }

    @Test
    public void testUpdate() throws Exception {
        class TestTransformScanner extends BaseJaspilerTransformScanner<TestTransformScanner> {
            @Override
            public TestTransformScanner visitAnnotation(AnnotationTree node, JaspilerTransformContext jaspilerTransformContext) {
                if (node.toString().startsWith(RETENTION_POLICY_RUNTIME)) {
                    var jtAnnotation = (JTAnnotation) node;
                    jtAnnotation.setAnnotationType(JTTreeFactory.createFieldAccess("X", "Y", "Z"));
                    jtAnnotation.getArguments().addAll(List.of(
                            JTTreeFactory.createFieldAccess("A1", "B1", "C1"),
                            JTTreeFactory.createFieldAccess("A2", "B2", "C2")));
                }
                return super.visitAnnotation(node, jaspilerTransformContext);
            }
        }
        String code = transform(new TestTransformScanner(), MockAllInOnePublicClass.class);
        assertTrue(code.contains("@Inherited\n" +
                "@X.Y.Z(RetentionPolicy.RUNTIME, A1.B1.C1, A2.B2.C2)\n" +
                "@Target(ElementType.ANNOTATION_TYPE)\n"));
    }
}
