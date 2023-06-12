/*
 * Copyright (c) 2023-2023. caoccao.com Sam Cao
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The type Jaspiler Contract contains a set of annotations for customizations.
 *
 * @since 0.1.0
 */
public final class JaspilerContract {
    public static final String ANNOTATION_IGNORE = "@JaspilerContract.Ignore";
    public static final String DESCRIPTION = "Jaspiler is a Java to Java transpiler.";
    public static final String NAME = "Jaspiler";
    public static final String VERSION = "0.1.0";

    private JaspilerContract() {
    }

    public enum Action {
        Change,
        Ignore,
        NoChange;

        public boolean isChange() {
            return this == Action.Change;
        }

        public boolean isIgnore() {
            return this == Action.Ignore;
        }

        public boolean isNoChange() {
            return this == Action.NoChange;
        }
    }

    /**
     * The annotation Ignore provides conditional ignore over decorated class, method, etc.
     *
     * @since 0.1.0
     */
    @Retention(RetentionPolicy.SOURCE)
    public @interface Ignore {
        /**
         * Condition string.
         *
         * @return the condition string
         * @since 0.1.0
         */
        String condition() default "";
    }
}
