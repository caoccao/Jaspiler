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

package com.caoccao.jaspiler.visiters;

import com.caoccao.jaspiler.contexts.JaspilerTransformContext;
import com.sun.source.tree.*;
import com.sun.source.util.TreePathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("preview")
public class JaspilerTransformScanner extends TreePathScanner<JaspilerTransformScanner, JaspilerTransformContext> {
    protected final Logger logger;

    public JaspilerTransformScanner() {
        super();
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public JaspilerTransformScanner scan(Tree tree, JaspilerTransformContext jaspilerContext) {
        return super.scan(tree, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitAnnotatedType(AnnotatedTypeTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitAnnotatedType(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitAnnotation(AnnotationTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitAnnotation(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitArrayAccess(ArrayAccessTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitArrayAccess(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitArrayType(ArrayTypeTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitArrayType(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitAssert(AssertTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitAssert(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitAssignment(AssignmentTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitAssignment(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitBinary(BinaryTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitBinary(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitBindingPattern(BindingPatternTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitBindingPattern(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitBlock(BlockTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitBlock(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitBreak(BreakTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitBreak(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitCase(CaseTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitCase(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitCatch(CatchTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitCatch(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitClass(ClassTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitClass(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitCompilationUnit(CompilationUnitTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitCompilationUnit(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitCompoundAssignment(CompoundAssignmentTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitCompoundAssignment(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitConditionalExpression(ConditionalExpressionTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitConditionalExpression(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitContinue(ContinueTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitContinue(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitDefaultCaseLabel(DefaultCaseLabelTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitDefaultCaseLabel(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitDoWhileLoop(DoWhileLoopTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitDoWhileLoop(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitEmptyStatement(EmptyStatementTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitEmptyStatement(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitEnhancedForLoop(EnhancedForLoopTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitEnhancedForLoop(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitErroneous(ErroneousTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitErroneous(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitExports(ExportsTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitExports(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitExpressionStatement(ExpressionStatementTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitExpressionStatement(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitForLoop(ForLoopTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitForLoop(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitGuardedPattern(GuardedPatternTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitGuardedPattern(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitIdentifier(IdentifierTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitIdentifier(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitIf(IfTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitIf(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitImport(ImportTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitImport(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitInstanceOf(InstanceOfTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitInstanceOf(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitIntersectionType(IntersectionTypeTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitIntersectionType(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitLabeledStatement(LabeledStatementTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitLabeledStatement(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitLambdaExpression(LambdaExpressionTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitLambdaExpression(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitLiteral(LiteralTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitLiteral(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitMemberReference(MemberReferenceTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitMemberReference(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitMemberSelect(MemberSelectTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitMemberSelect(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitMethod(MethodTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitMethod(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitMethodInvocation(MethodInvocationTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitMethodInvocation(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitModifiers(ModifiersTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitModifiers(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitModule(ModuleTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitModule(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitNewArray(NewArrayTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitNewArray(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitNewClass(NewClassTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitNewClass(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitOpens(OpensTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitOpens(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitOther(Tree node, JaspilerTransformContext jaspilerContext) {
        return super.visitOther(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitPackage(PackageTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitPackage(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitParameterizedType(ParameterizedTypeTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitParameterizedType(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitParenthesized(ParenthesizedTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitParenthesized(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitParenthesizedPattern(ParenthesizedPatternTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitParenthesizedPattern(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitPrimitiveType(PrimitiveTypeTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitPrimitiveType(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitProvides(ProvidesTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitProvides(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitRequires(RequiresTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitRequires(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitReturn(ReturnTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitReturn(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitSwitch(SwitchTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitSwitch(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitSwitchExpression(SwitchExpressionTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitSwitchExpression(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitSynchronized(SynchronizedTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitSynchronized(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitThrow(ThrowTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitThrow(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitTry(TryTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitTry(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitTypeCast(TypeCastTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitTypeCast(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitTypeParameter(TypeParameterTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitTypeParameter(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitUnary(UnaryTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitUnary(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitUnionType(UnionTypeTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitUnionType(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitUses(UsesTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitUses(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitVariable(VariableTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitVariable(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitWhileLoop(WhileLoopTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitWhileLoop(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitWildcard(WildcardTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitWildcard(node, jaspilerContext);
    }

    @Override
    public JaspilerTransformScanner visitYield(YieldTree node, JaspilerTransformContext jaspilerContext) {
        return super.visitYield(node, jaspilerContext);
    }
}
