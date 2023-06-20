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
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.proxy.IJavetDirectProxyHandler;
import com.caoccao.javet.values.V8Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class JTFloat
        implements IJavetDirectProxyHandler<JaspilerCheckedException> {
    private static final String PROPERTY_VALUE = "value";
    private Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> stringGetterMap;
    private V8Runtime v8Runtime;
    private Float value;

    public JTFloat(Float value) {
        v8Runtime = null;
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public V8Runtime getV8Runtime() {
        return v8Runtime;
    }

    public Float getValue() {
        return value;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            stringGetterMap = new HashMap<>();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_VALUE, propertyName -> v8Runtime.createV8ValueDouble(getValue()));
        }
        return stringGetterMap;
    }

    @Override
    public void setV8Runtime(V8Runtime v8Runtime) {
        this.v8Runtime = v8Runtime;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
