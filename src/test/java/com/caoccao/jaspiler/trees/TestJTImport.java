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
import com.sun.source.tree.ImportTree;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJTImport extends BaseTestSuite {
    protected static final String COMMONS_LANG_3_STRING_UTILS = "import org.apache.commons.lang3.StringUtils;";

    @Test
    public void testIgnore() throws Exception {
        class TestTransformScanner extends BaseJaspilerTransformScanner<TestTransformScanner> {
            @Override
            public TestTransformScanner visitImport(ImportTree node, JaspilerTransformContext jaspilerTransformContext) {
                if (node.toString().startsWith(COMMONS_LANG_3_STRING_UTILS)) {
                    ((JTImport) node).setActionIgnore();
                }
                return super.visitImport(node, jaspilerTransformContext);
            }
        }
        String code = transform(new TestTransformScanner(), MockAllInOnePublicClass.class);
        assertFalse(code.contains(COMMONS_LANG_3_STRING_UTILS));
        var texts = List.of(
                "* Copyright (c)",
                "public abstract sealed class MockAllInOnePublicClass");
        texts.forEach(text -> assertTrue(code.contains(text), text));
    }

    @Test
    public void testUpdateQualifiedIdentifier() throws Exception {
        class TestTransformScanner extends BaseJaspilerTransformScanner<TestTransformScanner> {
            @Override
            public TestTransformScanner visitImport(ImportTree node, JaspilerTransformContext jaspilerTransformContext) {
                if (node.toString().startsWith(COMMONS_LANG_3_STRING_UTILS)) {
                    ((JTImport) node)
                            .setStaticImport(true)
                            .setQualifiedIdentifier(JTTreeFactory.createFieldAccess("abc", "def"));
                }
                return super.visitImport(node, jaspilerTransformContext);
            }
        }
        String code = transform(new TestTransformScanner(), MockAllInOnePublicClass.class);
        assertTrue(code.contains("import static abc.def;\n"));
    }
}
