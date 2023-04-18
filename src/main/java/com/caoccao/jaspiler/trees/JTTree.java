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

import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.sun.source.tree.Tree;

@SuppressWarnings("unchecked")
public abstract class JTTree<
        OriginalTree extends Tree,
        NewTree extends JTTree<OriginalTree, NewTree>>
        extends BaseLoggingObject
        implements IJTTree<OriginalTree, NewTree> {
    protected final OriginalTree originalTree;
    protected boolean dirty;
    protected JTTree<?, ?> parentTree;

    public JTTree(JTTree<?, ?> parentTree) {
        this(null, parentTree);
    }

    public JTTree(OriginalTree originalTree, JTTree<?, ?> parentTree) {
        super();
        this.originalTree = originalTree;
        setDirty(originalTree == null);
        setParentTree(parentTree);
    }

    @Override
    public OriginalTree getOriginalTree() {
        return originalTree;
    }

    @Override
    public JTTree<?, ?> getParentTree() {
        return parentTree;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public NewTree setDirty(boolean dirty) {
        this.dirty = dirty;
        if (dirty) {
            getParentTree().setDirty(dirty);
        }
        return (NewTree) this;
    }

    @Override
    public NewTree setParentTree(JTTree<?, ?> parentTree) {
        this.parentTree = parentTree;
        return (NewTree) this;
    }
}
