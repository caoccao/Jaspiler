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

import com.sun.source.tree.SwitchTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTSwitch
        extends JTStatement<SwitchTree, JTSwitch>
        implements SwitchTree {
    private final List<JTCase> caseStatements;
    private JTExpression<?, ?> expression;

    public JTSwitch() {
        this(null, null);
        setActionChange();
    }

    JTSwitch(SwitchTree switchTree, JTTree<?, ?> parentTree) {
        super(switchTree, parentTree);
        caseStatements = new ArrayList<>();
        expression = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitSwitch(this, data);
    }

    @Override
    JTSwitch analyze() {
        super.analyze();
        expression = JTTreeFactory.create(getOriginalTree().getExpression(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getCases(), this, JTCase::new, caseStatements::add);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(expression).ifPresent(nodes::add);
        caseStatements.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTCase> getCases() {
        return caseStatements;
    }

    @Override
    public JTExpression<?, ?> getExpression() {
        return expression;
    }

    @Override
    public Kind getKind() {
        return Kind.SWITCH;
    }

    public JTSwitch setExpression(JTExpression<?, ?> expression) {
        if (this.expression == expression) {
            return this;
        }
        this.expression = Optional.ofNullable(expression).map(o -> o.setParentTree(this)).orElse(null);
        return setActionChange();
    }
}
