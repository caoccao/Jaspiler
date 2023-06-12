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

package com.caoccao.jaspiler;

import com.caoccao.jaspiler.contexts.JaspilerDocContext;
import com.caoccao.jaspiler.contexts.JaspilerTransformContext;
import com.caoccao.jaspiler.options.JaspilerTransformOptions;
import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.caoccao.jaspiler.utils.MockUtils;
import com.sun.source.util.DocTreeScanner;
import com.sun.source.util.TreePathScanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.StringWriter;

public abstract class BaseTestSuite extends BaseLoggingObject {
    protected JaspilerCompiler compiler;

    @AfterEach
    protected void afterEach() {

    }

    @BeforeEach
    protected void beforeEach() {
        compiler = new JaspilerCompiler();
    }

    protected <TransformScanner extends TreePathScanner<TransformScanner, JaspilerTransformContext>>
    String transform(
            TransformScanner transformScanner,
            Class<?> clazz)
            throws IOException {
        return transform(transformScanner, null, clazz);
    }

    protected <TransformScanner extends TreePathScanner<TransformScanner, JaspilerTransformContext>,
            DocScanner extends DocTreeScanner<DocScanner, JaspilerDocContext>>
    String transform(
            TransformScanner transformScanner,
            DocScanner docScanner,
            Class<?> clazz)
            throws IOException {
        compiler.clearJavaFileObject();
        compiler.addJavaFileObjects(MockUtils.getSourcePath(clazz));
        compiler.transform(
                transformScanner,
                docScanner,
                JaspilerTransformOptions.Default);
        try (StringWriter writer = new StringWriter()) {
            compiler.getTransformContexts().get(0).getCompilationUnitTree().save(writer);
            return writer.toString();
        }
    }
}
