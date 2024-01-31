/*
 * Copyright (c) 2023-2024. caoccao.com Sam Cao
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
import com.sun.source.tree.VariableTree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJTVariableDecl extends BaseTestSuite {
    @Test
    public void testUpdateName() throws Exception {
        String newVariableName = "newVariableName";
        class TestTransformScanner extends BaseJaspilerTransformScanner<TestTransformScanner> {
            @Override
            public TestTransformScanner visitVariable(VariableTree node, JaspilerTransformContext jaspilerTransformContext) {
                var jtVariableDecl = (JTVariableDecl) node;
                if ("map".equals(jtVariableDecl.getName().getValue())) {
                    jtVariableDecl.setName(new JTName(newVariableName));
                }
                return super.visitVariable(node, jaspilerTransformContext);
            }
        }
        String code = transform(new TestTransformScanner(), MockAllInOnePublicClass.class);
        assertTrue(code.contains("private Map<String, Object> newVariableName = new HashMap<>() {{"));
    }
}
