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
import com.sun.source.tree.ErroneousTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class JTErroneous
        extends JTExpression<ErroneousTree, JTErroneous>
        implements ErroneousTree {
    private static final String PROPERTY_ERROR_TREES = "errorTrees";
    private final List<JTTree<?, ?>> errorTrees;

    public JTErroneous() {
        this(null, null);
        setActionChange();
    }

    JTErroneous(ErroneousTree erroneousTree, JTTree<?, ?> parentTree) {
        super(erroneousTree, parentTree);
        errorTrees = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitErroneous(this, data);
    }

    @Override
    JTErroneous analyze() {
        super.analyze();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getErrorTrees(), this, errorTrees::add);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        return super.getAllNodes();
    }

    @Override
    public List<JTTree<?, ?>> getErrorTrees() {
        return errorTrees;
    }

    @Override
    public Kind getKind() {
        return Kind.ERRONEOUS;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_ERROR_TREES, propertyName -> v8Runtime.toV8Value(getErrorTrees()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_ERROR_TREES, (propertyName, propertyValue) -> replaceTrees(errorTrees, propertyValue));
        }
        return stringSetterMap;
    }
}
