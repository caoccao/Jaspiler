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
import com.sun.source.tree.PackageTree;
import com.sun.source.tree.Tree;

import java.util.Objects;
import java.util.function.Function;

public class V8JaspilerTransformScanner
        extends BaseJaspilerTransformScanner<V8JaspilerTransformScanner>
        implements AutoCloseable {
    protected V8JaspilerOptions options;

    public V8JaspilerTransformScanner(V8JaspilerOptions options) {
        this.options = Objects.requireNonNull(options);
    }

    @Override
    public void close() throws JaspilerCheckedException {
    }

    protected <Node extends Tree> void forEachPlugin(
            Node node,
            Function<V8JaspilerOptions.Plugin, V8ValueFunction> functionGetter) {
        options.getPlugins().stream()
                .map(functionGetter)
                .filter(Objects::nonNull)
                .forEach(v8ValueFunction -> {
                    try {
                        v8ValueFunction.call(null, node);
                    } catch (Throwable t) {
                        logger.warn(t.getMessage(), t);
                    }
                });
    }

    @Override
    public V8JaspilerTransformScanner visitPackage(PackageTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitPackage());
        return super.visitPackage(node, jaspilerTransformContext);
    }
}
