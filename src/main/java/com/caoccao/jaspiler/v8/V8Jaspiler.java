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

package com.caoccao.jaspiler.v8;

import com.caoccao.jaspiler.JaspilerCompiler;
import com.caoccao.jaspiler.exceptions.JaspilerArgumentException;
import com.caoccao.jaspiler.exceptions.JaspilerCheckedException;
import com.caoccao.jaspiler.exceptions.JaspilerParseException;
import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.V8Scope;
import com.caoccao.javet.interop.callback.IJavetDirectCallable;
import com.caoccao.javet.interop.callback.JavetCallbackContext;
import com.caoccao.javet.interop.callback.JavetCallbackType;
import com.caoccao.javet.interop.proxy.IJavetDirectProxyHandler;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.caoccao.javet.values.reference.V8ValueObject;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class V8Jaspiler
        extends BaseLoggingObject
        implements IJavetDirectProxyHandler<JaspilerCheckedException>, AutoCloseable {
    public static final String NAME = "jaspiler";
    protected static final String FUNCTION_PARSE_SYNC = "parseSync";
    protected static final String PROPERTIES_AST = "ast";
    protected JaspilerCompiler jaspilerCompiler;
    protected Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> stringGetterMap;
    protected V8Runtime v8Runtime;

    public V8Jaspiler(V8Runtime v8Runtime) {
        super();
        jaspilerCompiler = new JaspilerCompiler();
        stringGetterMap = null;
        this.v8Runtime = v8Runtime;
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public V8Runtime getV8Runtime() {
        return v8Runtime;
    }

    public V8Value parseSync(V8Value... v8Values) throws JavetException, JaspilerCheckedException {
        String filePath = validateString(FUNCTION_PARSE_SYNC, v8Values, 0);
        try (V8Scope v8Scope = v8Runtime.getV8Scope()) {
            var scanner = new V8JaspilerParseScanner();
            jaspilerCompiler.clearJavaFileObject();
            jaspilerCompiler.addJavaFileObjects(new File(filePath));
            jaspilerCompiler.parse(scanner);
            V8ValueObject v8ValueObjectResult = v8Scope.createV8ValueObject();
            v8ValueObjectResult.set(
                    PROPERTIES_AST,
                    "TODO jaspilerCompiler.getParseContexts().get(0).getCompilationUnitTree()");
            v8Scope.setEscapable();
            return v8ValueObjectResult;
        } catch (IOException e) {
            throw new JaspilerParseException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            stringGetterMap = new HashMap<>();
            stringGetterMap.put(
                    FUNCTION_PARSE_SYNC,
                    (propertyName) -> v8Runtime.createV8ValueFunction(
                            new JavetCallbackContext(
                                    propertyName,
                                    JavetCallbackType.DirectCallNoThisAndResult,
                                    (IJavetDirectCallable.NoThisAndResult<?>) this::parseSync)));
        }
        return stringGetterMap;
    }

    protected void validateLength(
            String functionName, V8Value[] v8Values, int index)
            throws JaspilerArgumentException {
        if (v8Values == null || v8Values.length < index || index < 0) {
            throw new JaspilerArgumentException(
                    MessageFormat.format("Argument count mismatches in {0}.", functionName));
        }
    }

    protected String validateString(
            String functionName, V8Value[] v8Values, int index)
            throws JaspilerArgumentException {
        validateLength(functionName, v8Values, index);
        V8Value v8Value = v8Values[0];
        if (v8Value instanceof V8ValueString v8ValueString) {
            return v8ValueString.getValue();
        }
        throw new JaspilerArgumentException(
                MessageFormat.format("Argument type mismatches in {0}. String is expected.", functionName));
    }
}
