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
import com.sun.source.tree.ModuleTree;
import com.sun.source.tree.TreeVisitor;

import java.util.*;

/**
 * The type Module Declaration.
 * <p>
 * module a.b.c {
 * exports a.b.c;
 * opens a.b.c;
 * requires x.y.z;
 * }
 */
public final class JTModuleDecl
        extends JTTree<ModuleTree, JTModuleDecl>
        implements ModuleTree, IJTAnnotatable {
    private static final String PROPERTY_DIRECTIVES = "directives";
    private static final String PROPERTY_NAME = "name";
    private final List<JTAnnotation> annotations;
    private final List<JTDirective<?, ?>> directives;
    private ModuleKind moduleType;
    private JTExpression<?, ?> name;

    public JTModuleDecl() {
        this(null, null);
        setActionChange();
    }

    JTModuleDecl(ModuleTree moduleTree, JTTree<?, ?> parentTree) {
        super(moduleTree, parentTree);
        annotations = new ArrayList<>();
        directives = new ArrayList<>();
        moduleType = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitModule(this, data);
    }

    @Override
    JTModuleDecl analyze() {
        super.analyze();
        JTTreeFactory.createAndAdd(
                getOriginalTree().getAnnotations(), this, JTAnnotation::new, annotations::add);
        name = JTTreeFactory.create(getOriginalTree().getName(), this);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getDirectives(), this, (JTDirective<?, ?> o) -> directives.add(o));
        if (containsIgnore()) {
            setActionIgnore();
        }
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        annotations.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(name).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public List<JTAnnotation> getAnnotations() {
        return annotations;
    }

    @Override
    public List<JTDirective<?, ?>> getDirectives() {
        return directives;
    }

    @Override
    public Kind getKind() {
        return Kind.MODULE;
    }

    @Override
    public ModuleKind getModuleType() {
        return moduleType;
    }

    @Override
    public JTExpression<?, ?> getName() {
        return name;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_ANNOTATIONS, propertyName -> v8Runtime.toV8Value(getAnnotations()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_NAME, propertyName -> v8Runtime.toV8Value(getName()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_DIRECTIVES, propertyName -> v8Runtime.toV8Value(getDirectives()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_ANNOTATIONS,
                    (propertyName, propertyValue) -> {
                        if (v8Runtime.toObject(propertyValue) instanceof List<?> trees) {
                            annotations.clear();
                            trees.stream()
                                    .filter(tree -> tree instanceof JTAnnotation)
                                    .map(tree -> ((JTAnnotation) tree).setParentTree(this))
                                    .forEach(annotations::add);
                            setActionChange();
                            return true;
                        }
                        return false;
                    });
            V8Register.putStringSetter(stringSetterMap, PROPERTY_NAME,
                    (propertyName, propertyValue) -> {
                        if (v8Runtime.toObject(propertyValue) instanceof JTExpression<?, ?> tree) {
                            setName(tree);
                            return true;
                        }
                        return false;
                    });
            V8Register.putStringSetter(stringSetterMap, PROPERTY_DIRECTIVES,
                    (propertyName, propertyValue) -> {
                        if (v8Runtime.toObject(propertyValue) instanceof List<?> trees) {
                            directives.clear();
                            trees.stream()
                                    .filter(tree -> tree instanceof JTDirective<?, ?>)
                                    .map(tree -> ((JTDirective<?, ?>) tree).setParentTree(this))
                                    .forEach(directives::add);
                            setActionChange();
                            return true;
                        }
                        return false;
                    });
        }
        return stringSetterMap;
    }

    public JTModuleDecl setModuleType(ModuleKind moduleType) {
        if (this.moduleType == moduleType) {
            return this;
        }
        this.moduleType = Objects.requireNonNull(moduleType);
        return setActionChange();
    }

    public JTModuleDecl setName(JTExpression<?, ?> name) {
        if (this.name == name) {
            return this;
        }
        this.name = Objects.requireNonNull(name).setParentTree(this);
        return setActionChange();
    }
}
