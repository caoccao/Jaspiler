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
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.TreeVisitor;

import javax.lang.model.element.Modifier;
import java.util.*;
import java.util.stream.Stream;

public final class JTModifiers
        extends JTTree<ModifiersTree, JTModifiers>
        implements ModifiersTree, IJTAnnotatable {
    private final List<JTAnnotation> annotations;
    private final Set<Modifier> flags;

    JTModifiers(ModifiersTree modifiersTree, JTTree<?, ?> parentTree) {
        super(modifiersTree, parentTree);
        annotations = new ArrayList<>();
        flags = new HashSet<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitModifiers(this, data);
    }

    @Override
    JTModifiers analyze() {
        super.analyze();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getAnnotations(), this, JTAnnotation::new, annotations::add);
        flags.clear();
        flags.addAll(getOriginalTree().getFlags());
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        annotations.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public Set<Modifier> getFlags() {
        return flags;
    }

    @Override
    public Kind getKind() {
        return Kind.MODIFIERS;
    }

    @Override
    public String toString() {
        if (isActionChange()) {
            int indentAnnotation = getIndent(-1);
            final var sbp = new StringBuilderPlus();
            ForEachUtils.forEach(
                    annotations.stream().filter(Objects::nonNull).filter(tree -> !tree.isActionIgnore()).toList(),
                    sbp::append,
                    tree -> sbp.appendLineSeparator().appendSpace(indentAnnotation),
                    null,
                    trees -> sbp.appendLineSeparator());
            ForEachUtils.forEach(
                    Stream.of(Modifier.values()).filter(flags::contains).toList(),
                    sbp::append,
                    tree -> sbp.appendSpace(),
                    trees -> sbp.appendSpaceIfNeeded());
            return sbp.toString();
        }
        return super.toString();
    }
}
