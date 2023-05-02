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

import com.sun.source.tree.MemberReferenceTree;
import com.sun.source.tree.TreeVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JTMemberReference
        extends JTFunctionalExpression<MemberReferenceTree, JTMemberReference>
        implements MemberReferenceTree {
    private final List<JTExpression<?, ?>> typeArguments;
    private ReferenceMode mode;
    private JTName name;
    private JTExpression<?, ?> qualifiedExpression;

    public JTMemberReference() {
        this(null, null);
        setActionChange();
    }

    JTMemberReference(MemberReferenceTree memberReferenceTree, JTTree<?, ?> parentTree) {
        super(memberReferenceTree, parentTree);
        mode = null;
        name = null;
        qualifiedExpression = null;
        typeArguments = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitMemberReference(this, data);
    }

    @Override
    JTMemberReference analyze() {
        super.analyze();
        qualifiedExpression = JTTreeFactory.create(getOriginalTree().getQualifierExpression(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getTypeArguments(), this, (JTExpression<?, ?> o) -> typeArguments.add(o));
        mode = getOriginalTree().getMode();
        name = JTTreeFactory.createName(getOriginalTree().getName());
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(qualifiedExpression).ifPresent(nodes::add);
        typeArguments.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.MEMBER_REFERENCE;
    }

    @Override
    public ReferenceMode getMode() {
        return mode;
    }

    @Override
    public JTName getName() {
        return name;
    }

    @Override
    public JTExpression<?, ?> getQualifierExpression() {
        return qualifiedExpression;
    }

    @Override
    public List<JTExpression<?, ?>> getTypeArguments() {
        return typeArguments;
    }

    public JTMemberReference setMode(ReferenceMode mode) {
        if (this.mode == mode) {
            return this;
        }
        this.mode = Objects.requireNonNull(mode);
        return setActionChange();
    }

    public JTMemberReference setName(JTName name) {
        if (this.name == name) {
            return this;
        }
        this.name = Objects.requireNonNull(name);
        return setActionChange();
    }

    public JTMemberReference setQualifiedExpression(JTExpression<?, ?> qualifiedExpression) {
        if (this.qualifiedExpression == qualifiedExpression) {
            return this;
        }
        this.qualifiedExpression = Objects.requireNonNull(qualifiedExpression).setParentTree(this);
        return setActionChange();
    }
}
