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

import com.caoccao.jaspiler.JaspilerContract;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.TreeVisitor;

import java.io.IOException;
import java.io.Writer;

public final class JTLineSeparator
        extends JTExpression<IdentifierTree, JTLineSeparator>
        implements IdentifierTree {
    public static final JTLineSeparator L1 = new JTLineSeparator(1);
    public static final JTLineSeparator L2 = new JTLineSeparator(2);
    public static final JTLineSeparator L3 = new JTLineSeparator(3);
    private final JTName name;

    JTLineSeparator(int count) {
        super(null, null);
        name = new JTName(IJTConstants.LINE_SEPARATOR.repeat(count));
        setActionChange();
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitIdentifier(this, data);
    }

    @Override
    JTLineSeparator analyze() {
        return this;
    }

    @Override
    public Kind getKind() {
        return Kind.IDENTIFIER;
    }

    @Override
    public JTName getName() {
        return name;
    }

    @Override
    public boolean isActionChange() {
        return true;
    }

    @Override
    public boolean isActionIgnore() {
        return false;
    }

    @Override
    protected boolean save(Writer writer) throws IOException {
        writer.write(toString());
        return true;
    }

    @Override
    public JTLineSeparator setAction(JaspilerContract.Action action) {
        return this;
    }

    @Override
    public JTLineSeparator setActionIgnore() {
        return this;
    }

    @Override
    JTLineSeparator setParentTree(JTTree<?, ?> parentTree) {
        return this;
    }

    @Override
    public String toString() {
        return name.getValue();
    }
}
