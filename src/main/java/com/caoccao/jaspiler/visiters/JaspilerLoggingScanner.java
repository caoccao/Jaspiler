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

import com.caoccao.jaspiler.contexts.JaspilerParseContext;
import com.caoccao.jaspiler.trees.JTPosition;
import com.sun.source.tree.*;
import com.sun.source.util.TreePathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@SuppressWarnings("preview")
public class JaspilerLoggingScanner extends TreePathScanner<JaspilerLoggingScanner, JaspilerParseContext> {
    protected final Logger logger;

    public JaspilerLoggingScanner() {
        super();
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public JaspilerLoggingScanner scan(Tree tree, JaspilerParseContext jaspilerContext) {
        var optionalTree = Optional.ofNullable(tree);
        var optionalKind = optionalTree.map(Tree::getKind);
        logger.debug(
                "Kind: {} Type: {}",
                optionalKind.map(Object::toString).orElse("null"),
                optionalTree.map(Object::getClass).map(Class::getName).orElse("null"));
        if (optionalTree.isPresent()) {
            var position = JTPosition.from(jaspilerContext.getTrees(), jaspilerContext.getCompilationUnitTree(), tree);
            logger.debug(
                    "  L: {}, C: {}, S: {}, E: {}, Length: {}",
                    position.lineNumber(),
                    position.columnNumber(),
                    position.startPosition(),
                    position.endPosition(),
                    position.length());
        }
        return super.scan(tree, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitAnnotatedType(AnnotatedTypeTree node, JaspilerParseContext jaspilerContext) {
        logger.debug("  {}", node.toString());
        return super.visitAnnotatedType(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitAnnotation(AnnotationTree node, JaspilerParseContext jaspilerContext) {
        logger.debug("  {}", node.toString());
        return super.visitAnnotation(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitArrayAccess(ArrayAccessTree node, JaspilerParseContext jaspilerContext) {
        return super.visitArrayAccess(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitArrayType(ArrayTypeTree node, JaspilerParseContext jaspilerContext) {
        return super.visitArrayType(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitAssert(AssertTree node, JaspilerParseContext jaspilerContext) {
        return super.visitAssert(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitAssignment(AssignmentTree node, JaspilerParseContext jaspilerContext) {
        return super.visitAssignment(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitBinary(BinaryTree node, JaspilerParseContext jaspilerContext) {
        return super.visitBinary(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitBindingPattern(BindingPatternTree node, JaspilerParseContext jaspilerContext) {
        return super.visitBindingPattern(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitBlock(BlockTree node, JaspilerParseContext jaspilerContext) {
        return super.visitBlock(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitBreak(BreakTree node, JaspilerParseContext jaspilerContext) {
        return super.visitBreak(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitCase(CaseTree node, JaspilerParseContext jaspilerContext) {
        return super.visitCase(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitCatch(CatchTree node, JaspilerParseContext jaspilerContext) {
        return super.visitCatch(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitClass(ClassTree node, JaspilerParseContext jaspilerContext) {
        return super.visitClass(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitCompilationUnit(CompilationUnitTree node, JaspilerParseContext jaspilerContext) {
        return super.visitCompilationUnit(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitCompoundAssignment(CompoundAssignmentTree node, JaspilerParseContext jaspilerContext) {
        return super.visitCompoundAssignment(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitConditionalExpression(ConditionalExpressionTree node, JaspilerParseContext jaspilerContext) {
        return super.visitConditionalExpression(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitContinue(ContinueTree node, JaspilerParseContext jaspilerContext) {
        return super.visitContinue(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitDefaultCaseLabel(DefaultCaseLabelTree node, JaspilerParseContext jaspilerContext) {
        return super.visitDefaultCaseLabel(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitDoWhileLoop(DoWhileLoopTree node, JaspilerParseContext jaspilerContext) {
        return super.visitDoWhileLoop(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitEmptyStatement(EmptyStatementTree node, JaspilerParseContext jaspilerContext) {
        return super.visitEmptyStatement(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitEnhancedForLoop(EnhancedForLoopTree node, JaspilerParseContext jaspilerContext) {
        return super.visitEnhancedForLoop(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitErroneous(ErroneousTree node, JaspilerParseContext jaspilerContext) {
        return super.visitErroneous(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitExports(ExportsTree node, JaspilerParseContext jaspilerContext) {
        return super.visitExports(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitExpressionStatement(ExpressionStatementTree node, JaspilerParseContext jaspilerContext) {
        return super.visitExpressionStatement(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitForLoop(ForLoopTree node, JaspilerParseContext jaspilerContext) {
        return super.visitForLoop(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitGuardedPattern(GuardedPatternTree node, JaspilerParseContext jaspilerContext) {
        return super.visitGuardedPattern(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitIdentifier(IdentifierTree node, JaspilerParseContext jaspilerContext) {
        return super.visitIdentifier(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitIf(IfTree node, JaspilerParseContext jaspilerContext) {
        return super.visitIf(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitImport(ImportTree node, JaspilerParseContext jaspilerContext) {
        return super.visitImport(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitInstanceOf(InstanceOfTree node, JaspilerParseContext jaspilerContext) {
        return super.visitInstanceOf(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitIntersectionType(IntersectionTypeTree node, JaspilerParseContext jaspilerContext) {
        return super.visitIntersectionType(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitLabeledStatement(LabeledStatementTree node, JaspilerParseContext jaspilerContext) {
        return super.visitLabeledStatement(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitLambdaExpression(LambdaExpressionTree node, JaspilerParseContext jaspilerContext) {
        return super.visitLambdaExpression(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitLiteral(LiteralTree node, JaspilerParseContext jaspilerContext) {
        return super.visitLiteral(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitMemberReference(MemberReferenceTree node, JaspilerParseContext jaspilerContext) {
        logger.debug("  {}", node.toString());
        return super.visitMemberReference(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitMemberSelect(MemberSelectTree node, JaspilerParseContext jaspilerContext) {
        logger.debug("  {}", node.toString());
        return super.visitMemberSelect(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitMethod(MethodTree node, JaspilerParseContext jaspilerContext) {
        return super.visitMethod(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitMethodInvocation(MethodInvocationTree node, JaspilerParseContext jaspilerContext) {
        return super.visitMethodInvocation(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitModifiers(ModifiersTree node, JaspilerParseContext jaspilerContext) {
        return super.visitModifiers(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitModule(ModuleTree node, JaspilerParseContext jaspilerContext) {
        return super.visitModule(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitNewArray(NewArrayTree node, JaspilerParseContext jaspilerContext) {
        return super.visitNewArray(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitNewClass(NewClassTree node, JaspilerParseContext jaspilerContext) {
        return super.visitNewClass(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitOpens(OpensTree node, JaspilerParseContext jaspilerContext) {
        return super.visitOpens(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitOther(Tree node, JaspilerParseContext jaspilerContext) {
        return super.visitOther(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitPackage(PackageTree node, JaspilerParseContext jaspilerContext) {
        return super.visitPackage(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitParameterizedType(ParameterizedTypeTree node, JaspilerParseContext jaspilerContext) {
        return super.visitParameterizedType(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitParenthesized(ParenthesizedTree node, JaspilerParseContext jaspilerContext) {
        return super.visitParenthesized(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitParenthesizedPattern(ParenthesizedPatternTree node, JaspilerParseContext jaspilerContext) {
        return super.visitParenthesizedPattern(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitPrimitiveType(PrimitiveTypeTree node, JaspilerParseContext jaspilerContext) {
        return super.visitPrimitiveType(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitProvides(ProvidesTree node, JaspilerParseContext jaspilerContext) {
        return super.visitProvides(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitRequires(RequiresTree node, JaspilerParseContext jaspilerContext) {
        return super.visitRequires(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitReturn(ReturnTree node, JaspilerParseContext jaspilerContext) {
        return super.visitReturn(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitSwitch(SwitchTree node, JaspilerParseContext jaspilerContext) {
        return super.visitSwitch(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitSwitchExpression(SwitchExpressionTree node, JaspilerParseContext jaspilerContext) {
        return super.visitSwitchExpression(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitSynchronized(SynchronizedTree node, JaspilerParseContext jaspilerContext) {
        return super.visitSynchronized(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitThrow(ThrowTree node, JaspilerParseContext jaspilerContext) {
        return super.visitThrow(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitTry(TryTree node, JaspilerParseContext jaspilerContext) {
        return super.visitTry(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitTypeCast(TypeCastTree node, JaspilerParseContext jaspilerContext) {
        return super.visitTypeCast(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitTypeParameter(TypeParameterTree node, JaspilerParseContext jaspilerContext) {
        return super.visitTypeParameter(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitUnary(UnaryTree node, JaspilerParseContext jaspilerContext) {
        return super.visitUnary(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitUnionType(UnionTypeTree node, JaspilerParseContext jaspilerContext) {
        return super.visitUnionType(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitUses(UsesTree node, JaspilerParseContext jaspilerContext) {
        return super.visitUses(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitVariable(VariableTree node, JaspilerParseContext jaspilerContext) {
        return super.visitVariable(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitWhileLoop(WhileLoopTree node, JaspilerParseContext jaspilerContext) {
        return super.visitWhileLoop(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitWildcard(WildcardTree node, JaspilerParseContext jaspilerContext) {
        return super.visitWildcard(node, jaspilerContext);
    }

    @Override
    public JaspilerLoggingScanner visitYield(YieldTree node, JaspilerParseContext jaspilerContext) {
        return super.visitYield(node, jaspilerContext);
    }
}
