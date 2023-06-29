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
import com.caoccao.jaspiler.exceptions.JaspilerExecutionException;
import com.caoccao.jaspiler.exceptions.JaspilerParseException;
import com.caoccao.jaspiler.styles.StandardStyleWriter;
import com.caoccao.jaspiler.trees.*;
import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interfaces.IJavetClosable;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.V8Scope;
import com.caoccao.javet.interop.callback.IJavetDirectCallable;
import com.caoccao.javet.interop.proxy.IJavetDirectProxyHandler;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.primitive.V8ValueDouble;
import com.caoccao.javet.values.primitive.V8ValueInteger;
import com.caoccao.javet.values.primitive.V8ValueLong;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.caoccao.javet.values.reference.V8ValueObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class V8Jaspiler
        extends BaseLoggingObject
        implements IJavetDirectProxyHandler<JaspilerCheckedException>, IJavetClosable {
    public static final String NAME = "jaspiler";
    private static final String FUNCTION_CREATE_CHARACTER = "createCharacter";
    private static final String FUNCTION_CREATE_FIELD_ACCESS = "createFieldAccess";
    private static final String FUNCTION_CREATE_FLOAT = "createFloat";
    private static final String FUNCTION_CREATE_IDENT = "createIdent";
    private static final String FUNCTION_CREATE_LITERAL = "createLiteral";
    private static final String FUNCTION_CREATE_NAME = "createName";
    private static final String FUNCTION_TRANSFORM_SYNC = "transformSync";
    private static final String PROPERTIES_AST = "ast";
    private static final String PROPERTIES_CODE = "code";
    private static final Map<String, Supplier<JTTree<?, ?>>> constructorMap;

    static {
        constructorMap = new HashMap<>();
        constructorMap.put("newAnnotation", JTAnnotation::new);
        constructorMap.put("newAnnotatedType", JTAnnotatedType::new);
        constructorMap.put("newArrayAccess", JTArrayAccess::new);
        constructorMap.put("newArrayType", JTArrayType::new);
        constructorMap.put("newAssert", JTAssert::new);
        constructorMap.put("newAssign", JTAssign::new);
        constructorMap.put("newAssignOp", JTAssignOp::new);
        constructorMap.put("newBinary", JTBinary::new);
        constructorMap.put("newBindingPattern", JTBindingPattern::new);
        constructorMap.put("newBlock", JTBlock::new);
        constructorMap.put("newBreak", JTBreak::new);
        constructorMap.put("newCase", JTCase::new);
        constructorMap.put("newCatch", JTCatch::new);
        constructorMap.put("newClassDecl", JTClassDecl::new);
        constructorMap.put("newConditional", JTConditional::new);
        constructorMap.put("newContinue", JTContinue::new);
        constructorMap.put("newDefaultCaseLabel", JTDefaultCaseLabel::new);
        constructorMap.put("newDoWhileLoop", JTDoWhileLoop::new);
        constructorMap.put("newEnhancedForLoop", JTEnhancedForLoop::new);
        constructorMap.put("newErroneous", JTErroneous::new);
        constructorMap.put("newExports", JTExports::new);
        constructorMap.put("newExpressionStatement", JTExpressionStatement::new);
        constructorMap.put("newFieldAccess", JTFieldAccess::new);
        constructorMap.put("newForLoop", JTForLoop::new);
        constructorMap.put("newGuardedPattern", JTGuardedPattern::new);
        constructorMap.put("newIdent", JTIdent::new);
        constructorMap.put("newIf", JTIf::new);
        constructorMap.put("newImport", JTImport::new);
        constructorMap.put("newInstanceOf", JTInstanceOf::new);
        constructorMap.put("newLabeledStatement", JTLabeledStatement::new);
        constructorMap.put("newLambda", JTLambda::new);
        constructorMap.put("newLiteral", JTLiteral::new);
        constructorMap.put("newMemberReference", JTMemberReference::new);
        constructorMap.put("newMethodDecl", JTMethodDecl::new);
        constructorMap.put("newMethodInvocation", JTMethodInvocation::new);
        constructorMap.put("newModifiers", JTModifiers::new);
        constructorMap.put("newModuleDecl", JTModuleDecl::new);
        constructorMap.put("newNewArray", JTNewArray::new);
        constructorMap.put("newNewClass", JTNewClass::new);
        constructorMap.put("newOpens", JTOpens::new);
        constructorMap.put("newPackageDecl", JTPackageDecl::new);
        constructorMap.put("newParens", JTParens::new);
        constructorMap.put("newParenthesizedPattern", JTParenthesizedPattern::new);
        constructorMap.put("newPrimitiveType", JTPrimitiveType::new);
        constructorMap.put("newProvides", JTProvides::new);
        constructorMap.put("newRequires", JTRequires::new);
        constructorMap.put("newReturn", JTReturn::new);
        constructorMap.put("newSkip", JTSkip::new);
        constructorMap.put("newSwitch", JTSwitch::new);
        constructorMap.put("newSwitchExpression", JTSwitchExpression::new);
        constructorMap.put("newSynchronized", JTSynchronized::new);
        constructorMap.put("newThrow", JTThrow::new);
        constructorMap.put("newTry", JTTry::new);
        constructorMap.put("newTypeApply", JTTypeApply::new);
        constructorMap.put("newTypeCast", JTTypeCast::new);
        constructorMap.put("newTypeIntersection", JTTypeIntersection::new);
        constructorMap.put("newTypeParameter", JTTypeParameter::new);
        constructorMap.put("newTypeUnion", JTTypeUnion::new);
        constructorMap.put("newUnary", JTUnary::new);
        constructorMap.put("newUses", JTUses::new);
        constructorMap.put("newVariableDecl", JTVariableDecl::new);
        constructorMap.put("newWhileLoop", JTWhileLoop::new);
        constructorMap.put("newWildcard", JTWildcard::new);
        constructorMap.put("newYield", JTYield::new);
    }

    private final Map<String, IJavetDirectCallable.NoThisAndResult<?>> creatorMap;
    private final V8Runtime v8Runtime;
    private JaspilerCompiler jaspilerCompiler;
    private Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> stringGetterMap;

    public V8Jaspiler(V8Runtime v8Runtime) {
        super();
        creatorMap = new HashMap<>();
        creatorMap.put(FUNCTION_CREATE_CHARACTER, this::createCharacter);
        creatorMap.put(FUNCTION_CREATE_FIELD_ACCESS, this::createFieldAccess);
        creatorMap.put(FUNCTION_CREATE_FLOAT, this::createFloat);
        creatorMap.put(FUNCTION_CREATE_IDENT, this::createIdent);
        creatorMap.put(FUNCTION_CREATE_LITERAL, this::createLiteral);
        creatorMap.put(FUNCTION_CREATE_NAME, this::createName);
        creatorMap.put(FUNCTION_TRANSFORM_SYNC, this::transformSync);
        jaspilerCompiler = new JaspilerCompiler();
        stringGetterMap = null;
        this.v8Runtime = v8Runtime;
    }

    @Override
    public void close() {
        jaspilerCompiler = null;
    }

    public V8Value createCharacter(V8Value... v8Values) throws JavetException, JaspilerArgumentException {
        validateLength(FUNCTION_CREATE_FLOAT, v8Values, 1);
        String value = validateString(FUNCTION_CREATE_FLOAT, v8Values, 0);
        return v8Runtime.toV8Value(new JTCharacter(StringUtils.isEmpty(value) ? '\0' : value.charAt(0)));
    }

    public V8Value createFieldAccess(V8Value... v8Values) throws JavetException, JaspilerArgumentException {
        validateLength(FUNCTION_CREATE_FIELD_ACCESS, v8Values, 1);
        String[] strings = new String[v8Values.length];
        for (int i = 0; i < v8Values.length; ++i) {
            strings[i] = validateString(FUNCTION_CREATE_FIELD_ACCESS, v8Values, i);
        }
        return v8Runtime.toV8Value(JTTreeFactory.createFieldAccess(strings));
    }

    public V8Value createFloat(V8Value... v8Values) throws JavetException, JaspilerArgumentException {
        validateLength(FUNCTION_CREATE_FLOAT, v8Values, 1);
        Double value = validateDouble(FUNCTION_CREATE_FLOAT, v8Values, 0);
        return v8Runtime.toV8Value(new JTFloat(value.floatValue()));
    }

    public V8Value createIdent(V8Value... v8Values) throws JavetException, JaspilerArgumentException {
        validateLength(FUNCTION_CREATE_IDENT, v8Values, 1);
        String value = validateString(FUNCTION_CREATE_IDENT, v8Values, 0);
        return v8Runtime.toV8Value(JTTreeFactory.createIdent(value));
    }

    public V8Value createLiteral(V8Value... v8Values) throws JavetException, JaspilerArgumentException {
        validateLength(FUNCTION_CREATE_LITERAL, v8Values, 1);
        Object value = v8Runtime.toObject(v8Values[0]);
        return v8Runtime.toV8Value(JTTreeFactory.createLiteral(value));
    }

    public V8Value createName(V8Value... v8Values) throws JavetException, JaspilerArgumentException {
        validateLength(FUNCTION_CREATE_NAME, v8Values, 1);
        String value = validateString(FUNCTION_CREATE_NAME, v8Values, 0);
        return v8Runtime.toV8Value(new JTName(value));
    }

    @Override
    public V8Runtime getV8Runtime() {
        return v8Runtime;
    }

    @Override
    public boolean isClosed() {
        return jaspilerCompiler == null;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            stringGetterMap = new HashMap<>();
            constructorMap.forEach((key, value) -> registerStringGetterFunction(key, v8Values -> v8Runtime.toV8Value(value.get())));
            creatorMap.forEach(this::registerStringGetterFunction);
        }
        return stringGetterMap;
    }

    public V8Value transformSync(V8Value... v8Values) throws JavetException, JaspilerCheckedException {
        try (var v8JaspilerOptions = new V8JaspilerOptions();
             var jaspilerTransformScanner = new V8JaspilerTransformScanner(v8JaspilerOptions);
             var jaspilerDocScanner = new V8JaspilerDocScanner()) {
            if (v8Values.length > 1) {
                v8JaspilerOptions.deserialize(validateObject(FUNCTION_TRANSFORM_SYNC, v8Values, 1));
            }
            jaspilerCompiler.clearJavaFileObject();
            if (v8JaspilerOptions.getSourceType() == V8JaspilerOptions.SourceType.File) {
                File file = validateFile(validateString(FUNCTION_TRANSFORM_SYNC, v8Values, 0));
                jaspilerCompiler.addJavaFileObjects(file);
            } else {
                String codeString = validateString(FUNCTION_TRANSFORM_SYNC, v8Values, 0);
                jaspilerCompiler.addJavaFileStringObject(v8JaspilerOptions.getFileName(), codeString);
            }
            jaspilerCompiler.transform(jaspilerTransformScanner, jaspilerDocScanner);
            if (CollectionUtils.isNotEmpty(jaspilerTransformScanner.getExceptions())) {
                var e = jaspilerTransformScanner.getExceptions().get(0);
                throw new JaspilerExecutionException(e.getMessage(), e);
            }
            var compilationUnitTree = jaspilerCompiler.getTransformContexts().get(0).getCompilationUnitTree();
            try (V8Scope v8Scope = v8Runtime.getV8Scope()) {
                var v8ValueObjectResult = v8Scope.createV8ValueObject();
                if (v8JaspilerOptions.isAst()) {
                    v8ValueObjectResult.set(PROPERTIES_AST, compilationUnitTree);
                }
                if (v8JaspilerOptions.isCode()) {
                    var writer = new StandardStyleWriter(v8JaspilerOptions.getStyleOptions());
                    if (compilationUnitTree.serialize(writer)) {
                        v8ValueObjectResult.set(PROPERTIES_CODE, writer.toString());
                    }
                }
                v8Scope.setEscapable();
                return v8ValueObjectResult;
            }
        } catch (IOException e) {
            throw new JaspilerParseException(e.getMessage(), e);
        }
    }

    private Double validateDouble(
            String functionName, V8Value[] v8Values, int index)
            throws JaspilerArgumentException {
        validateLength(functionName, v8Values, index);
        V8Value v8Value = v8Values[index];
        if (v8Value instanceof V8ValueDouble v8ValueDouble) {
            return v8ValueDouble.getValue();
        } else if (v8Value instanceof V8ValueInteger v8ValueInteger) {
            return v8ValueInteger.getValue().doubleValue();
        } else if (v8Value instanceof V8ValueLong v8ValueLong) {
            return v8ValueLong.getValue().doubleValue();
        }
        throw new JaspilerArgumentException(
                MessageFormat.format("Argument type mismatches in {0}. Double is expected.", functionName));
    }

    private File validateFile(String filePath) throws JaspilerArgumentException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new JaspilerArgumentException(
                    MessageFormat.format("[{0}] is not found.", file.getAbsolutePath()));
        }
        if (!file.isFile()) {
            throw new JaspilerArgumentException(
                    MessageFormat.format("[{0}] is not a file.", file.getAbsolutePath()));
        }
        if (!file.canRead()) {
            throw new JaspilerArgumentException(
                    MessageFormat.format("File [{0}] cannot be read.", file.getAbsolutePath()));
        }
        return file;
    }

    private void validateLength(
            String functionName, V8Value[] v8Values, int index)
            throws JaspilerArgumentException {
        if (v8Values == null || v8Values.length < index || index < 0) {
            throw new JaspilerArgumentException(
                    MessageFormat.format("Argument count mismatches in {0}.", functionName));
        }
    }

    private V8ValueObject validateObject(
            String functionName, V8Value[] v8Values, int index)
            throws JaspilerArgumentException {
        validateLength(functionName, v8Values, index);
        V8Value v8Value = v8Values[index];
        if (v8Value instanceof V8ValueObject v8ValueObject) {
            return v8ValueObject;
        }
        throw new JaspilerArgumentException(
                MessageFormat.format("Argument type mismatches in {0}. Object is expected.", functionName));
    }

    private String validateString(
            String functionName, V8Value[] v8Values, int index)
            throws JaspilerArgumentException {
        validateLength(functionName, v8Values, index);
        V8Value v8Value = v8Values[index];
        if (v8Value instanceof V8ValueString v8ValueString) {
            return v8ValueString.getValue();
        }
        throw new JaspilerArgumentException(
                MessageFormat.format("Argument type mismatches in {0}. String is expected.", functionName));
    }
}
