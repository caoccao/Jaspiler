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

package com.caoccao.jaspiler.enums;

public enum JavaKeyword {
    ABSTRACT("abstract"),
    ASSERT("assert"),
    BOOLEAN("boolean"),
    BREAK("break"),
    BYTE("byte"),
    CASE("case"),
    CATCH("catch"),
    CHAR("char"),
    CLASS("class"),
    CONST("const"),
    CONTINUE("continue"),
    DEFAULT("default"),
    DO("do"),
    DOUBLE("double"),
    ELSE("else"),
    ENUM("enum"),
    EXPORTS("exports"),
    EXTENDS("extends"),
    FINAL("final"),
    FINALLY("finally"),
    FLOAT("float"),
    FOR("for"),
    GOTO("goto"),
    IF("if"),
    IMPLEMENTS("implements"),
    IMPORT("import"),
    INSTANCEOF("instanceOf"),
    INT("int"),
    INTERFACE("interface"),
    LONG("long"),
    MODULE("module"),
    NATIve("native"),
    NEW("new"),
    NON("non"),
    OPEN("open"),
    OPENS("opens"),
    PACKAGE("package"),
    PERMITS("permits"),
    PRIVATE("private"),
    PROTECTED("protected"),
    PROVIDES("provides"),
    PUBLic("public"),
    RECORD("record"),
    REQUIRES("requires"),
    RETURN("return"),
    SEALED("sealed"),
    SHORT("short"),
    STATIC("static"),
    STRICTFP("strictfp"),
    SUPER("super"),
    SWITCH("switch"),
    SYNCHRONIZED("synchronized"),
    THIS("this"),
    THROW("throw"),
    THROWS("throws"),
    TRANSIENT("transient"),
    TRY("try"),
    UNDERSCORE("_"),
    USES("uses"),
    VAR("var"),
    VOID("void"),
    VOLATILE("volatile"),
    WHILE("while"),
    YIELD("yield");

    private final String value;

    JavaKeyword(String value) {
        this.value = value;
    }

    public int getLength() {
        return value.length();
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
