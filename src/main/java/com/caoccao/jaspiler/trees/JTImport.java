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

import com.caoccao.jaspiler.enums.JavaKeyword;
import com.caoccao.jaspiler.exceptions.JaspilerCheckedException;
import com.caoccao.jaspiler.styles.IStyleWriter;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JTImport
        extends JTTree<ImportTree, JTImport>
        implements ImportTree {
    public static final String PROPERTY_QUALIFIED_IDENTIFIER = "qualifiedIdentifier";
    public static final String PROPERTY_STATIC_IMPORT = "staticImport";
    private JTTree<?, ?> qualifiedIdentifier;
    private boolean staticImport;

    public JTImport() {
        this(null, null);
        setActionChange();
    }

    JTImport(ImportTree importTree, JTTree<?, ?> parentTree) {
        super(importTree, parentTree);
        staticImport = false;
        qualifiedIdentifier = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitImport(this, data);
    }

    @Override
    JTImport analyze() {
        super.analyze();
        qualifiedIdentifier = JTTreeFactory.create(getOriginalTree().getQualifiedIdentifier(), this);
        staticImport = getOriginalTree().isStatic();
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(qualifiedIdentifier).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public Kind getKind() {
        return Kind.IMPORT;
    }

    @Override
    public JTTree<?, ?> getQualifiedIdentifier() {
        return qualifiedIdentifier;
    }

    @Override
    public boolean isStatic() {
        return staticImport;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_QUALIFIED_IDENTIFIER, propertyName -> v8Runtime.toV8Value(getQualifiedIdentifier()));
            registerStringGetter(PROPERTY_STATIC_IMPORT, propertyName -> v8Runtime.createV8ValueBoolean(isStatic()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_QUALIFIED_IDENTIFIER, (propertyName, propertyValue) -> replaceTree(this::setQualifiedIdentifier, propertyValue));
            registerStringSetter(PROPERTY_STATIC_IMPORT, (propertyName, propertyValue) -> replaceBoolean(this::setStaticImport, propertyValue));
        }
        return stringSetterMap;
    }

    @Override
    public boolean serialize(IStyleWriter<?> writer) {
        if (isActionChange()) {
            writer.appendKeyword(JavaKeyword.IMPORT).appendSpace();
            if (staticImport) {
                writer.appendKeyword(JavaKeyword.STATIC).appendSpace();
            }
            writer.append(qualifiedIdentifier).appendSemiColon();
            return true;
        }
        return super.serialize(writer);
    }

    public JTImport setQualifiedIdentifier(JTTree<?, ?> qualifiedIdentifier) {
        if (this.qualifiedIdentifier == qualifiedIdentifier) {
            return this;
        }
        this.qualifiedIdentifier = Objects.requireNonNull(qualifiedIdentifier).setParentTree(this);
        return setActionChange();
    }

    public JTImport setStaticImport(boolean staticImport) {
        if (this.staticImport == staticImport) {
            return this;
        }
        this.staticImport = staticImport;
        return setActionChange();
    }
}
