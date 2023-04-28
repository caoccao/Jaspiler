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

import javax.lang.model.element.Name;
import java.util.Objects;

public final class JTName implements Name {
    private String value;

    public JTName(String value) {
        setValue(value);
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public boolean contentEquals(CharSequence cs) {
        if (cs == null) {
            return false;
        }
        return value.equals(cs.toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    public int length() {
        return value.length();
    }

    public JTName setValue(String value) {
        this.value = Objects.requireNonNull(value);
        return this;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }

    @Override
    public String toString() {
        return value;
    }
}