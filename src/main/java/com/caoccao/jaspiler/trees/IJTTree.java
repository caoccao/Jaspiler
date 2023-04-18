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

import com.sun.source.tree.Tree;

import java.io.IOException;
import java.io.Writer;

@SuppressWarnings("unchecked")
public interface IJTTree<
        OriginalTree extends Tree,
        NewTree extends IJTTree<OriginalTree, NewTree>>
        extends Tree {
    NewTree analyze();

    default JTCompilationUnit getCompilationUnit() {
        return getParentTree().getCompilationUnit();
    }

    default JTPosition getOriginalPosition() {
        return getCompilationUnit().getOriginalPosition(getOriginalTree());
    }

    OriginalTree getOriginalTree();

    JTTree<?, ?> getParentTree();

    boolean isDirty();

    default NewTree save(Writer writer) throws IOException {
        if (isDirty() || getOriginalTree() == null) {
            throw new UnsupportedOperationException();
        }
        String code = getCompilationUnit().getOriginalCode();
        JTPosition position = getOriginalPosition();
        writer.write(code, (int) position.startPosition(), (int) (position.endPosition() - position.startPosition()));
        return (NewTree) this;
    }

    NewTree setDirty(boolean dirty);

    NewTree setParentTree(JTTree<?, ?> parentTree);
}
