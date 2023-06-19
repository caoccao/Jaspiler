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

import java.util.Objects;

@SuppressWarnings("unchecked")
public abstract class JTPolyExpression<
        OriginalTree extends Tree,
        NewTree extends JTExpression<OriginalTree, NewTree>>
        extends JTExpression<OriginalTree, NewTree> {
    protected JTPolyKind polyKind;

    JTPolyExpression(OriginalTree originalTree, JTTree<?, ?> parentTree) {
        super(originalTree, parentTree);
        polyKind = JTPolyKind.STANDALONE;
    }

    @Override
    public boolean isPoly() {
        return polyKind == JTPolyKind.POLY;
    }

    @Override
    public boolean isStandalone() {
        return polyKind == JTPolyKind.STANDALONE;
    }

    public NewTree setPolyKind(JTPolyKind polyKind) {
        if (this.polyKind == polyKind) {
            return (NewTree) this;
        }
        this.polyKind = Objects.requireNonNull(polyKind);
        return setActionChange();
    }
}
