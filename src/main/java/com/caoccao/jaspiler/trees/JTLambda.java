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

import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTLambda
        extends JTFunctionalExpression<LambdaExpressionTree, JTLambda>
        implements LambdaExpressionTree {
    private final List<JTVariableDecl> parameters;
    private JTTree<?, ?> body;
    private BodyKind bodyKind;

    public JTLambda() {
        this(null, null);
        setActionChange();
    }

    JTLambda(LambdaExpressionTree lambdaExpressionTree, JTTree<?, ?> parentTree) {
        super(lambdaExpressionTree, parentTree);
        body = null;
        bodyKind = null;
        parameters = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitLambdaExpression(this, data);
    }

    @Override
    JTLambda analyze() {
        super.analyze();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getParameters(), this, JTVariableDecl::new, parameters::add);
        body = JTTreeFactory.create(getOriginalTree().getBody(), this);
        bodyKind = getOriginalTree().getBodyKind();
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        parameters.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(body).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTTree<?, ?> getBody() {
        return body;
    }

    @Override
    public BodyKind getBodyKind() {
        return bodyKind;
    }

    @Override
    public Kind getKind() {
        return Kind.LAMBDA_EXPRESSION;
    }

    @Override
    public List<JTVariableDecl> getParameters() {
        return parameters;
    }

    public JTLambda setBody(JTTree<?, ?> body) {
        if (this.body == body) {
            return this;
        }
        this.body = Objects.requireNonNull(body).setParentTree(this);
        return setActionChange();
    }

    public JTLambda setBodyKind(BodyKind bodyKind) {
        if (this.bodyKind == bodyKind) {
            return this;
        }
        this.bodyKind = Objects.requireNonNull(bodyKind);
        return setActionChange();
    }
}
