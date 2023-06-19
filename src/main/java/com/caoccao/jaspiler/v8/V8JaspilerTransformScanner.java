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

import com.caoccao.jaspiler.contexts.JaspilerTransformContext;
import com.caoccao.jaspiler.visiters.BaseJaspilerTransformScanner;
import com.caoccao.javet.exceptions.BaseJavetScriptingException;
import com.caoccao.javet.values.reference.V8ValueFunction;
import com.sun.source.tree.*;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("preview")
public class V8JaspilerTransformScanner
        extends BaseJaspilerTransformScanner<V8JaspilerTransformScanner>
        implements AutoCloseable {
    protected final List<Throwable> exceptions;
    protected V8JaspilerOptions options;

    public V8JaspilerTransformScanner(V8JaspilerOptions options) {
        exceptions = new ArrayList<>();
        this.options = Objects.requireNonNull(options);
    }

    @Override
    public void close() {
        exceptions.clear();
    }

    protected <Node extends Tree> void forEachPlugin(
            Node node,
            Function<V8JaspilerOptions.Plugin, V8ValueFunction> functionGetter) {
        if (CollectionUtils.isNotEmpty(options.getPlugins())) {
            options.getPlugins().stream()
                    .map(functionGetter)
                    .filter(Objects::nonNull)
                    .forEach(v8ValueFunction -> {
                        try {
                            v8ValueFunction.call(null, node);
                        } catch (Throwable t) {
                            getExceptions().add(t);
                            if (t instanceof BaseJavetScriptingException baseJavetScriptingException) {
                                logger.warn(baseJavetScriptingException.getScriptingError().toString(), t);
                            } else {
                                logger.warn(t.getMessage(), t);
                            }
                        }
                    });
        }
    }

    public List<Throwable> getExceptions() {
        return exceptions;
    }

    @Override
    public V8JaspilerTransformScanner visitAnnotatedType(AnnotatedTypeTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitAnnotatedType());
        return super.visitAnnotatedType(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitAnnotation(AnnotationTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitAnnotation());
        return super.visitAnnotation(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitArrayAccess(ArrayAccessTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitArrayAccess());
        return super.visitArrayAccess(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitArrayType(ArrayTypeTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitArrayType());
        return super.visitArrayType(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitAssert(AssertTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitAssert());
        return super.visitAssert(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitAssignment(AssignmentTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitAssignment());
        return super.visitAssignment(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitBinary(BinaryTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitBinary());
        return super.visitBinary(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitBindingPattern(BindingPatternTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitBindingPattern());
        return super.visitBindingPattern(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitBlock(BlockTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitBlock());
        return super.visitBlock(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitBreak(BreakTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitBreak());
        return super.visitBreak(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitCase(CaseTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitCase());
        return super.visitCase(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitCatch(CatchTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitCatch());
        return super.visitCatch(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitClass(
            ClassTree node,
            JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitClass());
        return super.visitClass(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitCompilationUnit(
            CompilationUnitTree node,
            JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitCompilationUnit());
        return super.visitCompilationUnit(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitCompoundAssignment(CompoundAssignmentTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitCompoundAssignment());
        return super.visitCompoundAssignment(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitConditionalExpression(ConditionalExpressionTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitConditionalExpression());
        return super.visitConditionalExpression(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitContinue(ContinueTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitContinue());
        return super.visitContinue(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitDefaultCaseLabel(DefaultCaseLabelTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitDefaultCaseLabel());
        return super.visitDefaultCaseLabel(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitDoWhileLoop(DoWhileLoopTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitDoWhileLoop());
        return super.visitDoWhileLoop(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitEnhancedForLoop(EnhancedForLoopTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitEnhancedForLoop());
        return super.visitEnhancedForLoop(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitIdentifier(
            IdentifierTree node,
            JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitIdentifier());
        return super.visitIdentifier(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitImport(ImportTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitImport());
        return super.visitImport(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitMethod(MethodTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitMethod());
        return super.visitMethod(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitPackage(PackageTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitPackage());
        return super.visitPackage(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitVariable(VariableTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitVariable());
        return super.visitVariable(node, jaspilerTransformContext);
    }
}
