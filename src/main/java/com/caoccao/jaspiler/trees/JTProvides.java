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
import com.sun.source.tree.ProvidesTree;
import com.sun.source.tree.TreeVisitor;

import java.util.*;

public final class JTProvides
        extends JTDirective<ProvidesTree, JTProvides>
        implements ProvidesTree {
    private static final String PROPERTY_IMPLEMENTATION_NAMES = "implementationNames";
    private static final String PROPERTY_SERVICE_NAME = "serviceName";
    private final List<JTExpression<?, ?>> implementationNames;
    private JTExpression<?, ?> serviceName;

    public JTProvides() {
        this(null, null);
        setActionChange();
    }

    JTProvides(ProvidesTree providesTree, JTTree<?, ?> parentTree) {
        super(providesTree, parentTree);
        implementationNames = new ArrayList<>();
        serviceName = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitProvides(this, data);
    }

    @Override
    JTProvides analyze() {
        super.analyze();
        serviceName = JTTreeFactory.create(getOriginalTree().getServiceName(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getImplementationNames(), this, (JTExpression<?, ?> o) -> implementationNames.add(o));
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(serviceName).ifPresent(nodes::add);
        implementationNames.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTExpression<?, ?>> getImplementationNames() {
        return implementationNames;
    }

    @Override
    public Kind getKind() {
        return Kind.PROVIDES;
    }

    @Override
    public JTExpression<?, ?> getServiceName() {
        return serviceName;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_IMPLEMENTATION_NAMES, propertyName -> v8Runtime.toV8Value(getImplementationNames()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_SERVICE_NAME, propertyName -> v8Runtime.toV8Value(getServiceName()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_IMPLEMENTATION_NAMES,
                    (propertyName, propertyValue) -> replaceExpressions(implementationNames, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_SERVICE_NAME,
                    (propertyName, propertyValue) -> replaceExpression(this::setServiceName, propertyValue));
        }
        return stringSetterMap;
    }

    public JTProvides setServiceName(JTExpression<?, ?> serviceName) {
        if (this.serviceName == serviceName) {
            return this;
        }
        this.serviceName = Objects.requireNonNull(serviceName).setParentTree(this);
        return setActionChange();
    }
}
