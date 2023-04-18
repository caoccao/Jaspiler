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

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.TreeVisitor;

import javax.lang.model.element.Name;
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

    public JTFieldAccess(MemberSelectTree originalTree, JTTree<?, ?> parentTree) {
        super(originalTree, parentTree);
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitMemberSelect(this, data);
    }

    @Override
    public JTFieldAccess analyze() {
        setExpression(Optional.ofNullable(getOriginalTree().getExpression())
                .map(e -> JTExpression.from(e, this))
                .map(JTTree::analyze)
                .orElse(null));
        setIdentifier(Optional.ofNullable(getOriginalTree().getIdentifier())
                .map(Object::toString)
                .map(JTName::new)
                .orElse(null));
        return this;
    }

    @Override
    public ExpressionTree getExpression() {
        return expression;
    }

    @Override
    public Name getIdentifier() {
        return identifier;
    }

    @Override
    public Kind getKind() {
        return Kind.MEMBER_SELECT;
    }

    public JTName getName() {
        return identifier;
    }

    public JTFieldAccess setExpression(JTExpression<?, ?> expression) {
        this.expression = expression;
        return this;
    }

    public JTFieldAccess setIdentifier(JTName identifier) {
        this.identifier = identifier;
        return this;
    }
}
