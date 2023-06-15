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

package com.caoccao.jaspiler;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJaspilerMain {
    @Test
    public void testQuickStart() throws Exception {
        var jaspilerMain = new JaspilerMain();
        jaspilerMain.setFile(new File("scripts/node/test/test_quick_start.js").getAbsoluteFile());
        assertEquals(0, jaspilerMain.call());
    }

    @Test
    public void testTransform() throws Exception {
        var jaspilerMain = new JaspilerMain();
        jaspilerMain.setFile(new File("scripts/node/test/test_transform.js").getAbsoluteFile());
        assertEquals(0, jaspilerMain.call());
    }
}
