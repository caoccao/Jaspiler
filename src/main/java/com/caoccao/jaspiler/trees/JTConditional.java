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

import com.sun.source.tree.ConditionalExpressionTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTConditional
        extends JTPolyExpression<ConditionalExpressionTree, JTConditional>
        implements ConditionalExpressionTree {
    private JTExpression<?, ?> condition;
    private JTExpression<?, ?> falseExpression;
    private JTExpression<?, ?> trueExpression;

    public JTConditional() {
        this(null, null);
        setActionChange();
    }

    JTConditional(ConditionalExpressionTree conditionalExpressionTree, JTTree<?, ?> parentTree) {
        super(conditionalExpressionTree, parentTree);
        condition = null;
        falseExpression = null;
        trueExpression = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitConditionalExpression(this, data);
    }

    @Override
    JTConditional analyze() {
        super.analyze();
        condition = JTTreeFactory.create(getOriginalTree().getCondition(), this);
        falseExpression = JTTreeFactory.create(getOriginalTree().getFalseExpression(), this);
        trueExpression = JTTreeFactory.create(getOriginalTree().getTrueExpression(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(condition).ifPresent(nodes::add);
        Optional.ofNullable(falseExpression).ifPresent(nodes::add);
        Optional.ofNullable(trueExpression).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTExpression<?, ?> getCondition() {
        return condition;
    }

    @Override
    public JTExpression<?, ?> getFalseExpression() {
        return falseExpression;
    }

    @Override
    public Kind getKind() {
        return Kind.CONDITIONAL_EXPRESSION;
    }

    @Override
    public JTExpression<?, ?> getTrueExpression() {
        return trueExpression;
    }

    public JTConditional setCondition(JTExpression<?, ?> condition) {
        if (this.condition == condition) {
            return this;
        }
        this.condition = Objects.requireNonNull(condition).setParentTree(this);
        return setActionChange();
    }

    public JTConditional setFalseExpression(JTExpression<?, ?> falseExpression) {
        if (this.falseExpression == falseExpression) {
            return this;
        }
        this.falseExpression = Objects.requireNonNull(falseExpression).setParentTree(this);
        return setActionChange();
    }

    public JTConditional setTrueExpression(JTExpression<?, ?> trueExpression) {
        if (this.trueExpression == trueExpression) {
            return this;
        }
        this.trueExpression = Objects.requireNonNull(trueExpression).setParentTree(this);
        return setActionChange();
    }
}
