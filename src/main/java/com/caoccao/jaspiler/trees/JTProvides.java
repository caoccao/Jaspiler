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

import com.sun.source.tree.ProvidesTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTProvides
        extends JTDirective<ProvidesTree, JTProvides>
        implements ProvidesTree {
    private final List<JTExpression<?, ?>> implementationNames;
    private JTExpression<?, ?> serviceName;

    public JTProvides() {
        this(null, null);
        setActionChange();
    }

    JTProvides(ProvidesTree providesTree, JTTree<?, ?> parentTree) {
        super(providesTree, parentTree);
        implementationNames = new ArrayList<>();
        serviceName = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitProvides(this, data);
    }

    @Override
    JTProvides analyze() {
        super.analyze();
        serviceName = JTTreeFactory.create(getOriginalTree().getServiceName(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getImplementationNames(), this, (JTExpression<?, ?> o) -> implementationNames.add(o));
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(serviceName).ifPresent(nodes::add);
        implementationNames.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTExpression<?, ?>> getImplementationNames() {
        return implementationNames;
    }

    @Override
    public Kind getKind() {
        return Kind.PROVIDES;
    }

    @Override
    public JTExpression<?, ?> getServiceName() {
        return serviceName;
    }

    public JTProvides setServiceName(JTExpression<?, ?> serviceName) {
        if (this.serviceName == serviceName) {
            return this;
        }
        this.serviceName = Objects.requireNonNull(serviceName).setParentTree(this);
        return setActionChange();
    }
}
