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

import com.sun.source.tree.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Name;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "preview"})
public final class JTTreeFactory {
    private static final Logger logger = LoggerFactory.getLogger(JTTreeFactory.class);

    private JTTreeFactory() {
    }

    public static <T extends Tree, R extends JTTree<?, ?>> R create(
            T tree,
            JTTree<?, ?> parentTree,
            BiFunction<T, JTTree<?, ?>, R> constructor) {
        return Optional.ofNullable(tree)
                .map(o -> constructor.apply(o, parentTree))
                .map(o -> (R) o.analyze())
                .orElse(null);
    }

    public static <T extends Tree, R extends JTTree<?, ?>> R create(
            T tree,
            JTTree<?, ?> parentTree) {
        Objects.requireNonNull(parentTree);
        R r = null;
        if (tree != null) {
            switch (tree.getKind()) {
                case AND, CONDITIONAL_AND, CONDITIONAL_OR, DIVIDE, EQUAL_TO,
                        GREATER_THAN, GREATER_THAN_EQUAL, LEFT_SHIFT, LESS_THAN, LESS_THAN_EQUAL,
                        MINUS, MULTIPLY, NOT_EQUAL_TO, OR, PLUS,
                        REMAINDER, RIGHT_SHIFT, UNSIGNED_RIGHT_SHIFT, XOR ->
                        r = (R) create((BinaryTree) tree, parentTree, JTBinary::new);
                case AND_ASSIGNMENT, DIVIDE_ASSIGNMENT, LEFT_SHIFT_ASSIGNMENT, MINUS_ASSIGNMENT, MULTIPLY_ASSIGNMENT,
                        OR_ASSIGNMENT, PLUS_ASSIGNMENT, REMAINDER_ASSIGNMENT, RIGHT_SHIFT_ASSIGNMENT, UNSIGNED_RIGHT_SHIFT_ASSIGNMENT,
                        XOR_ASSIGNMENT -> r = (R) create((CompoundAssignmentTree) tree, parentTree, JTAssignOp::new);
                case ANNOTATION_TYPE, CLASS, ENUM, INTERFACE, RECORD ->
                        r = (R) create((ClassTree) tree, parentTree, JTClassDecl::new);
                case ARRAY_ACCESS -> r = (R) create((ArrayAccessTree) tree, parentTree, JTArrayAccess::new);
                case ARRAY_TYPE -> r = (R) create((ArrayTypeTree) tree, parentTree, JTArrayTypeTree::new);
                case ASSERT -> r = (R) create((AssertTree) tree, parentTree, JTAssert::new);
                case ASSIGNMENT -> r = (R) create((AssignmentTree) tree, parentTree, JTAssign::new);
                case BINDING_PATTERN -> r = (R) create((BindingPatternTree) tree, parentTree, JTBindingPattern::new);
                case BITWISE_COMPLEMENT, LOGICAL_COMPLEMENT, POSTFIX_DECREMENT, POSTFIX_INCREMENT, PREFIX_DECREMENT,
                        PREFIX_INCREMENT, UNARY_MINUS, UNARY_PLUS ->
                        r = (R) create((UnaryTree) tree, parentTree, JTUnary::new);
                case BLOCK -> r = (R) create((BlockTree) tree, parentTree, JTBlock::new);
                case CASE -> r = (R) create((CaseTree) tree, parentTree, JTCase::new);
                case CATCH -> r = (R) create((CatchTree) tree, parentTree, JTCatch::new);
                case CONDITIONAL_EXPRESSION ->
                        r = (R) create((ConditionalExpressionTree) tree, parentTree, JTConditional::new);
                case DEFAULT_CASE_LABEL ->
                        r = (R) create((DefaultCaseLabelTree) tree, parentTree, JTDefaultCaseLabel::new);
                case ENHANCED_FOR_LOOP ->
                        r = (R) create((EnhancedForLoopTree) tree, parentTree, JTEnhancedForLoop::new);
                case EXPRESSION_STATEMENT ->
                        r = (R) create((ExpressionStatementTree) tree, parentTree, JTExpressionStatement::new);
                case EXTENDS_WILDCARD, SUPER_WILDCARD, UNBOUNDED_WILDCARD ->
                        r = (R) create((WildcardTree) tree, parentTree, JTWildcard::new);
                case FOR_LOOP -> r = (R) create((ForLoopTree) tree, parentTree, JTForLoop::new);
                case IDENTIFIER -> r = (R) create((IdentifierTree) tree, parentTree, JTIdent::new);
                case IF -> r = (R) create((IfTree) tree, parentTree, JTIf::new);
                case INSTANCE_OF -> r = (R) create((InstanceOfTree) tree, parentTree, JTInstanceOf::new);
                case INT_LITERAL, LONG_LITERAL, FLOAT_LITERAL, DOUBLE_LITERAL,
                        BOOLEAN_LITERAL, CHAR_LITERAL, STRING_LITERAL, NULL_LITERAL ->
                        r = (R) create((LiteralTree) tree, parentTree, JTLiteral::new);
                case LAMBDA_EXPRESSION -> r = (R) create((LambdaExpressionTree) tree, parentTree, JTLambda::new);
                case MEMBER_REFERENCE -> r = (R) create((MemberReferenceTree) tree, parentTree, JTMemberReference::new);
                case MEMBER_SELECT -> r = (R) create((MemberSelectTree) tree, parentTree, JTFieldAccess::new);
                case METHOD -> r = (R) create((MethodTree) tree, parentTree, JTMethodDecl::new);
                case METHOD_INVOCATION ->
                        r = (R) create((MethodInvocationTree) tree, parentTree, JTMethodInvocation::new);
                case NEW_ARRAY -> r = (R) create((NewArrayTree) tree, parentTree, JTNewArray::new);
                case NEW_CLASS -> r = (R) create((NewClassTree) tree, parentTree, JTNewClass::new);
                case PARENTHESIZED -> r = (R) create((ParenthesizedTree) tree, parentTree, JTParens::new);
                case PRIMITIVE_TYPE -> r = (R) create((PrimitiveTypeTree) tree, parentTree, JTPrimitiveTypeTree::new);
                case PARAMETERIZED_TYPE -> r = (R) create((ParameterizedTypeTree) tree, parentTree, JTTypeApply::new);
                case RETURN -> r = (R) create((ReturnTree) tree, parentTree, JTReturn::new);
                case SWITCH -> r = (R) create((SwitchTree) tree, parentTree, JTSwitch::new);
                case THROW -> r = (R) create((ThrowTree) tree, parentTree, JTThrow::new);
                case TRY -> r = (R) create((TryTree) tree, parentTree, JTTry::new);
                case TYPE_CAST -> r = (R) create((TypeCastTree) tree, parentTree, JTTypeCast::new);
                case UNION_TYPE -> r = (R) create((UnionTypeTree) tree, parentTree, JTTypeUnion::new);
                case VARIABLE -> r = (R) create((VariableTree) tree, parentTree, JTVariableDecl::new);
                default -> {
                    parentTree.getCompilationUnit().getUnsupportedTrees().add(tree);
                    String message = MessageFormat.format(
                            "Type {0} and kind {1} is not supported.",
                            tree.getClass().getName(),
                            tree.getKind().name());
                    logger.warn("{}\n{}", message, tree);
                }
            }
        }
        return r;
    }

    public static <T extends Tree, R extends JTTree<?, ?>> void createAndAdd(
            List<T> trees,
            JTTree<?, ?> parentTree,
            BiFunction<T, JTTree<?, ?>, R> constructor,
            Consumer<R> consumer) {
        if (CollectionUtils.isNotEmpty(trees)) {
            trees.stream()
                    .filter(Objects::nonNull)
                    .map(o -> constructor.apply(o, parentTree))
                    .filter(Objects::nonNull)
                    .map(o -> (R) o.analyze())
                    .forEach(consumer);
        }
    }

    public static <T extends Tree, R extends JTTree<?, ?>> void createAndAdd(
            List<T> trees,
            JTTree<?, ?> parentTree,
            Consumer<R> consumer) {
        createAndAdd(trees, parentTree, JTTreeFactory::create, consumer);
    }

    public static JTFieldAccess createFieldAccess(String... strings) {
        assert strings.length > 0 : "String array must not be empty.";
        var jtFieldAccess = new JTFieldAccess();
        jtFieldAccess.setIdentifier(new JTName(strings[strings.length - 1]));
        if (strings.length > 1) {
            jtFieldAccess.setExpression(createFieldAccess(Arrays.copyOfRange(strings, 0, strings.length - 1)));
        }
        return jtFieldAccess;
    }

    public static JTName createName(Name name) {
        return Optional.ofNullable(name)
                .map(Object::toString)
                .map(JTName::new)
                .orElse(null);
    }
}
