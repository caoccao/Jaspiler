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

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.TreeVisitor;

import javax.lang.model.element.Name;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTClassDecl
        extends JTStatement<ClassTree, JTClassDecl>
        implements ClassTree {
    private final List<JTTypeParameter> typeParameters;
    private JTModifiers modifiers;

    public JTClassDecl() {
        this(null, null);
        setActionChange();
    }

    JTClassDecl(ClassTree classTree, JTTree<?, ?> parentTree) {
        super(classTree, parentTree);
        modifiers = null;
        typeParameters = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitClass(this, data);
    }

    @Override
    JTClassDecl analyze() {
        super.analyze();
        modifiers = Optional.ofNullable(getOriginalTree().getModifiers())
                .map(o -> new JTModifiers(o, this).analyze())
                .orElse(null);
        getOriginalTree().getTypeParameters().stream()
                .map(o -> new JTTypeParameter(o, this).analyze())
                .forEach(typeParameters::add);
//        r = scanAndReduce(node.getTypeParameters(), p, r);
//        r = scanAndReduce(node.getExtendsClause(), p, r);
//        r = scanAndReduce(node.getImplementsClause(), p, r);
//        r = scanAndReduce(node.getPermitsClause(), p, r);
//        r = scanAndReduce(node.getMembers(), p, r);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(modifiers).ifPresent(nodes::add);
        typeParameters.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Tree getExtendsClause() {
        return null;
    }

    @Override
    public List<? extends Tree> getImplementsClause() {
        return null;
    }

    @Override
    public Kind getKind() {
        return Kind.CLASS;
    }

    @Override
    protected int getLineSeparatorCount() {
        return 2;
    }

    @Override
    public List<? extends Tree> getMembers() {
        return null;
    }

    @Override
    public JTModifiers getModifiers() {
        return modifiers;
    }

    @Override
    public Name getSimpleName() {
        return null;
    }

    @Override
    public List<JTTypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public JTClassDecl setModifiers(JTModifiers modifiers) {
        this.modifiers = Objects.requireNonNull(modifiers).setParentTree(this);
        return setActionChange();
    }
}
