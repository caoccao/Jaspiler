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
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.UsesTree;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JTUses
        extends JTDirective<UsesTree, JTUses>
        implements UsesTree {
    private static final String PROPERTY_SERVICE_NAME = "serviceName";
    private JTExpression<?, ?> serviceName;

    public JTUses() {
        this(null, null);
        setActionChange();
    }

    JTUses(UsesTree usesTree, JTTree<?, ?> parentTree) {
        super(usesTree, parentTree);
        serviceName = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitUses(this, data);
    }

    @Override
    JTUses analyze() {
        super.analyze();
        serviceName = JTTreeFactory.create(getOriginalTree().getServiceName(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(serviceName).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.USES;
    }

    @Override
    public JTExpression<?, ?> getServiceName() {
        return serviceName;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_SERVICE_NAME, propertyName -> v8Runtime.toV8Value(getServiceName()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_SERVICE_NAME, (propertyName, propertyValue) -> replaceExpression(this::setServiceName, propertyValue));
        }
        return stringSetterMap;
    }

    public JTUses setServiceName(JTExpression<?, ?> serviceName) {
        if (this.serviceName == serviceName) {
            return this;
        }
        this.serviceName = Objects.requireNonNull(serviceName).setParentTree(this);
        return setActionChange();
    }
}
