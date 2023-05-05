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

import com.sun.source.tree.RequiresTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTRequires
        extends JTDirective<RequiresTree, JTRequires>
        implements RequiresTree {
    private JTExpression<?, ?> moduleName;
    private boolean staticPhase;
    private boolean transitive;

    public JTRequires() {
        this(null, null);
        setActionChange();
    }

    JTRequires(RequiresTree requiresTree, JTTree<?, ?> parentTree) {
        super(requiresTree, parentTree);
        moduleName = null;
        staticPhase = false;
        transitive = false;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitRequires(this, data);
    }

    @Override
    JTRequires analyze() {
        super.analyze();
        moduleName = JTTreeFactory.create(getOriginalTree().getModuleName(), this);
        staticPhase = getOriginalTree().isStatic();
        transitive = getOriginalTree().isTransitive();
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(moduleName).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.REQUIRES;
    }

    @Override
    public JTExpression<?, ?> getModuleName() {
        return moduleName;
    }

    @Override
    public boolean isStatic() {
        return staticPhase;
    }

    @Override
    public boolean isTransitive() {
        return transitive;
    }

    public JTRequires setModuleName(JTExpression<?, ?> moduleName) {
        if (this.moduleName == moduleName) {
            return this;
        }
        this.moduleName = Objects.requireNonNull(moduleName).setParentTree(this);
        return setActionChange();
    }

    public JTRequires setStaticPhase(boolean staticPhase) {
        if (this.staticPhase == staticPhase) {
            return this;
        }
        this.staticPhase = staticPhase;
        return setActionChange();
    }

    public JTRequires setTransitive(boolean transitive) {
        if (this.transitive == transitive) {
            return this;
        }
        this.transitive = transitive;
        return setActionChange();
    }
}
