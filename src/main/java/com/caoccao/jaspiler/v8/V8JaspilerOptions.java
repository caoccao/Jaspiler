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

import com.caoccao.jaspiler.utils.JsonUtils;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.caoccao.javet.values.reference.V8ValueArray;
import com.caoccao.javet.values.reference.V8ValueObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;

public final class V8JaspilerOptions {
    public static final String PROPERTY_PARSE_RULES = "parseRules";
    public static final String PROPERTY_TRANSFORM_RULES = "transformRules";

    private final List<Rule> parseRules;
    private final List<Rule> transformRules;

    public V8JaspilerOptions() {
        parseRules = new ArrayList<>();
        transformRules = new ArrayList<>();
    }

    public static V8JaspilerOptions deserialize(V8ValueObject v8ValueObject) throws JavetException {
        var jaspilerOptions = new V8JaspilerOptions();
        try (V8Value v8Value = v8ValueObject.get(PROPERTY_PARSE_RULES)) {
            if (v8Value instanceof V8ValueArray v8ValueArray) {
                v8ValueArray.forEach(v8ValueRule -> {
                    if (v8ValueRule instanceof V8ValueObject v8ValueObjectRule) {
                        Optional.of(Rule.deserialize(v8ValueObjectRule))
                                .filter(Rule::isValid)
                                .ifPresent(jaspilerOptions.getParseRules()::add);
                    }
                });
            }
        }
        try (V8Value v8Value = v8ValueObject.get(PROPERTY_TRANSFORM_RULES)) {
            if (v8Value instanceof V8ValueArray v8ValueArray) {
                v8ValueArray.forEach(v8ValueRule -> {
                    if (v8ValueRule instanceof V8ValueObject v8ValueObjectRule) {
                        Optional.of(Rule.deserialize(v8ValueObjectRule))
                                .filter(Rule::isValid)
                                .ifPresent(jaspilerOptions.getTransformRules()::add);
                    }
                });
            }
        }
        return jaspilerOptions;
    }

    public List<Rule> getParseRules() {
        return parseRules;
    }

    public List<Rule> getTransformRules() {
        return transformRules;
    }

    @JsonIgnore
    public boolean isValid() {
        return CollectionUtils.isNotEmpty(parseRules) || CollectionUtils.isNotEmpty(transformRules);
    }

    @JsonIgnore
    public String toJson() {
        return JsonUtils.getJsonStringBeautified(this, true);
    }

    public static final class Rule {
        private static final String PROPERTY_FILTERS = "filters";
        private static final String PROPERTY_PATH = "path";
        private static final String PROPERTY_SOURCE_ROOT_PATH = "sourceRootPath";
        private static final String PROPERTY_TARGET_ROOT_PATH = "targetRootPath";
        private final Set<String> filters;
        private String path;
        private String sourceRootPath;
        private String targetRootPath;

        public Rule() {
            filters = new HashSet<>();
            setPath(null);
            setSourceRootPath(null);
            setTargetRootPath(null);
        }

        public static Rule deserialize(V8ValueObject v8ValueObject) throws JavetException {
            var rule = new Rule();
            rule.setPath(v8ValueObject.getString(PROPERTY_PATH));
            rule.setSourceRootPath(v8ValueObject.getString(PROPERTY_SOURCE_ROOT_PATH));
            rule.setTargetRootPath(v8ValueObject.getString(PROPERTY_TARGET_ROOT_PATH));
            try (V8Value v8Value = v8ValueObject.get(PROPERTY_FILTERS)) {
                if (v8Value instanceof V8ValueArray v8ValueArray) {
                    v8ValueArray.forEach(v8ValueFilter -> {
                        if (v8ValueFilter instanceof V8ValueString v8ValueStringFilter) {
                            Optional.ofNullable(v8ValueStringFilter.getValue()).ifPresent(rule.getFilters()::add);
                        }
                    });
                }
            }
            return rule;
        }

        public Set<String> getFilters() {
            return filters;
        }

        public String getPath() {
            return path;
        }

        public String getSourceRootPath() {
            return sourceRootPath;
        }

        public String getTargetRootPath() {
            return targetRootPath;
        }

        @JsonIgnore
        public boolean isValid() {
            return ObjectUtils.allNotNull(path, sourceRootPath, targetRootPath);
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setSourceRootPath(String sourceRootPath) {
            this.sourceRootPath = sourceRootPath;
        }

        public void setTargetRootPath(String targetRootPath) {
            this.targetRootPath = targetRootPath;
        }
    }
}
