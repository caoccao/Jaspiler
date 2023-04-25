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

import com.sun.source.tree.CaseLabelTree;

/**
 * The type Jt case label.
 * It reference com.sun.tools.javac.tree.JCTree.JCCaseLabel.
 */
@SuppressWarnings("preview")
public abstract class JTCaseLabel<
        OriginalTree extends CaseLabelTree,
        NewTree extends JTCaseLabel<OriginalTree, NewTree>>
        extends JTTree<OriginalTree, NewTree>
        implements CaseLabelTree {
    JTCaseLabel(OriginalTree originalTree, JTTree<?, ?> parentTree) {
        super(originalTree, parentTree);
    }

    public abstract boolean isExpression();

    public boolean isNullPattern() {
        return isExpression();
    }

    public abstract boolean isPattern();
}
