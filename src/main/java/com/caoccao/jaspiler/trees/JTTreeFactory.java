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

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public final class JTTreeFactory {
    private static final Logger logger = LoggerFactory.getLogger(JTTreeFactory.class);

    private JTTreeFactory() {
    }

    public static <T extends Tree, R extends JTTree<?, ?>> R createFrom(T tree, JTTree<?, ?> parentTree) {
        R r = null;
        if (tree != null) {
            switch (tree.getKind()) {
                case ANNOTATION_TYPE, CLASS -> r = (R) new JTClassDecl((ClassTree) tree, parentTree);
                case IDENTIFIER -> r = (R) new JTIdent((IdentifierTree) tree, parentTree);
                case MEMBER_SELECT -> r = (R) new JTFieldAccess((MemberSelectTree) tree, parentTree);
                default -> {
                    String message = MessageFormat.format(
                            "Type {0} and kind {1} is not supported.",
                            tree.getClass().getName(),
                            tree.getKind().name());
                    logger.error("{}\n{}", message, tree);
                    throw new UnsupportedOperationException(message);
                }
            }
        }
        if (r != null) {
            r.analyze();
        }
        return r;
    }

    public static JTFieldAccess createJTFieldAccess(String... strings) {
        assert strings.length > 0 : "String array must not be empty.";
        var jtFieldAccess = new JTFieldAccess();
        jtFieldAccess.setIdentifier(new JTName(strings[strings.length - 1]));
        if (strings.length > 1) {
            jtFieldAccess.setExpression(createJTFieldAccess(Arrays.copyOfRange(strings, 0, strings.length - 1)));
        }
        return jtFieldAccess;
    }
}
