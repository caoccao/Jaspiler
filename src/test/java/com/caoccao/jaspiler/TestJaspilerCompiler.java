/*
 * Copyright (c) 2023-2023. caoccao.com Sam Cao
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

import com.caoccao.jaspiler.contexts.JaspilerTransformContext;
import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.caoccao.jaspiler.utils.SystemUtils;
import com.sun.source.util.TreePathScanner;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;

public class TestJaspilerCompiler extends BaseLoggingObject {
    @Test
    public void testTransform() throws IOException {
        class TestScanner extends TreePathScanner<TestScanner, JaspilerTransformContext> {
        }
        Path outputPath = SystemUtils.SYSTEM_TMP_PATH.resolve(JaspilerContract.NAME);
        var compiler = new JaspilerCompiler();
        compiler.addJavaFileStringObject("com.test.Test", """
                // This is a test.
                package com.test;
                // This is a test.
                class Test {
                    private String a;
                    public int b;
                    public Test() {
                        a = "abc";
                        b = 1;
                    }
                }""");
        try (StringWriter writer = new StringWriter()) {
            compiler.transform(new TestScanner(), writer);
            logger.info(writer.toString());
        }
    }
}
