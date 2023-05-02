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

import com.sun.source.tree.InstanceOfTree;
import com.sun.source.tree.TreeVisitor;

import java.util.List;
import java.util.Optional;

public final class JTInstanceOf
        extends JTExpression<InstanceOfTree, JTInstanceOf>
        implements InstanceOfTree {
    private JTExpression<?, ?> expression;
    private JTTree<?, ?> pattern;

    public JTInstanceOf() {
        this(null, null);
        setActionChange();
    }

    JTInstanceOf(InstanceOfTree instanceOfTree, JTTree<?, ?> parentTree) {
        super(instanceOfTree, parentTree);
        expression = null;
        pattern = null;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitInstanceOf(this, data);
    }

    @Override
    JTInstanceOf analyze() {
        super.analyze();
        expression = JTTreeFactory.create(getOriginalTree().getExpression(), this);
        pattern = JTTreeFactory.create(getOriginalTree().getPattern(), this);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        return super.getAllNodes();
    }

    @Override
    public JTExpression<?, ?> getExpression() {
        return expression;
    }

    @Override
    public Kind getKind() {
        return Kind.INSTANCE_OF;
    }

    @Override
    public JTPattern<?, ?> getPattern() {
        return pattern instanceof JTPattern<?, ?> jtPattern ? jtPattern : null;
    }

    @Override
    public JTTree<?, ?> getType() {
        if (pattern instanceof JTPattern<?, ?>) {
            if (pattern instanceof JTBindingPattern bindingPattern) {
                return bindingPattern.getVariable().getType();
            } else {
                return null;
            }
        }
        return pattern;
    }

    public JTInstanceOf setPattern(JTTree<?, ?> pattern) {
        if (this.pattern == pattern) {
            return this;
        }
        this.pattern = Optional.ofNullable(pattern).map(o -> o.setParentTree(this)).orElse(null);
        return setActionChange();
    }
}
