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
import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.TreeVisitor;

import java.util.*;

public final class JTMemberReference
        extends JTFunctionalExpression<MemberReferenceTree, JTMemberReference>
        implements MemberReferenceTree {
    private static final String PROPERTY_MODE = "mode";
    private static final String PROPERTY_NAME = "name";
    private static final String PROPERTY_QUALIFIED_EXPRESSION = "qualifiedExpression";
    private static final String PROPERTY_TYPE_ARGUMENTS = "typeArguments";
    private final List<JTExpression<?, ?>> typeArguments;
    private ReferenceMode mode;
    private JTName name;
    private JTExpression<?, ?> qualifiedExpression;

    public JTMemberReference() {
        this(null, null);
        setActionChange();
    }

    JTMemberReference(MemberReferenceTree memberReferenceTree, JTTree<?, ?> parentTree) {
        super(memberReferenceTree, parentTree);
        mode = null;
        name = null;
        qualifiedExpression = null;
        typeArguments = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitMemberReference(this, data);
    }

    @Override
    JTMemberReference analyze() {
        super.analyze();
        qualifiedExpression = JTTreeFactory.create(getOriginalTree().getQualifierExpression(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getTypeArguments(), this, (JTExpression<?, ?> o) -> typeArguments.add(o));
        mode = getOriginalTree().getMode();
        name = JTTreeFactory.createName(getOriginalTree().getName());
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(qualifiedExpression).ifPresent(nodes::add);
        typeArguments.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.MEMBER_REFERENCE;
    }

    @Override
    public ReferenceMode getMode() {
        return mode;
    }

    @Override
    public JTName getName() {
        return name;
    }

    @Override
    public JTExpression<?, ?> getQualifierExpression() {
        return qualifiedExpression;
    }

    @Override
    public List<JTExpression<?, ?>> getTypeArguments() {
        return typeArguments;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_MODE, propertyName -> v8Runtime.createV8ValueString(getMode().name()));
            registerStringGetter(PROPERTY_NAME, propertyName -> v8Runtime.toV8Value(getName()));
            registerStringGetter(PROPERTY_QUALIFIED_EXPRESSION, propertyName -> v8Runtime.toV8Value(getQualifierExpression()));
            registerStringGetter(PROPERTY_TYPE_ARGUMENTS, propertyName -> v8Runtime.toV8Value(getTypeArguments()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_MODE, (propertyName, propertyValue) -> setMode(propertyValue));
            registerStringSetter(PROPERTY_NAME, (propertyName, propertyValue) -> replaceName(this::setName, propertyValue));
            registerStringSetter(PROPERTY_QUALIFIED_EXPRESSION, (propertyName, propertyValue) -> replaceExpression(this::setQualifiedExpression, propertyValue));
            registerStringSetter(PROPERTY_TYPE_ARGUMENTS, (propertyName, propertyValue) -> replaceExpressions(typeArguments, propertyValue));
        }
        return stringSetterMap;
    }

    private boolean setMode(V8Value v8Value) {
        if (v8Value instanceof V8ValueString v8ValueString) {
            setMode(ReferenceMode.valueOf(v8ValueString.getValue()));
            return true;
        }
        return false;
    }

    public JTMemberReference setMode(ReferenceMode mode) {
        if (this.mode == mode) {
            return this;
        }
        this.mode = Objects.requireNonNull(mode);
        return setActionChange();
    }

    public JTMemberReference setName(JTName name) {
        if (this.name == name) {
            return this;
        }
        this.name = Objects.requireNonNull(name);
        return setActionChange();
    }

    public JTMemberReference setQualifiedExpression(JTExpression<?, ?> qualifiedExpression) {
        if (this.qualifiedExpression == qualifiedExpression) {
            return this;
        }
        this.qualifiedExpression = Objects.requireNonNull(qualifiedExpression).setParentTree(this);
        return setActionChange();
    }
}
