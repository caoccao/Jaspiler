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
import com.sun.source.tree.GuardedPatternTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("preview")
public final class JTGuardedPattern
        extends JTPattern<GuardedPatternTree, JTGuardedPattern>
        implements GuardedPatternTree {
    private static final String PROPERTY_EXPRESSION = "expression";
    private static final String PROPERTY_PATTERN = "pattern";
    private JTExpression<?, ?> expression;
    private JTPattern<?, ?> pattern;

    public JTGuardedPattern() {
        this(null, null);
        setActionChange();
    }

    JTGuardedPattern(GuardedPatternTree guardedPatternTree, JTTree<?, ?> parentTree) {
        super(guardedPatternTree, parentTree);
        expression = null;
        pattern = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitGuardedPattern(this, data);
    }

    @Override
    JTGuardedPattern analyze() {
        super.analyze();
        pattern = JTTreeFactory.create(getOriginalTree().getPattern(), this);
        expression = JTTreeFactory.create(getOriginalTree().getExpression(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(pattern).ifPresent(nodes::add);
        Optional.ofNullable(expression).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTExpression<?, ?> getExpression() {
        return expression;
    }

    @Override
    public Kind getKind() {
        return Kind.GUARDED_PATTERN;
    }

    @Override
    public JTPattern<?, ?> getPattern() {
        return pattern;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_EXPRESSION, propertyName -> v8Runtime.toV8Value(getExpression()));
            registerStringGetter(PROPERTY_PATTERN, propertyName -> v8Runtime.toV8Value(getPattern()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_EXPRESSION, (propertyName, propertyValue) -> replaceExpression(this::setExpression, propertyValue));
            registerStringSetter(PROPERTY_PATTERN, (propertyName, propertyValue) -> replacePattern(this::setPattern, propertyValue));
        }
        return stringSetterMap;
    }

    public JTGuardedPattern setExpression(JTExpression<?, ?> expression) {
        if (this.expression == expression) {
            return this;
        }
        this.expression = Objects.requireNonNull(expression).setParentTree(this);
        return setActionChange();
    }

    public JTGuardedPattern setPattern(JTPattern<?, ?> pattern) {
        if (this.pattern == pattern) {
            return this;
        }
        this.pattern = Objects.requireNonNull(pattern).setParentTree(this);
        return setActionChange();
    }
}
