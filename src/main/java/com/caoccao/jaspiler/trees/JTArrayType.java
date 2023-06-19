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

import com.sun.source.tree.ArrayTypeTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTArrayType
        extends JTExpression<ArrayTypeTree, JTArrayType>
        implements ArrayTypeTree {
    private JTExpression<?, ?> type;

    public JTArrayType() {
        this(null, null);
        setActionChange();
    }

    JTArrayType(ArrayTypeTree arrayTypeTree, JTTree<?, ?> parentTree) {
        super(arrayTypeTree, parentTree);
        type = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitArrayType(this, data);
    }

    @Override
    JTArrayType analyze() {
        super.analyze();
        type = JTTreeFactory.create(getOriginalTree().getType(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(type).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.ARRAY_TYPE;
    }

    @Override
    public JTExpression<?, ?> getType() {
        return type;
    }

    public JTArrayType setType(JTExpression<?, ?> type) {
        if (this.type == type) {
            return this;
        }
        this.type = Objects.requireNonNull(type).setParentTree(this);
        return setActionChange();
    }
}
