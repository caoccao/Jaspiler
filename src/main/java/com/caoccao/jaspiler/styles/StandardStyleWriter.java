/*
 * Copyright (c) 2023-2025. caoccao.com Sam Cao
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

import com.caoccao.jaspiler.enums.JavaKeyword;
import org.apache.commons.text.StringEscapeUtils;

public class StandardStyleWriter extends BaseStyleWriter<StandardStyleWriter> {

    public StandardStyleWriter(StyleOptions options) {
        super(options);
    }

    @Override
    public StandardStyleWriter appendBlockClose() {
        return appendRightCurlyBracket();
    }

    @Override
    public StandardStyleWriter appendBlockOpen() {
        return appendLeftCurlyBracket().appendLineSeparator();
    }

    @Override
    public StandardStyleWriter appendClassClose() {
        return appendIndent(getDepth()).appendRightCurlyBracket();
    }

    @Override
    public StandardStyleWriter appendClassOpen() {
        return appendLeftCurlyBracket().appendLineSeparator();
    }

    @Override
    public StandardStyleWriter appendIndent() {
        return appendIndent(getDepth());
    }

    @Override
    public StandardStyleWriter appendJavaCharacter(char c) {
        return appendSingleQuote().append(c).appendSingleQuote();
    }

    @Override
    public StandardStyleWriter appendJavaCharacter(String str) {
        return appendSingleQuote().append(str).appendSingleQuote();
    }

    @Override
    public StandardStyleWriter appendJavaString(String str) {
        if (str == null) {
            return append((String) null);
        }
        return appendQuote().append(StringEscapeUtils.escapeJava(str)).appendQuote();
    }

    @Override
    public StandardStyleWriter appendKeyword(JavaKeyword javaKeyword) {
        if (stringBuilder.length() + javaKeyword.getLength() >= options.getWordWrapColumn()) {
            appendLineSeparator().appendIndent(getDepth()).appendContinuationIndent();
        }
        return super.appendKeyword(javaKeyword);
    }

    @Override
    public StandardStyleWriter appendQuote() {
        return append(QUOTE);
    }

    @Override
    public StandardStyleWriter appendSingleQuote() {
        return append(SINGLE_QUOTE);
    }

    @Override
    public StandardStyleWriter appendTypeSeparator() {
        return appendLineSeparator(2);
    }
}
