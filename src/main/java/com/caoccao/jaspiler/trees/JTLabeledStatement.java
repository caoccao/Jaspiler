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
import com.sun.source.tree.LabeledStatementTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JTLabeledStatement
        extends JTStatement<LabeledStatementTree, JTLabeledStatement>
        implements LabeledStatementTree {
    private static final String PROPERTY_LABEL = "label";
    private static final String PROPERTY_STATEMENT = "statement";
    private JTName label;
    private JTStatement<?, ?> statement;

    public JTLabeledStatement() {
        this(null, null);
        setActionChange();
    }

    JTLabeledStatement(LabeledStatementTree labeledStatementTree, JTTree<?, ?> parentTree) {
        super(labeledStatementTree, parentTree);
        label = null;
        statement = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitLabeledStatement(this, data);
    }

    @Override
    JTLabeledStatement analyze() {
        super.analyze();
        label = JTTreeFactory.createName(getOriginalTree().getLabel());
        statement = JTTreeFactory.create(getOriginalTree().getStatement(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(statement).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.LABELED_STATEMENT;
    }

    @Override
    public JTName getLabel() {
        return label;
    }

    @Override
    public JTStatement<?, ?> getStatement() {
        return statement;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_LABEL, propertyName -> v8Runtime.toV8Value(getLabel()));
            registerStringGetter(PROPERTY_STATEMENT, propertyName -> v8Runtime.toV8Value(getStatement()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_LABEL, (propertyName, propertyValue) -> replaceName(this::setLabel, propertyValue));
            registerStringSetter(PROPERTY_STATEMENT, (propertyName, propertyValue) -> replaceStatement(this::setStatement, propertyValue));
        }
        return stringSetterMap;
    }

    public JTLabeledStatement setLabel(JTName label) {
        if (this.label == label) {
            return this;
        }
        this.label = Objects.requireNonNull(label);
        return setActionChange();
    }

    public JTLabeledStatement setStatement(JTStatement<?, ?> statement) {
        if (this.statement == statement) {
            return this;
        }
        this.statement = Objects.requireNonNull(statement).setParentTree(this);
        return setActionChange();
    }
}
