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

import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTNewClass
        extends JTPolyExpression<NewClassTree, JTNewClass>
        implements NewClassTree {
    private final List<JTExpression<?, ?>> arguments;
    private final List<JTExpression<?, ?>> typeArguments;
    private JTClassDecl classBody;
    private JTExpression<?, ?> enclosingExpression;
    private JTExpression<?, ?> identifier;

    public JTNewClass() {
        this(null, null);
        setActionChange();
    }

    JTNewClass(NewClassTree newClassTree, JTTree<?, ?> parentTree) {
        super(newClassTree, parentTree);
        arguments = new ArrayList<>();
        classBody = null;
        enclosingExpression = null;
        identifier = null;
        typeArguments = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitNewClass(this, data);
    }

    @Override
    JTNewClass analyze() {
        super.analyze();
        enclosingExpression = JTTreeFactory.create(getOriginalTree().getEnclosingExpression(), this);
        identifier = JTTreeFactory.create(getOriginalTree().getIdentifier(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getTypeArguments(), this, (JTExpression<?, ?> o) -> typeArguments.add(o));
        JTTreeFactory.createAndAdd(
                getOriginalTree().getArguments(), this, (JTExpression<?, ?> o) -> arguments.add(o));
        classBody = JTTreeFactory.create(getOriginalTree().getClassBody(), this, JTClassDecl::new);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(enclosingExpression).ifPresent(nodes::add);
        Optional.ofNullable(identifier).ifPresent(nodes::add);
        typeArguments.stream().filter(Objects::nonNull).forEach(nodes::add);
        arguments.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(classBody).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTExpression<?, ?>> getArguments() {
        return arguments;
    }

    @Override
    public JTClassDecl getClassBody() {
        return classBody;
    }

    @Override
    public JTExpression<?, ?> getEnclosingExpression() {
        return enclosingExpression;
    }

    @Override
    public JTExpression<?, ?> getIdentifier() {
        return identifier;
    }

    @Override
    public Kind getKind() {
        return Kind.NEW_CLASS;
    }

    @Override
    public List<JTExpression<?, ?>> getTypeArguments() {
        return typeArguments;
    }

    public JTNewClass setClassBody(JTClassDecl classBody) {
        if (this.classBody == classBody) {
            return this;
        }
        this.classBody = Optional.ofNullable(classBody).map(o -> o.setParentTree(this)).orElse(null);
        return setActionChange();
    }

    public JTNewClass setEnclosingExpression(JTExpression<?, ?> enclosingExpression) {
        if (this.enclosingExpression == enclosingExpression) {
            return this;
        }
        this.enclosingExpression = Optional.ofNullable(enclosingExpression).map(o -> o.setParentTree(this)).orElse(null);
        return setActionChange();
    }

    public JTNewClass setIdentifier(JTExpression<?, ?> identifier) {
        if (this.identifier == identifier) {
            return this;
        }
        this.identifier = Objects.requireNonNull(identifier).setParentTree(this);
        return setActionChange();
    }
}
