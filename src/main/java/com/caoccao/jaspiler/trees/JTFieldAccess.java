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

import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.TreeVisitor;

import java.io.IOException;
import java.io.Writer;
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
        expression = Optional.ofNullable(getOriginalTree().getExpression())
                .map(o -> (JTExpression<?, ?>) JTTree.from(o, this))
                .orElse(null);
        identifier = Optional.ofNullable(getOriginalTree().getIdentifier())
                .map(Object::toString)
                .map(JTName::new)
                .orElse(null);
        return this;
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

    public JTName getName() {
        return identifier;
    }

    @Override
    public boolean isActionChange() {
        return isActionChange(getExpression());
    }

    @Override
    protected boolean save(Writer writer) throws IOException {
        if (isActionChange()) {
            if (expression != null) {
                expression.save(writer);
                writeStrings(writer, ".");
            }
            if (identifier != null) {
                writeStrings(writer, identifier.getValue());
            }
            return true;
        }
        return super.save(writer);
    }

    public JTFieldAccess setExpression(JTExpression<?, ?> expression) {
        this.expression = Objects.requireNonNull(expression).setParentTree(this).setOriginalPosition(this.expression);
        return setActionChange();
    }

    public JTFieldAccess setIdentifier(JTName identifier) {
        this.identifier = Objects.requireNonNull(identifier);
        return setActionChange();
    }

    @Override
    public String toString() {
        if (isActionChange()) {
            var stringBuilder = new StringBuilder();
            if (expression != null) {
                stringBuilder.append(expression).append(IJTConstants.DOT);
            }
            if (identifier != null) {
                stringBuilder.append(identifier);
            }
            return stringBuilder.toString();
        }
        return super.toString();
    }
}
