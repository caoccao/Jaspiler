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
import com.caoccao.jaspiler.exceptions.JaspilerNotSupportedException;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.sun.source.tree.PrimitiveTypeTree;
import com.sun.source.tree.TreeVisitor;

import javax.lang.model.type.TypeKind;
import java.util.Map;
import java.util.Objects;

public final class JTPrimitiveType
        extends JTExpression<PrimitiveTypeTree, JTPrimitiveType>
        implements PrimitiveTypeTree {
    private static final String PROPERTY_PRIMITIVE_TYPE_KIND = "primitiveTypeKind";
    private TypeKind primitiveTypeKind;

    public JTPrimitiveType() {
        this(null, null);
        setActionChange();
    }

    JTPrimitiveType(PrimitiveTypeTree primitiveTypeTree, JTTree<?, ?> parentTree) {
        super(primitiveTypeTree, parentTree);
        primitiveTypeKind = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitPrimitiveType(this, data);
    }

    @Override
    JTPrimitiveType analyze() {
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

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_PRIMITIVE_TYPE_KIND, propertyName -> v8Runtime.createV8ValueString(getPrimitiveTypeKind().name()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_PRIMITIVE_TYPE_KIND, (propertyName, propertyValue) -> setPrimitiveTypeKind(propertyValue));
        }
        return stringSetterMap;
    }

    private boolean setPrimitiveTypeKind(V8Value v8Value) {
        if (v8Value instanceof V8ValueString v8ValueString) {
            setPrimitiveTypeKind(TypeKind.valueOf(v8ValueString.getValue()));
            return true;
        }
        return false;
    }

    public JTPrimitiveType setPrimitiveTypeKind(TypeKind primitiveTypeKind) {
        if (this.primitiveTypeKind == primitiveTypeKind) {
            return this;
        }
        switch (Objects.requireNonNull(primitiveTypeKind)) {
            case BOOLEAN, BYTE, SHORT, INT, LONG, CHAR, FLOAT, DOUBLE, VOID -> {
                this.primitiveTypeKind = primitiveTypeKind;
                return setActionChange();
            }
            default -> throw new JaspilerNotSupportedException(primitiveTypeKind.name() + " is not supported.");
        }
    }
}
