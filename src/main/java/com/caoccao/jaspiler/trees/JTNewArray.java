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

import com.sun.source.tree.NewArrayTree;
import com.sun.source.tree.TreeVisitor;

import java.util.*;

public final class JTNewArray
        extends JTExpression<NewArrayTree, JTNewArray>
        implements NewArrayTree, IJTAnnotatable {
    private final List<JTAnnotation> annotations;
    private final List<List<JTAnnotation>> dimAnnotations;
    private final List<JTExpression<?, ?>> dimensions;
    private final List<JTExpression<?, ?>> initializers;
    private JTExpression<?, ?> type;

    public JTNewArray() {
        this(null, null);
        setActionChange();
    }

    JTNewArray(NewArrayTree newArrayTree, JTTree<?, ?> parentTree) {
        super(newArrayTree, parentTree);
        annotations = new ArrayList<>();
        dimAnnotations = new ArrayList<>();
        dimensions = new ArrayList<>();
        initializers = new ArrayList<>();
        type = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitNewArray(this, data);
    }

    @Override
    JTNewArray analyze() {
        super.analyze();
        type = JTTreeFactory.create(getOriginalTree().getType(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getDimensions(), this, (JTExpression<?, ?> o) -> dimensions.add(o));
        JTTreeFactory.createAndAdd(
                getOriginalTree().getInitializers(), this, (JTExpression<?, ?> o) -> initializers.add(o));
        JTTreeFactory.createAndAdd(
                getOriginalTree().getAnnotations(), this, JTAnnotation::new, annotations::add);
        for (var originalDimAnnotation : getOriginalTree().getDimAnnotations()) {
            List<JTAnnotation> dimAnnotation = new ArrayList<>();
            dimAnnotations.add(dimAnnotation);
            JTTreeFactory.createAndAdd(
                    originalDimAnnotation, this, JTAnnotation::new, dimAnnotation::add);
        }
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(type).ifPresent(nodes::add);
        dimensions.stream().filter(Objects::nonNull).forEach(nodes::add);
        initializers.stream().filter(Objects::nonNull).forEach(nodes::add);
        annotations.stream().filter(Objects::nonNull).forEach(nodes::add);
        dimAnnotations.stream().filter(Objects::nonNull).flatMap(Collection::stream).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public List<List<JTAnnotation>> getDimAnnotations() {
        return dimAnnotations;
    }

    @Override
    public List<JTExpression<?, ?>> getDimensions() {
        return dimensions;
    }

    @Override
    public List<JTExpression<?, ?>> getInitializers() {
        return initializers;
    }

    @Override
    public Kind getKind() {
        return Kind.NEW_ARRAY;
    }

    @Override
    public JTExpression<?, ?> getType() {
        return type;
    }

    public JTNewArray setType(JTExpression<?, ?> type) {
        if (this.type == type) {
            return this;
        }
        this.type = Objects.requireNonNull(type).setParentTree(this);
        return setActionChange();
    }
}
