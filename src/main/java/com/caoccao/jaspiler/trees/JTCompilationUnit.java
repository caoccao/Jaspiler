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

import com.caoccao.jaspiler.exceptions.JaspilerCheckedException;
import com.caoccao.jaspiler.options.JaspilerTransformOptions;
import com.caoccao.jaspiler.styles.IStyleWriter;
import com.caoccao.jaspiler.utils.ForEachUtils;
import com.caoccao.jaspiler.utils.V8Register;
import com.caoccao.javet.interfaces.IJavetBiFunction;
import com.caoccao.javet.interfaces.IJavetUniFunction;
import com.caoccao.javet.values.V8Value;
import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.tree.*;
import com.sun.source.util.DocSourcePositions;
import com.sun.source.util.DocTrees;
import com.sun.source.util.SourcePositions;
import com.sun.source.util.Trees;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.*;

/**
 * The type Jt compilation unit.
 * It references com.sun.tools.javac.tree.JCTree.JCCompilationUnit.
 */
public final class JTCompilationUnit
        extends JTTree<CompilationUnitTree, JTCompilationUnit>
        implements CompilationUnitTree {
    private static final String PROPERTY_IMPORTS = "imports";
    private static final String PROPERTY_MODULE = "module";
    private static final String PROPERTY_PACKAGE = "package";
    private static final String PROPERTY_SOURCE_FILE = "sourceFile";
    private static final String PROPERTY_TYPE_DECLS = "typeDecls";
    private final DocCommentTree docCommentTree;
    private final DocSourcePositions docSourcePositions;
    private final DocTrees docTrees;
    private final List<JTImport> imports;
    private final JaspilerTransformOptions options;
    private final SourcePositions sourcePositions;
    private final Trees trees;
    private final List<JTTree<?, ?>> typeDecls;
    private JTModuleDecl moduleTree;
    private String originalCode;
    private JTPackageDecl packageTree;
    private int unsupportedTreeCount;

    public JTCompilationUnit(
            Trees trees,
            DocTrees docTrees,
            CompilationUnitTree originalTree,
            JaspilerTransformOptions options) {
        super(Objects.requireNonNull(originalTree), null);
        docCommentTree = Objects.requireNonNull(docTrees).getDocCommentTree(getOriginalTree().getSourceFile());
        docSourcePositions = docTrees.getSourcePositions();
        this.docTrees = docTrees;
        imports = new ArrayList<>();
        this.options = options;
        originalCode = null;
        packageTree = null;
        sourcePositions = Objects.requireNonNull(trees).getSourcePositions();
        this.trees = trees;
        typeDecls = new ArrayList<>();
        unsupportedTreeCount = 0;
    }

    @Override
    public <R, D> R accept(TreeVisitor<R, D> visitor, D data) {
        return visitor.visitCompilationUnit(this, data);
    }

    @Override
    public JTCompilationUnit analyze() {
        super.analyze();
        packageTree = JTTreeFactory.create(
                getOriginalTree().getPackage(), this, JTPackageDecl::new);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getImports(), this, JTImport::new, imports::add);
        JTTreeFactory.createAndAdd(
                getOriginalTree().getTypeDecls(), this, typeDecls::add);
        moduleTree = JTTreeFactory.create(
                getOriginalTree().getModule(), this, JTModuleDecl::new);
        return this;
    }

    @Override
    List<JTTree<?, ?>> getAllNodes() {
        var nodes = super.getAllNodes();
        Optional.ofNullable(packageTree).ifPresent(nodes::add);
        imports.stream().filter(Objects::nonNull).forEach(nodes::add);
        typeDecls.stream().filter(Objects::nonNull).forEach(nodes::add);
        Optional.ofNullable(moduleTree).ifPresent(nodes::add);
        nodes.forEach(node -> node.setParentTree(this));
        return nodes;
    }

    @Override
    public JTCompilationUnit getCompilationUnit() {
        return this;
    }

    public DocCommentTree getDocCommentTree() {
        return docCommentTree;
    }

    public DocTrees getDocTrees() {
        return docTrees;
    }

    @Override
    public List<JTImport> getImports() {
        return imports;
    }

    @Override
    public Kind getKind() {
        return Kind.COMPILATION_UNIT;
    }

    @Override
    public LineMap getLineMap() {
        return getOriginalTree().getLineMap();
    }

    @Override
    public ModuleTree getModule() {
        return moduleTree;
    }

    public JaspilerTransformOptions getOptions() {
        return options;
    }

    @Override
    public String getOriginalCode() {
        if (originalCode == null) {
            try {
                originalCode = getOriginalTree().getSourceFile().getCharContent(true).toString();
            } catch (IOException e) {
                logger.error("Failed to get the source code from [{}].", getSourceFile().getName());
                throw new RuntimeException(e);
            }
        }
        return originalCode;
    }

    public JTPosition getOriginalDocPosition(DocTree docTree) {
        try {
            return docTree == null
                    ? JTPosition.Invalid
                    : JTPosition.from(docSourcePositions, this, docCommentTree, docTree);
        } catch (Throwable e) {
            logger.error("Failed to get position in [{}].", getSourceFile().getName());
            throw e;
        }
    }

    public JTPosition getOriginalPosition(Tree tree) {
        try {
            return tree == null
                    ? JTPosition.Invalid
                    : JTPosition.from(sourcePositions, getOriginalTree(), tree);
        } catch (Throwable e) {
            logger.error("Failed to get position in [{}].", getSourceFile().getName());
            throw e;
        }
    }

    @Override
    public JTPackageDecl getPackage() {
        return packageTree;
    }

    @Override
    public List<? extends AnnotationTree> getPackageAnnotations() {
        return getPackage().getAnnotations();
    }

    @Override
    public ExpressionTree getPackageName() {
        return getPackage().getPackageName();
    }

    @Override
    public JavaFileObject getSourceFile() {
        return getOriginalTree().getSourceFile();
    }

    public Trees getTrees() {
        return trees;
    }

    @Override
    public List<? extends Tree> getTypeDecls() {
        return typeDecls;
    }

    public int getUnsupportedTreeCount() {
        return unsupportedTreeCount;
    }

    public JTCompilationUnit incrementUnsupportedTreeCount() {
        unsupportedTreeCount++;
        return this;
    }

    @Override
    public Map<String, IJavetUniFunction<String, ? extends V8Value, JaspilerCheckedException>> proxyGetStringGetterMap() {
        if (stringGetterMap == null) {
            super.proxyGetStringGetterMap();
            V8Register.putStringGetter(stringGetterMap, PROPERTY_IMPORTS,
                    propertyName -> v8Runtime.toV8Value(getImports()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_MODULE,
                    propertyName -> v8Runtime.toV8Value(getModule()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_PACKAGE,
                    propertyName -> v8Runtime.toV8Value(getPackage()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_SOURCE_FILE,
                    propertyName -> v8Runtime.createV8ValueString(getSourceFile().getName()));
            V8Register.putStringGetter(stringGetterMap, PROPERTY_TYPE_DECLS,
                    propertyName -> v8Runtime.toV8Value(getTypeDecls()));
        }
        return stringGetterMap;
    }

    @Override
    public Map<String, IJavetBiFunction<String, V8Value, Boolean, JaspilerCheckedException>> proxyGetStringSetterMap() {
        if (stringSetterMap == null) {
            super.proxyGetStringSetterMap();
            V8Register.putStringSetter(stringSetterMap, PROPERTY_IMPORTS,
                    (propertyName, propertyValue) -> replaceImports(imports, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_MODULE,
                    (propertyName, propertyValue) -> replaceModuleDecl(this::setModule, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_PACKAGE,
                    (propertyName, propertyValue) -> replacePackageDecl(this::setPackageTree, propertyValue));
            V8Register.putStringSetter(stringSetterMap, PROPERTY_TYPE_DECLS,
                    (propertyName, propertyValue) -> replaceTrees(typeDecls, propertyValue));
        }
        return stringSetterMap;
    }

    @Override
    public boolean save(IStyleWriter<?> writer) {
        if (isActionChange()) {
            /*
             * If the public type is annotated with {@link JaspilerContract.Ignore},
             * the whole file will be ignored.
             */
            boolean ignore = typeDecls.stream()
                    .filter(typeDecl -> typeDecl instanceof JTClassDecl)
                    .anyMatch(IJTTree::isActionIgnore);
            if (ignore) {
                setActionIgnore();
                return false;
            }
            if (getOptions().isPreserveCopyrights()
                    && getOriginalPosition().isValid()
                    && getOriginalPosition().startPosition() > 0) {
                writer.append(getOriginalCode().substring(0, (int) getOriginalPosition().startPosition()));
            }
            Optional.ofNullable(packageTree).ifPresent(tree -> writer.append(tree).appendLineSeparator());
            ForEachUtils.forEach(
                    imports.stream().filter(Objects::nonNull).filter(tree -> !tree.isActionIgnore()).toList(),
                    writer::append,
                    tree -> writer.appendLineSeparator(),
                    trees -> writer.appendLineSeparator(),
                    trees -> writer.appendLineSeparator());
            ForEachUtils.forEach(
                    typeDecls.stream().filter(Objects::nonNull).filter(tree -> !tree.isActionIgnore()).toList(),
                    writer::append,
                    tree -> writer.appendLineSeparator(),
                    trees -> writer.appendLineSeparator(),
                    trees -> writer.appendLineSeparator());
            Optional.ofNullable(moduleTree).ifPresent(tree -> writer.append(tree).appendLineSeparator());
        } else {
            writer.append(getOriginalCode());
        }
        return true;
    }

    public JTCompilationUnit setModule(JTModuleDecl moduleTree) {
        if (this.moduleTree == moduleTree) {
            return this;
        }
        this.moduleTree = Optional.ofNullable(moduleTree).map(o -> o.setParentTree(this)).orElse(null);
        return setActionChange();
    }

    public JTCompilationUnit setPackageTree(JTPackageDecl packageTree) {
        if (this.packageTree == packageTree) {
            return this;
        }
        this.packageTree = Objects.requireNonNull(packageTree).setParentTree(this);
        return setActionChange();
    }
}
