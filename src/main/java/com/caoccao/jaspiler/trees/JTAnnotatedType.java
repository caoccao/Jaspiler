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

import com.sun.source.tree.AnnotatedTypeTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTAnnotatedType
        extends JTExpression<AnnotatedTypeTree, JTAnnotatedType>
        implements AnnotatedTypeTree, IJTAnnotatable {

    private final List<JTAnnotation> annotations;
    private JTExpression<?, ?> underlyingType;

    public JTAnnotatedType() {
        this(null, null);
        setActionChange();
    }

    JTAnnotatedType(AnnotatedTypeTree annotatedTypeTree, JTTree<?, ?> parentTree) {
        super(annotatedTypeTree, parentTree);
        annotations = new ArrayList<>();
        underlyingType = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitAnnotatedType(this, data);
    }

    @Override
    JTAnnotatedType analyze() {
        super.analyze();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getAnnotations(), this, JTAnnotation::new, annotations::add);
        underlyingType = JTTreeFactory.create(getOriginalTree().getUnderlyingType(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        annotations.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(underlyingType).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public Kind getKind() {
        return Kind.ANNOTATED_TYPE;
    }

    @Override
    public JTExpression<?, ?> getUnderlyingType() {
        return underlyingType;
    }

    public JTAnnotatedType setUnderlyingType(JTExpression<?, ?> underlyingType) {
        if (this.underlyingType == underlyingType) {
            return this;
        }
        this.underlyingType = Objects.requireNonNull(underlyingType).setParentTree(this);
        return setActionChange();
    }
}
