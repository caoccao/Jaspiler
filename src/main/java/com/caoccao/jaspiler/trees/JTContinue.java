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
import com.sun.source.tree.ContinueTree;
import com.sun.source.tree.TreeVisitor;

import java.util.Map;

public final class JTContinue
        extends JTStatement<ContinueTree, JTContinue>
        implements ContinueTree {
    private static final String PROPERTY_LABEL = "label";
    private JTName label;

    public JTContinue() {
        this(null, null);
        setActionChange();
    }

    JTContinue(ContinueTree continueTree, JTTree<?, ?> parentTree) {
        super(continueTree, parentTree);
        label = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitContinue(this, data);
    }

    @Override
    public Kind getKind() {
        return Kind.CONTINUE;
    }

    @Override
    public JTName getLabel() {
        return label;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_LABEL, propertyName -> v8Runtime.toV8Value(getLabel()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_LABEL,
                    (propertyName, propertyValue) -> replaceName(this::setLabel, propertyValue));
        }
        return stringSetterMap;
    }

    public JTContinue setLabel(JTName label) {
        if (this.label == label) {
            return this;
        }
        this.label = label;
        return setActionChange();
    }
}
