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

import com.sun.source.tree.OpensTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTOpens
        extends JTDirective<OpensTree, JTOpens>
        implements OpensTree {
    private final List<JTExpression<?, ?>> moduleNames;
    private JTExpression<?, ?> packageName;

    public JTOpens() {
        this(null, null);
        setActionChange();
    }

    JTOpens(OpensTree opensTree, JTTree<?, ?> parentTree) {
        super(opensTree, parentTree);
        moduleNames = new ArrayList<>();
        packageName = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitOpens(this, data);
    }

    @Override
    JTOpens analyze() {
        super.analyze();
        packageName = JTTreeFactory.create(getOriginalTree().getPackageName(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getModuleNames(), this, (JTExpression<?, ?> o) -> moduleNames.add(o));
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(packageName).ifPresent(nodes::add);
        moduleNames.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.OPENS;
    }

    @Override
    public List<JTExpression<?, ?>> getModuleNames() {
        return moduleNames;
    }

    @Override
    public JTExpression<?, ?> getPackageName() {
        return packageName;
    }

    public JTOpens setPackageName(JTExpression<?, ?> packageName) {
        if (this.packageName == packageName) {
            return this;
        }
        this.packageName = Objects.requireNonNull(packageName).setParentTree(this);
        return setActionChange();
    }
}
