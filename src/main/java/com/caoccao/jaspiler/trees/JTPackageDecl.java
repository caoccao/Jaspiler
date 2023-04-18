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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
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

    public JTPackageDecl(PackageTree originalTree, JTTree<?, ?> parentTree) {
        super(originalTree, parentTree);
        annotations = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitPackage(this, data);
    }

    @Override
    public JTPackageDecl analyze() {
        setPackageName(Optional.ofNullable(getOriginalTree().getPackageName())
                .map(p -> JTExpression.from(p, this))
                .map(JTTree::analyze)
                .orElse(null));
        return this;
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

    @Override
    public JTPackageDecl save(Writer writer) throws IOException {
        if (isDirty()) {
            throw new UnsupportedOperationException();
        }
        return super.save(writer);
    }

    public JTPackageDecl setPackageName(JTExpression<?, ?> packageName) {
        this.packageName = packageName;
        return this;
    }
}
