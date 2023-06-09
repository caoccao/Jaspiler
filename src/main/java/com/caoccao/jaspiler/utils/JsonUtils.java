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

package com.caoccao.jaspiler.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class.getName());
    private static final ObjectMapper OBJECT_MAPPER_BEAUTIFIED = JsonMapper.builder()
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();
    private static final ObjectMapper OBJECT_MAPPER_BEAUTIFIED_SORTED = JsonMapper.builder()
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();

    private JsonUtils() {
    }

    public static String getJsonStringBeautified(Object obj) {
        return getJsonStringBeautified(obj, false);
    }

    public static String getJsonStringBeautified(Object obj, boolean sorted) {
        try {
            if (sorted) {
                return OBJECT_MAPPER_BEAUTIFIED_SORTED.writeValueAsString(obj);
            } else {
                return OBJECT_MAPPER_BEAUTIFIED.writeValueAsString(obj);
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize object to Json string.", e);
        } catch (Throwable t) {
            LOGGER.error("Failed to serialize object to Json string with unknown error.", t);
        }
        return null;
    }
}
