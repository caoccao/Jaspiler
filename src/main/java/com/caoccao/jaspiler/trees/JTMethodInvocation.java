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

import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTMethodInvocation
        extends JTPolyExpression<MethodInvocationTree, JTMethodInvocation>
        implements MethodInvocationTree {
    private final List<JTExpression<?, ?>> arguments;
    private final List<JTExpression<?, ?>> typeArguments;
    private JTExpression<?, ?> methodSelect;

    public JTMethodInvocation() {
        this(null, null);
        setActionChange();
    }

    JTMethodInvocation(MethodInvocationTree methodInvocationTree, JTTree<?, ?> parentTree) {
        super(methodInvocationTree, parentTree);
        arguments = new ArrayList<>();
        methodSelect = null;
        typeArguments = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitMethodInvocation(this, data);
    }

    @Override
    JTMethodInvocation analyze() {
        super.analyze();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getTypeArguments(), this, (JTExpression<?, ?> o) -> typeArguments.add(o));
        methodSelect = JTTreeFactory.create(getOriginalTree().getMethodSelect(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getArguments(), this, (JTExpression<?, ?> o) -> arguments.add(o));
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        typeArguments.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(methodSelect).ifPresent(nodes::add);
        arguments.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTExpression<?, ?>> getArguments() {
        return arguments;
    }

    @Override
    public Kind getKind() {
        return Kind.METHOD_INVOCATION;
    }

    @Override
    public JTExpression<?, ?> getMethodSelect() {
        return methodSelect;
    }

    @Override
    public List<JTExpression<?, ?>> getTypeArguments() {
        return typeArguments;
    }

    public JTMethodInvocation setMethodSelect(JTExpression<?, ?> methodSelect) {
        if (this.methodSelect == methodSelect) {
            return this;
        }
        this.methodSelect = Objects.requireNonNull(methodSelect).setParentTree(this);
        return setActionChange();
    }
}
