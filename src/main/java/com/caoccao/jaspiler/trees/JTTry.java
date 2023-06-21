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
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.TryTree;

import java.util.*;

public final class JTTry
        extends JTStatement<TryTree, JTTry>
        implements TryTree {
    private static final String PROPERTY_BLOCK = "block";
    private static final String PROPERTY_CATCHES = "catches";
    private static final String PROPERTY_FINALLY_BLOCK = "finallyBlock";
    private static final String PROPERTY_RESOURCES = "resources";
    private final List<JTCatch> catches;
    private final List<JTTree<?, ?>> resources;
    private JTBlock block;
    private JTBlock finallyBlock;

    public JTTry() {
        this(null, null);
        setActionChange();
    }

    JTTry(TryTree tryTree, JTTree<?, ?> parentTree) {
        super(tryTree, parentTree);
        block = null;
        catches = new ArrayList<>();
        finallyBlock = null;
        resources = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitTry(this, data);
    }

    @Override
    JTTry analyze() {
        super.analyze();
        JTTreeFactory.createAndAdd(getOriginalTree().getResources(), this, resources::add);
        block = JTTreeFactory.create(
                getOriginalTree().getBlock(), this, JTBlock::new);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getCatches(), this, JTCatch::new, catches::add);
        finallyBlock = JTTreeFactory.create(
                getOriginalTree().getFinallyBlock(), this, JTBlock::new);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        resources.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(block).ifPresent(nodes::add);
        catches.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(finallyBlock).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTBlock getBlock() {
        return block;
    }

    @Override
    public List<JTCatch> getCatches() {
        return catches;
    }

    @Override
    public JTBlock getFinallyBlock() {
        return finallyBlock;
    }

    @Override
    public Kind getKind() {
        return Kind.TRY;
    }

    @Override
    public List<JTTree<?, ?>> getResources() {
        return resources;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_BLOCK, propertyName -> v8Runtime.toV8Value(getBlock()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_CATCHES, propertyName -> v8Runtime.toV8Value(getCatches()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_FINALLY_BLOCK, propertyName -> v8Runtime.toV8Value(getFinallyBlock()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_RESOURCES, propertyName -> v8Runtime.toV8Value(getResources()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_BLOCK,
                    (propertyName, propertyValue) -> replaceBlock(this::setBlock, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_CATCHES,
                    (propertyName, propertyValue) -> replaceCatches(catches, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_FINALLY_BLOCK,
                    (propertyName, propertyValue) -> replaceBlock(this::setFinallyBlock, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_RESOURCES,
                    (propertyName, propertyValue) -> replaceTrees(resources, propertyValue));
        }
        return stringSetterMap;
    }

    public JTTry setBlock(JTBlock block) {
        if (this.block == block) {
            return this;
        }
        this.block = Objects.requireNonNull(block).setParentTree(this);
        return setActionChange();
    }

    public JTTry setFinallyBlock(JTBlock finallyBlock) {
        if (this.finallyBlock == finallyBlock) {
            return this;
        }
        this.finallyBlock = Optional.ofNullable(finallyBlock).map(o -> o.setParentTree(this)).orElse(null);
        return setActionChange();
    }
}
