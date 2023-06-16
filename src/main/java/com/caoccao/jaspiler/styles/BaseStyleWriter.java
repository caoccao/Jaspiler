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
public abstract class BaseStyleWriter<StyleWriter extends BaseStyleWriter<StyleWriter>>
        implements IStyleWriter<StyleWriter>, Appendable, CharSequence {
    protected final List<String> lines;
    protected int lengthOfLines;
    protected StringBuilder stringBuilder;

    public BaseStyleWriter() {
        lines = new ArrayList<>();
        lengthOfLines = 0;
        stringBuilder = new StringBuilder();
    }

    @Override
    public StyleWriter append(CharSequence csq) {
        stringBuilder.append(csq);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter append(CharSequence csq, int start, int end) {
        stringBuilder.append(csq, start, end);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter append(boolean b) {
        stringBuilder.append(b);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter append(char c) {
        stringBuilder.append(c);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter append(double d) {
        stringBuilder.append(d);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter append(float f) {
        stringBuilder.append(f);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter append(int i) {
        stringBuilder.append(i);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter append(long l) {
        stringBuilder.append(l);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter append(short s) {
        stringBuilder.append(s);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter append(Object object) {
        stringBuilder.append(object);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter append(char[] str) {
        stringBuilder.append(str);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendAt() {
        stringBuilder.append(AT);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendComma() {
        stringBuilder.append(COMMA);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendDot() {
        stringBuilder.append(DOT);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendEqual() {
        stringBuilder.append(EQUAL);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendKeyword(JavaKeyword javaKeyword) {
        stringBuilder.append(Objects.requireNonNull(javaKeyword).getValue());
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendLeftArrow() {
        stringBuilder.append(LEFT_ARROW);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendLeftCurlyBracket() {
        stringBuilder.append(LEFT_CURLY_BRACKET);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendLeftParenthesis() {
        stringBuilder.append(LEFT_PARENTHESIS);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendLineSeparator() {
        String line = stringBuilder.toString();
        lines.add(line);
        lengthOfLines += line.length();
        stringBuilder = new StringBuilder();
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendLineSeparator(int count) {
        for (int i = 0; i < count; i++) {
            appendLineSeparator();
        }
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendRightArrow() {
        stringBuilder.append(RIGHT_ARROW);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendRightCurlyBracket() {
        stringBuilder.append(RIGHT_CURLY_BRACKET);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendRightParenthesis() {
        stringBuilder.append(RIGHT_PARENTHESIS);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendSemiColon() {
        stringBuilder.append(SEMI_COLON);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendSpace() {
        stringBuilder.append(SPACE);
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendSpace(int count) {
        if (count == 1) {
            appendSpace();
        } else if (count > 1) {
            stringBuilder.append(StringUtils.repeat(SPACE, count));
        }
        return (StyleWriter) this;
    }

    @Override
    public StyleWriter appendSpaceIfNeeded() {
        if (!endsWithWhitespace()) {
            appendSpace();
        }
        return (StyleWriter) this;
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
