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

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.TreeVisitor;

import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

public final class JTIdent
        extends JTExpression<IdentifierTree, JTIdent>
        implements IdentifierTree {
    private JTName name;

    public JTIdent() {
        this(null, null);
        setActionChange();
    }

    JTIdent(IdentifierTree originalTree, JTTree<?, ?> parentTree) {
        super(originalTree, parentTree);
        name = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitIdentifier(this, data);
    }

    @Override
    JTIdent analyze() {
        super.analyze();
        name = JTTreeFactory.createName(getOriginalTree().getName());
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
        return getAction().isChange();
    }

    @Override
    protected boolean save(Writer writer) throws IOException {
        if (isActionChange()) {
            if (name != null) {
                writeStrings(writer, name.getValue());
            }
            return true;
        }
        return super.save(writer);
    }

    public JTIdent setName(JTName name) {
        this.name = Objects.requireNonNull(name);
        return setActionChange();
    }

    @Override
    public String toString() {
        if (isActionChange()) {
            var stringBuilder = new StringBuilder();
            if (name != null) {
                stringBuilder.append(name);
            }
            return stringBuilder.toString();
        }
        return super.toString();
    }
}
