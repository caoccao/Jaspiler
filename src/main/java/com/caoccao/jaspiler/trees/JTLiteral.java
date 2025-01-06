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
import com.caoccao.jaspiler.styles.IStyleWriter;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.primitive.*;
import com.caoccao.javet.values.reference.V8ValueObject;
import com.sun.source.tree.LiteralTree;
import com.sun.source.tree.TreeVisitor;

import java.util.Map;

public final class JTLiteral
        extends JTExpression<LiteralTree, JTLiteral>
        implements LiteralTree {
    private static final String DEFAULT_BOOLEAN = "false";
    private static final String DEFAULT_CHAR = "\\0";
    private static final String DEFAULT_DOUBLE = "0D";
    private static final String DEFAULT_FLOAT = "0F";
    private static final String DEFAULT_INT = "0";
    private static final String DEFAULT_LONG = "0L";
    private static final String PROPERTY_VALUE = "value";
    private Kind kind;
    private Object value;

    public JTLiteral() {
        this(null, null);
        setActionChange();
    }

    JTLiteral(LiteralTree literalTree, JTTree<?, ?> parentTree) {
        super(literalTree, parentTree);
        kind = Kind.NULL_LITERAL;
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

    private V8Value getV8Value() throws JavetException {
        if (value instanceof Integer valueInteger) {
            return v8Runtime.createV8ValueInteger(valueInteger);
        }
        if (value instanceof Long valueLong) {
            return v8Runtime.createV8ValueLong(valueLong);
        }
        if (value instanceof Float valueFloat) {
            return v8Runtime.toV8Value(new JTFloat(valueFloat));
        }
        if (value instanceof Double valueDouble) {
            return v8Runtime.createV8ValueDouble(valueDouble);
        }
        if (value instanceof Boolean valueBoolean) {
            return v8Runtime.createV8ValueBoolean(valueBoolean);
        }
        if (value instanceof Character valueCharacter) {
            return v8Runtime.toV8Value(new JTCharacter(valueCharacter));
        }
        if (value instanceof String valueString) {
            return v8Runtime.toV8Value(valueString);
        }
        return v8Runtime.createV8ValueNull();
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_VALUE, propertyName -> getV8Value());
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_VALUE, (propertyName, propertyValue) -> setValue(propertyValue));
            registerStringSetter(PROPERTY_KIND, (propertyName, propertyValue) -> replaceKind(this::setKind, propertyValue));
        }
        return stringSetterMap;
    }

    @Override
    public boolean serialize(IStyleWriter<?> writer) {
        if (isActionChange()) {
            switch (kind) {
                case BOOLEAN_LITERAL -> writer.append(value == null ? DEFAULT_BOOLEAN : String.valueOf(value));
                case CHAR_LITERAL -> {
                    String charString = DEFAULT_CHAR;
                    if (value instanceof Character character) {
                        charString = character.toString();
                    } else if (value != null) {
                        charString = String.valueOf(value);
                    }
                    writer.appendJavaCharacter(charString);
                }
                case DOUBLE_LITERAL -> writer.append(value == null ? DEFAULT_DOUBLE : String.valueOf(value));
                case FLOAT_LITERAL -> writer.append(value == null ? DEFAULT_FLOAT : String.valueOf(value));
                case INT_LITERAL -> writer.append(value == null ? DEFAULT_INT : String.valueOf(value));
                case LONG_LITERAL -> writer.append(value == null ? DEFAULT_LONG : String.valueOf(value));
                case NULL_LITERAL -> writer.append((String) null);
                case STRING_LITERAL -> {
                    if (value == null) {
                        writer.append((String) null);
                    } else {
                        writer.appendJavaString(String.valueOf(value));
                    }
                }
            }
            return true;
        }
        return super.serialize(writer);
    }

    public JTLiteral setKind(Kind kind) {
        switch (kind) {
            case INT_LITERAL, LONG_LITERAL, FLOAT_LITERAL, DOUBLE_LITERAL, BOOLEAN_LITERAL,
                    CHAR_LITERAL, STRING_LITERAL, NULL_LITERAL -> this.kind = kind;
            default -> this.kind = Kind.NULL_LITERAL;
        }
        return setActionChange();
    }

    private boolean setValue(V8Value v8Value) throws JavetException {
        Object valueObject = null;
        if (v8Value instanceof V8ValueInteger v8ValueInteger) {
            valueObject = v8ValueInteger.getValue();
        } else if (v8Value instanceof V8ValueLong v8ValueLong) {
            valueObject = v8ValueLong.getValue();
        } else if (v8Value instanceof V8ValueDouble v8ValueDouble) {
            valueObject = v8ValueDouble.getValue();
        } else if (v8Value instanceof V8ValueBoolean v8ValueBoolean) {
            valueObject = v8ValueBoolean.getValue();
        } else if (v8Value instanceof V8ValueString v8ValueString) {
            valueObject = v8ValueString.getValue();
        } else if (v8Value instanceof V8ValueObject v8ValueObject) {
            valueObject = v8Runtime.toObject(v8ValueObject);
            if (!(valueObject instanceof Float || valueObject instanceof Character)) {
                valueObject = null;
            }
        }
        setValue(valueObject);
        return true;
    }

    public JTLiteral setValue(Object value) {
        if (this.value == value) {
            return this;
        }
        this.value = value;
        kind = parseKind(this.value);
        return setActionChange();
    }
}
