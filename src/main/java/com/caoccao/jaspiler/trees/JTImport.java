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
        qualifiedIdentifier = Optional.ofNullable(getOriginalTree().getQualifiedIdentifier())
                .map(o -> (JTTree<?, ?>) JTTreeFactory.createFrom(o, this))
                .orElse(null);
        staticImport = getOriginalTree().isStatic();
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        nodes.add(qualifiedIdentifier);
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
        this.qualifiedIdentifier = Objects.requireNonNull(qualifiedIdentifier).setParentTree(this);
        return setActionChange();
    }

    public JTImport setStaticImport(boolean staticImport) {
        this.staticImport = staticImport;
        return setActionChange();
    }

    @Override
    public String toString() {
        if (isActionChange()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(IJTConstants.IMPORT_);
            if (staticImport) {
                stringBuilder.append(IJTConstants.STATIC_);
            }
            stringBuilder.append(qualifiedIdentifier).append(IJTConstants.SEMI_COLON)
                    .append(IJTConstants.LINE_SEPARATOR);
            return stringBuilder.toString();
        }
        return super.toString();
    }
}
