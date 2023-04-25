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

import com.caoccao.jaspiler.contexts.JaspilerParseContext;
import com.caoccao.jaspiler.contexts.JaspilerTransformContext;
import com.caoccao.jaspiler.trees.JTCompilationUnit;
import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.caoccao.jaspiler.utils.JavaFileStringObject;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Jaspiler compiler.
 * <p>
 * Limitations:
 * 1. Some comments may be lost due to the technical limitations.
 * Please refer to <a href="https://openjdk.org/groups/compiler/analyzing-doc-comments/analyze-doc-comments.html">Analyzing Documentation Comments</a> for detail.
 */
public final class JaspilerCompiler extends BaseLoggingObject {
    private final DiagnosticCollector<JavaFileObject> diagnosticCollector;
    private final JavaCompiler javaCompiler;
    private final StandardJavaFileManager javaFileManager;
    private final List<JavaFileObject> javaFileObjects;

    public JaspilerCompiler() {
        this(ToolProvider.getSystemJavaCompiler());
    }

    public JaspilerCompiler(JavaCompiler javaCompiler) {
        super();
        diagnosticCollector = new DiagnosticCollector<>();
        this.javaCompiler = javaCompiler;
        javaFileObjects = new ArrayList<>();
        javaFileManager = javaCompiler.getStandardFileManager(diagnosticCollector, null, null);
    }

    public JaspilerCompiler addJavaFileObjects(String... names) {
        javaFileManager.getJavaFileObjects(names).forEach(javaFileObjects::add);
        return this;
    }

    public JaspilerCompiler addJavaFileObjects(File... files) {
        javaFileManager.getJavaFileObjects(files).forEach(javaFileObjects::add);
        return this;
    }

    public JaspilerCompiler addJavaFileObjects(Path... paths) {
        javaFileManager.getJavaFileObjects(paths).forEach(javaFileObjects::add);
        return this;
    }

    public JaspilerCompiler addJavaFileStringObject(String name, String code) {
        javaFileObjects.add(new JavaFileStringObject(name, code));
        return this;
    }

    public <Scanner extends TreePathScanner<Scanner, JaspilerParseContext>> JaspilerCompiler parse(Scanner scanner)
            throws IOException {
        var task = (JavacTask) javaCompiler.getTask(
                null, javaFileManager, diagnosticCollector, null, null, javaFileObjects);
        var trees = Trees.instance(task);
        for (var compilationUnit : task.parse()) {
            var jaspilerContext = new JaspilerParseContext(compilationUnit, trees);
            scanner.scan(compilationUnit, jaspilerContext);
        }
        return this;
    }

    public <Scanner extends TreePathScanner<Scanner, JaspilerTransformContext>> JaspilerCompiler transform(
            Scanner scanner, Writer writer)
            throws IOException {
        var task = (JavacTask) javaCompiler.getTask(
                null, javaFileManager, diagnosticCollector, null, null, javaFileObjects);
        var trees = Trees.instance(task);
        for (var compilationUnit : task.parse()) {
            var jtCompilationUnit = new JTCompilationUnit(trees, compilationUnit).analyze();
            var jaspilerContext = new JaspilerTransformContext(jtCompilationUnit, trees);
            scanner.scan(jtCompilationUnit, jaspilerContext);
            jtCompilationUnit.save(writer);
        }
        return this;
    }
}
