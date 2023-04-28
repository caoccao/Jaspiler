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

import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.TreeVisitor;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class JTModifiers
        extends JTTree<ModifiersTree, JTModifiers>
        implements ModifiersTree {
    private final List<JTAnnotation> annotations;
    private long flags;

    JTModifiers(ModifiersTree modifiersTree, JTTree<?, ?> parentTree) {
        super(modifiersTree, parentTree);
        annotations = new ArrayList<>();
        flags = 0L;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitModifiers(this, data);
    }

    @Override
    JTModifiers analyze() {
        super.analyze();
        getOriginalTree().getAnnotations().stream()
                .map(o -> new JTAnnotation(o, this).analyze())
                .forEach(annotations::add);
        flags = JTFlags.fromModifierSet(getOriginalTree().getFlags());
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        annotations.forEach(annotation -> nodes.add(annotation.setParentTree(this)));
        return nodes;
    }

    @Override
    public List<JTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public Set<Modifier> getFlags() {
        return JTFlags.toModifierSet(flags);
    }

    @Override
    public Kind getKind() {
        return Kind.MODIFIERS;
    }

    @Override
    protected int getLineSeparatorCount() {
        return 1;
    }

    public JTModifiers setFlags(long flags) {
        if (this.flags != flags) {
            this.flags = flags;
            return setActionChange();
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        return stringBuilder.toString();
    }
}