/*
 * Copyright (c) 2023-2025. caoccao.com Sam Cao
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

import com.caoccao.jaspiler.exceptions.JaspilerCheckedException;
import com.caoccao.jaspiler.styles.IStyleWriter;
import com.caoccao.jaspiler.utils.ForEachUtils;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.caoccao.javet.values.reference.V8ValueArray;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.TreeVisitor;

import javax.lang.model.element.Modifier;
import java.util.*;

public final class JTModifiers
        extends JTTree<ModifiersTree, JTModifiers>
        implements ModifiersTree, IJTAnnotatable {
    private static final List<Modifier> ABSTRACT_OR_DEFAULT_OR_STATIC_MODIFIERS = List.of(
            Modifier.ABSTRACT, Modifier.DEFAULT, Modifier.STATIC);
    private static final List<Modifier> OTHER_MODIFIERS = List.of(
            Modifier.FINAL, Modifier.TRANSIENT, Modifier.VOLATILE,
            Modifier.SYNCHRONIZED, Modifier.NATIVE, Modifier.STRICTFP);
    private static final String PROPERTY_FLAGS = "flags";
    private static final List<Modifier> SCOPE_MODIFIERS = List.of(
            Modifier.PUBLIC, Modifier.PROTECTED, Modifier.PRIVATE);
    private static final List<Modifier> SEALED_OR_NON_SEALED_MODIFIERS = List.of(
            Modifier.SEALED, Modifier.NON_SEALED);
    private final List<JTAnnotation> annotations;
    private final Set<Modifier> flags;

    public JTModifiers() {
        this(null, null);
        setActionChange();
    }

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
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_ANNOTATIONS, propertyName -> v8Runtime.toV8Value(getAnnotations()));
            registerStringGetter(PROPERTY_FLAGS, propertyName -> v8Runtime.toV8Value(getFlags().stream()
                    .map(Modifier::toString)
                    .toList()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_ANNOTATIONS, (propertyName, propertyValue) -> replaceAnnotations(annotations, propertyValue));
            registerStringSetter(PROPERTY_FLAGS, (propertyName, propertyValue) -> setFlags(propertyValue));
        }
        return stringSetterMap;
    }

    @Override
    public boolean serialize(IStyleWriter<?> writer) {
        if (isActionChange()) {
            int annotationDepth = writer.getDepth() - 1;
            ForEachUtils.forEach(
                    annotations.stream().filter(Objects::nonNull).filter(tree -> !tree.isActionIgnore()).toList(),
                    tree -> writer.appendIndent(annotationDepth).append(tree).appendLineSeparator());
            List<Modifier> modifiers = new ArrayList<>();
            SCOPE_MODIFIERS.stream().filter(flags::contains).findFirst().ifPresent(modifiers::add);
            ABSTRACT_OR_DEFAULT_OR_STATIC_MODIFIERS.stream().filter(flags::contains).findFirst().ifPresent(modifiers::add);
            SEALED_OR_NON_SEALED_MODIFIERS.stream().filter(flags::contains).findFirst().ifPresent(modifiers::add);
            OTHER_MODIFIERS.stream().filter(flags::contains).forEach(modifiers::add);
            ForEachUtils.forEach(
                    modifiers,
                    writer::append,
                    tree -> writer.appendSpace(),
                    trees -> writer.appendSpaceIfNeeded());
            return true;
        }
        return super.serialize(writer);
    }

    private boolean setFlags(V8Value v8Value) throws JavetException {
        if (v8Value instanceof V8ValueArray v8ValueArray) {
            flags.clear();
            v8ValueArray.forEach(v8ValueItem -> {
                if (v8ValueItem instanceof V8ValueString v8ValueString) {
                    try {
                        flags.add(Modifier.valueOf(v8ValueString.getValue()));
                    } catch (Throwable ignored) {
                    }
                }
            });
            return true;
        }
        return false;
    }
}
