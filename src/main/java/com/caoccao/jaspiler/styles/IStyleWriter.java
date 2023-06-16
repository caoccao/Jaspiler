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

public interface IStyleWriter<StyleWriter extends IStyleWriter<StyleWriter>> {
    String AT = "@";
    String COMMA = ",";
    String COMMA_ = ", ";
    String DOT = ".";
    String EMPTY = "";
    String EQUAL = "=";
    String LEFT_ARROW = "<";
    String LEFT_CURLY_BRACKET = "{";
    String LEFT_PARENTHESIS = "(";
    String LINE_SEPARATOR = "\n";
    String RIGHT_ARROW = ">";
    String RIGHT_CURLY_BRACKET = "}";
    String RIGHT_PARENTHESIS = ")";
    String SEMI_COLON = ";";
    String SPACE = " ";

    StyleWriter append(boolean b);

    StyleWriter append(double d);

    StyleWriter append(float f);

    StyleWriter append(int i);

    StyleWriter append(long l);

    StyleWriter append(short s);

    StyleWriter append(Object object);

    StyleWriter append(char[] str);

    StyleWriter appendAt();

    StyleWriter appendComma();

    StyleWriter appendDot();

    StyleWriter appendEqual();

    StyleWriter appendKeyword(JavaKeyword javaKeyword);

    StyleWriter appendLeftArrow();

    StyleWriter appendLeftCurlyBracket();

    StyleWriter appendLeftParenthesis();

    StyleWriter appendLineSeparator();

    StyleWriter appendLineSeparator(int count);

    StyleWriter appendRightArrow();

    StyleWriter appendRightCurlyBracket();

    StyleWriter appendRightParenthesis();

    StyleWriter appendSemiColon();

    StyleWriter appendSpace();

    StyleWriter appendSpace(int count);

    StyleWriter appendSpaceIfNeeded();

    boolean endsWithWhitespace();
}
