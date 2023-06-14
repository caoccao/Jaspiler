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

package com.caoccao.jaspiler.v8;

import com.caoccao.jaspiler.contexts.JaspilerTransformContext;
import com.caoccao.jaspiler.exceptions.JaspilerCheckedException;
import com.caoccao.jaspiler.visiters.BaseJaspilerTransformScanner;
import com.caoccao.javet.values.reference.V8ValueFunction;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.PackageTree;
import com.sun.source.tree.Tree;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class V8JaspilerTransformScanner
        extends BaseJaspilerTransformScanner<V8JaspilerTransformScanner>
        implements AutoCloseable {
    protected final List<Throwable> exceptions;
    protected V8JaspilerOptions options;

    public V8JaspilerTransformScanner(V8JaspilerOptions options) {
        exceptions = new ArrayList<>();
        this.options = Objects.requireNonNull(options);
    }

    @Override
    public void close() throws JaspilerCheckedException {
        exceptions.clear();
    }

    protected <Node extends Tree> void forEachPlugin(
            Node node,
            Function<V8JaspilerOptions.Plugin, V8ValueFunction> functionGetter) {
        if (CollectionUtils.isNotEmpty(options.getPlugins())) {
            options.getPlugins().stream()
                    .map(functionGetter)
                    .filter(Objects::nonNull)
                    .forEach(v8ValueFunction -> {
                        try {
                            v8ValueFunction.call(null, node);
                        } catch (Throwable t) {
                            getExceptions().add(t);
                            logger.warn(t.getMessage(), t);
                        }
                    });
        }
    }

    public List<Throwable> getExceptions() {
        return exceptions;
    }

    @Override
    public V8JaspilerTransformScanner visitClass(
            ClassTree node,
            JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitClass());
        return super.visitClass(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitCompilationUnit(
            CompilationUnitTree node,
            JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitCompilationUnit());
        return super.visitCompilationUnit(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitPackage(PackageTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitPackage());
        return super.visitPackage(node, jaspilerTransformContext);
    }
}
