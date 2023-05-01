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

import com.sun.source.tree.TreeVisitor;
import com.sun.source.tree.TypeParameterTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class JTTypeParameter
        extends JTTree<TypeParameterTree, JTTypeParameter>
        implements TypeParameterTree {
    private final List<JTAnnotation> annotations;
    private final List<JTExpression<?, ?>> bounds;
    private JTName name;

    public JTTypeParameter() {
        this(null, null);
        setActionChange();
    }

    JTTypeParameter(TypeParameterTree typeParameterTree, JTTree<?, ?> parentTree) {
        super(typeParameterTree, parentTree);
        annotations = new ArrayList<>();
        bounds = new ArrayList<>();
        name = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitTypeParameter(this, data);
    }

    @Override
    JTTypeParameter analyze() {
        super.analyze();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getAnnotations(), this, JTAnnotation::new, annotations::add);
        name = JTTreeFactory.createName(getOriginalTree().getName());
        JTTreeFactory.createAndAdd(
                getOriginalTree().getBounds(), this, (JTExpression<?, ?> o) -> bounds.add(o));
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        annotations.stream().filter(Objects::nonNull).forEach(nodes::add);
        bounds.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public List<JTExpression<?, ?>> getBounds() {
        return bounds;
    }

    @Override
    public Kind getKind() {
        return Kind.TYPE_PARAMETER;
    }

    @Override
    public JTName getName() {
        return name;
    }

    public JTTypeParameter setName(JTName name) {
        if (this.name == name) {
            return this;
        }
        this.name = Objects.requireNonNull(name);
        return setActionChange();
    }
}
