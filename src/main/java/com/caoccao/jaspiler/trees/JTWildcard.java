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

import com.caoccao.jaspiler.exceptions.JaspilerNotSupportedException;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.WildcardTree;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTWildcard
        extends JTExpression<WildcardTree, JTWildcard>
        implements WildcardTree {
    private JTTree<?, ?> bound;
    private Kind kind;

    public JTWildcard() {
        this(null, null);
        setActionChange();
    }

    JTWildcard(WildcardTree wildcardTree, JTTree<?, ?> parentTree) {
        super(wildcardTree, parentTree);
        bound = null;
        kind = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitWildcard(this, data);
    }

    @Override
    JTWildcard analyze() {
        super.analyze();
        bound = JTTreeFactory.create(getOriginalTree().getBound(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(bound).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTTree<?, ?> getBound() {
        return bound;
    }

    @Override
    public Kind getKind() {
        return kind;
    }

    public JTWildcard setBound(JTTree<?, ?> bound) {
        if (this.bound == bound) {
            return this;
        }
        this.bound = Objects.requireNonNull(bound).setParentTree(this);
        return setActionChange();
    }

    public JTWildcard setKind(Kind kind) {
        if (this.kind == kind) {
            return this;
        }
        switch (Objects.requireNonNull(kind)) {
            case EXTENDS_WILDCARD, SUPER_WILDCARD, UNBOUNDED_WILDCARD -> this.kind = kind;
            default -> throw new JaspilerNotSupportedException(kind.name() + " is not supported.");
        }
        return setActionChange();
    }
}
