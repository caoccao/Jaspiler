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

import com.caoccao.jaspiler.exceptions.JaspilerCheckedException;
import com.caoccao.jaspiler.utils.V8Register;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.tree.ParenthesizedTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JTParens
        extends JTExpression<ParenthesizedTree, JTParens>
        implements ParenthesizedTree {
    private static final String PROPERTY_EXPRESSION = "expression";
    private JTExpression<?, ?> expression;

    public JTParens() {
        this(null, null);
        setActionChange();
    }

    JTParens(ParenthesizedTree parenthesizedTree, JTTree<?, ?> parentTree) {
        super(parenthesizedTree, parentTree);
        expression = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitParenthesized(this, data);
    }

    @Override
    JTParens analyze() {
        super.analyze();
        expression = JTTreeFactory.create(getOriginalTree().getExpression(), this);
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
        return Kind.PARENTHESIZED;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_EXPRESSION, propertyName -> v8Runtime.toV8Value(getExpression()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_EXPRESSION,
                    (propertyName, propertyValue) -> replaceExpression(this::setExpression, propertyValue));
        }
        return stringSetterMap;
    }

    public JTParens setExpression(JTExpression<?, ?> expression) {
        if (this.expression == expression) {
            return this;
        }
        this.expression = Objects.requireNonNull(expression).setParentTree(this);
        return setActionChange();
    }
}
