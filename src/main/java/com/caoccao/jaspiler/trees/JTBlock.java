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

import com.caoccao.jaspiler.enums.JavaKeyword;
import com.caoccao.jaspiler.exceptions.JaspilerCheckedException;
import com.caoccao.jaspiler.styles.IStyleWriter;
import com.caoccao.jaspiler.utils.ForEachUtils;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class JTBlock
        extends JTStatement<BlockTree, JTBlock>
        implements BlockTree {
    private static final String PROPERTY_STATEMENTS = "statements";
    private static final String PROPERTY_STATIC = "static";
    private final List<JTStatement<?, ?>> statements;
    private boolean staticBlock;

    public JTBlock() {
        this(null, null);
        setActionChange();
    }

    JTBlock(BlockTree blockTree, JTTree<?, ?> parentTree) {
        super(blockTree, parentTree);
        statements = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitBlock(this, data);
    }

    @Override
    JTBlock analyze() {
        super.analyze();
        staticBlock = getOriginalTree().isStatic();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getStatements(), this, (JTStatement<?, ?> o) -> statements.add(o));
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        statements.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.BLOCK;
    }

    @Override
    public List<JTStatement<?, ?>> getStatements() {
        return statements;
    }

    @Override
    public boolean isStatic() {
        return staticBlock;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_STATEMENTS, propertyName -> v8Runtime.toV8Value(getStatements()));
            registerStringGetter(PROPERTY_STATIC, propertyName -> v8Runtime.createV8ValueBoolean(isStatic()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_STATEMENTS, (propertyName, propertyValue) -> replaceStatements(statements, propertyValue));
            registerStringSetter(PROPERTY_STATIC, (propertyName, propertyValue) -> replaceBoolean(this::setStatic, propertyValue));
        }
        return stringSetterMap;
    }

    @Override
    public boolean serialize(IStyleWriter<?> writer) {
        if (isActionChange()) {
            if (staticBlock) {
                writer.appendLineSeparator().appendIndent().appendKeyword(JavaKeyword.STATIC).appendSpace();
            }
            writer.appendBlockOpen();
            writer.increaseDepth();
            ForEachUtils.forEach(
                    statements.stream().filter(Objects::nonNull).filter(tree -> !tree.isActionIgnore()).toList(),
                    tree -> writer.appendIndent().append(tree).appendLineSeparator());
            writer.decreaseDepth();
            writer.appendIndent().appendBlockClose();
            return true;
        }
        return super.serialize(writer);
    }

    public JTBlock setStatic(boolean staticBlock) {
        if (this.staticBlock == staticBlock) {
            return this;
        }
        this.staticBlock = staticBlock;
        return setActionChange();
    }
}
