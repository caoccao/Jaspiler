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

import com.caoccao.jaspiler.contexts.JaspilerDocContext;
import com.caoccao.jaspiler.contexts.JaspilerParseContext;
import com.caoccao.jaspiler.contexts.JaspilerTransformContext;
import com.caoccao.jaspiler.styles.StyleOptions;
import com.caoccao.jaspiler.trees.JTCompilationUnit;
import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.caoccao.jaspiler.utils.JavaFileStringObject;
import com.sun.source.util.*;
import org.apache.commons.collections4.CollectionUtils;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Jaspiler compiler.
 * <p>
 * Limitations:
 * 1. Some comments may be lost due to the technical limitations.
 * Please refer to <a href="https://openjdk.org/groups/compiler/analyzing-doc-comments/analyze-doc-comments.html">Analyzing Documentation Comments</a> for detail.
 */
public final class JaspilerCompiler extends BaseLoggingObject {
    private static final int MIN_FILE_SIZE = 10;
    private final DiagnosticCollector<JavaFileObject> diagnosticCollector;
    private final List<JaspilerDocContext> docContexts;
    private final JavaCompiler javaCompiler;
    private final StandardJavaFileManager javaFileManager;
    private final List<JavaFileObject> javaFileObjects;
    private final List<JaspilerParseContext> parseContexts;
    private final List<JaspilerTransformContext> transformContexts;

    public JaspilerCompiler() {
        this(ToolProvider.getSystemJavaCompiler());
    }

    public JaspilerCompiler(JavaCompiler javaCompiler) {
        super();
        diagnosticCollector = new DiagnosticCollector<>();
        docContexts = new ArrayList<>();
        this.javaCompiler = javaCompiler;
        javaFileObjects = new ArrayList<>();
        javaFileManager = javaCompiler.getStandardFileManager(diagnosticCollector, null, null);
        parseContexts = new ArrayList<>();
        transformContexts = new ArrayList<>();
    }

    public JaspilerCompiler addJavaFileObjects(String... names) {
        javaFileManager.getJavaFileObjectsFromFiles(filterFiles(Stream.of(names).map(File::new))).forEach(javaFileObjects::add);
        return this;
    }

    public JaspilerCompiler addJavaFileObjects(File... files) {
        javaFileManager.getJavaFileObjectsFromFiles(filterFiles(Stream.of(files))).forEach(javaFileObjects::add);
        return this;
    }

    public JaspilerCompiler addJavaFileObjects(Path... paths) {
        javaFileManager.getJavaFileObjectsFromFiles(filterFiles(Stream.of(paths).map(Path::toFile))).forEach(javaFileObjects::add);
        return this;
    }

    public JaspilerCompiler addJavaFileStringObject(String name, String code) {
        javaFileObjects.add(new JavaFileStringObject(name, code));
        return this;
    }

    public JaspilerCompiler clearJavaFileObject() {
        javaFileObjects.clear();
        return this;
    }

    private Iterable<File> filterFiles(Stream<File> fileStream) {
        return fileStream
                .map(file -> {
                    if (file.exists() && file.isFile() && file.canRead() && file.length() >= MIN_FILE_SIZE) {
                        return file;
                    }
                    logger.warn("Ignored [{}].", file.getAbsolutePath());
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<JaspilerDocContext> getDocContexts() {
        return docContexts;
    }

    public List<JaspilerParseContext> getParseContexts() {
        return parseContexts;
    }

    public List<JaspilerTransformContext> getTransformContexts() {
        return transformContexts;
    }

    public <Scanner extends TreePathScanner<Scanner, JaspilerParseContext>> JaspilerCompiler parse(Scanner scanner)
            throws IOException {
        parseContexts.clear();
        if (CollectionUtils.isNotEmpty(javaFileObjects)) {
            var task = (JavacTask) javaCompiler.getTask(
                    null, javaFileManager, diagnosticCollector, null, null, javaFileObjects);
            var trees = Trees.instance(task);
            for (var compilationUnit : task.parse()) {
                var parseContext = new JaspilerParseContext(compilationUnit);
                parseContexts.add(parseContext);
                scanner.scan(compilationUnit, parseContext);
            }
        }
        return this;
    }

    public <TransformScanner extends TreePathScanner<TransformScanner, JaspilerTransformContext>,
            DocScanner extends DocTreeScanner<DocScanner, JaspilerDocContext>> JaspilerCompiler transform(
            TransformScanner transformScanner,
            DocScanner docScanner)
            throws IOException {
        transformContexts.clear();
        docContexts.clear();
        if (CollectionUtils.isNotEmpty(javaFileObjects)) {
            var task = (JavacTask) javaCompiler.getTask(
                    null, javaFileManager, diagnosticCollector, null, null, javaFileObjects);
            var trees = Trees.instance(task);
            var docTrees = DocTrees.instance(task);
            for (var compilationUnit : task.parse()) {
                var jtCompilationUnit = new JTCompilationUnit(trees, docTrees, compilationUnit).analyze();
                var transformContext = new JaspilerTransformContext(jtCompilationUnit);
                transformContexts.add(transformContext);
                transformScanner.scan(jtCompilationUnit, transformContext);
                if (docScanner != null) {
                    var docContext = new JaspilerDocContext(jtCompilationUnit);
                    docContexts.add(docContext);
                    docScanner.scan(jtCompilationUnit.getDocCommentTree(), docContext);
                }
            }
        }
        return this;
    }
}
