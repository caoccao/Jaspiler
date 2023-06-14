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
import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.caoccao.jaspiler.utils.V8Register;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.reference.V8ValueSymbol;
import com.caoccao.javet.values.reference.builtin.V8ValueBuiltInSymbol;
import com.sun.source.tree.Tree;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class JTTree<
        OriginalTree extends Tree,
        NewTree extends JTTree<OriginalTree, NewTree>>
        extends BaseLoggingObject
        implements IJTTree<OriginalTree, NewTree> {
    protected static final String FUNCTION_GET_PARENT_TREE = "getParentTree";
    protected static final String FUNCTION_IS_ACTION_CHANGE = "isActionChange";
    protected static final String FUNCTION_IS_ACTION_IGNORE = "isActionIgnore";
    protected static final String FUNCTION_IS_ACTION_NO_CHANGE = "isActionNoChange";
    protected static final String FUNCTION_SET_ACTION_CHANGE = "setActionChange";
    protected static final String FUNCTION_SET_ACTION_IGNORE = "setActionIgnore";
    protected static final String FUNCTION_SET_ACTION_NO_CHANGE = "setActionNoChange";
    protected static final String FUNCTION_TO_STRING = "toString";
    protected static final String PROPERTY_CLASS_NAME = "className";
    protected static final String PROPERTY_CLASS_SIMPLE_NAME = "classSimpleName";
    protected static final long INVALID_POSITION = -1L;
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

    int getIndent() {
        return getIndent(0);
    }

    int getIndent(int depth) {
        int indent = depth * getCompilationUnit().getOptions().getIndentSize();
        var ancestorTree = getParentTree();
        while (ancestorTree != null && !(ancestorTree instanceof JTCompilationUnit)) {
            if (ancestorTree instanceof JTClassDecl
                    || ancestorTree instanceof JTMethodDecl) {
                indent += getCompilationUnit().getOptions().getIndentSize();
            }
            ancestorTree = ancestorTree.getParentTree();
        }
        return Math.max(0, indent);
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
            V8Register.putStringGetter(v8Runtime, stringGetterMap, FUNCTION_GET_PARENT_TREE,
                    property -> v8Runtime.toV8Value(getParentTree()));
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

    protected boolean save(Writer writer) throws IOException {
        if (isActionIgnore()) {
            return false;
        }
        writer.write(toString());
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
        if (isActionIgnore()) {
            return IJTConstants.EMPTY;
        }
        if (isActionChange()) {
            throw new JaspilerNotImplementedException(getClass().getSimpleName() + "{} is not implemented yet.");
        }
        if (!getOriginalPosition().isValid()) {
            return IJTConstants.EMPTY;
        }
        return getOriginalCode().substring(
                (int) getOriginalPosition().startPosition(),
                (int) getOriginalPosition().endPosition());
    }

    protected NewTree writeStrings(Writer writer, String... strings) throws IOException {
        for (String str : strings) {
            writer.write(str);
        }
        return (NewTree) this;
    }
}
