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

package com.caoccao.jaspiler.styles;

import com.caoccao.jaspiler.enums.JavaKeyword;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unchecked")
public abstract class BaseStyle<Style extends BaseStyle<Style>>
        implements IStyle<Style>, Appendable, CharSequence {
    protected final List<String> lines;
    protected int lengthOfLines;
    protected StringBuilder stringBuilder;

    public BaseStyle() {
        lines = new ArrayList<>();
        lengthOfLines = 0;
        stringBuilder = new StringBuilder();
    }

    @Override
    public Style append(CharSequence csq) {
        stringBuilder.append(csq);
        return (Style) this;
    }

    @Override
    public Style append(CharSequence csq, int start, int end) {
        stringBuilder.append(csq, start, end);
        return (Style) this;
    }

    @Override
    public Style append(boolean b) {
        stringBuilder.append(b);
        return (Style) this;
    }

    @Override
    public Style append(char c) {
        stringBuilder.append(c);
        return (Style) this;
    }

    @Override
    public Style append(double d) {
        stringBuilder.append(d);
        return (Style) this;
    }

    @Override
    public Style append(float f) {
        stringBuilder.append(f);
        return (Style) this;
    }

    @Override
    public Style append(int i) {
        stringBuilder.append(i);
        return (Style) this;
    }

    @Override
    public Style append(long l) {
        stringBuilder.append(l);
        return (Style) this;
    }

    @Override
    public Style append(short s) {
        stringBuilder.append(s);
        return (Style) this;
    }

    @Override
    public Style append(Object object) {
        stringBuilder.append(object);
        return (Style) this;
    }

    @Override
    public Style append(char[] str) {
        stringBuilder.append(str);
        return (Style) this;
    }

    @Override
    public Style appendAt() {
        stringBuilder.append(AT);
        return (Style) this;
    }

    @Override
    public Style appendComma() {
        stringBuilder.append(COMMA);
        return (Style) this;
    }

    @Override
    public Style appendDot() {
        stringBuilder.append(DOT);
        return (Style) this;
    }

    @Override
    public Style appendEqual() {
        stringBuilder.append(EQUAL);
        return (Style) this;
    }

    @Override
    public Style appendKeyword(JavaKeyword javaKeyword) {
        stringBuilder.append(Objects.requireNonNull(javaKeyword).getValue());
        return (Style) this;
    }

    @Override
    public Style appendLeftArrow() {
        stringBuilder.append(LEFT_ARROW);
        return (Style) this;
    }

    @Override
    public Style appendLeftCurlyBracket() {
        stringBuilder.append(LEFT_CURLY_BRACKET);
        return (Style) this;
    }

    @Override
    public Style appendLeftParenthesis() {
        stringBuilder.append(LEFT_PARENTHESIS);
        return (Style) this;
    }

    @Override
    public Style appendLineSeparator() {
        String line = stringBuilder.toString();
        lines.add(line);
        lengthOfLines += line.length();
        stringBuilder = new StringBuilder();
        return (Style) this;
    }

    @Override
    public Style appendLineSeparator(int count) {
        for (int i = 0; i < count; i++) {
            appendLineSeparator();
        }
        return (Style) this;
    }

    @Override
    public Style appendRightArrow() {
        stringBuilder.append(RIGHT_ARROW);
        return (Style) this;
    }

    @Override
    public Style appendRightCurlyBracket() {
        stringBuilder.append(RIGHT_CURLY_BRACKET);
        return (Style) this;
    }

    @Override
    public Style appendRightParenthesis() {
        stringBuilder.append(RIGHT_PARENTHESIS);
        return (Style) this;
    }

    @Override
    public Style appendSemiColon() {
        stringBuilder.append(SEMI_COLON);
        return (Style) this;
    }

    @Override
    public Style appendSpace() {
        stringBuilder.append(SPACE);
        return (Style) this;
    }

    @Override
    public Style appendSpace(int count) {
        if (count == 1) {
            appendSpace();
        } else if (count > 1) {
            stringBuilder.append(StringUtils.repeat(SPACE, count));
        }
        return (Style) this;
    }

    @Override
    public Style appendSpaceIfNeeded() {
        if (!endsWithWhitespace()) {
            appendSpace();
        }
        return (Style) this;
    }

    @Override
    public char charAt(int index) {
        return stringBuilder.charAt(index);
    }

    @Override
    public boolean endsWithWhitespace() {
        if (stringBuilder.isEmpty()) {
            return true;
        }
        return switch (stringBuilder.charAt(stringBuilder.length() - 1)) {
            case ' ', '\t', '\r', '\n' -> true;
            default -> false;
        };
    }

    @Override
    public int length() {
        return lengthOfLines + stringBuilder.length();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return stringBuilder.subSequence(start, end);
    }

    @Override
    public String toString() {
        var totalLines = new ArrayList<>(lines);
        totalLines.add(stringBuilder.toString());
        return String.join(LINE_SEPARATOR, totalLines);
    }
}
