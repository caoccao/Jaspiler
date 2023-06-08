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

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.function.Consumer;

public final class ForEachUtils {
    private ForEachUtils() {
    }

    public static <T> int forEach(
            List<T> list,
            Consumer<T> itemConsumer,
            Consumer<T> interItemConsumer) {
        return forEach(list, itemConsumer, interItemConsumer, null, null);
    }

    public static <T> int forEach(
            List<T> list,
            Consumer<T> itemConsumer,
            Consumer<T> interItemConsumer,
            Consumer<List<T>> preConsumer) {
        return forEach(list, itemConsumer, interItemConsumer, preConsumer, null);
    }

    public static <T> int forEach(
            List<T> list,
            Consumer<T> itemConsumer,
            Consumer<T> interItemConsumer,
            Consumer<List<T>> preConsumer,
            Consumer<List<T>> postConsumer) {
        int length = 0;
        if (CollectionUtils.isNotEmpty(list)) {
            if (preConsumer != null) {
                preConsumer.accept(list);
            }
            length = list.size();
            for (int i = 0; i < length; i++) {
                T item = list.get(i);
                itemConsumer.accept(item);
                if (interItemConsumer != null && i < length - 1) {
                    interItemConsumer.accept(item);
                }
            }
            if (postConsumer != null) {
                postConsumer.accept(list);
            }
        }
        return length;
    }
}
