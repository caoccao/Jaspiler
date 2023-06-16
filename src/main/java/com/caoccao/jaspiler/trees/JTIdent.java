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
import com.caoccao.jaspiler.utils.V8Register;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.TreeVisitor;

import java.util.Map;
import java.util.Objects;

public final class JTIdent
        extends JTExpression<IdentifierTree, JTIdent>
        implements IdentifierTree {
    private static final String PROPERTY_NAME = "name";
    private JTName name;

    public JTIdent() {
        this(null, null);
        setActionChange();
    }

    JTIdent(IdentifierTree originalTree, JTTree<?, ?> parentTree) {
        super(originalTree, parentTree);
        name = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitIdentifier(this, data);
    }

    @Override
    JTIdent analyze() {
        super.analyze();
        name = JTTreeFactory.createName(getOriginalTree().getName());
        return this;
    }

    @Override
    public Kind getKind() {
        return Kind.IDENTIFIER;
    }

    @Override
    public JTName getName() {
        return name;
    }

    @Override
    public boolean isActionChange() {
        return getAction().isChange();
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_NAME, propertyName -> v8Runtime.toV8Value(getName()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_NAME,
                    (propertyName, propertyValue) -> replaceName(this::setName, propertyValue));
        }
        return stringSetterMap;
    }

    @Override
    public boolean save(IStyleWriter<?> writer) {
        if (isActionChange()) {
            if (name != null) {
                writer.append(name);
            }
            return true;
        }
        return super.save(writer);
    }

    public JTIdent setName(JTName name) {
        if (this.name == name) {
            return this;
        }
        this.name = Objects.requireNonNull(name);
        return setActionChange();
    }
}
