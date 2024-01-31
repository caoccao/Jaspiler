/*
 * Copyright (c) 2023-2024. caoccao.com Sam Cao
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
import com.caoccao.jaspiler.utils.ForEachUtils;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.tree.PackageTree;
import com.sun.source.tree.TreeVisitor;

import java.util.*;

/**
 * The type Jt package tree.
 * It references com.sun.tools.javac.tree.JCTree.JCPackageDecl.
 */
public final class JTPackageDecl
        extends JTTree<PackageTree, JTPackageDecl>
        implements PackageTree, IJTAnnotatable {
    private static final String PROPERTY_PACKAGE_NAME = "packageName";
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
        packageName = JTTreeFactory.create(getOriginalTree().getPackageName(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getAnnotations(), this, JTAnnotation::new, annotations::add);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        annotations.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(packageName).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
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
    public JTExpression<?, ?> getPackageName() {
        return packageName;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            registerStringGetter(PROPERTY_ANNOTATIONS, propertyName -> v8Runtime.toV8Value(getAnnotations()));
            registerStringGetter(PROPERTY_PACKAGE_NAME, propertyName -> v8Runtime.toV8Value(getPackageName()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            registerStringSetter(PROPERTY_ANNOTATIONS, (propertyName, propertyValue) -> replaceAnnotations(annotations, propertyValue));
            registerStringSetter(PROPERTY_PACKAGE_NAME, (propertyName, propertyValue) -> replaceExpression(this::setPackageName, propertyValue));
        }
        return stringSetterMap;
    }

    @Override
    public boolean serialize(IStyleWriter<?> writer) {
        if (isActionChange()) {
            ForEachUtils.forEach(
                    annotations.stream().filter(Objects::nonNull).filter(tree -> !tree.isActionIgnore()).toList(),
                    writer::append,
                    tree -> writer.appendLineSeparator(),
                    null,
                    trees -> writer.appendLineSeparator());
            Optional.ofNullable(packageName)
                    .ifPresent(tree -> writer.appendKeyword(JavaKeyword.PACKAGE).appendSpace().append(tree).appendSemiColon());
            return true;
        }
        return super.serialize(writer);
    }

    public JTPackageDecl setPackageName(JTExpression<?, ?> packageName) {
        if (this.packageName == packageName) {
            return this;
        }
        this.packageName = Objects.requireNonNull(packageName).setParentTree(this);
        return setActionChange();
    }
}
