/*
 * Copyright (c) 2023-2025. caoccao.com Sam Cao
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
import com.caoccao.jaspiler.exceptions.JaspilerNotSupportedException;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.UnaryTree;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JTUnary
        extends JTOperatorExpression<UnaryTree, JTUnary>
        implements UnaryTree {
    private static final String PROPERTY_EXPRESSION = "expression";
    private static final String PROPERTY_KIND = "kind";
    private JTExpression<?, ?> expression;
    private Kind kind;

    public JTUnary() {
        this(null, null);
        setActionChange();
    }

    JTUnary(UnaryTree unaryTree, JTTree<?, ?> parentTree) {
        super(unaryTree, parentTree);
        expression = null;
        kind = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitUnary(this, data);
    }

    @Override
    JTUnary analyze() {
        super.analyze();
        expression = JTTreeFactory.create(getOriginalTree().getExpression(), this);
        kind = getOriginalTree().getKind();
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(expression).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
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
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_EXPRESSION, propertyName -> v8Runtime.toV8Value(getExpression()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_EXPRESSION, (propertyName, propertyValue) -> replaceExpression(this::setExpression, propertyValue));
            registerStringSetter(PROPERTY_KIND, (propertyName, propertyValue) -> replaceKind(this::setKind, propertyValue));
        }
        return stringSetterMap;
    }

    public JTUnary setExpression(JTExpression<?, ?> expression) {
        if (this.expression == expression) {
            return this;
        }
        this.expression = Objects.requireNonNull(expression).setParentTree(this);
        return setActionChange();
    }

    public JTUnary setKind(Kind kind) {
        if (this.kind == kind) {
            return this;
        }
        switch (Objects.requireNonNull(kind)) {
            case BITWISE_COMPLEMENT, LOGICAL_COMPLEMENT, POSTFIX_DECREMENT, POSTFIX_INCREMENT, PREFIX_DECREMENT,
                    PREFIX_INCREMENT, UNARY_MINUS, UNARY_PLUS -> this.kind = kind;
            default -> throw new JaspilerNotSupportedException(kind.name() + " is not supported.");
        }
        return setActionChange();
    }
}
