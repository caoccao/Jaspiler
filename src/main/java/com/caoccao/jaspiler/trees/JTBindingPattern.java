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
import com.sun.source.tree.BindingPatternTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JTBindingPattern
        extends JTPattern<BindingPatternTree, JTBindingPattern>
        implements BindingPatternTree {
    private static final String PROPERTY_VARIABLE = "variable";
    private JTVariableDecl variable;

    public JTBindingPattern() {
        this(null, null);
        setActionChange();
    }

    JTBindingPattern(BindingPatternTree bindingPatternTree, JTTree<?, ?> parentTree) {
        super(bindingPatternTree, parentTree);
        variable = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitBindingPattern(this, data);
    }

    @Override
    JTBindingPattern analyze() {
        super.analyze();
        variable = JTTreeFactory.create(getOriginalTree().getVariable(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(variable).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.BINDING_PATTERN;
    }

    @Override
    public JTVariableDecl getVariable() {
        return variable;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_VARIABLE, propertyName -> v8Runtime.toV8Value(getVariable()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_VARIABLE,
                    (propertyName, propertyValue) -> replaceVariableDecl(this::setVariable, propertyValue));
        }
        return stringSetterMap;
    }

    public JTBindingPattern setVariable(JTVariableDecl variable) {
        if (this.variable == variable) {
            return this;
        }
        this.variable = Objects.requireNonNull(variable).setParentTree(this);
        return setActionChange();
    }
}
