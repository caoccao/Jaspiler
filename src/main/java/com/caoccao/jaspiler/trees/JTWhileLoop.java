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
import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.WhileLoopTree;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JTWhileLoop
        extends JTStatement<WhileLoopTree, JTWhileLoop>
        implements WhileLoopTree {
    private static final String PROPERTY_CONDITION = "condition";
    private static final String PROPERTY_STATEMENT = "statement";
    private JTExpression<?, ?> condition;
    private JTStatement<?, ?> statement;

    public JTWhileLoop() {
        this(null, null);
        setActionChange();
    }

    JTWhileLoop(WhileLoopTree whileLoopTree, JTTree<?, ?> parentTree) {
        super(whileLoopTree, parentTree);
        condition = null;
        statement = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitWhileLoop(this, data);
    }

    @Override
    JTWhileLoop analyze() {
        super.analyze();
        condition = JTTreeFactory.create(getOriginalTree().getCondition(), this);
        statement = JTTreeFactory.create(getOriginalTree().getStatement(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(condition).ifPresent(nodes::add);
        Optional.ofNullable(statement).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTExpression<?, ?> getCondition() {
        return condition;
    }

    @Override
    public Kind getKind() {
        return Kind.WHILE_LOOP;
    }

    @Override
    public JTStatement<?, ?> getStatement() {
        return statement;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_CONDITION, propertyName -> v8Runtime.toV8Value(getCondition()));
            registerStringGetter(PROPERTY_STATEMENT, propertyName -> v8Runtime.toV8Value(getStatement()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_CONDITION, (propertyName, propertyValue) -> replaceExpression(this::setCondition, propertyValue));
            registerStringSetter(PROPERTY_STATEMENT, (propertyName, propertyValue) -> replaceStatement(this::setStatement, propertyValue));
        }
        return stringSetterMap;
    }

    public JTWhileLoop setCondition(JTExpression<?, ?> condition) {
        if (this.condition == condition) {
            return this;
        }
        this.condition = Objects.requireNonNull(condition).setParentTree(this);
        return setActionChange();
    }

    public JTWhileLoop setStatement(JTStatement<?, ?> statement) {
        if (this.statement == statement) {
            return this;
        }
        this.statement = Objects.requireNonNull(statement).setParentTree(this);
        return setActionChange();
    }
}
