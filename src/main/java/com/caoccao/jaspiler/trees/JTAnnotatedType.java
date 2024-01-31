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
import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.TreeVisitor;

import java.util.*;

public final class JTAnnotatedType
        extends JTExpression<AnnotatedTypeTree, JTAnnotatedType>
        implements AnnotatedTypeTree, IJTAnnotatable {

    private static final String PROPERTY_UNDERLYING_TYPE = "underlyingType";
    private final List<JTAnnotation> annotations;
    private JTExpression<?, ?> underlyingType;

    public JTAnnotatedType() {
        this(null, null);
        setActionChange();
    }

    JTAnnotatedType(AnnotatedTypeTree annotatedTypeTree, JTTree<?, ?> parentTree) {
        super(annotatedTypeTree, parentTree);
        annotations = new ArrayList<>();
        underlyingType = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitAnnotatedType(this, data);
    }

    @Override
    JTAnnotatedType analyze() {
        super.analyze();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getAnnotations(), this, JTAnnotation::new, annotations::add);
        underlyingType = JTTreeFactory.create(getOriginalTree().getUnderlyingType(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        annotations.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(underlyingType).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public Kind getKind() {
        return Kind.ANNOTATED_TYPE;
    }

    @Override
    public JTExpression<?, ?> getUnderlyingType() {
        return underlyingType;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_ANNOTATIONS, propertyName -> v8Runtime.toV8Value(getAnnotations()));
            registerStringGetter(PROPERTY_UNDERLYING_TYPE, propertyName -> v8Runtime.toV8Value(getUnderlyingType()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_ANNOTATIONS, (propertyName, propertyValue) -> replaceAnnotations(annotations, propertyValue));
            registerStringSetter(PROPERTY_UNDERLYING_TYPE, (propertyName, propertyValue) -> replaceExpression(this::setUnderlyingType, propertyValue));
        }
        return stringSetterMap;
    }

    public JTAnnotatedType setUnderlyingType(JTExpression<?, ?> underlyingType) {
        if (this.underlyingType == underlyingType) {
            return this;
        }
        this.underlyingType = Objects.requireNonNull(underlyingType).setParentTree(this);
        return setActionChange();
    }
}
