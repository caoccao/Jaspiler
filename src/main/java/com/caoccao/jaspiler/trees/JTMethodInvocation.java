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
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.TreeVisitor;

import java.util.*;

public final class JTMethodInvocation
        extends JTPolyExpression<MethodInvocationTree, JTMethodInvocation>
        implements MethodInvocationTree {
    private static final String PROPERTY_ARGUMENTS = "arguments";
    private static final String PROPERTY_METHOD_SELECT = "methodSelect";
    private static final String PROPERTY_TYPE_ARGUMENTS = "typeArguments";
    private final List<JTExpression<?, ?>> arguments;
    private final List<JTExpression<?, ?>> typeArguments;
    private JTExpression<?, ?> methodSelect;

    public JTMethodInvocation() {
        this(null, null);
        setActionChange();
    }

    JTMethodInvocation(MethodInvocationTree methodInvocationTree, JTTree<?, ?> parentTree) {
        super(methodInvocationTree, parentTree);
        arguments = new ArrayList<>();
        methodSelect = null;
        typeArguments = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitMethodInvocation(this, data);
    }

    @Override
    JTMethodInvocation analyze() {
        super.analyze();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getTypeArguments(), this, (JTExpression<?, ?> o) -> typeArguments.add(o));
        methodSelect = JTTreeFactory.create(getOriginalTree().getMethodSelect(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getArguments(), this, (JTExpression<?, ?> o) -> arguments.add(o));
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        typeArguments.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(methodSelect).ifPresent(nodes::add);
        arguments.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTExpression<?, ?>> getArguments() {
        return arguments;
    }

    @Override
    public Kind getKind() {
        return Kind.METHOD_INVOCATION;
    }

    @Override
    public JTExpression<?, ?> getMethodSelect() {
        return methodSelect;
    }

    @Override
    public List<JTExpression<?, ?>> getTypeArguments() {
        return typeArguments;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_ARGUMENTS, propertyName -> v8Runtime.toV8Value(getArguments()));
            registerStringGetter(PROPERTY_METHOD_SELECT, propertyName -> v8Runtime.toV8Value(getMethodSelect()));
            registerStringGetter(PROPERTY_TYPE_ARGUMENTS, propertyName -> v8Runtime.toV8Value(getTypeArguments()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_ARGUMENTS, (propertyName, propertyValue) -> replaceExpressions(arguments, propertyValue));
            registerStringSetter(PROPERTY_METHOD_SELECT, (propertyName, propertyValue) -> replaceExpression(this::setMethodSelect, propertyValue));
            registerStringSetter(PROPERTY_TYPE_ARGUMENTS, (propertyName, propertyValue) -> replaceExpressions(typeArguments, propertyValue));
        }
        return stringSetterMap;
    }

    public JTMethodInvocation setMethodSelect(JTExpression<?, ?> methodSelect) {
        if (this.methodSelect == methodSelect) {
            return this;
        }
        this.methodSelect = Objects.requireNonNull(methodSelect).setParentTree(this);
        return setActionChange();
    }
}
