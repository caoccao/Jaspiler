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

import com.caoccao.jaspiler.exceptions.JaspilerNotSupportedException;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.TreeVisitor;

import javax.lang.model.type.TypeKind;
import java.util.Objects;

public final class JTPrimitiveTypeTree
        extends JTExpression<PrimitiveTypeTree, JTPrimitiveTypeTree>
        implements PrimitiveTypeTree {
    private TypeKind primitiveTypeKind;

    public JTPrimitiveTypeTree() {
        this(null, null);
        setActionChange();
    }

    JTPrimitiveTypeTree(PrimitiveTypeTree primitiveTypeTree, JTTree<?, ?> parentTree) {
        super(primitiveTypeTree, parentTree);
        primitiveTypeKind = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitPrimitiveType(this, data);
    }

    @Override
    JTPrimitiveTypeTree analyze() {
        super.analyze();
        primitiveTypeKind = getOriginalTree().getPrimitiveTypeKind();
        return this;
    }

    @Override
    public Kind getKind() {
        return Kind.PRIMITIVE_TYPE;
    }

    @Override
    public TypeKind getPrimitiveTypeKind() {
        return primitiveTypeKind;
    }

    public JTPrimitiveTypeTree setPrimitiveTypeKind(TypeKind primitiveTypeKind) {
        if (this.primitiveTypeKind == Objects.requireNonNull(primitiveTypeKind)) {
            return this;
        }
        switch (primitiveTypeKind) {
            case BOOLEAN, BYTE, SHORT, INT, LONG, CHAR, FLOAT, DOUBLE, VOID -> {
                this.primitiveTypeKind = primitiveTypeKind;
                return setActionChange();
            }
            default -> throw new JaspilerNotSupportedException(primitiveTypeKind.name() + " is not supported.");
        }
    }
}
