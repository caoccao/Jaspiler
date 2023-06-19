/*
 * Copyright (c) 2023-2023. caoccao.com Sam Cao
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

import com.caoccao.jaspiler.styles.StyleOptions;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interfaces.IJavetClosable;
import com.caoccao.javet.utils.JavetResourceUtils;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.primitive.V8ValueBoolean;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.caoccao.javet.values.reference.V8ValueArray;
import com.caoccao.javet.values.reference.V8ValueFunction;
import com.caoccao.javet.values.reference.V8ValueObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class V8JaspilerOptions implements IJavetClosable {
    private static final String DEFAULT_FILE_NAME = "Dummy";
    private static final String PROPERTY_AST = "ast";
    private static final String PROPERTY_CODE = "code";
    private static final String PROPERTY_CONTINUATION_INDENT_SIZE = "continuationIndentSize";
    private static final String PROPERTY_FILE_NAME = "fileName";
    private static final String PROPERTY_INDENT_SIZE = "indentSize";
    private static final String PROPERTY_PLUGINS = "plugins";
    private static final String PROPERTY_PRESERVE_COPYRIGHTS = "preserveCopyrights";
    private static final String PROPERTY_SOURCE_TYPE = "sourceType";
    private static final String PROPERTY_STYLE = "style";
    private static final String PROPERTY_WORD_WRAP_COLUMN = "wordWrapColumn";
    private final List<Plugin> plugins;
    private boolean ast;
    private boolean code;
    private String fileName;
    private SourceType sourceType;
    private StyleOptions styleOptions;

    public V8JaspilerOptions() {
        ast = false;
        code = true;
        fileName = null;
        plugins = new ArrayList<>();
        sourceType = SourceType.File;
        styleOptions = StyleOptions.Default;
    }

    @Override
    public void close() {
        JavetResourceUtils.safeClose(plugins);
        plugins.clear();
    }

    public V8JaspilerOptions deserialize(V8ValueObject v8ValueObject) throws JavetException {
        deserializeAst(v8ValueObject);
        deserializeCode(v8ValueObject);
        deserializePlugins(v8ValueObject);
        deserializeSourceTypeAndFileName(v8ValueObject);
        deserializeStyle(v8ValueObject);
        return this;
    }

    private void deserializeAst(V8ValueObject v8ValueObject) throws JavetException {
        try (V8Value v8Value = v8ValueObject.get(PROPERTY_AST)) {
            if (v8Value instanceof V8ValueBoolean v8ValueBoolean) {
                ast = v8ValueBoolean.getValue();
            }
        }
    }

    private void deserializeCode(V8ValueObject v8ValueObject) throws JavetException {
        try (V8Value v8Value = v8ValueObject.get(PROPERTY_CODE)) {
            if (v8Value instanceof V8ValueBoolean v8ValueBoolean) {
                code = v8ValueBoolean.getValue();
            }
        }
    }

    private void deserializePlugins(V8ValueObject v8ValueObject) throws JavetException {
        try (V8Value v8Value = v8ValueObject.get(PROPERTY_PLUGINS)) {
            if (v8Value instanceof V8ValueArray v8ValueArray) {
                v8ValueArray.forEach(v8ValueRule -> {
                    if (v8ValueRule instanceof V8ValueObject v8ValueObjectPlugin) {
                        plugins.add(new Plugin().deserialize(v8ValueObjectPlugin));
                    }
                });
            }
        }
    }

    private void deserializeSourceTypeAndFileName(V8ValueObject v8ValueObject) throws JavetException {
        try (V8Value v8Value = v8ValueObject.get(PROPERTY_SOURCE_TYPE)) {
            if (v8Value instanceof V8ValueString v8ValueString) {
                if (StringUtils.equalsIgnoreCase(SourceType.String.name(), v8ValueString.getValue())) {
                    sourceType = SourceType.String;
                } else {
                    sourceType = SourceType.File;
                }
            }
        }
        if (sourceType == SourceType.String) {
            try (V8Value v8Value = v8ValueObject.get(PROPERTY_FILE_NAME)) {
                if (v8Value instanceof V8ValueString v8ValueString) {
                    fileName = v8ValueString.getValue();
                } else {
                    fileName = DEFAULT_FILE_NAME;
                }
            }
        }
    }

    private void deserializeStyle(V8ValueObject v8ValueObject) throws JavetException {
        try (V8Value v8Value = v8ValueObject.get(PROPERTY_STYLE)) {
            if (v8Value instanceof V8ValueObject v8ValueObjectStyle) {
                styleOptions = new StyleOptions();
                Optional.ofNullable(v8ValueObjectStyle.getInteger(PROPERTY_CONTINUATION_INDENT_SIZE)).ifPresent(styleOptions::setContinuationIndentSize);
                Optional.ofNullable(v8ValueObjectStyle.getInteger(PROPERTY_INDENT_SIZE)).ifPresent(styleOptions::setIndentSize);
                Optional.ofNullable(v8ValueObjectStyle.getBoolean(PROPERTY_PRESERVE_COPYRIGHTS)).ifPresent(styleOptions::setPreserveCopyrights);
                Optional.ofNullable(v8ValueObjectStyle.getInteger(PROPERTY_WORD_WRAP_COLUMN)).ifPresent(styleOptions::setWordWrapColumn);
                // TODO: To support type.
                styleOptions.seal();
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public StyleOptions getStyleOptions() {
        return styleOptions;
    }

    public boolean isAst() {
        return ast;
    }

    @Override
    public boolean isClosed() {
        return CollectionUtils.isEmpty(plugins);
    }

    public boolean isCode() {
        return code;
    }

    public void setAst(boolean ast) {
        this.ast = ast;
    }

    public void setCode(boolean code) {
        this.code = code;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public void setStyleOptions(StyleOptions styleOptions) {
        this.styleOptions = styleOptions;
    }

    public enum SourceType {
        File,
        String,
    }

    public static final class Plugin implements IJavetClosable {
        private static final String PROPERTY_VISITOR = "visitor";

        private Visitor visitor;

        public Plugin() {
            setVisitor(null);
        }

        @Override
        public void close() {
            JavetResourceUtils.safeClose(visitor);
            setVisitor(null);
        }

        public Plugin deserialize(V8ValueObject v8ValueObject) throws JavetException {
            try (var v8Value = v8ValueObject.get(PROPERTY_VISITOR)) {
                if (v8Value instanceof V8ValueObject v8ValueObjectVisitor) {
                    setVisitor(new Visitor().deserialize(v8ValueObjectVisitor));
                }
            }
            return this;
        }

        public Visitor getVisitor() {
            return visitor;
        }

        @Override
        public boolean isClosed() {
            return ObjectUtils.allNull(visitor);
        }

        public boolean isValid() {
            return ObjectUtils.allNotNull(visitor) && visitor.isValid();
        }

        public void setVisitor(Visitor visitor) {
            this.visitor = visitor;
        }
    }

    public static final class Visitor implements IJavetClosable {
        private final List<String> properties;
        private final List<Supplier<V8ValueFunction>> propertyGetters;
        private final List<Consumer<V8ValueFunction>> propertySetters;
        private V8ValueFunction visitAnnotatedType;
        private V8ValueFunction visitAnnotation;
        private V8ValueFunction visitArrayAccess;
        private V8ValueFunction visitArrayType;
        private V8ValueFunction visitAssert;
        private V8ValueFunction visitAssignment;
        private V8ValueFunction visitBinary;
        private V8ValueFunction visitBindingPattern;
        private V8ValueFunction visitBlock;
        private V8ValueFunction visitBreak;
        private V8ValueFunction visitCase;
        private V8ValueFunction visitCatch;
        private V8ValueFunction visitClass;
        private V8ValueFunction visitCompilationUnit;
        private V8ValueFunction visitCompoundAssignment;
        private V8ValueFunction visitConditionalExpression;
        private V8ValueFunction visitContinue;
        private V8ValueFunction visitDefaultCaseLabel;
        private V8ValueFunction visitDoWhileLoop;
        private V8ValueFunction visitEnhancedForLoop;
        private V8ValueFunction visitErroneous;
        private V8ValueFunction visitExports;
        private V8ValueFunction visitExpressionStatement;
        private V8ValueFunction visitForLoop;
        private V8ValueFunction visitIdentifier;
        private V8ValueFunction visitImport;
        private V8ValueFunction visitMemberSelect;
        private V8ValueFunction visitMethod;
        private V8ValueFunction visitPackage;
        private V8ValueFunction visitVariable;
        public Visitor() {
            properties = List.of(
                    "AnnotatedType",
                    "Annotation",
                    "ArrayAccess",
                    "ArrayType",
                    "Assert",
                    "Assignment",
                    "Binary",
                    "BindingPattern",
                    "Block",
                    "Break",
                    "Case",
                    "Catch",
                    "Class",
                    "CompilationUnit",
                    "CompoundAssignment",
                    "ConditionalExpression",
                    "Continue",
                    "DefaultCaseLabel",
                    "DoWhileLoop",
                    "EnhancedForLoop",
                    "Erroneous",
                    "Exports",
                    "ExpressionStatement",
                    "ForLoop",
                    "Identifier",
                    "Import",
                    "MemberSelect",
                    "Method",
                    "Package",
                    "Variable");
            propertyGetters = List.of(
                    this::getVisitAnnotatedType,
                    this::getVisitAnnotation,
                    this::getVisitArrayAccess,
                    this::getVisitArrayType,
                    this::getVisitAssert,
                    this::getVisitAssignment,
                    this::getVisitBinary,
                    this::getVisitBindingPattern,
                    this::getVisitBlock,
                    this::getVisitBreak,
                    this::getVisitCase,
                    this::getVisitCatch,
                    this::getVisitClass,
                    this::getVisitCompilationUnit,
                    this::getVisitCompoundAssignment,
                    this::getVisitConditionalExpression,
                    this::getVisitContinue,
                    this::getVisitDefaultCaseLabel,
                    this::getVisitDoWhileLoop,
                    this::getVisitEnhancedForLoop,
                    this::getVisitErroneous,
                    this::getVisitExports,
                    this::getVisitExpressionStatement,
                    this::getVisitForLoop,
                    this::getVisitIdentifier,
                    this::getVisitImport,
                    this::getVisitMemberSelect,
                    this::getVisitMethod,
                    this::getVisitPackage,
                    this::getVisitVariable);
            propertySetters = List.of(
                    this::setVisitAnnotatedType,
                    this::setVisitAnnotation,
                    this::setVisitArrayAccess,
                    this::setVisitArrayType,
                    this::setVisitAssert,
                    this::setVisitAssignment,
                    this::setVisitBinary,
                    this::setVisitBindingPattern,
                    this::setVisitBlock,
                    this::setVisitBreak,
                    this::setVisitCase,
                    this::setVisitCatch,
                    this::setVisitClass,
                    this::setVisitCompilationUnit,
                    this::setVisitCompoundAssignment,
                    this::setVisitConditionalExpression,
                    this::setVisitContinue,
                    this::setVisitDefaultCaseLabel,
                    this::setVisitDoWhileLoop,
                    this::setVisitEnhancedForLoop,
                    this::setVisitErroneous,
                    this::setVisitExports,
                    this::setVisitExpressionStatement,
                    this::setVisitForLoop,
                    this::setVisitIdentifier,
                    this::setVisitImport,
                    this::setVisitMemberSelect,
                    this::setVisitMethod,
                    this::setVisitPackage,
                    this::setVisitVariable);
            reset();
        }

        @Override
        public void close() {
            JavetResourceUtils.safeClose(propertyGetters.stream().map(Supplier::get).toArray());
            reset();
        }

        public Visitor deserialize(V8ValueObject v8ValueObject) throws JavetException {
            final int length = properties.size();
            V8Value[] v8ValueKeys = new V8Value[length];
            V8Value[] v8ValueValues = new V8Value[length];
            try {
                var v8Runtime = v8ValueObject.getV8Runtime();
                for (int i = 0; i < length; i++) {
                    v8ValueKeys[i] = v8Runtime.createV8ValueString(properties.get(i));
                }
                v8ValueObject.batchGet(v8ValueKeys, v8ValueValues, length);
                for (int i = 0; i < length; i++) {
                    if (v8ValueValues[i] instanceof V8ValueFunction v8ValueFunction) {
                        propertySetters.get(i).accept(v8ValueFunction);
                        v8ValueValues[i] = null;
                    }
                }
            } finally {
                JavetResourceUtils.safeClose(v8ValueKeys);
                JavetResourceUtils.safeClose(v8ValueValues);
            }
            return this;
        }

        public V8ValueFunction getVisitAnnotatedType() {
            return visitAnnotatedType;
        }

        public V8ValueFunction getVisitAnnotation() {
            return visitAnnotation;
        }

        public V8ValueFunction getVisitArrayAccess() {
            return visitArrayAccess;
        }

        public V8ValueFunction getVisitArrayType() {
            return visitArrayType;
        }

        public V8ValueFunction getVisitAssert() {
            return visitAssert;
        }

        public V8ValueFunction getVisitAssignment() {
            return visitAssignment;
        }

        public V8ValueFunction getVisitBinary() {
            return visitBinary;
        }

        public V8ValueFunction getVisitBindingPattern() {
            return visitBindingPattern;
        }

        public V8ValueFunction getVisitBlock() {
            return visitBlock;
        }

        public V8ValueFunction getVisitBreak() {
            return visitBreak;
        }

        public V8ValueFunction getVisitCase() {
            return visitCase;
        }

        public V8ValueFunction getVisitCatch() {
            return visitCatch;
        }

        public V8ValueFunction getVisitClass() {
            return visitClass;
        }

        public V8ValueFunction getVisitCompilationUnit() {
            return visitCompilationUnit;
        }

        public V8ValueFunction getVisitCompoundAssignment() {
            return visitCompoundAssignment;
        }

        public V8ValueFunction getVisitConditionalExpression() {
            return visitConditionalExpression;
        }

        public V8ValueFunction getVisitContinue() {
            return visitContinue;
        }

        public V8ValueFunction getVisitDefaultCaseLabel() {
            return visitDefaultCaseLabel;
        }

        public V8ValueFunction getVisitDoWhileLoop() {
            return visitDoWhileLoop;
        }

        public V8ValueFunction getVisitEnhancedForLoop() {
            return visitEnhancedForLoop;
        }

        public V8ValueFunction getVisitErroneous() {
            return visitErroneous;
        }

        public V8ValueFunction getVisitExports() {
            return visitExports;
        }

        public V8ValueFunction getVisitExpressionStatement() {
            return visitExpressionStatement;
        }

        public V8ValueFunction getVisitForLoop() {
            return visitForLoop;
        }

        public V8ValueFunction getVisitIdentifier() {
            return visitIdentifier;
        }

        public V8ValueFunction getVisitImport() {
            return visitImport;
        }

        public V8ValueFunction getVisitMemberSelect() {
            return visitMemberSelect;
        }

        public V8ValueFunction getVisitMethod() {
            return visitMethod;
        }

        public V8ValueFunction getVisitPackage() {
            return visitPackage;
        }

        public V8ValueFunction getVisitVariable() {
            return visitVariable;
        }

        @Override
        public boolean isClosed() {
            return propertyGetters.stream().map(Supplier::get).allMatch(Objects::isNull);
        }

        public boolean isValid() {
            return !isClosed();
        }

        private void reset() {
            propertySetters.forEach(setter -> setter.accept(null));
        }

        public void setVisitAnnotatedType(V8ValueFunction visitAnnotatedType) {
            this.visitAnnotatedType = visitAnnotatedType;
        }

        public void setVisitAnnotation(V8ValueFunction visitAnnotation) {
            this.visitAnnotation = visitAnnotation;
        }

        public void setVisitArrayAccess(V8ValueFunction visitArrayAccess) {
            this.visitArrayAccess = visitArrayAccess;
        }

        public void setVisitArrayType(V8ValueFunction visitArrayType) {
            this.visitArrayType = visitArrayType;
        }

        public void setVisitAssert(V8ValueFunction visitAssert) {
            this.visitAssert = visitAssert;
        }

        public void setVisitAssignment(V8ValueFunction visitAssignment) {
            this.visitAssignment = visitAssignment;
        }

        public void setVisitBinary(V8ValueFunction visitBinary) {
            this.visitBinary = visitBinary;
        }

        public void setVisitBindingPattern(V8ValueFunction visitBindingPattern) {
            this.visitBindingPattern = visitBindingPattern;
        }

        public void setVisitBlock(V8ValueFunction visitBlock) {
            this.visitBlock = visitBlock;
        }

        public void setVisitBreak(V8ValueFunction visitBreak) {
            this.visitBreak = visitBreak;
        }

        public void setVisitCase(V8ValueFunction visitCase) {
            this.visitCase = visitCase;
        }

        public void setVisitCatch(V8ValueFunction visitCatch) {
            this.visitCatch = visitCatch;
        }

        public void setVisitClass(V8ValueFunction visitClass) {
            this.visitClass = visitClass;
        }

        public void setVisitCompilationUnit(V8ValueFunction visitCompilationUnit) {
            this.visitCompilationUnit = visitCompilationUnit;
        }

        public void setVisitCompoundAssignment(V8ValueFunction visitCompoundAssignment) {
            this.visitCompoundAssignment = visitCompoundAssignment;
        }

        public void setVisitConditionalExpression(V8ValueFunction visitConditionalExpression) {
            this.visitConditionalExpression = visitConditionalExpression;
        }

        public void setVisitContinue(V8ValueFunction visitContinue) {
            this.visitContinue = visitContinue;
        }

        public void setVisitDefaultCaseLabel(V8ValueFunction visitDefaultCaseLabel) {
            this.visitDefaultCaseLabel = visitDefaultCaseLabel;
        }

        public void setVisitDoWhileLoop(V8ValueFunction visitDoWhileLoop) {
            this.visitDoWhileLoop = visitDoWhileLoop;
        }

        public void setVisitEnhancedForLoop(V8ValueFunction visitEnhancedForLoop) {
            this.visitEnhancedForLoop = visitEnhancedForLoop;
        }

        public void setVisitErroneous(V8ValueFunction visitErroneous) {
            this.visitErroneous = visitErroneous;
        }

        public void setVisitExports(V8ValueFunction visitExports) {
            this.visitExports = visitExports;
        }

        public void setVisitExpressionStatement(V8ValueFunction visitExpressionStatement) {
            this.visitExpressionStatement = visitExpressionStatement;
        }

        public void setVisitForLoop(V8ValueFunction visitForLoop) {
            this.visitForLoop = visitForLoop;
        }

        public void setVisitIdentifier(V8ValueFunction visitIdentifier) {
            this.visitIdentifier = visitIdentifier;
        }

        public void setVisitImport(V8ValueFunction visitImport) {
            this.visitImport = visitImport;
        }

        public void setVisitMemberSelect(V8ValueFunction visitMemberSelect) {
            this.visitMemberSelect = visitMemberSelect;
        }

        public void setVisitMethod(V8ValueFunction visitMethod) {
            this.visitMethod = visitMethod;
        }

        public void setVisitPackage(V8ValueFunction visitPackage) {
            this.visitPackage = visitPackage;
        }

        public void setVisitVariable(V8ValueFunction visitVariable) {
            this.visitVariable = visitVariable;
        }
    }
}
