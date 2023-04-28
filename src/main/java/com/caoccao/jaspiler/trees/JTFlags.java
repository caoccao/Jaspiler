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

package com.caoccao.jaspiler.trees;

import org.apache.commons.collections4.CollectionUtils;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class JTFlags {
    public static final int ABSTRACT = 1 << 10;
    public static final long DEFAULT = 1L << 43;
    public static final int FINAL = 1 << 4;
    public static final int INTERFACE = 1 << 9;
    public static final int NATIVE = 1 << 8;
    public static final long NON_SEALED = 1L << 63; // ClassSymbols
    public static final int PRIVATE = 1 << 1;
    public static final int PROTECTED = 1 << 2;
    public static final int PUBLIC = 1;
    public static final long SEALED = 1L << 62; // ClassSymbols
    public static final int STATIC = 1 << 3;
    public static final int STRICTFP = 1 << 11;
    public static final int SYNCHRONIZED = 1 << 5;
    public static final int TRANSIENT = 1 << 7;
    public static final int VOLATILE = 1 << 6;
    private static final Map<Long, Set<Modifier>> modifierSets = new ConcurrentHashMap<>(64);

    private JTFlags() {
    }

    public static long fromModifierSet(Set<Modifier> modifiers) {
        long flags = 0L;
        if (CollectionUtils.isNotEmpty(modifiers)) {
            if (modifiers.contains(Modifier.PUBLIC)) flags |= PUBLIC;
            if (modifiers.contains(Modifier.PROTECTED)) flags |= PROTECTED;
            if (modifiers.contains(Modifier.PRIVATE)) flags |= PRIVATE;
            if (modifiers.contains(Modifier.ABSTRACT)) flags |= ABSTRACT;
            if (modifiers.contains(Modifier.STATIC)) flags |= STATIC;
            if (modifiers.contains(Modifier.SEALED)) flags |= SEALED;
            if (modifiers.contains(Modifier.NON_SEALED)) flags |= NON_SEALED;
            if (modifiers.contains(Modifier.FINAL)) flags |= FINAL;
            if (modifiers.contains(Modifier.TRANSIENT)) flags |= TRANSIENT;
            if (modifiers.contains(Modifier.VOLATILE)) flags |= VOLATILE;
            if (modifiers.contains(Modifier.SYNCHRONIZED)) flags |= SYNCHRONIZED;
            if (modifiers.contains(Modifier.NATIVE)) flags |= NATIVE;
            if (modifiers.contains(Modifier.STRICTFP)) flags |= STRICTFP;
            if (modifiers.contains(Modifier.DEFAULT)) flags |= DEFAULT;
        }
        return flags;
    }

    public static Set<Modifier> toModifierSet(long flags) {
        Set<Modifier> modifiers = modifierSets.get(flags);
        if (modifiers == null) {
            modifiers = EnumSet.noneOf(Modifier.class);
            if (0 != (flags & PUBLIC)) modifiers.add(Modifier.PUBLIC);
            if (0 != (flags & PROTECTED)) modifiers.add(Modifier.PROTECTED);
            if (0 != (flags & PRIVATE)) modifiers.add(Modifier.PRIVATE);
            if (0 != (flags & ABSTRACT)) modifiers.add(Modifier.ABSTRACT);
            if (0 != (flags & STATIC)) modifiers.add(Modifier.STATIC);
            if (0 != (flags & SEALED)) modifiers.add(Modifier.SEALED);
            if (0 != (flags & NON_SEALED)) modifiers.add(Modifier.NON_SEALED);
            if (0 != (flags & FINAL)) modifiers.add(Modifier.FINAL);
            if (0 != (flags & TRANSIENT)) modifiers.add(Modifier.TRANSIENT);
            if (0 != (flags & VOLATILE)) modifiers.add(Modifier.VOLATILE);
            if (0 != (flags & SYNCHRONIZED)) modifiers.add(Modifier.SYNCHRONIZED);
            if (0 != (flags & NATIVE)) modifiers.add(Modifier.NATIVE);
            if (0 != (flags & STRICTFP)) modifiers.add(Modifier.STRICTFP);
            if (0 != (flags & DEFAULT)) modifiers.add(Modifier.DEFAULT);
            modifiers = Collections.unmodifiableSet(modifiers);
            modifierSets.put(flags, modifiers);
        }
        return modifiers;
    }
}
