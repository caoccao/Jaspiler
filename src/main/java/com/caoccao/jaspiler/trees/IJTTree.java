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

package com.caoccao.jaspiler.trees;

import com.caoccao.jaspiler.JaspilerContract;
import com.caoccao.jaspiler.exceptions.JaspilerCheckedException;
import com.caoccao.jaspiler.styles.IStyleWriter;
import com.caoccao.javet.interop.proxy.IJavetDirectProxyHandler;
import com.sun.source.tree.Tree;

public interface IJTTree<
        OriginalTree extends Tree,
        NewTree extends IJTTree<OriginalTree, NewTree>>
        extends Tree, IJavetDirectProxyHandler<JaspilerCheckedException> {
    JaspilerContract.Action getAction();

    default JTCompilationUnit getCompilationUnit() {
        return getParentTree().getCompilationUnit();
    }

    JTPosition getOriginalPosition();

    OriginalTree getOriginalTree();

    JTTree<?, ?> getParentTree();

    boolean isActionChange();

    default boolean isActionIgnore() {
        return getAction().isIgnore();
    }

    default boolean isActionNoChange() {
        return !isActionIgnore() && !isActionChange();
    }

    boolean serialize(IStyleWriter<?> writer);

    NewTree setAction(JaspilerContract.Action action);

    default NewTree setActionChange() {
        return setAction(JaspilerContract.Action.Change);
    }

    default NewTree setActionIgnore() {
        return setAction(JaspilerContract.Action.Ignore);
    }

    default NewTree setActionNoChange() {
        return setAction(JaspilerContract.Action.NoChange);
    }
}
