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
import com.sun.source.tree.OpensTree;
import com.sun.source.tree.TreeVisitor;

import java.util.*;

public final class JTOpens
        extends JTDirective<OpensTree, JTOpens>
        implements OpensTree {
    private static final String PROPERTY_MODULE_NAMES = "moduleNames";
    private static final String PROPERTY_PACKAGE_NAME = "packageName";
    private final List<JTExpression<?, ?>> moduleNames;
    private JTExpression<?, ?> packageName;

    public JTOpens() {
        this(null, null);
        setActionChange();
    }

    JTOpens(OpensTree opensTree, JTTree<?, ?> parentTree) {
        super(opensTree, parentTree);
        moduleNames = new ArrayList<>();
        packageName = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitOpens(this, data);
    }

    @Override
    JTOpens analyze() {
        super.analyze();
        packageName = JTTreeFactory.create(getOriginalTree().getPackageName(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getModuleNames(), this, (JTExpression<?, ?> o) -> moduleNames.add(o));
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(packageName).ifPresent(nodes::add);
        moduleNames.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.OPENS;
    }

    @Override
    public List<JTExpression<?, ?>> getModuleNames() {
        return moduleNames;
    }

    @Override
    public JTExpression<?, ?> getPackageName() {
        return packageName;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_MODULE_NAMES, propertyName -> v8Runtime.toV8Value(getModuleNames()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_PACKAGE_NAME, propertyName -> v8Runtime.toV8Value(getPackageName()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_MODULE_NAMES,
                    (propertyName, propertyValue) -> replaceExpressions(moduleNames, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_PACKAGE_NAME,
                    (propertyName, propertyValue) -> replaceExpression(this::setPackageName, propertyValue));
        }
        return stringSetterMap;
    }

    public JTOpens setPackageName(JTExpression<?, ?> packageName) {
        if (this.packageName == packageName) {
            return this;
        }
        this.packageName = Objects.requireNonNull(packageName).setParentTree(this);
        return setActionChange();
    }
}
