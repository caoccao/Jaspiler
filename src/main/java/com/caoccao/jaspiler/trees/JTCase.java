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
import com.caoccao.javet.values.primitive.V8ValueString;
import com.sun.source.tree.CaseTree;
import com.sun.source.tree.TreeVisitor;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("preview")
public final class JTCase
        extends JTStatement<CaseTree, JTCase>
        implements CaseTree {
    private static final String PROPERTY_BODY = "body";
    private static final String PROPERTY_CASE_KIND = "caseKind";
    private static final String PROPERTY_LABELS = "labels";
    private static final String PROPERTY_STATEMENTS = "statements";
    private final List<JTCaseLabel<?, ?>> labels;
    private final List<JTStatement<?, ?>> statements;
    private JTTree<?, ?> body;
    private CaseKind caseKind;

    public JTCase() {
        this(null, null);
        setActionChange();
    }

    JTCase(CaseTree caseTree, JTTree<?, ?> parentTree) {
        super(caseTree, parentTree);
        body = null;
        caseKind = null;
        labels = new ArrayList<>();
        statements = new ArrayList<>();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitCase(this, data);
    }

    @Override
    JTCase analyze() {
        super.analyze();
        caseKind = getOriginalTree().getCaseKind();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getLabels(), this, (JTCaseLabel<?, ?> o) -> labels.add(o));
        if (caseKind == CaseTree.CaseKind.RULE) {
            body = JTTreeFactory.create(getOriginalTree().getBody(), this);
        } else {
            JTTreeFactory.createAndAdd(
                    getOriginalTree().getStatements(), this, (JTStatement<?, ?> o) -> statements.add(o));
        }
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        labels.stream().filter(Objects::nonNull).forEach(nodes::add);
        if (caseKind == CaseTree.CaseKind.RULE) {
            Optional.ofNullable(body).ifPresent(nodes::add);
        } else {
            statements.stream().filter(Objects::nonNull).forEach(nodes::add);
        }
        return nodes;
    }

    @Override
    public JTTree<?, ?> getBody() {
        return body;
    }

    @Override
    public CaseKind getCaseKind() {
        return caseKind;
    }

    @Deprecated
    @Override
    public JTExpression<?, ?> getExpression() {
        return labels.stream()
                .filter(o -> o instanceof JTExpression<?, ?>)
                .map(o -> (JTExpression<?, ?>) o)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<JTExpression<?, ?>> getExpressions() {
        return labels.stream()
                .filter(o -> o instanceof JTExpression<?, ?>)
                .map(o -> (JTExpression<?, ?>) o)
                .collect(Collectors.toList());
    }

    @Override
    public Kind getKind() {
        return Kind.CASE;
    }

    @Override
    public List<JTCaseLabel<?, ?>> getLabels() {
        return labels;
    }

    @Override
    public List<JTStatement<?, ?>> getStatements() {
        return caseKind == CaseKind.STATEMENT ? statements : null;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_BODY, propertyName -> v8Runtime.toV8Value(getBody()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_CASE_KIND, propertyName -> v8Runtime.createV8ValueString(getCaseKind().name()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_LABELS, propertyName -> v8Runtime.toV8Value(getLabels()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_STATEMENTS, propertyName -> v8Runtime.toV8Value(getStatements()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_BODY,
                    (propertyName, propertyValue) -> replaceTree(this::setBody, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_CASE_KIND,
                    (propertyName, propertyValue) -> setCaseKind(propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_LABELS,
                    (propertyName, propertyValue) -> replaceCaseLabels(labels, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_STATEMENTS,
                    (propertyName, propertyValue) -> replaceStatements(statements, propertyValue));
        }
        return stringSetterMap;
    }

    public JTCase setBody(JTTree<?, ?> body) {
        if (this.body == body) {
            return this;
        }
        this.body = Objects.requireNonNull(body).setParentTree(this);
        return setActionChange();
    }

    private boolean setCaseKind(V8Value v8Value) {
        if (v8Value instanceof V8ValueString v8ValueString) {
            setCaseKind(CaseKind.valueOf(v8ValueString.getValue()));
            return true;
        }
        return false;
    }

    public JTCase setCaseKind(CaseKind caseKind) {
        if (this.caseKind == caseKind) {
            return this;
        }
        this.caseKind = Objects.requireNonNull(caseKind);
        return setActionChange();
    }
}
