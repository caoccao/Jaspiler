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

import com.caoccao.jaspiler.JaspilerContract;
import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.Tree;

import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class JTTree<
        OriginalTree extends Tree,
        NewTree extends JTTree<OriginalTree, NewTree>>
        extends BaseLoggingObject
        implements IJTTree<OriginalTree, NewTree> {
    protected static final long INVALID_POSITION = -1L;
    protected JaspilerContract.Action action;
    protected JTPosition originalPosition;
    protected OriginalTree originalTree;
    protected JTTree<?, ?> parentTree;

    JTTree(OriginalTree originalTree, JTTree<?, ?> parentTree) {
        super();
        originalPosition = JTPosition.Invalid;
        this.originalTree = originalTree;
        this.parentTree = parentTree;
        setAction(JaspilerContract.Action.NoChange);
    }

    public static <T extends Tree, R extends T> R from(T tree, JTTree<?, ?> parentTree) {
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

    NewTree analyze() {
        originalPosition = getCompilationUnit().getOriginalPosition(getOriginalTree());
        return (NewTree) this;
    }

    @Override
    public JaspilerContract.Action getAction() {
        return action;
    }

    protected int getLineSeparatorCount() {
        return 0;
    }

    protected long getOptionalEndPosition(long position) {
        return getOriginalPosition().isValid() ? getOriginalPosition().endPosition() : position;
    }

    public String getOriginalCode() {
        return getCompilationUnit().getOriginalCode();
    }

    @Override
    public JTPosition getOriginalPosition() {
        return originalPosition;
    }

    @Override
    public OriginalTree getOriginalTree() {
        return originalTree;
    }

    @Override
    public JTTree<?, ?> getParentTree() {
        return parentTree;
    }

    boolean isActionChange(Object... objects) {
        if (getAction().isChange()) {
            return true;
        }
        for (Object object : objects) {
            if (object instanceof IJTTree<?, ?> jtTree) {
                if (jtTree.isActionChange() || jtTree.isActionIgnore()) {
                    return true;
                }
            } else if (object instanceof List<?> jtTrees) {
                for (var item : jtTrees) {
                    if (item instanceof IJTTree<?, ?> jtTree) {
                        if (jtTree.isActionChange() || jtTree.isActionIgnore()) {
                            return true;
                        }
                    } else if (item == null) {
                        // Pass.
                    } else {
                        throw new IllegalArgumentException("Object type is not supported.");
                    }
                }
            } else if (object == null) {
                // Pass.
            } else {
                throw new IllegalArgumentException("Object type is not supported.");
            }
        }
        return false;
    }

    protected boolean save(Writer writer) throws IOException {
        if (isActionIgnore()) {
            return false;
        }
        writer.write(toString());
        return true;
    }

    public NewTree setAction(JaspilerContract.Action action) {
        this.action = action;
        return (NewTree) this;
    }

    @Override
    public NewTree setActionChange() {
        return setAction(JaspilerContract.Action.Change);
    }

    @Override
    public NewTree setActionIgnore() {
        return setAction(JaspilerContract.Action.Ignore);
    }

    NewTree setOriginalPosition(IJTTree<?, ?> jtTree) {
        if (jtTree != null) {
            this.originalPosition = jtTree.getOriginalPosition();
        }
        return (NewTree) this;
    }

    NewTree setParentTree(JTTree<?, ?> parentTree) {
        this.parentTree = parentTree;
        return (NewTree) this;
    }

    @Override
    public String toString() {
        if (isActionChange() || !getOriginalPosition().isValid()) {
            return IJTConstants.UNEXPECTED;
        }
        String code = getOriginalCode().substring(
                (int) getOriginalPosition().startPosition(),
                (int) getOriginalPosition().endPosition());
        if (getLineSeparatorCount() > 0) {
            code += IJTConstants.LINE_SEPARATOR_X_10.substring(0, getLineSeparatorCount());
        }
        return code;
    }

    protected NewTree writeStrings(Writer writer, String... strings) throws IOException {
        for (String str : strings) {
            writer.write(str);
        }
        return (NewTree) this;
    }
}
