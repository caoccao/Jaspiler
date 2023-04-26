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

public final class JaspilerOptions {
    public static final JaspilerOptions Default = new JaspilerOptions().seal();
    private boolean preserveCopyrights;
    private boolean sealed;

    public JaspilerOptions() {
        setPreserveCopyrights(true);
    }

    public boolean isPreserveCopyrights() {
        return preserveCopyrights;
    }

    public boolean isSealed() {
        return sealed;
    }

    public JaspilerOptions seal() {
        sealed = true;
        return this;
    }

    public JaspilerOptions setPreserveCopyrights(boolean preserveCopyrights) {
        if (!isSealed()) {
            this.preserveCopyrights = preserveCopyrights;
        }
        return this;
    }
}
