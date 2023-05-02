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

@SuppressWarnings("unchecked")
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
                case ANNOTATION_TYPE, CLASS -> r = (R) create((ClassTree) tree, parentTree, JTClassDecl::new);
                case ARRAY_TYPE -> r = (R) create((ArrayTypeTree) tree, parentTree, JTArrayTypeTree::new);
                case ASSIGNMENT -> r = (R) create((AssignmentTree) tree, parentTree, JTAssign::new);
                case EXPRESSION_STATEMENT ->
                        r = (R) create((ExpressionStatementTree) tree, parentTree, JTExpressionStatement::new);
                case IDENTIFIER -> r = (R) create((IdentifierTree) tree, parentTree, JTIdent::new);
                case INT_LITERAL, LONG_LITERAL, FLOAT_LITERAL, DOUBLE_LITERAL,
                        BOOLEAN_LITERAL, CHAR_LITERAL, STRING_LITERAL, NULL_LITERAL ->
                        r = (R) create((LiteralTree) tree, parentTree, JTLiteral::new);
                case MEMBER_REFERENCE -> r = (R) create((MemberReferenceTree) tree, parentTree, JTMemberReference::new);
                case MEMBER_SELECT -> r = (R) create((MemberSelectTree) tree, parentTree, JTFieldAccess::new);
                case METHOD -> r = (R) create((MethodTree) tree, parentTree, JTMethodDecl::new);
                case METHOD_INVOCATION ->
                        r = (R) create((MethodInvocationTree) tree, parentTree, JTMethodInvocation::new);
                case NEW_CLASS -> r = (R) create((NewClassTree) tree, parentTree, JTNewClass::new);
                case PRIMITIVE_TYPE -> r = (R) create((PrimitiveTypeTree) tree, parentTree, JTPrimitiveTypeTree::new);
                case PARAMETERIZED_TYPE -> r = (R) create((ParameterizedTypeTree) tree, parentTree, JTTypeApply::new);
                case RETURN -> r = (R) create((ReturnTree) tree, parentTree, JTReturn::new);
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
        if (trees != null) {
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
