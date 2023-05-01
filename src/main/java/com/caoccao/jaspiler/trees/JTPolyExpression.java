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

@SuppressWarnings("unchecked")
public abstract class JTPolyExpression<
        OriginalTree extends Tree,
        NewTree extends JTExpression<OriginalTree, NewTree>>
        extends JTExpression<OriginalTree, NewTree> {
    protected boolean poly;
    protected boolean standalone;

    JTPolyExpression(OriginalTree originalTree, JTTree<?, ?> parentTree) {
        super(originalTree, parentTree);
    }

    @Override
    public boolean isPoly() {
        return poly;
    }

    @Override
    public boolean isStandalone() {
        return standalone;
    }

    public NewTree setPoly(boolean poly) {
        if (this.poly == poly) {
            return (NewTree) this;
        }
        this.poly = poly;
        return setActionChange();
    }

    public NewTree setStandalone(boolean standalone) {
        if (this.standalone == standalone) {
            return (NewTree) this;
        }
        this.standalone = standalone;
        return setActionChange();
    }
}
