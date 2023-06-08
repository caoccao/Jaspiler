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
import com.sun.source.tree.PackageTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The type Jt package tree.
 * It references com.sun.tools.javac.tree.JCTree.JCPackageDecl.
 */
public final class JTPackageDecl
        extends JTTree<PackageTree, JTPackageDecl>
        implements PackageTree, IJTAnnotatable {
    private final List<JTAnnotation> annotations;
    private JTExpression<?, ?> packageName;

    public JTPackageDecl() {
        this(null, null);
        setActionChange();
    }

    JTPackageDecl(PackageTree originalTree, JTTree<?, ?> parentTree) {
        super(originalTree, parentTree);
        annotations = new ArrayList<>();
        packageName = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitPackage(this, data);
    }

    @Override
    JTPackageDecl analyze() {
        super.analyze();
        packageName = JTTreeFactory.create(getOriginalTree().getPackageName(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getAnnotations(), this, JTAnnotation::new, annotations::add);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        annotations.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(packageName).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public Kind getKind() {
        return Kind.PACKAGE;
    }

    @Override
    public JTExpression<?, ?> getPackageName() {
        return packageName;
    }

    public JTPackageDecl setPackageName(JTExpression<?, ?> packageName) {
        if (this.packageName == packageName) {
            return this;
        }
        this.packageName = Objects.requireNonNull(packageName).setParentTree(this);
        return setActionChange();
    }

    @Override
    public String toString() {
        if (isActionChange()) {
            final var sbp = new StringBuilderPlus();
            ForEachUtils.forEach(
                    annotations.stream().filter(Objects::nonNull).filter(tree -> !tree.isActionIgnore()).toList(),
                    sbp::append,
                    tree -> sbp.appendLineSeparator(),
                    null,
                    trees -> sbp.appendLineSeparator());
            Optional.ofNullable(packageName)
                    .ifPresent(tree -> sbp.append(IJTConstants.PACKAGE).appendSpace().append(tree)
                            .appendSemiColon().appendLineSeparator(2));
            return sbp.toString();
        }
        return super.toString();
    }
}
