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
import com.sun.source.tree.ArrayAccessTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JTArrayAccess
        extends JTExpression<ArrayAccessTree, JTArrayAccess>
        implements ArrayAccessTree {

    private static final String PROPERTY_EXPRESSION = "expression";
    private static final String PROPERTY_INDEX = "index";
    private JTExpression<?, ?> expression;
    private JTExpression<?, ?> index;

    public JTArrayAccess() {
        this(null, null);
        setActionChange();
    }

    JTArrayAccess(ArrayAccessTree arrayAccessTree, JTTree<?, ?> parentTree) {
        super(arrayAccessTree, parentTree);
        expression = null;
        index = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitArrayAccess(this, data);
    }

    @Override
    JTArrayAccess analyze() {
        super.analyze();
        expression = JTTreeFactory.create(getOriginalTree().getExpression(), this);
        index = JTTreeFactory.create(getOriginalTree().getIndex(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(expression).ifPresent(nodes::add);
        Optional.ofNullable(index).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTExpression<?, ?> getExpression() {
        return expression;
    }

    @Override
    public JTExpression<?, ?> getIndex() {
        return index;
    }

    @Override
    public Kind getKind() {
        return Kind.ARRAY_ACCESS;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_EXPRESSION, propertyName -> v8Runtime.toV8Value(getExpression()));
            registerStringGetter(PROPERTY_INDEX, propertyName -> v8Runtime.toV8Value(getIndex()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_EXPRESSION, (propertyName, propertyValue) -> replaceExpression(this::setExpression, propertyValue));
            registerStringSetter(PROPERTY_INDEX, (propertyName, propertyValue) -> replaceExpression(this::setIndex, propertyValue));
        }
        return stringSetterMap;
    }

    public JTArrayAccess setExpression(JTExpression<?, ?> expression) {
        if (this.expression == expression) {
            return this;
        }
        this.expression = Objects.requireNonNull(expression).setParentTree(this);
        return setActionChange();
    }

    public JTArrayAccess setIndex(JTExpression<?, ?> index) {
        if (this.index == index) {
            return this;
        }
        this.index = Objects.requireNonNull(index).setParentTree(this);
        return setActionChange();
    }
}
