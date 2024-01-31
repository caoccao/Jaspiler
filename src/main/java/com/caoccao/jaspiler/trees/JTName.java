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
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.proxy.IJavetDirectProxyHandler;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.caoccao.javet.values.reference.V8ValueSymbol;
import com.caoccao.javet.values.reference.builtin.V8ValueBuiltInSymbol;

import javax.lang.model.element.Name;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class JTName implements Name, IJavetDirectProxyHandler<JaspilerCheckedException> {
    public static final String PROPERTY_VALUE = "value";
    private static final String FUNCTION_TO_STRING = "toString";
    private Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> stringGetterMap;
    private Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> stringSetterMap;
    private Map<String, IJavetUniFunction<V8ValueSymbol, ? extends V8Value, JaspilerCheckedException>> symbolGetterMap;
    private V8Runtime v8Runtime;
    private String value;

    public JTName(String value) {
        stringGetterMap = null;
        stringSetterMap = null;
        symbolGetterMap = null;
        setV8Runtime(null);
        setValue(value);
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public boolean contentEquals(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        return value.equals(cs.toString());
    }

    @Override
    public V8Runtime getV8Runtime() {
        return v8Runtime;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            stringGetterMap = new HashMap<>();
            registerStringGetter(PROPERTY_VALUE, property -> v8Runtime.createV8ValueString(getValue()));
            registerStringGetterFunction(FUNCTION_TO_STRING, property -> v8Runtime.createV8ValueString(toString()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            stringSetterMap = new HashMap<>();
            registerStringSetter(PROPERTY_VALUE, (propertyName, propertyValue) -> setValue(propertyValue));
        }
        return stringSetterMap;
    }

    @Override
    public Map<String, IJavetUniFunction<V8ValueSymbol, ? extends V8Value, JaspilerCheckedException>> proxyGetSymbolGetterMap() {
        if (symbolGetterMap == null) {
            symbolGetterMap = new HashMap<>();
            registerSymbolGetterFunction(V8ValueBuiltInSymbol.SYMBOL_PROPERTY_TO_PRIMITIVE, description -> v8Runtime.createV8ValueString(toString()));
        }
        return symbolGetterMap;
    }

    @Override
    public void setV8Runtime(V8Runtime v8Runtime) {
        this.v8Runtime = v8Runtime;
    }

    private boolean setValue(V8Value v8Value) {
        if (v8Value instanceof V8ValueString v8ValueStringValue) {
            setValue(v8ValueStringValue.getValue());
            return true;
        }
        return false;
    }

    public JTName setValue(String value) {
        this.value = Objects.requireNonNull(value);
        return this;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    @Override
    public String toString() {
        return value;
    }
}
