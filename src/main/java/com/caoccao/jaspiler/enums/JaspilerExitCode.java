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

package com.caoccao.jaspiler.enums;

public enum JaspilerExitCode {
    UnknownError(1, "The is an unknown error: {}"),
    OptionsInvalid(2, "The options is invalid."),
    ScriptAbsent(101, "The script is absent."),
    ScriptNotFound(102, "The script [{}] is not found."),
    ScriptEmpty(103, "The script [{}] is empty."),
    EngineUnknownError(201, "The Node.js engine met an unknown error: {}"),
    NoError(0, "There is no error.");

    private final int exitCode;
    private final String messageFormat;

    JaspilerExitCode(int exitCode, String messageFormat) {
        this.exitCode = exitCode;
        this.messageFormat = messageFormat;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getMessageFormat() {
        return messageFormat;
    }
}
