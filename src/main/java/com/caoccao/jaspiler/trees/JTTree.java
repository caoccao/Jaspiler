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
import com.sun.source.tree.Tree;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
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

    NewTree analyze() {
        originalPosition = getCompilationUnit().getOriginalPosition(getOriginalTree());
        return (NewTree) this;
    }

    @Override
    public JaspilerContract.Action getAction() {
        return action;
    }

    List<JTTree<?, ?>> getAllNodes() {
        return new ArrayList<>();
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

    @Override
    public boolean isActionChange() {
        if (isActionIgnore()) {
            return false;
        }
        if (getAction().isChange()) {
            return true;
        }
        return getAllNodes().stream()
                .anyMatch(jtTree -> jtTree.isActionChange() || jtTree.isActionIgnore());
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

    NewTree setParentTree(JTTree<?, ?> parentTree) {
        if (this.parentTree != parentTree) {
            this.parentTree = parentTree;
            return setActionChange();
        }
        return (NewTree) this;
    }

    @Override
    public String toString() {
        if (isActionIgnore()) {
            return IJTConstants.EMPTY;
        }
        if (isActionChange()) {
            var stringBuilder = new StringBuilder();
            getAllNodes().forEach(stringBuilder::append);
            return stringBuilder.toString();
        }
        if (!getOriginalPosition().isValid()) {
            return IJTConstants.UNEXPECTED;
        }
        String code = getOriginalCode().substring(
                (int) getOriginalPosition().startPosition(),
                (int) getOriginalPosition().endPosition());
        if (getLineSeparatorCount() > 0) {
            code += IJTConstants.LINE_SEPARATOR.repeat(getLineSeparatorCount());
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
