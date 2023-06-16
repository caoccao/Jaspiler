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

import com.caoccao.jaspiler.styles.StandardStyle;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The type Jt field access.
 * It references com.sun.tools.javac.tree.JCTree.JCFieldAccess.
 */
public final class JTFieldAccess
        extends JTExpression<MemberSelectTree, JTFieldAccess>
        implements MemberSelectTree {
    private JTExpression<?, ?> expression;
    private JTName identifier;

    public JTFieldAccess() {
        this(null, null);
        setActionChange();
    }

    JTFieldAccess(MemberSelectTree originalTree, JTTree<?, ?> parentTree) {
        super(originalTree, parentTree);
        expression = null;
        identifier = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitMemberSelect(this, data);
    }

    @Override
    JTFieldAccess analyze() {
        super.analyze();
        expression = JTTreeFactory.create(getOriginalTree().getExpression(), this);
        identifier = JTTreeFactory.createName(getOriginalTree().getIdentifier());
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(expression).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTExpression<?, ?> getExpression() {
        return expression;
    }

    @Override
    public JTName getIdentifier() {
        return identifier;
    }

    @Override
    public Kind getKind() {
        return Kind.MEMBER_SELECT;
    }

    public JTFieldAccess setExpression(JTExpression<?, ?> expression) {
        if (this.expression == expression) {
            return this;
        }
        this.expression = Objects.requireNonNull(expression).setParentTree(this);
        return setActionChange();
    }

    public JTFieldAccess setIdentifier(JTName identifier) {
        if (this.identifier == identifier) {
            return this;
        }
        this.identifier = Objects.requireNonNull(identifier);
        return setActionChange();
    }

    @Override
    public String toString() {
        if (isActionChange()) {
            final var sbp = new StandardStyle();
            if (expression != null) {
                sbp.append(expression).appendDot();
            }
            if (identifier != null) {
                sbp.append(identifier);
            }
            return sbp.toString();
        }
        return super.toString();
    }
}
