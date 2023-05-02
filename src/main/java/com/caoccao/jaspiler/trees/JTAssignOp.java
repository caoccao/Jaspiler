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
import com.sun.source.tree.CompoundAssignmentTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTAssignOp
        extends JTOperatorExpression<CompoundAssignmentTree, JTAssignOp>
        implements CompoundAssignmentTree {
    private JTExpression<?, ?> expression;
    private Kind kind;
    private JTExpression<?, ?> variable;

    public JTAssignOp() {
        this(null, null);
        setActionChange();
    }

    JTAssignOp(CompoundAssignmentTree compoundAssignmentTree, JTTree<?, ?> parentTree) {
        super(compoundAssignmentTree, parentTree);
        expression = null;
        kind = null;
        variable = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitCompoundAssignment(this, data);
    }

    @Override
    JTAssignOp analyze() {
        super.analyze();
        variable = JTTreeFactory.create(getOriginalTree().getVariable(), this);
        expression = JTTreeFactory.create(getOriginalTree().getExpression(), this);
        kind = getOriginalTree().getKind();
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(variable).ifPresent(nodes::add);
        Optional.ofNullable(expression).ifPresent(nodes::add);
        return nodes;
    }

    @Override
    public JTExpression<?, ?> getExpression() {
        return expression;
    }

    @Override
    public Kind getKind() {
        return kind;
    }

    @Override
    public JTExpression<?, ?> getVariable() {
        return variable;
    }

    public JTAssignOp setExpression(JTExpression<?, ?> expression) {
        if (this.expression == expression) {
            return this;
        }
        this.expression = Objects.requireNonNull(expression).setParentTree(this);
        return setActionChange();
    }

    public JTAssignOp setKind(Kind kind) {
        if (this.kind == kind) {
            return this;
        }
        switch (Objects.requireNonNull(kind)) {
            case AND_ASSIGNMENT, DIVIDE_ASSIGNMENT, LEFT_SHIFT_ASSIGNMENT, MINUS_ASSIGNMENT, MULTIPLY_ASSIGNMENT,
                    OR_ASSIGNMENT, PLUS_ASSIGNMENT, REMAINDER_ASSIGNMENT, RIGHT_SHIFT_ASSIGNMENT, UNSIGNED_RIGHT_SHIFT_ASSIGNMENT,
                    XOR_ASSIGNMENT -> this.kind = kind;
            default -> throw new JaspilerNotSupportedException(kind.name() + " is not supported.");
        }
        return setActionChange();
    }

    public JTAssignOp setVariable(JTExpression<?, ?> variable) {
        if (this.variable == variable) {
            return this;
        }
        this.variable = Objects.requireNonNull(variable).setParentTree(this);
        return setActionChange();
    }
}
