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
import com.sun.source.tree.EnhancedForLoopTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JTEnhancedForLoop
        extends JTStatement<EnhancedForLoopTree, JTEnhancedForLoop>
        implements EnhancedForLoopTree {
    private static final String PROPERTY_EXPRESSION = "expression";
    private static final String PROPERTY_STATEMENT = "statement";
    private static final String PROPERTY_VARIABLE = "variable";
    private JTExpression<?, ?> expression;
    private JTStatement<?, ?> statement;
    private JTVariableDecl variable;
    public JTEnhancedForLoop() {
        this(null, null);
        setActionChange();
        expression = null;
        statement = null;
        variable = null;
    }
    JTEnhancedForLoop(EnhancedForLoopTree enhancedForLoopTree, JTTree<?, ?> parentTree) {
        super(enhancedForLoopTree, parentTree);
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitEnhancedForLoop(this, data);
    }

    @Override
    JTEnhancedForLoop analyze() {
        super.analyze();
        variable = JTTreeFactory.create(getOriginalTree().getVariable(), this, JTVariableDecl::new);
        expression = JTTreeFactory.create(getOriginalTree().getExpression(), this);
        statement = JTTreeFactory.create(getOriginalTree().getStatement(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(variable).ifPresent(nodes::add);
        Optional.ofNullable(expression).ifPresent(nodes::add);
        Optional.ofNullable(statement).ifPresent(nodes::add);
        return nodes;
    }

    @Override
    public JTExpression<?, ?> getExpression() {
        return expression;
    }

    @Override
    public Kind getKind() {
        return Kind.ENHANCED_FOR_LOOP;
    }

    @Override
    public JTStatement<?, ?> getStatement() {
        return statement;
    }

    @Override
    public JTVariableDecl getVariable() {
        return variable;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_EXPRESSION, propertyName -> v8Runtime.toV8Value(getExpression()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_STATEMENT, propertyName -> v8Runtime.toV8Value(getStatement()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_VARIABLE, propertyName -> v8Runtime.toV8Value(getVariable()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_EXPRESSION,
                    (propertyName, propertyValue) -> replaceExpression(this::setExpression, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_STATEMENT,
                    (propertyName, propertyValue) -> replaceStatement(this::setStatement, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_VARIABLE,
                    (propertyName, propertyValue) -> replaceVariableDecl(this::setVariable, propertyValue));
        }
        return stringSetterMap;
    }

    public JTEnhancedForLoop setExpression(JTExpression<?, ?> expression) {
        if (this.expression == expression) {
            return this;
        }
        this.expression = Objects.requireNonNull(expression).setParentTree(this);
        return setActionChange();
    }

    public JTEnhancedForLoop setStatement(JTStatement<?, ?> statement) {
        if (this.statement == statement) {
            return this;
        }
        this.statement = Objects.requireNonNull(statement).setParentTree(this);
        return setActionChange();
    }

    public JTEnhancedForLoop setVariable(JTVariableDecl variable) {
        if (this.variable == variable) {
            return this;
        }
        this.variable = Objects.requireNonNull(variable).setParentTree(this);
        return setActionChange();
    }
}
