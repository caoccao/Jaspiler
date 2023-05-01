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

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTMethodDecl
        extends JTTree<MethodTree, JTMethodDecl>
        implements MethodTree {
    private final List<JTVariableDecl> parameters;
    private final List<JTExpression<?, ?>> throwExpressions;
    private final List<JTTypeParameter> typeParameters;
    private JTBlock body;
    private JTExpression<?, ?> defaultValue;
    private JTModifiers modifiers;
    private JTName name;
    private JTVariableDecl receiverParameter;
    private JTExpression<?, ?> returnType;

    public JTMethodDecl() {
        this(null, null);
        setActionChange();
    }

    JTMethodDecl(MethodTree methodTree, JTTree<?, ?> parentTree) {
        super(methodTree, parentTree);
        body = null;
        defaultValue = null;
        modifiers = null;
        name = null;
        parameters = new ArrayList<>();
        receiverParameter = null;
        returnType = null;
        throwExpressions = new ArrayList<>();
        typeParameters = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitMethod(this, data);
    }

    @Override
    JTMethodDecl analyze() {
        super.analyze();
        modifiers = JTTreeFactory.create(
                getOriginalTree().getModifiers(), this, JTModifiers::new);
        returnType = JTTreeFactory.create(getOriginalTree().getReturnType(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getTypeParameters(), this, JTTypeParameter::new, typeParameters::add);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getParameters(), this, JTVariableDecl::new, parameters::add);
        receiverParameter = JTTreeFactory.create(
                getOriginalTree().getReceiverParameter(), this, JTVariableDecl::new);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getThrows(), this, (JTExpression<?, ?> o) -> throwExpressions.add(o));
        body = JTTreeFactory.create(
                getOriginalTree().getBody(), this, JTBlock::new);
        defaultValue = JTTreeFactory.create(getOriginalTree().getDefaultValue(), this);
        name = JTTreeFactory.createName(getOriginalTree().getName());
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(modifiers).ifPresent(nodes::add);
        Optional.ofNullable(returnType).ifPresent(nodes::add);
        typeParameters.stream().filter(Objects::nonNull).forEach(nodes::add);
        parameters.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(receiverParameter).ifPresent(nodes::add);
        throwExpressions.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(body).ifPresent(nodes::add);
        Optional.ofNullable(defaultValue).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTBlock getBody() {
        return body;
    }

    @Override
    public JTExpression<?, ?> getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Kind getKind() {
        return Kind.METHOD;
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
    public List<JTVariableDecl> getParameters() {
        return parameters;
    }

    @Override
    public JTVariableDecl getReceiverParameter() {
        return receiverParameter;
    }

    @Override
    public JTExpression<?, ?> getReturnType() {
        return returnType;
    }

    @Override
    public List<JTExpression<?, ?>> getThrows() {
        return throwExpressions;
    }

    @Override
    public List<JTTypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public JTMethodDecl setBody(JTBlock body) {
        if (this.body == body) {
            return this;
        }
        this.body = Objects.requireNonNull(body);
        return setActionChange();
    }

    public JTMethodDecl setDefaultValue(JTExpression<?, ?> defaultValue) {
        if (this.defaultValue == defaultValue) {
            return this;
        }
        this.defaultValue = defaultValue;
        return setActionChange();
    }

    public JTMethodDecl setModifiers(JTModifiers modifiers) {
        if (this.modifiers == modifiers) {
            return this;
        }
        this.modifiers = Objects.requireNonNull(modifiers).setParentTree(this);
        return setActionChange();
    }

    public JTMethodDecl setName(JTName name) {
        if (this.name == name) {
            return this;
        }
        this.name = Objects.requireNonNull(name);
        return setActionChange();
    }

    public JTMethodDecl setReceiverParameter(JTVariableDecl receiverParameter) {
        if (this.receiverParameter == receiverParameter) {
            return this;
        }
        this.receiverParameter = receiverParameter;
        return setActionChange();
    }

    public JTMethodDecl setReturnType(JTExpression<?, ?> returnType) {
        if (this.returnType == returnType) {
            return this;
        }
        this.returnType = Objects.requireNonNull(returnType).setParentTree(this);
        return setActionChange();
    }
}
