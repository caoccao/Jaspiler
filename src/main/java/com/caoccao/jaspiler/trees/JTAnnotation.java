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

import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Jt annotation tree.
 * It references com.sun.tools.javac.tree.JCTree.JCAnnotation.
 */
public final class JTAnnotation
        extends JTExpression<AnnotationTree, JTAnnotation>
        implements AnnotationTree {
    private final List<JTExpression<?, ?>> arguments;
    private JTTree<?, ?> annotationType;

    public JTAnnotation() {
        this(null, null);
        setActionChange();
    }

    JTAnnotation(AnnotationTree originalTree, JTTree<?, ?> parentTree) {
        super(originalTree, parentTree);
        annotationType = null;
        arguments = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitAnnotation(this, data);
    }

    @Override
    JTAnnotation analyze() {
        super.analyze();

        return this;
    }

    @Override
    public JTTree<?, ?> getAnnotationType() {
        return annotationType;
    }

    @Override
    public List<JTExpression<?, ?>> getArguments() {
        return arguments;
    }

    @Override
    public Kind getKind() {
        return Kind.ANNOTATION;
    }

    @Override
    public boolean isActionChange() {
        return isActionChange(getAnnotationType(), getArguments());
    }

    public JTAnnotation setAnnotationType(JTTree<?, ?> annotationType) {
        this.annotationType = Objects.requireNonNull(annotationType).setParentTree(this).setOriginalPosition(this.annotationType);
        return setActionChange();
    }
}
