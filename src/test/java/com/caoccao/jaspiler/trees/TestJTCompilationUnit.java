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
import com.caoccao.jaspiler.mock.MockPublicAnnotation;
import com.caoccao.jaspiler.utils.MockUtils;
import com.caoccao.jaspiler.visiters.BaseJaspilerTransformScanner;
import com.sun.source.tree.CompilationUnitTree;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestJTCompilationUnit extends BaseTestSuite {
    @Test
    public void testGetSourceFile() throws Exception {
        class TestTransformScanner extends BaseJaspilerTransformScanner<TestTransformScanner> {
            @Override
            public TestTransformScanner visitCompilationUnit(
                    CompilationUnitTree node,
                    JaspilerTransformContext jaspilerTransformContext) {
                var jtCompilationUnit = (JTCompilationUnit) node;
                var sourceFile = jtCompilationUnit.getSourceFile();
                assertNotNull(sourceFile);
                assertEquals(JavaFileObject.Kind.SOURCE, sourceFile.getKind());
                assertEquals(MockUtils.getSourcePath(MockPublicAnnotation.class).toString(), sourceFile.getName());
                return super.visitCompilationUnit(node, jaspilerTransformContext);
            }
        }
        String code = transform(new TestTransformScanner(), MockPublicAnnotation.class);
        assertNotNull(code);
    }
}
