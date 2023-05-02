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

import com.sun.source.tree.CatchTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTCatch
        extends JTTree<CatchTree, JTCatch>
        implements CatchTree {
    private JTBlock block;
    private JTVariableDecl parameter;

    public JTCatch() {
        this(null, null);
        setActionChange();
    }

    JTCatch(CatchTree catchTree, JTTree<?, ?> parentTree) {
        super(catchTree, parentTree);
        block = null;
        parameter = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitCatch(this, data);
    }

    @Override
    JTCatch analyze() {
        super.analyze();
        block = JTTreeFactory.create(
                getOriginalTree().getBlock(), this, JTBlock::new);
        parameter = JTTreeFactory.create(
                getOriginalTree().getParameter(), this, JTVariableDecl::new);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(block).ifPresent(nodes::add);
        Optional.ofNullable(parameter).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTBlock getBlock() {
        return block;
    }

    @Override
    public Kind getKind() {
        return Kind.CATCH;
    }

    @Override
    public JTVariableDecl getParameter() {
        return parameter;
    }

    public JTCatch setBlock(JTBlock block) {
        if (this.block == block) {
            return this;
        }
        this.block = Objects.requireNonNull(block).setParentTree(this);
        return setActionChange();
    }

    public JTCatch setParameter(JTVariableDecl parameter) {
        if (this.parameter == parameter) {
            return this;
        }
        this.parameter = Objects.requireNonNull(parameter).setParentTree(this);
        return setActionChange();
    }
}
