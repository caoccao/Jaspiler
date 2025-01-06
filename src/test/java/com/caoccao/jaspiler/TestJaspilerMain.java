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

package com.caoccao.jaspiler;

import com.caoccao.jaspiler.enums.JaspilerExitCode;
import com.caoccao.jaspiler.utils.SystemUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJaspilerMain {
    @Test
    public void testArgv() {
        String scriptPath = SystemUtils.INITIAL_WORKING_DIRECTORY
                .resolve("scripts/node/test/test_argv.js")
                .toAbsolutePath().toFile().getAbsolutePath();
        assertEquals(
                JaspilerExitCode.NoError,
                new JaspilerMain().execute(new String[]{scriptPath, "-a", "b", "-c"}));
    }

    @Test
    public void testTransform() {
        String scriptPath = SystemUtils.INITIAL_WORKING_DIRECTORY
                .resolve("scripts/node/test/test_transform.js")
                .toAbsolutePath().toFile().getAbsolutePath();
        assertEquals(
                JaspilerExitCode.NoError,
                new JaspilerMain().execute(new String[]{scriptPath}));
    }

    @Test
    public void testTutorials01QuickStart() {
        String scriptPath = SystemUtils.INITIAL_WORKING_DIRECTORY
                .resolve("scripts/node/tutorials/01_quick_start.js")
                .toAbsolutePath().toFile().getAbsolutePath();
        assertEquals(
                JaspilerExitCode.NoError,
                new JaspilerMain().execute(new String[]{scriptPath}));
    }

    @Test
    public void testTutorials02PlayWithTypes() {
        String scriptPath = SystemUtils.INITIAL_WORKING_DIRECTORY
                .resolve("scripts/node/tutorials/02_play_with_types.js")
                .toAbsolutePath().toFile().getAbsolutePath();
        assertEquals(
                JaspilerExitCode.NoError,
                new JaspilerMain().execute(new String[]{scriptPath}));
    }

    @Test
    public void testTutorials03BuiltinAnnotations() {
        String scriptPath = SystemUtils.INITIAL_WORKING_DIRECTORY
                .resolve("scripts/node/tutorials/03_builtin_annotations.js")
                .toAbsolutePath().toFile().getAbsolutePath();
        assertEquals(
                JaspilerExitCode.NoError,
                new JaspilerMain().execute(new String[]{scriptPath}));
    }

    @Test
    public void testTutorials04ParseArgv() {
        String scriptPath = SystemUtils.INITIAL_WORKING_DIRECTORY
                .resolve("scripts/node/tutorials/04_parse_argv.js")
                .toAbsolutePath().toFile().getAbsolutePath();
        assertEquals(
                JaspilerExitCode.NoError,
                new JaspilerMain().execute(new String[]{scriptPath, "-i", "a", "-o", "b"}));
    }
}
