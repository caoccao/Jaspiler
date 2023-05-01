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

import com.sun.source.tree.ParameterizedTypeTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTTypeApply
        extends JTExpression<ParameterizedTypeTree, JTTypeApply>
        implements ParameterizedTypeTree {
    private final List<JTExpression<?, ?>> typeArguments;
    private JTExpression<?, ?> type;

    public JTTypeApply() {
        this(null, null);
        setActionChange();
    }

    JTTypeApply(ParameterizedTypeTree parameterizedTypeTree, JTTree<?, ?> parentTree) {
        super(parameterizedTypeTree, parentTree);
        typeArguments = new ArrayList<>();
        type = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitParameterizedType(this, data);
    }

    @Override
    JTTypeApply analyze() {
        super.analyze();
        type = JTTreeFactory.create(getOriginalTree().getType(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getTypeArguments(), this, (JTExpression<?, ?> o) -> typeArguments.add(o));
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(type).ifPresent(nodes::add);
        typeArguments.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.PARAMETERIZED_TYPE;
    }

    @Override
    public Tree getType() {
        return type;
    }

    @Override
    public List<JTExpression<?, ?>> getTypeArguments() {
        return typeArguments;
    }

    public JTTypeApply setType(JTExpression<?, ?> type) {
        if (this.type == type) {
            return this;
        }
        this.type = Objects.requireNonNull(type).setParentTree(this);
        return setActionChange();
    }
}
