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
import com.caoccao.jaspiler.utils.ForEachUtils;
import com.caoccao.jaspiler.utils.V8Register;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.TreeVisitor;

import java.util.*;

public final class JTMethodDecl
        extends JTTree<MethodTree, JTMethodDecl>
        implements MethodTree {
    private static final String PROPERTY_BODY = "body";
    private static final String PROPERTY_DEFAULT_VALUE = "defaultValue";
    private static final String PROPERTY_MODIFIERS = "modifiers";
    private static final String PROPERTY_NAME = "name";
    private static final String PROPERTY_PARAMETERS = "parameters";
    private static final String PROPERTY_RECEIVER_PARAMETER = "receiverParameter";
    private static final String PROPERTY_RETURN_TYPE = "returnType";
    private static final String PROPERTY_THROW_EXPRESSIONS = "throwExpressions";
    private static final String PROPERTY_TYPE_PARAMETERS = "typeParameters";
    private final List<JTVariableDecl> parameters;
    private final List<JTExpression<?, ?>> throwExpressions;
    private final List<JTTypeParameter> typeParameters;
    private JTBlock body;
    private JTExpression<?, ?> defaultValue;
    private JTModifiers modifiers;
    private JTName name;
    private JTVariableDecl receiverParameter;
    private JTExpression<?, ?> returnType;

    public JTMethodDecl() {
        this(null, null);
        setActionChange();
    }

    JTMethodDecl(MethodTree methodTree, JTTree<?, ?> parentTree) {
        super(methodTree, parentTree);
        body = null;
        defaultValue = null;
        modifiers = null;
        name = null;
        parameters = new ArrayList<>();
        receiverParameter = null;
        returnType = null;
        throwExpressions = new ArrayList<>();
        typeParameters = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitMethod(this, data);
    }

    @Override
    JTMethodDecl analyze() {
        super.analyze();
        modifiers = JTTreeFactory.create(
                getOriginalTree().getModifiers(), this, JTModifiers::new);
        returnType = JTTreeFactory.create(getOriginalTree().getReturnType(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getTypeParameters(), this, JTTypeParameter::new, typeParameters::add);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getParameters(), this, JTVariableDecl::new, parameters::add);
        receiverParameter = JTTreeFactory.create(
                getOriginalTree().getReceiverParameter(), this, JTVariableDecl::new);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getThrows(), this, (JTExpression<?, ?> o) -> throwExpressions.add(o));
        body = JTTreeFactory.create(
                getOriginalTree().getBody(), this, JTBlock::new);
        defaultValue = JTTreeFactory.create(getOriginalTree().getDefaultValue(), this);
        name = JTTreeFactory.createName(getOriginalTree().getName());
        if (Optional.ofNullable(modifiers).filter(IJTAnnotatable::containsIgnore).isPresent()) {
            setActionIgnore();
        }
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(modifiers).ifPresent(nodes::add);
        Optional.ofNullable(returnType).ifPresent(nodes::add);
        typeParameters.stream().filter(Objects::nonNull).forEach(nodes::add);
        parameters.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(receiverParameter).ifPresent(nodes::add);
        throwExpressions.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(body).ifPresent(nodes::add);
        Optional.ofNullable(defaultValue).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTBlock getBody() {
        return body;
    }

    @Override
    public JTExpression<?, ?> getDefaultValue() {
        return defaultValue;
    }

    @Override
    public Kind getKind() {
        return Kind.METHOD;
    }

    @Override
    public JTModifiers getModifiers() {
        return modifiers;
    }

    @Override
    public JTName getName() {
        return name;
    }

    @Override
    public List<JTVariableDecl> getParameters() {
        return parameters;
    }

    @Override
    public JTVariableDecl getReceiverParameter() {
        return receiverParameter;
    }

    @Override
    public JTExpression<?, ?> getReturnType() {
        return returnType;
    }

    @Override
    public List<JTExpression<?, ?>> getThrows() {
        return throwExpressions;
    }

    @Override
    public List<JTTypeParameter> getTypeParameters() {
        return typeParameters;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_BODY, propertyName -> v8Runtime.toV8Value(getBody()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_DEFAULT_VALUE, propertyName -> v8Runtime.toV8Value(getDefaultValue()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_MODIFIERS, propertyName -> v8Runtime.toV8Value(getModifiers()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_NAME, propertyName -> v8Runtime.toV8Value(getName()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_PARAMETERS, propertyName -> v8Runtime.toV8Value(getParameters()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_RECEIVER_PARAMETER, propertyName -> v8Runtime.toV8Value(getReceiverParameter()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_RETURN_TYPE, propertyName -> v8Runtime.toV8Value(getReturnType()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_THROW_EXPRESSIONS, propertyName -> v8Runtime.toV8Value(getThrows()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_TYPE_PARAMETERS, propertyName -> v8Runtime.toV8Value(getTypeParameters()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_BODY,
                    (propertyName, propertyValue) -> replaceBlock(this::setBody, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_DEFAULT_VALUE,
                    (propertyName, propertyValue) -> replaceExpression(this::setDefaultValue, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_MODIFIERS,
                    (propertyName, propertyValue) -> replaceModifiers(this::setModifiers, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_NAME,
                    (propertyName, propertyValue) -> replaceName(this::setName, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_PARAMETERS,
                    (propertyName, propertyValue) -> replaceVariableDecls(parameters, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_RECEIVER_PARAMETER,
                    (propertyName, propertyValue) -> replaceVariableDecl(this::setReceiverParameter, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_RETURN_TYPE,
                    (propertyName, propertyValue) -> replaceExpression(this::setReturnType, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_THROW_EXPRESSIONS,
                    (propertyName, propertyValue) -> replaceExpressions(throwExpressions, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_TYPE_PARAMETERS,
                    (propertyName, propertyValue) -> replaceTypeParameters(typeParameters, propertyValue));
        }
        return stringSetterMap;
    }

    @Override
    public boolean save(IStyleWriter<?> writer) {
        if (isActionChange()) {
            Optional.ofNullable(modifiers).ifPresent(writer::append);
            ForEachUtils.forEach(
                    typeParameters.stream().filter(Objects::nonNull).filter(tree -> !tree.isActionIgnore()).toList(),
                    writer::append,
                    tree -> writer.appendComma().appendSpace(),
                    trees -> writer.appendSpaceIfNeeded().appendLeftArrow(),
                    trees -> writer.appendRightArrow());
            Optional.of(returnType)
                    .filter(tree -> !tree.isActionIgnore())
                    .ifPresent(tree -> writer.appendSpaceIfNeeded().append(tree));
            writer.appendSpaceIfNeeded().append(name).appendLeftParenthesis();
            ForEachUtils.forEach(
                    parameters.stream().filter(Objects::nonNull).filter(tree -> !tree.isActionIgnore()).toList(),
                    writer::append,
                    tree -> writer.appendComma().appendSpace());
            writer.appendRightParenthesis();
            Optional.ofNullable(receiverParameter)
                    .filter(tree -> !tree.isActionIgnore())
                    .ifPresent(tree -> writer.appendSpace().append(tree));
            ForEachUtils.forEach(
                    throwExpressions.stream().filter(Objects::nonNull).filter(tree -> !tree.isActionIgnore()).toList(),
                    writer::append,
                    tree -> writer.appendComma().appendSpace(),
                    trees -> writer.appendKeyword(JavaKeyword.THROWS).appendSpace());
            Optional.ofNullable(defaultValue)
                    .filter(tree -> !tree.isActionIgnore())
                    .ifPresent(tree -> writer.appendKeyword(JavaKeyword.DEFAULT).appendSpace().append(tree));
            if (body != null && !body.isActionIgnore()) {
                writer.appendSpaceIfNeeded().append(body);
            } else {
                writer.appendSemiColon();
            }
            return true;
        }
        return super.save(writer);
    }

    public JTMethodDecl setBody(JTBlock body) {
        if (this.body == body) {
            return this;
        }
        this.body = Objects.requireNonNull(body).setParentTree(this);
        return setActionChange();
    }

    public JTMethodDecl setDefaultValue(JTExpression<?, ?> defaultValue) {
        if (this.defaultValue == defaultValue) {
            return this;
        }
        this.defaultValue = Optional.ofNullable(defaultValue).map(o -> o.setParentTree(this)).orElse(null);
        return setActionChange();
    }

    public JTMethodDecl setModifiers(JTModifiers modifiers) {
        if (this.modifiers == modifiers) {
            return this;
        }
        this.modifiers = Objects.requireNonNull(modifiers).setParentTree(this);
        return setActionChange();
    }

    public JTMethodDecl setName(JTName name) {
        if (this.name == name) {
            return this;
        }
        this.name = Objects.requireNonNull(name);
        return setActionChange();
    }

    public JTMethodDecl setReceiverParameter(JTVariableDecl receiverParameter) {
        if (this.receiverParameter == receiverParameter) {
            return this;
        }
        this.receiverParameter = Optional.ofNullable(receiverParameter).map(o -> o.setParentTree(this)).orElse(null);
        return setActionChange();
    }

    public JTMethodDecl setReturnType(JTExpression<?, ?> returnType) {
        if (this.returnType == returnType) {
            return this;
        }
        this.returnType = Objects.requireNonNull(returnType).setParentTree(this);
        return setActionChange();
    }
}
