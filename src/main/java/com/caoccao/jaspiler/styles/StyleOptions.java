/*
 * Copyright (c) 2023-2024. caoccao.com Sam Cao
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

package com.caoccao.jaspiler.styles;

public final class StyleOptions {
    public static final StyleOptions Default = new StyleOptions().seal();
    private static final int DEFAULT_CONTINUATION_INDENT_SIZE = 8;
    private static final int DEFAULT_INDENT_SIZE = 4;
    private static final int DEFAULT_WORD_WRAP_COLUMN = 120;
    private static final int MAX_INDENT_SIZE = 100;
    private static final int MIN_INDENT_SIZE = 0;
    private static final int MIN_WORD_WRAP_COLUMN = 60;
    private int continuationIndentSize;
    private int indentSize;
    private boolean preserveCopyrights;
    private boolean sealed;
    private int wordWrapColumn;

    public StyleOptions() {
        setContinuationIndentSize(DEFAULT_CONTINUATION_INDENT_SIZE);
        setIndentSize(DEFAULT_INDENT_SIZE);
        setPreserveCopyrights(true);
        setWordWrapColumn(DEFAULT_WORD_WRAP_COLUMN);
    }

    public int getContinuationIndentSize() {
        return continuationIndentSize;
    }

    public int getIndentSize() {
        return indentSize;
    }

    public int getWordWrapColumn() {
        return wordWrapColumn;
    }

    public boolean isPreserveCopyrights() {
        return preserveCopyrights;
    }

    public boolean isSealed() {
        return sealed;
    }

    public StyleOptions seal() {
        sealed = true;
        return this;
    }

    public StyleOptions setContinuationIndentSize(int continuationIndentSize) {
        if (!sealed) {
            this.continuationIndentSize = Math.min(Math.max(continuationIndentSize, MIN_INDENT_SIZE), MAX_INDENT_SIZE);
        }
        return this;
    }

    public StyleOptions setIndentSize(int indentSize) {
        if (!sealed) {
            this.indentSize = Math.min(Math.max(indentSize, MIN_INDENT_SIZE), MAX_INDENT_SIZE);
        }
        return this;
    }

    public StyleOptions setPreserveCopyrights(boolean preserveCopyrights) {
        if (!sealed) {
            this.preserveCopyrights = preserveCopyrights;
        }
        return this;
    }

    public StyleOptions setWordWrapColumn(int wordWrapColumn) {
        if (!sealed) {
            this.wordWrapColumn = Math.max(wordWrapColumn, MIN_WORD_WRAP_COLUMN);
        }
        return this;
    }
}
