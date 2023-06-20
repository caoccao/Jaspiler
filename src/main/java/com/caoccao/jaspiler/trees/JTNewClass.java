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
import com.caoccao.jaspiler.utils.V8Register;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.TreeVisitor;

import java.util.*;

public final class JTNewClass
        extends JTPolyExpression<NewClassTree, JTNewClass>
        implements NewClassTree {
    private static final String PROPERTY_ARGUMENTS = "arguments";
    private static final String PROPERTY_CLASS_BODY = "classBody";
    private static final String PROPERTY_ENCLOSING_EXPRESSION = "enclosingExpression";
    private static final String PROPERTY_IDENTIFIER = "identifier";
    private static final String PROPERTY_TYPE_ARGUMENTS = "typeArguments";
    private final List<JTExpression<?, ?>> arguments;
    private final List<JTExpression<?, ?>> typeArguments;
    private JTClassDecl classBody;
    private JTExpression<?, ?> enclosingExpression;
    private JTExpression<?, ?> identifier;

    public JTNewClass() {
        this(null, null);
        setActionChange();
    }

    JTNewClass(NewClassTree newClassTree, JTTree<?, ?> parentTree) {
        super(newClassTree, parentTree);
        arguments = new ArrayList<>();
        classBody = null;
        enclosingExpression = null;
        identifier = null;
        typeArguments = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitNewClass(this, data);
    }

    @Override
    JTNewClass analyze() {
        super.analyze();
        enclosingExpression = JTTreeFactory.create(getOriginalTree().getEnclosingExpression(), this);
        identifier = JTTreeFactory.create(getOriginalTree().getIdentifier(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getTypeArguments(), this, (JTExpression<?, ?> o) -> typeArguments.add(o));
        JTTreeFactory.createAndAdd(
                getOriginalTree().getArguments(), this, (JTExpression<?, ?> o) -> arguments.add(o));
        classBody = JTTreeFactory.create(getOriginalTree().getClassBody(), this, JTClassDecl::new);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(enclosingExpression).ifPresent(nodes::add);
        Optional.ofNullable(identifier).ifPresent(nodes::add);
        typeArguments.stream().filter(Objects::nonNull).forEach(nodes::add);
        arguments.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(classBody).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTExpression<?, ?>> getArguments() {
        return arguments;
    }

    @Override
    public JTClassDecl getClassBody() {
        return classBody;
    }

    @Override
    public JTExpression<?, ?> getEnclosingExpression() {
        return enclosingExpression;
    }

    @Override
    public JTExpression<?, ?> getIdentifier() {
        return identifier;
    }

    @Override
    public Kind getKind() {
        return Kind.NEW_CLASS;
    }

    @Override
    public List<JTExpression<?, ?>> getTypeArguments() {
        return typeArguments;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_ARGUMENTS, propertyName -> v8Runtime.toV8Value(getArguments()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_CLASS_BODY, propertyName -> v8Runtime.toV8Value(getClassBody()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_ENCLOSING_EXPRESSION, propertyName -> v8Runtime.toV8Value(getEnclosingExpression()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_IDENTIFIER, propertyName -> v8Runtime.toV8Value(getIdentifier()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_TYPE_ARGUMENTS, propertyName -> v8Runtime.toV8Value(getTypeArguments()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_ARGUMENTS,
                    (propertyName, propertyValue) -> replaceExpressions(arguments, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_CLASS_BODY,
                    (propertyName, propertyValue) -> replaceClassDecl(this::setClassBody, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_ENCLOSING_EXPRESSION,
                    (propertyName, propertyValue) -> replaceExpression(this::setEnclosingExpression, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_IDENTIFIER,
                    (propertyName, propertyValue) -> replaceExpression(this::setIdentifier, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_TYPE_ARGUMENTS,
                    (propertyName, propertyValue) -> replaceExpressions(typeArguments, propertyValue));
        }
        return stringSetterMap;
    }

    public JTNewClass setClassBody(JTClassDecl classBody) {
        if (this.classBody == classBody) {
            return this;
        }
        this.classBody = Optional.ofNullable(classBody).map(o -> o.setParentTree(this)).orElse(null);
        return setActionChange();
    }

    public JTNewClass setEnclosingExpression(JTExpression<?, ?> enclosingExpression) {
        if (this.enclosingExpression == enclosingExpression) {
            return this;
        }
        this.enclosingExpression = Optional.ofNullable(enclosingExpression).map(o -> o.setParentTree(this)).orElse(null);
        return setActionChange();
    }

    public JTNewClass setIdentifier(JTExpression<?, ?> identifier) {
        if (this.identifier == identifier) {
            return this;
        }
        this.identifier = Objects.requireNonNull(identifier).setParentTree(this);
        return setActionChange();
    }
}
