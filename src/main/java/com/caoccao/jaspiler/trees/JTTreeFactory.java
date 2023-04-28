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

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.Tree;

import java.text.MessageFormat;

@SuppressWarnings("unchecked")
public final class JTTreeFactory {
    private JTTreeFactory() {
    }

    public static <T extends Tree, R extends T> R createFrom(T tree, JTTree<?, ?> parentTree) {
        R r = null;
        if (tree != null) {
            switch (tree.getKind()) {
                case IDENTIFIER -> {
                    r = (R) new JTIdent((IdentifierTree) tree, parentTree);
                }
                case MEMBER_SELECT -> {
                    r = (R) new JTFieldAccess((MemberSelectTree) tree, parentTree);
                }
                default -> {
                    throw new UnsupportedOperationException(
                            MessageFormat.format(
                                    "Kind {0} is not supported.",
                                    tree.getKind().name()));
                }
            }
        }
        if (r instanceof JTTree<?, ?> jtTree) {
            jtTree.analyze();
        }
        return r;
    }
}
