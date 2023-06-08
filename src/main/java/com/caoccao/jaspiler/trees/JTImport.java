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

import com.caoccao.jaspiler.utils.StringBuilderPlus;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTImport
        extends JTTree<ImportTree, JTImport>
        implements ImportTree {
    private JTTree<?, ?> qualifiedIdentifier;
    private boolean staticImport;

    public JTImport() {
        this(null, null);
        setActionChange();
    }

    JTImport(ImportTree importTree, JTTree<?, ?> parentTree) {
        super(importTree, parentTree);
        staticImport = false;
        qualifiedIdentifier = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitImport(this, data);
    }

    @Override
    JTImport analyze() {
        super.analyze();
        qualifiedIdentifier = JTTreeFactory.create(getOriginalTree().getQualifiedIdentifier(), this);
        staticImport = getOriginalTree().isStatic();
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(qualifiedIdentifier).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.IMPORT;
    }

    @Override
    protected int getLineSeparatorCount() {
        return 1;
    }

    @Override
    public JTTree<?, ?> getQualifiedIdentifier() {
        return qualifiedIdentifier;
    }

    @Override
    public boolean isStatic() {
        return staticImport;
    }

    public JTImport setQualifiedIdentifier(JTTree<?, ?> qualifiedIdentifier) {
        if (this.qualifiedIdentifier == qualifiedIdentifier) {
            return this;
        }
        this.qualifiedIdentifier = Objects.requireNonNull(qualifiedIdentifier).setParentTree(this);
        return setActionChange();
    }

    public JTImport setStaticImport(boolean staticImport) {
        if (this.staticImport == staticImport) {
            return this;
        }
        this.staticImport = staticImport;
        return setActionChange();
    }

    @Override
    public String toString() {
        if (isActionChange()) {
            final var sbp = new StringBuilderPlus();
            sbp.append(IJTConstants.IMPORT).appendSpace();
            if (staticImport) {
                sbp.append(IJTConstants.STATIC).appendSpace();
            }
            sbp.append(qualifiedIdentifier).append(IJTConstants.SEMI_COLON).appendLineSeparator();
            return sbp.toString();
        }
        return super.toString();
    }
}
