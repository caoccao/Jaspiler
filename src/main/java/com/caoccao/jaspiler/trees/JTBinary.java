/*
 * Copyright (c) 2023-2025. caoccao.com Sam Cao
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
import com.caoccao.jaspiler.exceptions.JaspilerNotSupportedException;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.tree.BinaryTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JTBinary
        extends JTOperatorExpression<BinaryTree, JTBinary>
        implements BinaryTree {
    private static final String PROPERTY_KIND = "kind";
    private static final String PROPERTY_LEFT_OPERAND = "leftOperand";
    private static final String PROPERTY_RIGHT_OPERAND = "rightOperand";
    private Kind kind;
    private JTExpression<?, ?> leftOperand;
    private JTExpression<?, ?> rightOperand;

    public JTBinary() {
        this(null, null);
        setActionChange();
    }

    JTBinary(BinaryTree binaryTree, JTTree<?, ?> parentTree) {
        super(binaryTree, parentTree);
        kind = null;
        leftOperand = null;
        rightOperand = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitBinary(this, data);
    }

    @Override
    JTBinary analyze() {
        super.analyze();
        leftOperand = JTTreeFactory.create(getOriginalTree().getLeftOperand(), this);
        rightOperand = JTTreeFactory.create(getOriginalTree().getRightOperand(), this);
        kind = getOriginalTree().getKind();
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(leftOperand).ifPresent(nodes::add);
        Optional.ofNullable(rightOperand).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return kind;
    }

    @Override
    public JTExpression<?, ?> getLeftOperand() {
        return leftOperand;
    }

    @Override
    public JTExpression<?, ?> getRightOperand() {
        return rightOperand;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_LEFT_OPERAND, propertyName -> v8Runtime.toV8Value(getLeftOperand()));
            registerStringGetter(PROPERTY_RIGHT_OPERAND, propertyName -> v8Runtime.toV8Value(getRightOperand()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_LEFT_OPERAND, (propertyName, propertyValue) -> replaceExpression(this::setLeftOperand, propertyValue));
            registerStringSetter(PROPERTY_KIND, (propertyName, propertyValue) -> replaceKind(this::setKind, propertyValue));
            registerStringSetter(PROPERTY_RIGHT_OPERAND, (propertyName, propertyValue) -> replaceExpression(this::setRightOperand, propertyValue));
        }
        return stringSetterMap;
    }

    public JTBinary setKind(Kind kind) {
        if (this.kind == kind) {
            return this;
        }
        switch (Objects.requireNonNull(kind)) {
            case AND, CONDITIONAL_AND, CONDITIONAL_OR, DIVIDE, EQUAL_TO,
                    GREATER_THAN, GREATER_THAN_EQUAL, LEFT_SHIFT, LESS_THAN, LESS_THAN_EQUAL,
                    MINUS, MULTIPLY, NOT_EQUAL_TO, OR, PLUS,
                    REMAINDER, RIGHT_SHIFT, UNSIGNED_RIGHT_SHIFT, XOR -> this.kind = kind;
            default -> throw new JaspilerNotSupportedException(kind.name() + " is not supported.");
        }
        return setActionChange();
    }

    public JTBinary setLeftOperand(JTExpression<?, ?> leftOperand) {
        if (this.leftOperand == leftOperand) {
            return this;
        }
        this.leftOperand = Objects.requireNonNull(leftOperand).setParentTree(this);
        return setActionChange();
    }

    public JTBinary setRightOperand(JTExpression<?, ?> rightOperand) {
        if (this.rightOperand == rightOperand) {
            return this;
        }
        this.rightOperand = Objects.requireNonNull(rightOperand).setParentTree(this);
        return setActionChange();
    }
}
