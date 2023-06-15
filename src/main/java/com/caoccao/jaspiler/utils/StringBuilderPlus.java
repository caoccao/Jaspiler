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

import com.caoccao.jaspiler.trees.IJTConstants;
import org.apache.commons.lang3.StringUtils;

public class StringBuilderPlus implements IJTConstants, Appendable, CharSequence {
    protected StringBuilder stringBuilder;

    public StringBuilderPlus() {
        stringBuilder = new StringBuilder();
    }

    @Override
    public StringBuilderPlus append(CharSequence csq) {
        stringBuilder.append(csq);
        return this;
    }

    @Override
    public StringBuilderPlus append(CharSequence csq, int start, int end) {
        stringBuilder.append(csq, start, end);
        return this;
    }

    public StringBuilderPlus append(boolean b) {
        stringBuilder.append(b);
        return this;
    }

    @Override
    public StringBuilderPlus append(char c) {
        stringBuilder.append(c);
        return this;
    }

    public StringBuilderPlus append(double d) {
        stringBuilder.append(d);
        return this;
    }

    public StringBuilderPlus append(float f) {
        stringBuilder.append(f);
        return this;
    }

    public StringBuilderPlus append(int i) {
        stringBuilder.append(i);
        return this;
    }

    public StringBuilderPlus append(long l) {
        stringBuilder.append(l);
        return this;
    }

    public StringBuilderPlus append(short s) {
        stringBuilder.append(s);
        return this;
    }

    public StringBuilderPlus append(Object object) {
        stringBuilder.append(object);
        return this;
    }

    public StringBuilderPlus append(char[] str) {
        stringBuilder.append(str);
        return this;
    }

    public StringBuilderPlus appendAt() {
        stringBuilder.append(AT);
        return this;
    }

    public StringBuilderPlus appendComma() {
        stringBuilder.append(COMMA);
        return this;
    }

    public StringBuilderPlus appendDot() {
        stringBuilder.append(DOT);
        return this;
    }

    public StringBuilderPlus appendEqual() {
        stringBuilder.append(EQUAL);
        return this;
    }

    public StringBuilderPlus appendLeftArrow() {
        stringBuilder.append(LEFT_ARROW);
        return this;
    }

    public StringBuilderPlus appendLeftCurlyBracket() {
        stringBuilder.append(LEFT_CURLY_BRACKET);
        return this;
    }

    public StringBuilderPlus appendLeftParenthesis() {
        stringBuilder.append(LEFT_PARENTHESIS);
        return this;
    }

    public StringBuilderPlus appendLineSeparator() {
        stringBuilder.append(LINE_SEPARATOR);
        return this;
    }

    public StringBuilderPlus appendLineSeparator(int count) {
        if (count == 1) {
            appendLineSeparator();
        } else if (count > 1) {
            stringBuilder.append(StringUtils.repeat(LINE_SEPARATOR, count));
        }
        return this;
    }

    public StringBuilderPlus appendRightArrow() {
        stringBuilder.append(RIGHT_ARROW);
        return this;
    }

    public StringBuilderPlus appendRightCurlyBracket() {
        stringBuilder.append(RIGHT_CURLY_BRACKET);
        return this;
    }

    public StringBuilderPlus appendRightParenthesis() {
        stringBuilder.append(RIGHT_PARENTHESIS);
        return this;
    }

    public StringBuilderPlus appendSemiColon() {
        stringBuilder.append(SEMI_COLON);
        return this;
    }

    public StringBuilderPlus appendSpace() {
        stringBuilder.append(SPACE);
        return this;
    }

    public StringBuilderPlus appendSpace(int count) {
        if (count == 1) {
            appendSpace();
        } else if (count > 1) {
            stringBuilder.append(StringUtils.repeat(SPACE, count));
        }
        return this;
    }

    public StringBuilderPlus appendSpaceIfNeeded() {
        if (!endsWithWhitespace()) {
            appendSpace();
        }
        return this;
    }

    @Override
    public char charAt(int index) {
        return stringBuilder.charAt(index);
    }

    public boolean endsWithWhitespace() {
        if (stringBuilder == null || stringBuilder.isEmpty()) {
            return true;
        }
        return switch (stringBuilder.charAt(stringBuilder.length() - 1)) {
            case ' ', '\n' -> true;
            default -> false;
        };
    }

    @Override
    public int length() {
        return stringBuilder.length();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return stringBuilder.subSequence(start, end);
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
