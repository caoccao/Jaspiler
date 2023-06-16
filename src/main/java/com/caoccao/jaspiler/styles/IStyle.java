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

public interface IStyle<Style extends IStyle<Style>> {
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

    Style append(boolean b);

    Style append(double d);

    Style append(float f);

    Style append(int i);

    Style append(long l);

    Style append(short s);

    Style append(Object object);

    Style append(char[] str);

    Style appendAt();

    Style appendComma();

    Style appendDot();

    Style appendEqual();

    Style appendKeyword(JavaKeyword javaKeyword);

    Style appendLeftArrow();

    Style appendLeftCurlyBracket();

    Style appendLeftParenthesis();

    Style appendLineSeparator();

    Style appendLineSeparator(int count);

    Style appendRightArrow();

    Style appendRightCurlyBracket();

    Style appendRightParenthesis();

    Style appendSemiColon();

    Style appendSpace();

    Style appendSpace(int count);

    Style appendSpaceIfNeeded();

    boolean endsWithWhitespace();
}
