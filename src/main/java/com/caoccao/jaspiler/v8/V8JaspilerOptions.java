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
import java.util.function.Consumer;

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
        private static final String PROPERTY_COMPILATION_UNIT = "CompilationUnit";
        private static final String PROPERTY_PACKAGE = "Package";
        private V8ValueFunction visitCompilationUnit;
        private V8ValueFunction visitPackage;

        public Visitor() {
            reset();
        }

        @Override
        public void close() {
            JavetResourceUtils.safeClose(
                    visitCompilationUnit,
                    visitPackage);
            reset();
        }

        public Visitor deserialize(V8ValueObject v8ValueObject) throws JavetException {
            deserializeFunction(v8ValueObject, PROPERTY_COMPILATION_UNIT, this::setVisitCompilationUnit);
            deserializeFunction(v8ValueObject, PROPERTY_PACKAGE, this::setVisitPackage);
            return this;
        }

        private void deserializeFunction(
                V8ValueObject v8ValueObject,
                String propertyName,
                Consumer<V8ValueFunction> setter) throws JavetException {
            V8Value v8Value = v8ValueObject.get(propertyName);
            if (v8Value instanceof V8ValueFunction v8ValueFunction) {
                setter.accept(v8ValueFunction);
            } else {
                JavetResourceUtils.safeClose(v8Value);
            }
        }

        public V8ValueFunction getVisitCompilationUnit() {
            return visitCompilationUnit;
        }

        public V8ValueFunction getVisitPackage() {
            return visitPackage;
        }

        @Override
        public boolean isClosed() {
            return ObjectUtils.allNull(visitCompilationUnit, visitPackage);
        }

        public boolean isValid() {
            return !isClosed();
        }

        private void reset() {
            setVisitCompilationUnit(null);
            setVisitPackage(null);
        }

        public void setVisitCompilationUnit(V8ValueFunction visitCompilationUnit) {
            this.visitCompilationUnit = visitCompilationUnit;
        }

        public void setVisitPackage(V8ValueFunction visitPackage) {
            this.visitPackage = visitPackage;
        }
    }
}
