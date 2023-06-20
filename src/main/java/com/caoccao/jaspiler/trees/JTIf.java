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
import com.sun.source.tree.IfTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JTIf
        extends JTStatement<IfTree, JTIf>
        implements IfTree {
    private static final String PROPERTY_CONDITION = "condition";
    private static final String PROPERTY_ELSE_STATEMENT = "elseStatement";
    private static final String PROPERTY_THEN_STATEMENT = "thenStatement";
    private JTExpression<?, ?> condition;
    private JTStatement<?, ?> elseStatement;
    private JTStatement<?, ?> thenStatement;

    public JTIf() {
        this(null, null);
        setActionChange();
    }

    JTIf(IfTree ifTree, JTTree<?, ?> parentTree) {
        super(ifTree, parentTree);
        condition = null;
        elseStatement = null;
        thenStatement = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitIf(this, data);
    }

    @Override
    JTIf analyze() {
        super.analyze();
        condition = JTTreeFactory.create(getOriginalTree().getCondition(), this);
        thenStatement = JTTreeFactory.create(getOriginalTree().getThenStatement(), this);
        elseStatement = JTTreeFactory.create(getOriginalTree().getElseStatement(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(condition).ifPresent(nodes::add);
        Optional.ofNullable(thenStatement).ifPresent(nodes::add);
        Optional.ofNullable(elseStatement).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTExpression<?, ?> getCondition() {
        return condition;
    }

    @Override
    public JTStatement<?, ?> getElseStatement() {
        return elseStatement;
    }

    @Override
    public Kind getKind() {
        return Kind.IF;
    }

    @Override
    public JTStatement<?, ?> getThenStatement() {
        return thenStatement;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_CONDITION, propertyName -> v8Runtime.toV8Value(getCondition()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_ELSE_STATEMENT, propertyName -> v8Runtime.toV8Value(getElseStatement()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_THEN_STATEMENT, propertyName -> v8Runtime.toV8Value(getThenStatement()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_CONDITION,
                    (propertyName, propertyValue) -> replaceExpression(this::setCondition, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_ELSE_STATEMENT,
                    (propertyName, propertyValue) -> replaceStatement(this::setElseStatement, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_THEN_STATEMENT,
                    (propertyName, propertyValue) -> replaceStatement(this::setThenStatement, propertyValue));
        }
        return stringSetterMap;
    }

    public JTIf setCondition(JTExpression<?, ?> condition) {
        if (this.condition == condition) {
            return this;
        }
        this.condition = Objects.requireNonNull(condition).setParentTree(this);
        return setActionChange();
    }

    public JTIf setElseStatement(JTStatement<?, ?> elseStatement) {
        if (this.elseStatement == elseStatement) {
            return this;
        }
        this.elseStatement = Optional.ofNullable(elseStatement).map(o -> o.setParentTree(this)).orElse(null);
        return setActionChange();
    }

    public JTIf setThenStatement(JTStatement<?, ?> thenStatement) {
        if (this.thenStatement == thenStatement) {
            return this;
        }
        this.thenStatement = Objects.requireNonNull(thenStatement).setParentTree(this);
        return setActionChange();
    }
}
