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
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTClassDecl
        extends JTStatement<ClassTree, JTClassDecl>
        implements ClassTree {
    private final List<JTExpression<?, ?>> implementsClauses;
    private final List<JTTree<?, ?>> members;
    private final List<JTExpression<?, ?>> permitsClauses;
    private final List<JTTypeParameter> typeParameters;
    private JTExpression<?, ?> extendsClause; private JTModifiers modifiers;
    private JTName simpleName;

    public JTClassDecl() {
        this(null, null);
        setActionChange();
    }

    JTClassDecl(ClassTree classTree, JTTree<?, ?> parentTree) {
        super(classTree, parentTree);
        extendsClause = null;
        implementsClauses = new ArrayList<>();
        members = new ArrayList<>();
        modifiers = null;
        permitsClauses = new ArrayList<>();
        simpleName = null;
        typeParameters = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitClass(this, data);
    }

    @Override
    JTClassDecl analyze() {
        super.analyze();
        modifiers = JTTreeFactory.create(
                getOriginalTree().getModifiers(), this, JTModifiers::new);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getTypeParameters(), this, JTTypeParameter::new, typeParameters::add);
        extendsClause = JTTreeFactory.create(getOriginalTree().getExtendsClause(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getImplementsClause(), this, (JTExpression<?, ?> o) -> implementsClauses.add(o));
        JTTreeFactory.createAndAdd(
                getOriginalTree().getPermitsClause(), this, (JTExpression<?, ?> o) -> permitsClauses.add(o));
        JTTreeFactory.createAndAdd(
                getOriginalTree().getMembers(), this, members::add);
        simpleName = JTTreeFactory.createName(getOriginalTree().getSimpleName());
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(modifiers).ifPresent(nodes::add);
        typeParameters.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(extendsClause).ifPresent(nodes::add);
        implementsClauses.stream().filter(Objects::nonNull).forEach(nodes::add);
        permitsClauses.stream().filter(Objects::nonNull).forEach(nodes::add);
        members.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTExpression<?, ?> getExtendsClause() {
        return extendsClause;
    }

    @Override
    public List<JTExpression<?, ?>> getImplementsClause() {
        return implementsClauses;
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
    public List<JTTree<?, ?>> getMembers() {
        return members;
    }

    @Override
    public JTModifiers getModifiers() {
        return modifiers;
    }

    @Override
    public List<JTExpression<?, ?>> getPermitsClause() {
        return permitsClauses;
    }

    @Override
    public JTName getSimpleName() {
        return simpleName;
    }

    @Override
    public List<JTTypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public JTClassDecl setExtendsClause(JTExpression<?, ?> extendsClause) {
        if (this.extendsClause == extendsClause) {
            return this;
        }
        this.extendsClause = Objects.requireNonNull(extendsClause).setParentTree(this);
        return setActionChange();
    }

    public JTClassDecl setModifiers(JTModifiers modifiers) {
        if (this.modifiers == modifiers) {
            return this;
        }
        this.modifiers = Objects.requireNonNull(modifiers).setParentTree(this);
        return setActionChange();
    }

    public JTClassDecl setSimpleName(JTName simpleName) {
        if (this.simpleName == simpleName) {
            return this;
        }
        this.simpleName = Objects.requireNonNull(simpleName);
        return setActionChange();
    }

    @Override
    public String toString() {
        if (isActionChange()) {
            var stringBuilder = new StringBuilder();
            getAllNodes().forEach(stringBuilder::append);
            // TODO
            return stringBuilder.toString();
        }
        return super.toString();
    }
}
