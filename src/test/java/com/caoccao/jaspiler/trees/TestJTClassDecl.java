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
import com.caoccao.jaspiler.mock.MockIgnorePublicClass;
import com.sun.source.util.TreePathScanner;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJTClassDecl extends BaseTestSuite {
    @Test
    public void testIgnore() throws Exception {
        class TestTransformScanner extends TreePathScanner<TestTransformScanner, JaspilerTransformContext> {
        }
        String code = transform(new TestTransformScanner(), MockIgnorePublicClass.class);
        assertTrue(StringUtils.isEmpty(code));
    }
}
