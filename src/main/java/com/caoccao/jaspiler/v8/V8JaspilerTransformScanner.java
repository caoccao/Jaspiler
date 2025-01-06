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
                            v8ValueFunction.call(null, node, options.getContext());
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
    public V8JaspilerTransformScanner scan(Tree tree, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(tree, plugin -> plugin.getVisitor().getScan());
        return super.scan(tree, jaspilerTransformContext);
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
    public V8JaspilerTransformScanner visitEmptyStatement(EmptyStatementTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitEmptyStatement());
        return super.visitEmptyStatement(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitEnhancedForLoop(EnhancedForLoopTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitEnhancedForLoop());
        return super.visitEnhancedForLoop(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitErroneous(ErroneousTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitErroneous());
        return super.visitErroneous(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitExports(ExportsTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitExports());
        return super.visitExports(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitExpressionStatement(ExpressionStatementTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitExpressionStatement());
        return super.visitExpressionStatement(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitForLoop(ForLoopTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitForLoop());
        return super.visitForLoop(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitGuardedPattern(GuardedPatternTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitGuardedPattern());
        return super.visitGuardedPattern(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitIdentifier(
            IdentifierTree node,
            JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitIdentifier());
        return super.visitIdentifier(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitIf(IfTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitIf());
        return super.visitIf(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitImport(ImportTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitImport());
        return super.visitImport(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitInstanceOf(InstanceOfTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitInstanceOf());
        return super.visitInstanceOf(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitIntersectionType(IntersectionTypeTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitIntersectionType());
        return super.visitIntersectionType(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitLabeledStatement(LabeledStatementTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitLabeledStatement());
        return super.visitLabeledStatement(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitLambdaExpression(LambdaExpressionTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitLambdaExpression());
        return super.visitLambdaExpression(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitLiteral(LiteralTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitLiteral());
        return super.visitLiteral(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitMemberReference(MemberReferenceTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitMemberReference());
        return super.visitMemberReference(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitMemberSelect(MemberSelectTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitMemberSelect());
        return super.visitMemberSelect(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitMethod(MethodTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitMethod());
        return super.visitMethod(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitMethodInvocation(MethodInvocationTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitMethodInvocation());
        return super.visitMethodInvocation(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitModifiers(ModifiersTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitModifiers());
        return super.visitModifiers(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitModule(ModuleTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitModule());
        return super.visitModule(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitNewArray(NewArrayTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitNewArray());
        return super.visitNewArray(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitNewClass(NewClassTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitNewClass());
        return super.visitNewClass(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitOpens(OpensTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitOpens());
        return super.visitOpens(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitOther(Tree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitOther());
        return super.visitOther(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitPackage(PackageTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitPackage());
        return super.visitPackage(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitParameterizedType(ParameterizedTypeTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitParameterizedType());
        return super.visitParameterizedType(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitParenthesized(ParenthesizedTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitParenthesized());
        return super.visitParenthesized(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitParenthesizedPattern(ParenthesizedPatternTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitParenthesizedPattern());
        return super.visitParenthesizedPattern(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitPrimitiveType(PrimitiveTypeTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitPrimitiveType());
        return super.visitPrimitiveType(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitProvides(ProvidesTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitProvides());
        return super.visitProvides(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitRequires(RequiresTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitRequires());
        return super.visitRequires(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitReturn(ReturnTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitReturn());
        return super.visitReturn(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitSwitch(SwitchTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitSwitch());
        return super.visitSwitch(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitSwitchExpression(SwitchExpressionTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitSwitchExpression());
        return super.visitSwitchExpression(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitSynchronized(SynchronizedTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitSynchronized());
        return super.visitSynchronized(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitThrow(ThrowTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitThrow());
        return super.visitThrow(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitTry(TryTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitTry());
        return super.visitTry(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitTypeCast(TypeCastTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitTypeCast());
        return super.visitTypeCast(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitTypeParameter(TypeParameterTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitTypeParameter());
        return super.visitTypeParameter(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitUnary(UnaryTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitUnary());
        return super.visitUnary(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitUnionType(UnionTypeTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitUnionType());
        return super.visitUnionType(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitUses(UsesTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitUses());
        return super.visitUses(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitVariable(VariableTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitVariable());
        return super.visitVariable(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitWhileLoop(WhileLoopTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitWhileLoop());
        return super.visitWhileLoop(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitWildcard(WildcardTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitWildcard());
        return super.visitWildcard(node, jaspilerTransformContext);
    }

    @Override
    public V8JaspilerTransformScanner visitYield(YieldTree node, JaspilerTransformContext jaspilerTransformContext) {
        forEachPlugin(node, plugin -> plugin.getVisitor().getVisitYield());
        return super.visitYield(node, jaspilerTransformContext);
    }
}
