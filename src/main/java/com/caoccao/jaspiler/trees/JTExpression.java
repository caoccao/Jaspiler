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

import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;

import java.text.MessageFormat;

/**
 * The type Jt expression.
 * It references com.sun.tools.javac.tree.JCTree.JCExpression.
 */
public abstract class JTExpression<
        OriginalTree extends ExpressionTree,
        NewTree extends JTExpression<OriginalTree, NewTree>>
        extends JTCaseLabel<OriginalTree, NewTree>
        implements ExpressionTree {
    public JTExpression(OriginalTree originalTree, JTTree<?, ?> parentTree) {
        super(originalTree, parentTree);
    }

    public static JTExpression<?, ?> from(ExpressionTree expressionTree, JTTree<?, ?> parentTree) {
        if (expressionTree != null) {
            switch (expressionTree.getKind()) {
                case IDENTIFIER -> {
                    return new JTIdent((IdentifierTree) expressionTree, parentTree);
                }
                case MEMBER_SELECT -> {
                    return new JTFieldAccess((MemberSelectTree) expressionTree, parentTree);
                }
                default -> {
                    throw new UnsupportedOperationException(
                            MessageFormat.format(
                                    "Expression {0} is not supported.",
                                    expressionTree.getKind().name()));
                }
            }
        }
        return null;
    }

    @Override
    public boolean isExpression() {
        return true;
    }

    @Override
    public boolean isPattern() {
        return false;
    }

    public boolean isPoly() {
        return false;
    }

    public boolean isStandalone() {
        return true;
    }
}
