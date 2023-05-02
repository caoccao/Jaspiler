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

import com.sun.source.tree.ForLoopTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTForLoop
        extends JTStatement<ForLoopTree, JTForLoop>
        implements ForLoopTree {
    private final List<JTStatement<?, ?>> initializer;
    private final List<JTExpressionStatement> update;
    private JTExpression<?, ?> condition;
    private JTStatement<?, ?> statement;

    public JTForLoop() {
        this(null, null);
        setActionChange();
    }

    JTForLoop(ForLoopTree forLoopTree, JTTree<?, ?> parentTree) {
        super(forLoopTree, parentTree);
        condition = null;
        initializer = new ArrayList<>();
        statement = null;
        update = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitForLoop(this, data);
    }

    @Override
    JTForLoop analyze() {
        super.analyze();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getInitializer(), this, (JTStatement<?, ?> o) -> initializer.add(o));
        condition = JTTreeFactory.create(getOriginalTree().getCondition(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getUpdate(), this, (JTExpressionStatement o) -> update.add(o));
        statement = JTTreeFactory.create(getOriginalTree().getStatement(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        initializer.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(condition).ifPresent(nodes::add);
        update.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(statement).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTExpression<?, ?> getCondition() {
        return condition;
    }

    @Override
    public List<JTStatement<?, ?>> getInitializer() {
        return initializer;
    }

    @Override
    public Kind getKind() {
        return Kind.FOR_LOOP;
    }

    @Override
    public JTStatement<?, ?> getStatement() {
        return statement;
    }

    @Override
    public List<JTExpressionStatement> getUpdate() {
        return update;
    }
}
