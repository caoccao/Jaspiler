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
        implements PackageTree {
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
        packageName = Optional.ofNullable(getOriginalTree().getPackageName())
                .map(o -> (JTExpression<?, ?>) JTTreeFactory.createFrom(o, this))
                .orElse(null);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        nodes.addAll(annotations);
        nodes.add(packageName);
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
    protected int getLineSeparatorCount() {
        return 2;
    }

    @Override
    public JTExpression<?, ?> getPackageName() {
        return packageName;
    }

    public JTPackageDecl setPackageName(JTExpression<?, ?> packageName) {
        this.packageName = Objects.requireNonNull(packageName).setParentTree(this);
        return setActionChange();
    }

    @Override
    public String toString() {
        if (isActionChange()) {
            var stringBuilder = new StringBuilder();
            annotations.forEach(stringBuilder::append);
            if (packageName != null) {
                stringBuilder.append(IJTConstants.PACKAGE_).append(packageName)
                        .append(IJTConstants.SEMI_COLON).append(JTLineSeparator.L2);
            }
            return stringBuilder.toString();
        }
        return super.toString();
    }
}
