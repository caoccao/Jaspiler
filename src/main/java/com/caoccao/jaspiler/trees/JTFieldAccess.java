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
import com.caoccao.jaspiler.styles.IStyleWriter;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The type Jt field access.
 * It references com.sun.tools.javac.tree.JCTree.JCFieldAccess.
 */
public final class JTFieldAccess
        extends JTExpression<MemberSelectTree, JTFieldAccess>
        implements MemberSelectTree {
    private static final String PROPERTY_EXPRESSION = "expression";
    private static final String PROPERTY_IDENTIFIER = "identifier";
    private JTExpression<?, ?> expression;
    private JTName identifier;

    public JTFieldAccess() {
        this(null, null);
        setActionChange();
    }

    JTFieldAccess(MemberSelectTree originalTree, JTTree<?, ?> parentTree) {
        super(originalTree, parentTree);
        expression = null;
        identifier = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitMemberSelect(this, data);
    }

    @Override
    JTFieldAccess analyze() {
        super.analyze();
        expression = JTTreeFactory.create(getOriginalTree().getExpression(), this);
        identifier = JTTreeFactory.createName(getOriginalTree().getIdentifier());
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(expression).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTExpression<?, ?> getExpression() {
        return expression;
    }

    @Override
    public JTName getIdentifier() {
        return identifier;
    }

    @Override
    public Kind getKind() {
        return Kind.MEMBER_SELECT;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_EXPRESSION, propertyName -> v8Runtime.toV8Value(getExpression()));
            registerStringGetter(PROPERTY_IDENTIFIER, propertyName -> v8Runtime.toV8Value(getIdentifier()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_EXPRESSION, (propertyName, propertyValue) -> replaceExpression(this::setExpression, propertyValue));
            registerStringSetter(PROPERTY_IDENTIFIER, (propertyName, propertyValue) -> replaceName(this::setIdentifier, propertyValue));
        }
        return stringSetterMap;
    }

    @Override
    public boolean serialize(IStyleWriter<?> writer) {
        if (isActionChange()) {
            if (expression != null) {
                writer.append(expression).appendDot();
            }
            if (identifier != null) {
                writer.append(identifier);
            }
            return true;
        }
        return super.serialize(writer);
    }

    public JTFieldAccess setExpression(JTExpression<?, ?> expression) {
        if (this.expression == expression) {
            return this;
        }
        this.expression = Objects.requireNonNull(expression).setParentTree(this);
        return setActionChange();
    }

    public JTFieldAccess setIdentifier(JTName identifier) {
        if (this.identifier == identifier) {
            return this;
        }
        this.identifier = Objects.requireNonNull(identifier);
        return setActionChange();
    }
}
