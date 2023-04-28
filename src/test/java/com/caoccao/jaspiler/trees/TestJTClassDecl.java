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
import com.caoccao.jaspiler.JaspilerOptions;
import com.caoccao.jaspiler.contexts.JaspilerTransformContext;
import com.caoccao.jaspiler.mock.MockIgnorePublicClass;
import com.caoccao.jaspiler.utils.MockUtils;
import com.sun.source.util.TreePathScanner;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJTClassDecl extends BaseTestSuite {
    @Test
    public void testIgnore() throws Exception {
        class TestTransformScanner extends TreePathScanner<TestTransformScanner, JaspilerTransformContext> {
        }
        compiler.addJavaFileObjects(MockUtils.getSourcePath(MockIgnorePublicClass.class));
        try (StringWriter writer = new StringWriter()) {
            compiler.transform(
                    new TestTransformScanner(),
                    null,
                    writer,
                    JaspilerOptions.Default);
            assertTrue(StringUtils.isEmpty(writer.toString()));
        }
    }
}
