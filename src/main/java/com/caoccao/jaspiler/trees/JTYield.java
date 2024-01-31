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
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.YieldTree;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JTYield
        extends JTStatement<YieldTree, JTYield>
        implements YieldTree {
    private static final String PROPERTY_VALUE = "value";
    private JTExpression<?, ?> value;

    public JTYield() {
        this(null, null);
        setActionChange();
    }

    JTYield(YieldTree yieldTree, JTTree<?, ?> parentTree) {
        super(yieldTree, parentTree);
        value = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitYield(this, data);
    }

    @Override
    JTYield analyze() {
        super.analyze();
        value = JTTreeFactory.create(getOriginalTree().getValue(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(value).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.YIELD;
    }

    @Override
    public JTExpression<?, ?> getValue() {
        return value;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_VALUE, propertyName -> v8Runtime.toV8Value(getValue()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_VALUE, (propertyName, propertyValue) -> replaceExpression(this::setValue, propertyValue));
        }
        return stringSetterMap;
    }

    public JTYield setValue(JTExpression<?, ?> value) {
        if (this.value == value) {
            return this;
        }
        this.value = Objects.requireNonNull(value).setParentTree(this);
        return setActionChange();
    }
}
