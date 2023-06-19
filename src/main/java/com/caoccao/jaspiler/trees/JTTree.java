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

import com.caoccao.jaspiler.JaspilerContract;
import com.caoccao.jaspiler.exceptions.JaspilerCheckedException;
import com.caoccao.jaspiler.exceptions.JaspilerNotImplementedException;
import com.caoccao.jaspiler.styles.IStyleWriter;
import com.caoccao.jaspiler.styles.StandardStyleWriter;
import com.caoccao.jaspiler.styles.StyleOptions;
import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.caoccao.jaspiler.utils.V8Register;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.reference.V8ValueSymbol;
import com.caoccao.javet.values.reference.builtin.V8ValueBuiltInSymbol;
import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public abstract class JTTree<
        OriginalTree extends Tree,
        NewTree extends JTTree<OriginalTree, NewTree>>
        extends BaseLoggingObject
        implements IJTTree<OriginalTree, NewTree> {
    protected static final String FUNCTION_IS_ACTION_CHANGE = "isActionChange";
    protected static final String FUNCTION_IS_ACTION_IGNORE = "isActionIgnore";
    protected static final String FUNCTION_IS_ACTION_NO_CHANGE = "isActionNoChange";
    protected static final String FUNCTION_SET_ACTION_CHANGE = "setActionChange";
    protected static final String FUNCTION_SET_ACTION_IGNORE = "setActionIgnore";
    protected static final String FUNCTION_SET_ACTION_NO_CHANGE = "setActionNoChange";
    protected static final String FUNCTION_TO_STRING = "toString";
    protected static final long INVALID_POSITION = -1L;
    protected static final String PROPERTY_CLASS_NAME = "className";
    protected static final String PROPERTY_CLASS_SIMPLE_NAME = "classSimpleName";
    protected static final String PROPERTY_KIND = "kind";
    protected static final String PROPERTY_PARENT_TREE = "parentTree";
    protected JaspilerContract.Action action;
    protected JTPosition originalPosition;
    protected OriginalTree originalTree;
    protected JTTree<?, ?> parentTree;
    protected Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> stringGetterMap;
    protected Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> stringSetterMap;
    protected Map<String, IJavetUniFunction<V8ValueSymbol, ? extends V8Value, JaspilerCheckedException>> symbolGetterMap;
    protected V8Runtime v8Runtime;

    JTTree(OriginalTree originalTree, JTTree<?, ?> parentTree) {
        super();
        originalPosition = JTPosition.Invalid;
        this.originalTree = originalTree;
        this.parentTree = parentTree;
        setAction(JaspilerContract.Action.NoChange);
        stringGetterMap = null;
        stringSetterMap = null;
        symbolGetterMap = null;
        setV8Runtime(null);
    }

    NewTree analyze() {
        originalPosition = getCompilationUnit().getOriginalPosition(getOriginalTree());
        return (NewTree) this;
    }

    @Override
    public JaspilerContract.Action getAction() {
        return action;
    }

    List<JTTree<?, ?>> getAllNodes() {
        return new ArrayList<>();
    }

    protected long getOptionalEndPosition(long position) {
        return getOriginalPosition().isValid() ? getOriginalPosition().endPosition() : position;
    }

    public String getOriginalCode() {
        return getCompilationUnit().getOriginalCode();
    }

    @Override
    public JTPosition getOriginalPosition() {
        return originalPosition;
    }

    @Override
    public OriginalTree getOriginalTree() {
        return originalTree;
    }

    @Override
    public JTTree<?, ?> getParentTree() {
        return parentTree;
    }

    @Override
    public V8Runtime getV8Runtime() {
        return v8Runtime;
    }

    @Override
    public boolean isActionChange() {
        if (isActionIgnore()) {
            return false;
        }
        if (getAction().isChange()) {
            return true;
        }
        return getAllNodes().stream()
                .anyMatch(jtTree -> jtTree.isActionChange() || jtTree.isActionIgnore());
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            stringGetterMap = new HashMap<>();
            V8Register.putStringGetter(v8Runtime, stringGetterMap, FUNCTION_IS_ACTION_CHANGE,
                    property -> v8Runtime.createV8ValueBoolean(isActionChange()));
            V8Register.putStringGetter(v8Runtime, stringGetterMap, FUNCTION_IS_ACTION_IGNORE,
                    property -> v8Runtime.createV8ValueBoolean(isActionIgnore()));
            V8Register.putStringGetter(v8Runtime, stringGetterMap, FUNCTION_IS_ACTION_NO_CHANGE,
                    property -> v8Runtime.createV8ValueBoolean(isActionNoChange()));
            V8Register.putStringGetter(v8Runtime, stringGetterMap, FUNCTION_SET_ACTION_CHANGE,
                    property -> {
                        setActionChange();
                        return v8Runtime.createV8ValueBoolean(true);
                    });
            V8Register.putStringGetter(v8Runtime, stringGetterMap, FUNCTION_SET_ACTION_IGNORE,
                    property -> {
                        setActionIgnore();
                        return v8Runtime.createV8ValueBoolean(true);
                    });
            V8Register.putStringGetter(v8Runtime, stringGetterMap, FUNCTION_SET_ACTION_NO_CHANGE,
                    property -> {
                        setActionNoChange();
                        return v8Runtime.createV8ValueBoolean(true);
                    });
            V8Register.putStringGetter(v8Runtime, stringGetterMap, FUNCTION_TO_STRING,
                    property -> v8Runtime.createV8ValueString(toString()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_CLASS_NAME,
                    property -> v8Runtime.createV8ValueString(getClass().getName()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_CLASS_SIMPLE_NAME,
                    property -> v8Runtime.createV8ValueString(getClass().getSimpleName()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_KIND,
                    property -> v8Runtime.createV8ValueString(getKind().name()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_PARENT_TREE,
                    property -> v8Runtime.toV8Value(getParentTree()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            stringSetterMap = new HashMap<>();
        }
        return stringSetterMap;
    }

    @Override
    public Map<String, IJavetUniFunction<V8ValueSymbol, ? extends V8Value, JaspilerCheckedException>> proxyGetSymbolGetterMap() {
        if (symbolGetterMap == null) {
            symbolGetterMap = new HashMap<>();
            V8Register.putSymbolGetter(v8Runtime, symbolGetterMap, V8ValueBuiltInSymbol.SYMBOL_PROPERTY_TO_PRIMITIVE,
                    description -> v8Runtime.createV8ValueString(toString()));
        }
        return symbolGetterMap;
    }

    protected boolean replaceAnnotations(List<JTAnnotation> list, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof List<?> trees) {
            list.clear();
            trees.stream()
                    .filter(tree -> tree instanceof JTAnnotation)
                    .map(tree -> ((JTAnnotation) tree).setParentTree(this))
                    .forEach(list::add);
            setActionChange();
            return true;
        }
        return false;
    }

    protected boolean replaceBlock(Function<JTBlock, NewTree> setter, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof JTBlock tree) {
            setter.apply(tree);
            return true;
        }
        return false;
    }

    protected boolean replaceCaseLabels(List<JTCaseLabel<?, ?>> list, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof List<?> trees) {
            list.clear();
            trees.stream()
                    .filter(tree -> tree instanceof JTCaseLabel<?, ?>)
                    .map(tree -> ((JTCaseLabel<?, ?>) tree).setParentTree(this))
                    .forEach(list::add);
            setActionChange();
            return true;
        }
        return false;
    }

    protected boolean replaceDirectives(List<JTDirective<?, ?>> list, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof List<?> trees) {
            list.clear();
            trees.stream()
                    .filter(tree -> tree instanceof JTDirective<?, ?>)
                    .map(tree -> ((JTDirective<?, ?>) tree).setParentTree(this))
                    .forEach(list::add);
            setActionChange();
            return true;
        }
        return false;
    }

    protected boolean replaceExpression(Function<JTExpression<?, ?>, NewTree> setter, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof JTExpression<?, ?> tree) {
            setter.apply(tree);
            return true;
        }
        return false;
    }

    protected boolean replaceExpressions(List<JTExpression<?, ?>> list, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof List<?> trees) {
            list.clear();
            trees.stream()
                    .filter(tree -> tree instanceof JTExpression<?, ?>)
                    .map(tree -> ((JTExpression<?, ?>) tree).setParentTree(this))
                    .forEach(list::add);
            setActionChange();
            return true;
        }
        return false;
    }

    protected boolean replaceImports(List<JTImport> list, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof List<?> trees) {
            list.clear();
            trees.stream()
                    .filter(tree -> tree instanceof JTImport)
                    .map(tree -> ((JTImport) tree).setParentTree(this))
                    .forEach(list::add);
            setActionChange();
            return true;
        }
        return false;
    }

    protected boolean replaceModifiers(Function<JTModifiers, NewTree> setter, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof JTModifiers tree) {
            setter.apply(tree);
            return true;
        }
        return false;
    }

    protected boolean replaceModuleDecl(Function<JTModuleDecl, NewTree> setter, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof JTModuleDecl tree) {
            setter.apply(tree);
            return true;
        }
        return false;
    }

    protected boolean replaceName(Function<JTName, NewTree> setter, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof JTName tree) {
            setter.apply(tree);
            return true;
        }
        return false;
    }

    protected boolean replacePackageDecl(Function<JTPackageDecl, NewTree> setter, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof JTPackageDecl tree) {
            setter.apply(tree);
            return true;
        }
        return false;
    }

    protected boolean replaceStatement(Function<JTStatement<?, ?>, NewTree> setter, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof JTStatement<?, ?> tree) {
            setter.apply(tree);
            return true;
        }
        return false;
    }

    protected boolean replaceStatements(List<JTStatement<?, ?>> list, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof List<?> trees) {
            list.clear();
            trees.stream()
                    .filter(tree -> tree instanceof JTStatement<?, ?>)
                    .map(tree -> ((JTStatement<?, ?>) tree).setParentTree(this))
                    .forEach(list::add);
            setActionChange();
            return true;
        }
        return false;
    }

    protected boolean replaceTree(Function<JTTree<?, ?>, NewTree> setter, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof JTTree<?, ?> tree) {
            setter.apply(tree);
            return true;
        }
        return false;
    }

    protected boolean replaceTrees(List<JTTree<?, ?>> list, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof List<?> trees) {
            list.clear();
            trees.stream()
                    .filter(tree -> tree instanceof JTTree<?, ?>)
                    .map(tree -> ((JTTree<?, ?>) tree).setParentTree(this))
                    .forEach(list::add);
            setActionChange();
            return true;
        }
        return false;
    }

    protected boolean replaceTypeParameters(List<JTTypeParameter> list, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof List<?> trees) {
            list.clear();
            trees.stream()
                    .filter(tree -> tree instanceof JTTypeParameter)
                    .map(tree -> ((JTTypeParameter) tree).setParentTree(this))
                    .forEach(list::add);
            setActionChange();
            return true;
        }
        return false;
    }

    protected boolean replaceVariableDecl(Function<JTVariableDecl, NewTree> setter, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof JTVariableDecl tree) {
            setter.apply(tree);
            return true;
        }
        return false;
    }

    protected boolean replaceVariableDecls(List<JTVariableDecl> list, V8Value v8Value) throws JavetException {
        if (v8Runtime.toObject(v8Value) instanceof List<?> trees) {
            list.clear();
            trees.stream()
                    .filter(tree -> tree instanceof JTVariableDecl)
                    .map(tree -> ((JTVariableDecl) tree).setParentTree(this))
                    .forEach(list::add);
            setActionChange();
            return true;
        }
        return false;
    }

    @Override
    public boolean save(IStyleWriter<?> writer) {
        if (isActionIgnore()) {
            return false;
        }
        if (isActionChange()) {
            throw new JaspilerNotImplementedException(getClass().getSimpleName() + "{} is not implemented yet.");
        }
        if (!getOriginalPosition().isValid()) {
            return false;
        }
        writer.append(getOriginalCode().substring(
                (int) getOriginalPosition().startPosition(),
                (int) getOriginalPosition().endPosition()));
        return true;
    }

    public NewTree setAction(JaspilerContract.Action action) {
        this.action = action;
        return (NewTree) this;
    }

    NewTree setParentTree(JTTree<?, ?> parentTree) {
        if (this.parentTree != parentTree) {
            this.parentTree = parentTree;
            return setActionChange();
        }
        return (NewTree) this;
    }

    @Override
    public void setV8Runtime(V8Runtime v8Runtime) {
        this.v8Runtime = v8Runtime;
    }

    @Override
    public String toString() {
        var writer = new StandardStyleWriter(StyleOptions.Default);
        save(writer);
        return writer.toString();
    }
}
