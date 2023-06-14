/*
 * Copyright (c) 2023-2023. caoccao.com Sam Cao
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

package com.caoccao.jaspiler.v8;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interfaces.IJavetClosable;
import com.caoccao.javet.utils.JavetResourceUtils;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.reference.V8ValueArray;
import com.caoccao.javet.values.reference.V8ValueFunction;
import com.caoccao.javet.values.reference.V8ValueObject;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class V8JaspilerOptions implements IJavetClosable {
    public static final String PROPERTY_PLUGINS = "plugins";

    private final List<Plugin> plugins;

    public V8JaspilerOptions() {
        plugins = new ArrayList<>();
    }

    @Override
    public void close() {
        JavetResourceUtils.safeClose(plugins);
        plugins.clear();
    }

    public V8JaspilerOptions deserialize(V8ValueObject v8ValueObject) throws JavetException {
        try (V8Value v8Value = v8ValueObject.get(PROPERTY_PLUGINS)) {
            if (v8Value instanceof V8ValueArray v8ValueArray) {
                v8ValueArray.forEach(v8ValueRule -> {
                    if (v8ValueRule instanceof V8ValueObject v8ValueObjectPlugin) {
                        plugins.add(new Plugin().deserialize(v8ValueObjectPlugin));
                    }
                });
            }
        }
        return this;
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    @Override
    public boolean isClosed() {
        return CollectionUtils.isEmpty(plugins);
    }

    public static final class Plugin implements IJavetClosable {
        private static final String PROPERTY_VISITOR = "visitor";

        private Visitor visitor;

        public Plugin() {
            setVisitor(null);
        }

        @Override
        public void close() {
            JavetResourceUtils.safeClose(visitor);
            setVisitor(null);
        }

        public Plugin deserialize(V8ValueObject v8ValueObject) throws JavetException {
            try (var v8Value = v8ValueObject.get(PROPERTY_VISITOR)) {
                if (v8Value instanceof V8ValueObject v8ValueObjectVisitor) {
                    setVisitor(new Visitor().deserialize(v8ValueObjectVisitor));
                }
            }
            return this;
        }

        public Visitor getVisitor() {
            return visitor;
        }

        @Override
        public boolean isClosed() {
            return ObjectUtils.allNull(visitor);
        }

        public boolean isValid() {
            return ObjectUtils.allNotNull(visitor) && visitor.isValid();
        }

        public void setVisitor(Visitor visitor) {
            this.visitor = visitor;
        }
    }

    public static final class Visitor implements IJavetClosable {
        private final List<String> properties;
        private final List<Supplier<V8ValueFunction>> propertyGetters;
        private final List<Consumer<V8ValueFunction>> propertySetters;
        private V8ValueFunction visitClass;
        private V8ValueFunction visitCompilationUnit;
        private V8ValueFunction visitPackage;

        public Visitor() {
            properties = List.of(
                    "Class",
                    "CompilationUnit",
                    "Package");
            propertyGetters = List.of(
                    this::getVisitClass,
                    this::getVisitCompilationUnit,
                    this::getVisitPackage);
            propertySetters = List.of(
                    this::setVisitClass,
                    this::setVisitCompilationUnit,
                    this::setVisitPackage);
            reset();
        }

        @Override
        public void close() {
            JavetResourceUtils.safeClose(propertyGetters.stream().map(Supplier::get).toArray());
            reset();
        }

        public Visitor deserialize(V8ValueObject v8ValueObject) throws JavetException {
            final int length = properties.size();
            V8Value[] v8ValueKeys = new V8Value[length];
            V8Value[] v8ValueValues = new V8Value[length];
            try {
                var v8Runtime = v8ValueObject.getV8Runtime();
                for (int i = 0; i < length; i++) {
                    v8ValueKeys[i] = v8Runtime.createV8ValueString(properties.get(i));
                }
                v8ValueObject.batchGet(v8ValueKeys, v8ValueValues, length);
                for (int i = 0; i < length; i++) {
                    if (v8ValueValues[i] instanceof V8ValueFunction v8ValueFunction) {
                        propertySetters.get(i).accept(v8ValueFunction);
                        v8ValueValues[i] = null;
                    }
                }
            } finally {
                JavetResourceUtils.safeClose(v8ValueKeys);
                JavetResourceUtils.safeClose(v8ValueValues);
            }
            return this;
        }

        public V8ValueFunction getVisitClass() {
            return visitClass;
        }

        public V8ValueFunction getVisitCompilationUnit() {
            return visitCompilationUnit;
        }

        public V8ValueFunction getVisitPackage() {
            return visitPackage;
        }

        @Override
        public boolean isClosed() {
            return propertyGetters.stream().map(Supplier::get).allMatch(Objects::isNull);
        }

        public boolean isValid() {
            return !isClosed();
        }

        private void reset() {
            propertySetters.forEach(setter -> setter.accept(null));
        }

        public void setVisitClass(V8ValueFunction visitClass) {
            this.visitClass = visitClass;
        }

        public void setVisitCompilationUnit(V8ValueFunction visitCompilationUnit) {
            this.visitCompilationUnit = visitCompilationUnit;
        }

        public void setVisitPackage(V8ValueFunction visitPackage) {
            this.visitPackage = visitPackage;
        }
    }
}
