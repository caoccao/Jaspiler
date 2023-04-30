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
import com.sun.source.tree.VariableTree;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTVariableDecl
        extends JTStatement<VariableTree, JTVariableDecl>
        implements VariableTree {
    private JTExpression<?, ?> initializer;
    private JTModifiers modifiers;
    private JTName name;
    private JTExpression<?, ?> nameExpression;
    private JTExpression<?, ?> type;

    public JTVariableDecl() {
        this(null, null);
        setActionChange();
    }

    JTVariableDecl(VariableTree variableTree, JTTree<?, ?> parentTree) {
        super(variableTree, parentTree);
        initializer = null;
        modifiers = null;
        name = null;
        nameExpression = null;
        type = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitVariable(this, data);
    }

    @Override
    JTVariableDecl analyze() {
        super.analyze();
        modifiers = JTTreeFactory.create(
                getOriginalTree().getModifiers(), this, JTModifiers::new);
        type = JTTreeFactory.create(getOriginalTree().getType(), this);
        nameExpression = JTTreeFactory.create(getOriginalTree().getNameExpression(), this);
        initializer = JTTreeFactory.create(getOriginalTree().getInitializer(), this);
        name = JTTreeFactory.createName(getOriginalTree().getName());
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(modifiers).ifPresent(nodes::add);
        Optional.ofNullable(type).ifPresent(nodes::add);
        Optional.ofNullable(nameExpression).ifPresent(nodes::add);
        Optional.ofNullable(initializer).ifPresent(nodes::add);
        return nodes;
    }

    @Override
    public JTExpression<?, ?> getInitializer() {
        return initializer;
    }

    @Override
    public Kind getKind() {
        return Kind.VARIABLE;
    }

    @Override
    public JTModifiers getModifiers() {
        return modifiers;
    }

    @Override
    public JTName getName() {
        return name;
    }

    @Override
    public JTExpression<?, ?> getNameExpression() {
        return nameExpression;
    }

    @Override
    public JTExpression<?, ?> getType() {
        return type;
    }

    public JTVariableDecl setInitializer(JTExpression<?, ?> initializer) {
        this.initializer = Optional.ofNullable(initializer).map(o -> o.setParentTree(this)).orElse(null);
        return setActionChange();
    }

    public JTVariableDecl setModifiers(JTModifiers modifiers) {
        this.modifiers = Objects.requireNonNull(modifiers).setParentTree(this);
        return setActionChange();
    }

    public JTVariableDecl setName(JTName name) {
        this.name = Objects.requireNonNull(name);
        return setActionChange();
    }

    public JTVariableDecl setNameExpression(JTExpression<?, ?> nameExpression) {
        this.nameExpression = Objects.requireNonNull(nameExpression).setParentTree(this);
        return this;
    }

    public JTVariableDecl setType(JTExpression<?, ?> type) {
        this.type = Optional.ofNullable(type).map(o -> o.setParentTree(this)).orElse(null);
        return setActionChange();
    }

    @Override
    public String toString() {
        if (isActionChange()) {
            var stringBuilder = new StringBuilder();
            getAllNodes().forEach(stringBuilder::append);
            // TODO
            return stringBuilder.toString();
        }
        return super.toString();
    }
}
