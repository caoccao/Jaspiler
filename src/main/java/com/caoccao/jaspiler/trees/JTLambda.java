/*
 * Copyright (c) 2023-2024. caoccao.com Sam Cao
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

import com.caoccao.jaspiler.exceptions.JaspilerCheckedException;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.sun.source.tree.LambdaExpressionTree;
import com.sun.source.tree.TreeVisitor;

import java.util.*;

public final class JTLambda
        extends JTFunctionalExpression<LambdaExpressionTree, JTLambda>
        implements LambdaExpressionTree {
    private static final String PROPERTY_BODY = "body";
    private static final String PROPERTY_BODY_KIND = "bodyKind";
    private static final String PROPERTY_PARAMETERS = "parameters";
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

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_BODY, propertyName -> v8Runtime.toV8Value(getBody()));
            registerStringGetter(PROPERTY_BODY_KIND, propertyName -> v8Runtime.createV8ValueString(getBodyKind().name()));
            registerStringGetter(PROPERTY_PARAMETERS, propertyName -> v8Runtime.toV8Value(getParameters()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_BODY, (propertyName, propertyValue) -> replaceLambda(this::setBody, propertyValue));
            registerStringSetter(PROPERTY_BODY_KIND, (propertyName, propertyValue) -> setBodyKind(propertyValue));
            registerStringSetter(PROPERTY_PARAMETERS, (propertyName, propertyValue) -> replaceVariableDecls(parameters, propertyValue));
        }
        return stringSetterMap;
    }

    public JTLambda setBody(JTTree<?, ?> body) {
        if (this.body == body) {
            return this;
        }
        this.body = Objects.requireNonNull(body).setParentTree(this);
        return setActionChange();
    }

    private boolean setBodyKind(V8Value v8Value) {
        if (v8Value instanceof V8ValueString v8ValueString) {
            setBodyKind(BodyKind.valueOf(v8ValueString.getValue()));
            return true;
        }
        return false;
    }

    public JTLambda setBodyKind(BodyKind bodyKind) {
        if (this.bodyKind == bodyKind) {
            return this;
        }
        this.bodyKind = Objects.requireNonNull(bodyKind);
        return setActionChange();
    }
}
