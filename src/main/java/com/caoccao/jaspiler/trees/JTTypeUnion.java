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
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.UnionTypeTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class JTTypeUnion
        extends JTExpression<UnionTypeTree, JTTypeUnion>
        implements UnionTypeTree {
    private static final String PROPERTY_TYPE_ALTERNATIVES = "typeAlternatives";
    private final List<JTExpression<?, ?>> typeAlternatives;

    public JTTypeUnion() {
        this(null, null);
        setActionChange();
    }

    JTTypeUnion(UnionTypeTree unionTypeTree, JTTree<?, ?> parentTree) {
        super(unionTypeTree, parentTree);
        typeAlternatives = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitUnionType(this, data);
    }

    @Override
    JTTypeUnion analyze() {
        super.analyze();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getTypeAlternatives(), this, (JTExpression<?, ?> o) -> typeAlternatives.add(o));
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        typeAlternatives.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.UNION_TYPE;
    }

    @Override
    public List<JTExpression<?, ?>> getTypeAlternatives() {
        return typeAlternatives;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_TYPE_ALTERNATIVES, propertyName -> v8Runtime.toV8Value(getTypeAlternatives()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_TYPE_ALTERNATIVES, (propertyName, propertyValue) -> replaceExpressions(typeAlternatives, propertyValue));
        }
        return stringSetterMap;
    }
}
