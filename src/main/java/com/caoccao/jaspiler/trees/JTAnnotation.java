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

import com.caoccao.jaspiler.exceptions.JaspilerCheckedException;
import com.caoccao.jaspiler.styles.IStyleWriter;
import com.caoccao.jaspiler.utils.ForEachUtils;
import com.caoccao.jaspiler.utils.V8Register;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.tree.AnnotationTree;
import com.sun.source.tree.TreeVisitor;

import java.util.*;

/**
 * The type Jt annotation tree.
 * It references com.sun.tools.javac.tree.JCTree.JCAnnotation.
 */
public final class JTAnnotation
        extends JTExpression<AnnotationTree, JTAnnotation>
        implements AnnotationTree {
    private static final String PROPERTY_ANNOTATION_TYPE = "annotationType";
    private static final String PROPERTY_ARGUMENTS = "arguments";
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
        annotationType = JTTreeFactory.create(getOriginalTree().getAnnotationType(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getArguments(), this, (JTExpression<?, ?> o) -> arguments.add(o));
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(annotationType).ifPresent(nodes::add);
        arguments.stream().filter(Objects::nonNull).forEach(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
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
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_ANNOTATION_TYPE, propertyName -> v8Runtime.toV8Value(getAnnotationType()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_ARGUMENTS, propertyName -> v8Runtime.toV8Value(getArguments()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_ANNOTATION_TYPE,
                    (propertyName, propertyValue) -> replaceTree(this::setAnnotationType, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_ARGUMENTS,
                    (propertyName, propertyValue) -> replaceExpressions(arguments, propertyValue));
        }
        return stringSetterMap;
    }

    @Override
    public boolean serialize(IStyleWriter<?> writer) {
        if (isActionChange()) {
            writer.appendAt().append(annotationType);
            ForEachUtils.forEach(
                    arguments.stream().filter(Objects::nonNull).filter(tree -> !tree.isActionIgnore()).toList(),
                    writer::append,
                    tree -> writer.appendComma().appendSpace(),
                    trees -> writer.appendLeftParenthesis(),
                    trees -> writer.appendRightParenthesis());
            return true;
        }
        return super.serialize(writer);
    }

    public JTAnnotation setAnnotationType(JTTree<?, ?> annotationType) {
        if (this.annotationType == annotationType) {
            return this;
        }
        this.annotationType = Objects.requireNonNull(annotationType).setParentTree(this);
        return setActionChange();
    }
}
