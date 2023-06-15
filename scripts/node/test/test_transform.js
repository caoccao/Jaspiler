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

/// <reference types="../jaspiler/index.d.ts"/>

const assert = require('chai').assert;
const path = require('path');
const process = require('process');
const JTKind = require('./jaspiler/jt_kind');

const workingDirectory = process.cwd();
const pathMockAllInOnePublicClass = path.join(
  workingDirectory,
  '../../../src/test/java/com/caoccao/jaspiler/mock/MockAllInOnePublicClass.java');
const pathMockPublicAnnotation = path.join(
  workingDirectory,
  '../../../src/test/java/com/caoccao/jaspiler/mock/MockPublicAnnotation.java');

function testAstForFile() {
  const result = jaspiler.transformSync(pathMockPublicAnnotation, { ast: true });
  // Assert { ast, code }
  assert.isObject(result);
  assert.isObject(result.ast);
  const expectedLine = 'public @interface MockPublicAnnotation {';
  assert.include(result.code, expectedLine, 'The code string should be generated.');
  assert.include(result.ast.toString(), expectedLine, 'The ast.toString() should work.');
  assert.include('' + result.ast, expectedLine, 'The ast[Symbol.toPrimitive]() should work.');
  // Assert ast
  const ast = result.ast;
  assert.equal(JTKind.COMPILATION_UNIT, ast.kind);
  const astImports = ast.imports;
  assert.isArray(astImports);
  assert.equal(2, astImports.length);
  const astModule = ast.module;
  assert.isNull(astModule);
  const astPackage = ast.package;
  assert.isObject(astPackage);
  assert.equal('package com.caoccao.jaspiler.mock;', astPackage.toString());
  const packageName = astPackage.packageName;
  assert.isObject(packageName);
  assert.equal('com.caoccao.jaspiler.mock', packageName.toString());
  const typeDecls = ast.typeDecls;
  assert.isArray(typeDecls);
  assert.equal(1, typeDecls.length);
  assert.include(typeDecls[0].toString(), expectedLine, 'The typeDecls[0].toString() should work.');
  assert.equal(pathMockPublicAnnotation, ast.sourceFile, 'The source file should match.');
}

function testAstForString() {
  const result = jaspiler.transformSync(
    `package a.b.c;
    public class A {
    }
    `,
    { ast: true, fileName: 'A', sourceType: 'string' });
  // Assert { ast, code }
  assert.isObject(result);
  assert.isObject(result.ast);
  assert.include(result.code, 'public class A');
  // Assert ast
  const ast = result.ast;
  assert.equal(JTKind.COMPILATION_UNIT, ast.kind);
}

// Package

function testIgnorePackage() {
  const result = jaspiler.transformSync(pathMockPublicAnnotation, {
    plugins: [{
      visitor: {
        Package(node) {
          assert.equal(0, node.annotations.length);
          assert.isTrue(node.isActionNoChange());
          assert.isFalse(node.isActionChange());
          assert.isFalse(node.isActionIgnore());
          assert.isTrue(node.setActionIgnore());
          assert.isFalse(node.isActionNoChange());
          assert.isFalse(node.isActionChange());
          assert.isTrue(node.isActionIgnore());
        },
      },
    }],
  });
  assert.notInclude(result.code, 'package com.caoccao.jaspiler.mock;');
}

function testReplacePackage() {
  const result = jaspiler.transformSync(pathMockPublicAnnotation, {
    plugins: [{
      visitor: {
        Package(node) {
          assert.equal(JTKind.PACKAGE, node.kind);
          assert.equal('com.caoccao.jaspiler.trees.JTPackageDecl', node.className);
          assert.equal('JTPackageDecl', node.classSimpleName);
          assert.equal('package com.caoccao.jaspiler.mock;', node.toString());
          const compilationUnit = node.parentTree;
          assert.equal('com.caoccao.jaspiler.trees.JTCompilationUnit', compilationUnit.className);
          assert.equal('JTCompilationUnit', compilationUnit.classSimpleName);
          assert.isNotNull(compilationUnit);
          const packageDecl = jaspiler.newPackageDecl();
          assert.equal('com.caoccao.jaspiler.trees.JTPackageDecl', packageDecl.className);
          assert.equal('JTPackageDecl', packageDecl.classSimpleName);
          packageDecl.packageName = jaspiler.createFieldAccess('abc', 'def', 'ghi');
          compilationUnit.package = packageDecl;
        },
      },
    }],
  });
  assert.include(result.code, 'package abc.def.ghi;');
}

function testReplacePackageName() {
  const result = jaspiler.transformSync(pathMockPublicAnnotation, {
    plugins: [{
      visitor: {
        Package(node) {
          assert.isTrue(node.isActionNoChange());
          node.packageName = jaspiler.createFieldAccess('abc', 'def', 'ghi');
          assert.isTrue(node.isActionChange());
        },
      },
    }],
  });
  assert.include(result.code, 'package abc.def.ghi;');
}

// Imports

function testImports() {
  const result = jaspiler.transformSync(pathMockPublicAnnotation, {
    plugins: [{
      visitor: {
        CompilationUnit(node) {
          assert.equal(JTKind.COMPILATION_UNIT, node.kind);
          assert.equal(pathMockPublicAnnotation, node.sourceFile, 'The source file should match.');
          const imports = node.imports;
          assert.equal(2, imports.length);
          imports.push(imports.shift());
          const newImport = jaspiler.newImport();
          newImport.qualifiedIdentifier = jaspiler.createFieldAccess('abc', 'def', 'ghi');
          newImport.staticImport = true;
          imports.push(newImport);
          node.imports = imports;
        },
      },
    }],
  });
  assert.include(
    result.code,
    'import java.lang.annotation.Inherited;\n'
    + 'import java.lang.annotation.Documented;\n'
    + 'import static abc.def.ghi;\n');
}

// Class

function testClass() {
  let isMockAnnotationFound = false;
  const result = jaspiler.transformSync(pathMockAllInOnePublicClass, {
    plugins: [{
      visitor: {
        Class(node) {
          assert.equal('com.caoccao.jaspiler.trees.JTClassDecl', node.className);
          const modifiers = node.modifiers;
          assert.equal('com.caoccao.jaspiler.trees.JTModifiers', modifiers.className);
          if ('MockAnnotation' == node.simpleName.value) {
            assert.equal(JTKind.ANNOTATION_TYPE, node.kind);
            isMockAnnotationFound = true;
            const annotations = modifiers.annotations;
            assert.equal(4, annotations.length);
            annotations.push(annotations.shift());
            assert.equal('@Inherited', annotations[0].toString());
            assert.equal('com.caoccao.jaspiler.trees.JTIdent', annotations[0].annotationType.className);
            assert.equal('Inherited', annotations[0].annotationType.name.value);
            assert.equal(0, annotations[0].arguments.length);
            assert.equal('Retention', annotations[1].annotationType.name.value);
            assert.equal(1, annotations[1].arguments.length);
            annotations[0].annotationType = jaspiler.createIdent('NotInherited');
            const args = annotations[1].arguments;
            args.push(jaspiler.createFieldAccess('aaa', 'bbb'));
            annotations[1].arguments = args;
            modifiers.annotations = annotations;
            assert.equal(0, node.typeParameters.length);
            assert.isNull(node.extendsClause);
            assert.equal(0, node.implementsClauses.length);
            assert.equal(0, node.permitsClauses.length);
            assert.equal(2, node.members.length);
            assert.equal('String[] names() default {"A", "B"};', node.members[0].toString());
          } else if ('MockAllInOnePublicClass' == node.simpleName.value) {
            assert.equal(JTKind.CLASS, node.kind);
            assert.isNotNull(node.extendsClause);
            assert.equal('Object', node.extendsClause.toString());
            assert.equal(2, node.implementsClauses.length);
            assert.equal('Serializable', node.implementsClauses[0].toString());
            assert.equal(1, node.permitsClauses.length);
            assert.equal('MockChild', node.permitsClauses[0].toString());
            assert.equal(6, node.members.length);
            assert.equal('private String a;', node.members[1].toString());
          } else if ('MockChild' == node.simpleName.value) {
            node.simpleName = jaspiler.createName('NewMockChild');
          }
        },
      },
    }],
  });
  assert.isTrue(isMockAnnotationFound, 'Class MockAnnotation should be found.');
  assert.include(result.code, '@NotInherited\n'
    + '@Retention(RetentionPolicy.RUNTIME, aaa.bbb)\n'
    + '@Target(ElementType.ANNOTATION_TYPE)\n'
    + '@Documented\n');
  assert.include(result.code, 'final class NewMockChild extends MockAllInOnePublicClass {');
}

// Identifier

function testIdentifier() {
  const values = [];
  const result = jaspiler.transformSync(pathMockPublicAnnotation, {
    plugins: [{
      visitor: {
        Identifier(node) {
          const value = node.name.value;
          values.push(value);
          if (value == 'Inherited') {
            node.name = jaspiler.createName('NotInherited');
          }
        },
      },
    }],
  });
  assert.equal('com,java,java,Documented,Inherited,String,String', values.join(','));
  assert.include(result.code, '@NotInherited');
}

// Import

function testImport() {
  const values = [];
  const result = jaspiler.transformSync(pathMockPublicAnnotation, {
    plugins: [{
      visitor: {
        Import(node) {
          assert.isFalse(node.staticImport);
          values.push(node.qualifiedIdentifier.toString());
          node.staticImport = true;
        },
      },
    }],
  });
  assert.equal(2, values.length);
  assert.equal('java.lang.annotation.Documented', values[0]);
  assert.equal('java.lang.annotation.Inherited', values[1]);
  assert.include(
    result.code,
    'import static java.lang.annotation.Documented;\n'
    + 'import static java.lang.annotation.Inherited;\n');
}

// Variable
function testVariable() {
  const result = jaspiler.transformSync(pathMockAllInOnePublicClass, {
    plugins: [{
      visitor: {
        Variable(node) {
          const value = node.name.value;
          if ('x' == value) {
            node.name = jaspiler.createName('xx');
          } else if ('y' == value) {
            node.name = jaspiler.createName('yy');
          } else if ('a' == value) {
            node.name = jaspiler.createName('aa');
          } else if ('c' == value) {
            node.name = jaspiler.createName('cc');
          }
        },
      },
    }],
  });
  console.info(result.code);
  assert.include(result.code, '\n    private String aa;\n');
  assert.include(result.code, '\n    @SuppressWarnings("unchecked")\n');
  assert.include(result.code, '\n    public final <T> void Test(T xx, @Deprecated int yy) throws IOException, NoClassDefFoundError {\n');
  assert.include(result.code, '\n        int cc = 5;\n');
}

testAstForFile();
testAstForString();
// Package
testIgnorePackage();
testReplacePackage();
testReplacePackageName();
// Imports
testImports();
// Class
testClass();
// Identifier
testIdentifier();
// Import
testImport();
// Variable
testVariable();
