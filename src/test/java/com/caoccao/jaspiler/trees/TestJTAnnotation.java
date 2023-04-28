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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJTAnnotation extends BaseTestSuite {
    @Test
    public void testToString() {
        var jtAnnotation = new JTAnnotation().setAnnotationType(JTTreeFactory.createJTFieldAccess("X", "Y", "Z"));
        jtAnnotation.getArguments().add(JTTreeFactory.createJTFieldAccess("A1", "B1", "C1"));
        jtAnnotation.getArguments().add(JTTreeFactory.createJTFieldAccess("A2", "B2", "C2"));
        assertEquals("@X.Y.Z(A1.B1.C1, A2.B2.C2)", jtAnnotation.toString());
    }
}
