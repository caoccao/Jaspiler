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
import com.sun.source.tree.IntersectionTypeTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class JTTypeIntersection
        extends JTExpression<IntersectionTypeTree, JTTypeIntersection>
        implements IntersectionTypeTree {
    private static final String PROPERTY_BOUNDS = "bounds";
    private final List<JTExpression<?, ?>> bounds;

    public JTTypeIntersection() {
        this(null, null);
        setActionChange();
    }

    JTTypeIntersection(IntersectionTypeTree intersectionTypeTree, JTTree<?, ?> parentTree) {
        super(intersectionTypeTree, parentTree);
        bounds = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitIntersectionType(this, data);
    }

    @Override
    JTTypeIntersection analyze() {
        super.analyze();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getBounds(), this, (JTExpression<?, ?> o) -> bounds.add(o));
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        bounds.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTExpression<?, ?>> getBounds() {
        return bounds;
    }

    @Override
    public Kind getKind() {
        return Kind.INTERSECTION_TYPE;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_BOUNDS, propertyName -> v8Runtime.toV8Value(getBounds()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_BOUNDS, (propertyName, propertyValue) -> replaceExpressions(bounds, propertyValue));
        }
        return stringSetterMap;
    }
}
