/*
 * Copyright (c) 2023. caoccao.com Sam Cao
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
import com.caoccao.jaspiler.visiters.JaspilerTransformScanner;
import com.sun.source.tree.AnnotationTree;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestJTAnnotation extends BaseTestSuite {
    @Test
    public void testIgnore() throws Exception {
        String annotationString = "@Retention(RetentionPolicy.RUNTIME)";
        class TestTransformScanner extends JaspilerTransformScanner<TestTransformScanner> {
            @Override
            public TestTransformScanner visitAnnotation(AnnotationTree node, JaspilerTransformContext jaspilerTransformContext) {
                if (node.toString().startsWith(annotationString)) {
                    ((JTAnnotation) node).setActionIgnore();
                }
                return super.visitAnnotation(node, jaspilerTransformContext);
            }
        }
        String code = transform(new TestTransformScanner(), MockAllInOnePublicClass.class);
        assertFalse(code.contains(annotationString));
        var texts = List.of(
                "* Copyright (c)",
                "public class MockAllInOnePublicClass");
        texts.forEach(text -> assertTrue(code.contains(text), text));
    }

    @Test
    public void testToString() {
        var jtAnnotation = new JTAnnotation().setAnnotationType(JTTreeFactory.createFieldAccess("X", "Y", "Z"));
        jtAnnotation.getArguments().add(JTTreeFactory.createFieldAccess("A1", "B1", "C1"));
        jtAnnotation.getArguments().add(JTTreeFactory.createFieldAccess("A2", "B2", "C2"));
        assertEquals("@X.Y.Z(A1.B1.C1, A2.B2.C2)\n", jtAnnotation.toString());
    }
}
