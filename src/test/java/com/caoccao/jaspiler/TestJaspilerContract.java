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

package com.caoccao.jaspiler;

import com.caoccao.jaspiler.contexts.JaspilerParseContext;
import com.caoccao.jaspiler.visiters.BaseJaspilerParseScanner;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.AssignmentTree;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJaspilerContract extends BaseTestSuite {
    @Test
    public void testIgnore() throws IOException {
        class TestScanner extends BaseJaspilerParseScanner<TestScanner> {
            @Override
            public TestScanner visitAnnotation(AnnotationTree node, JaspilerParseContext jaspilerContext) {
                assertEquals(1, node.getArguments().size());
                var argument = (AssignmentTree) node.getArguments().get(0);
                assertEquals("condition", argument.getVariable().toString());
                assertEquals("\"a\"", argument.getExpression().toString());
                return super.visitAnnotation(node, jaspilerContext);
            }
        }
        compiler.addJavaFileStringObject("A", """
                @JC.Ignore(condition = "a")
                class A {}""");
        compiler.parse(new TestScanner());
    }
}
