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

package com.caoccao.jaspiler.utils;

import com.caoccao.jaspiler.exceptions.JaspilerCheckedException;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.callback.IJavetDirectCallable;
import com.caoccao.javet.interop.callback.JavetCallbackContext;
import com.caoccao.javet.interop.callback.JavetCallbackType;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.reference.V8ValueSymbol;

import java.util.Map;

public final class V8Register {
    private V8Register() {
    }

    public static void putStringGetter(
            V8Runtime v8Runtime,
            Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> stringGetterMap,
            String propertyName,
            IJavetDirectCallable.NoThisAndResult<?> getter) {
        stringGetterMap.put(
                propertyName,
                innerPropertyName -> v8Runtime.createV8ValueFunction(
                        new JavetCallbackContext(
                                innerPropertyName,
                                JavetCallbackType.DirectCallNoThisAndResult,
                                getter)));
    }

    public static void putSymbolGetter(
            V8Runtime v8Runtime,
            Map<String, IJavetUniFunction<V8ValueSymbol, ? extends V8Value, JaspilerCheckedException>> symbolGetterMap,
            String propertyName,
            IJavetDirectCallable.NoThisAndResult<?> getter) {
        symbolGetterMap.put(
                propertyName,
                propertySymbol -> v8Runtime.createV8ValueFunction(
                        new JavetCallbackContext(
                                propertySymbol.getDescription(),
                                JavetCallbackType.DirectCallNoThisAndResult,
                                getter)));
    }
}
