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

import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.TypeCastTree;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTTypeCast
        extends JTExpression<TypeCastTree, JTTypeCast>
        implements TypeCastTree {
    private JTExpression<?, ?> expression;
    private JTTree<?, ?> type;

    public JTTypeCast() {
        this(null, null);
        setActionChange();
    }

    JTTypeCast(TypeCastTree typeCastTree, JTTree<?, ?> parentTree) {
        super(typeCastTree, parentTree);
        expression = null;
        type = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitTypeCast(this, data);
    }

    @Override
    JTTypeCast analyze() {
        super.analyze();
        type = JTTreeFactory.create(getOriginalTree().getType(), this);
        expression = JTTreeFactory.create(getOriginalTree().getExpression(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(type).ifPresent(nodes::add);
        Optional.ofNullable(expression).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTExpression<?, ?> getExpression() {
        return expression;
    }

    @Override
    public Kind getKind() {
        return Kind.TYPE_CAST;
    }

    @Override
    public JTTree<?, ?> getType() {
        return type;
    }

    public JTTypeCast setExpression(JTExpression<?, ?> expression) {
        if (this.expression == expression) {
            return this;
        }
        this.expression = Objects.requireNonNull(expression).setParentTree(this);
        return setActionChange();
    }

    public JTTypeCast setType(JTTree<?, ?> type) {
        if (this.type == type) {
            return this;
        }
        this.type = Objects.requireNonNull(type).setParentTree(this);
        return setActionChange();
    }
}
