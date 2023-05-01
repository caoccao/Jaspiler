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

import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.TreeVisitor;

import java.util.Objects;

public final class JTLiteral
        extends JTExpression<LiteralTree, JTLiteral>
        implements LiteralTree {
    private Kind kind;
    private Object value;

    public JTLiteral() {
        this(null, null);
        setActionChange();
    }

    JTLiteral(LiteralTree literalTree, JTTree<?, ?> parentTree) {
        super(literalTree, parentTree);
        kind = null;
        value = null;
    }

    private static Kind parseKind(Object value) {
        if (value instanceof Integer) {
            return Kind.INT_LITERAL;
        }
        if (value instanceof Long) {
            return Kind.LONG_LITERAL;
        }
        if (value instanceof Float) {
            return Kind.FLOAT_LITERAL;
        }
        if (value instanceof Double) {
            return Kind.DOUBLE_LITERAL;
        }
        if (value instanceof Boolean) {
            return Kind.BOOLEAN_LITERAL;
        }
        if (value instanceof Character) {
            return Kind.CHAR_LITERAL;
        }
        if (value instanceof String) {
            return Kind.STRING_LITERAL;
        }
        if (value == null) {
            return Kind.NULL_LITERAL;
        }
        throw new AssertionError(value + " is not supported.");
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitLiteral(this, data);
    }

    @Override
    JTLiteral analyze() {
        super.analyze();
        kind = getOriginalTree().getKind();
        value = getOriginalTree().getValue();
        return this;
    }

    @Override
    public Kind getKind() {
        return kind;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public JTLiteral setValue(Object value) {
        if (this.value == value) {
            return this;
        }
        this.value = Objects.requireNonNull(value);
        kind = parseKind(this.value);
        return setActionChange();
    }
}
