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

import com.caoccao.jaspiler.utils.ForEachUtils;
import com.caoccao.jaspiler.utils.StringBuilderPlus;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class JTBlock
        extends JTStatement<BlockTree, JTBlock>
        implements BlockTree {
    private final List<JTStatement<?, ?>> statements;
    private boolean staticBlock;

    public JTBlock() {
        this(null, null);
        setActionChange();
    }

    JTBlock(BlockTree blockTree, JTTree<?, ?> parentTree) {
        super(blockTree, parentTree);
        statements = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitBlock(this, data);
    }

    @Override
    JTBlock analyze() {
        super.analyze();
        staticBlock = getOriginalTree().isStatic();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getStatements(), this, (JTStatement<?, ?> o) -> statements.add(o));
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        statements.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.BLOCK;
    }

    @Override
    public List<JTStatement<?, ?>> getStatements() {
        return statements;
    }

    @Override
    public boolean isStatic() {
        return staticBlock;
    }

    public JTBlock setStatic(boolean staticBlock) {
        if (this.staticBlock == staticBlock) {
            return this;
        }
        this.staticBlock = staticBlock;
        return setActionChange();
    }

    @Override
    public String toString() {
        if (isActionChange()) {
            final var sbp = new StringBuilderPlus();
            int indent = getIndent();
            int parentIndent = indent - getCompilationUnit().getOptions().getIndentSize();
            if (staticBlock) {
                sbp.appendSpace(indent).append(IJTConstants.STATIC).appendSpace();
            }
            sbp.appendLeftCurlyBracket().appendLineSeparator();
            ForEachUtils.forEach(
                    statements.stream().filter(Objects::nonNull).filter(tree -> !tree.isActionIgnore()).toList(),
                    tree -> sbp.appendSpace(indent).append(tree),
                    tree -> sbp.appendLineSeparator());
            sbp.appendLineSeparator().appendSpace(parentIndent).appendRightCurlyBracket();
            return sbp.toString();
        }
        return super.toString();
    }
}
