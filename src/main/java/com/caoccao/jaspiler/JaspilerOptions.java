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

package com.caoccao.jaspiler;

public final class JaspilerOptions {
    public static final JaspilerOptions Default = new JaspilerOptions().seal();
    public static final int DEFAULT_INDENT_SIZE = 4;
    public static final int MIN_INDENT_SIZE = 0;
    public static final int MAX_INDENT_SIZE = 100;
    private int indentSize;
    private boolean preserveCopyrights;
    private boolean sealed;

    public JaspilerOptions() {
        setIndentSize(DEFAULT_INDENT_SIZE);
        setPreserveCopyrights(true);
    }

    public int getIndentSize() {
        return indentSize;
    }

    public boolean isPreserveCopyrights() {
        return preserveCopyrights;
    }

    public boolean isSealed() {
        return sealed;
    }

    public JaspilerOptions seal() {
        sealed = true;
        return this;
    }

    public JaspilerOptions setIndentSize(int indentSize) {
        if (!sealed) {
            this.indentSize = Math.min(Math.max(indentSize, MIN_INDENT_SIZE), MAX_INDENT_SIZE);
        }
        return this;
    }

    public JaspilerOptions setPreserveCopyrights(boolean preserveCopyrights) {
        if (!sealed) {
            this.preserveCopyrights = preserveCopyrights;
        }
        return this;
    }
}
