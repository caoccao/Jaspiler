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

import com.sun.source.tree.StatementTree;
import com.sun.source.tree.Tree;

public abstract class JTStatement<
        OriginalTree extends Tree,
        NewTree extends JTStatement<OriginalTree, NewTree>>
        extends JTTree<OriginalTree, NewTree>
        implements StatementTree {
    JTStatement(OriginalTree originalTree, JTTree<?, ?> parentTree) {
        super(originalTree, parentTree);
    }
}
