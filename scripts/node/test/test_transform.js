/*
 * Copyright (c) 2023-2024. caoccao.com Sam Cao
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

const { assert } = require('chai');
const path = require('path');
const process = require('process');
const { JTKind, PluginContractIgnore, PluginContractChangeMethod } = require('../jaspiler/jaspiler');

const workingDirectory = process.cwd();
const pathMockAllInOnePublicClass = path.join(
  workingDirectory,
  '../../../src/test/java/com/caoccao/jaspiler/mock/MockAllInOnePublicClass.java');
const pathMockForScan = path.join(
  workingDirectory,
  '../../../src/test/java/com/caoccao/jaspiler/mock/MockForScan.java');
const pathMockPublicAnnotation = path.join(
  workingDirectory,
  '../../../src/test/java/com/caoccao/jaspiler/mock/MockPublicAnnotation.java');

// AST

function testAstForFile() {
  const result = jaspiler.transformSync(pathMockPublicAnnotation, { ast: true });
  // Assert { ast, code }
  assert.isObject(result);
  assert.isObject(result.ast);
  const expectedLine = 'public @interface MockPublicAnnotation {';
  assert.include(result.code, expectedLine, 'The code string should be generated');
  assert.include(result.ast.toString(), expectedLine, 'The ast.toString() should work');
  assert.include('' + result.ast, expectedLine, 'The ast[Symbol.toPrimitive]() should work');
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
  assert.include(typeDecls[0].toString(), expectedLine, 'The typeDecls[0].toString() should work');
  assert.equal(pathMockPublicAnnotation, ast.sourceFile, 'The source file should match');
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

// Contract.Ignore

function testContractIgnoreCompilationUnit() {
  const result = jaspiler.transformSync(
    `package a.b.c;
    @JaspilerContract.Ignore
    public class A {
    }
    `,
    { plugins: [PluginContractIgnore], ast: true, fileName: 'A', sourceType: 'string' });
  // Assert { ast, code }
  assert.isObject(result);
  assert.isTrue(result.ast.isActionIgnore());
  assert.isUndefined(result.code);
}

function testContractIgnoreClass() {
  const result = jaspiler.transformSync(
    `package a.b.c;
    @JaspilerContract.Ignore
    class A {
    }
    `,
    { plugins: [PluginContractIgnore], ast: true, fileName: 'A', sourceType: 'string' });
  // Assert { ast, code }
  assert.isObject(result);
  assert.isFalse(result.ast.isActionIgnore());
  assert.include(result.code, 'package a.b.c;');
  assert.notInclude(result.code, 'class A');
}

function testContractIgnoreMethod() {
  const result = jaspiler.transformSync(
    `package a.b.c;
    public class A {
        @JaspilerContract.Ignore
        public void test() {}
    }
    `,
    { plugins: [PluginContractIgnore], ast: true, fileName: 'A', sourceType: 'string' });
  // Assert { ast, code }
  assert.isObject(result);
  assert.include(result.code, 'public class A {');
  assert.notInclude(result.code, 'public void test()');
}

function testContractIgnoreProperty() {
  const context = { hideB: false };
  const result = jaspiler.transformSync(
    `package a.b.c;
    public class A {
        @JaspilerContract.Ignore
        private int a;
        @JaspilerContract.Ignore(condition = "hideB")
        private int b;
    }
    `,
    { plugins: [PluginContractIgnore], context: context, ast: true, fileName: 'A', sourceType: 'string' });
  // Assert { ast, code }
  assert.isObject(result);
  assert.include(result.code, 'public class A {');
  assert.notInclude(result.code, 'private int a;');
  assert.include(result.code, 'private int b;');
}

function testContractChangeMethod() {
  const context = { options: {} };
  const result = jaspiler.transformSync(
    `package a.b.c;
    public class A {
        @JaspilerContract.Change(instruction = "options = { type: 'clear' }")
        public void testVoid() { 1 + 1; }
        @JaspilerContract.Change(instruction = "options = { type: 'clear' }")
        public boolean testBoolean() { return true; }
        @JaspilerContract.Change(instruction = "options = { type: 'clear' }")
        public char testChar() { return 'a'; }
        @JaspilerContract.Change(instruction = "options = { type: 'clear' }")
        public double testDouble() { return 1.23D; }
        @JaspilerContract.Change(instruction = "options = { type: 'clear' }")
        public float testFloat() { return 1.23F; }
        @JaspilerContract.Change(instruction = "options = { type: 'clear' }")
        public int testInt() { return 1; }
        @JaspilerContract.Change(instruction = "options = { type: 'clear' }")
        public long testLong() { return 1L; }
        @JaspilerContract.Change(instruction = "options = { type: 'clear' }")
        public Object testObject() { return new Object(); }
        @JaspilerContract.Change(instruction = "options = { type: 'clear' }")
        public String testString() { return "123"; }
    }
    `,
    { plugins: [PluginContractChangeMethod], context: context, ast: true, fileName: 'A', sourceType: 'string' });
  // Assert { ast, code }
  assert.isObject(result);
  assert.include(result.code, 'public void testVoid() {\n    }');
  assert.include(result.code, 'public boolean testBoolean() {\n        return false;\n    }');
  assert.include(result.code, 'public char testChar() {\n        return \'\\0\';\n    }');
  assert.include(result.code, 'public double testDouble() {\n        return 0D;\n    }');
  assert.include(result.code, 'public float testFloat() {\n        return 0F;\n    }');
  assert.include(result.code, 'public int testInt() {\n        return 0;\n    }');
  assert.include(result.code, 'public long testLong() {\n        return 0L;\n    }');
  assert.include(result.code, 'public Object testObject() {\n        return null;\n    }');
  assert.include(result.code, 'public String testString() {\n        return null;\n    }');
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
          assert.equal(pathMockPublicAnnotation, node.sourceFile, 'The source file should match');
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
  const simpleNames = []
  const result = jaspiler.transformSync(pathMockAllInOnePublicClass, {
    plugins: [{
      visitor: {
        Class(node) {
          assert.equal('com.caoccao.jaspiler.trees.JTClassDecl', node.className);
          const modifiers = node.modifiers;
          assert.equal('com.caoccao.jaspiler.trees.JTModifiers', modifiers.className);
          const simpleName = node.simpleName.value;
          simpleNames.push(simpleName);
          if ('MockAnnotation' == simpleName) {
            assert.equal(JTKind.ANNOTATION_TYPE, node.kind);
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
          } else if ('MockAllInOnePublicClass' == simpleName) {
            assert.equal(JTKind.CLASS, node.kind);
            assert.isNotNull(node.extendsClause);
            assert.equal('Object', node.extendsClause.toString());
            assert.equal(2, node.implementsClauses.length);
            assert.equal('Serializable', node.implementsClauses[0].toString());
            assert.equal(1, node.permitsClauses.length);
            assert.equal('MockChild', node.permitsClauses[0].toString());
            assert.equal(8, node.members.length);
            assert.equal('private String a;', node.members[1].toString());
          } else if ('MockChild' == simpleName) {
            node.simpleName = jaspiler.createName('NewMockChild');
          } else if ('MockChild1' == simpleName) {
            node.simpleName = jaspiler.createName('NewMockChild1');
          }
        },
      },
    }],
  });
  assert.equal('MockAnnotation,MockAllInOnePublicClass,,MockChild,MockChild1', simpleNames.join(','));
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
            node.name = jaspiler.createName('xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx');
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
  assert.include(result.code, '\n    private String aa;\n');
  assert.include(result.code, '\n    @SuppressWarnings("unchecked")\n');
  assert.include(
    result.code,
    '\n    @SuppressWarnings("unchecked")\n'
    + '    public final <T> void Test(T xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx, @Deprecated int yy)\n'
    + '            throws IOException, NoClassDefFoundError {\n');
  assert.include(result.code, '\n        int cc = 5;\n');
}

// Method

function testMethod() {
  const methodNames = [];
  const result = jaspiler.transformSync(pathMockAllInOnePublicClass, {
    plugins: [{
      visitor: {
        Method(node) {
          assert.equal('com.caoccao.jaspiler.trees.JTMethodDecl', node.className);
          const methodName = node.name.value;
          methodNames.push(methodName);
          if (methodName == 'names') {
            const modifiers = node.modifiers;
            assert.isEmpty(modifiers.toString(), 'modifiers.toString() should be empty');
            assert.isEmpty(modifiers.annotations, 'modifiers.annotations should be empty');
            assert.isEmpty(modifiers.flags, 'modifiers.flags should be empty');
            assert.isEmpty(node.parameters, 'node.parameters should be empty');
            assert.isNull(node.receiverParameter);
            assert.equal('String[]', node.returnType.toString(), 'node.returnType should be String[]');
            assert.isEmpty(node.throwExpressions, 'node.throwExpressions should be empty');
            assert.isEmpty(node.typeParameters, 'node.typeParameters should be empty');
            assert.equal('com.caoccao.jaspiler.trees.JTNewArray', node.defaultValue.className);
            assert.equal('{"A", "B"}', node.defaultValue.toString());
            node.defaultValue = jaspiler.createFieldAccess('ABC');
          } else if (methodName == 'value') {
            assert.equal('com.caoccao.jaspiler.trees.JTLiteral', node.defaultValue.className);
            assert.equal(JTKind.STRING_LITERAL, node.defaultValue.kind);
          } else if (methodName == 'Test') {
            assert.equal(1, node.modifiers.annotations.length);
            assert.equal(2, node.modifiers.flags.length);
            assert.equal(1, node.typeParameters.length);
            assert.equal('T', node.typeParameters[0].toString());
            assert.equal('void', node.returnType.toString());
            assert.equal(2, node.throwExpressions.length);
            assert.equal(2, node.parameters.length);
          }
        },
      },
    }],
  });
  assert.equal('names,value,Test,add,close', methodNames.join(','));
  assert.include(result.code, '\n    String[] names() default ABC;\n');
}

// Block
function testBlock() {
  const values = [];
  jaspiler.transformSync(pathMockAllInOnePublicClass, {
    plugins: [{
      visitor: {
        Block(node) {
          const value = node.toString();
          if (value.includes('System.out.println("static block");')) {
            values.push(value);
            assert.equal(1, node.statements.length);
            assert.isTrue(node.static);
          } else if (value.includes('put("a", 1);')) {
            values.push(value);
            assert.equal(2, node.statements.length);
            assert.isFalse(node.static);
          }
        },
      },
    }],
  });
  assert.equal(2, values.length);
}

// Other

function testOther() {
  let count = 0;
  jaspiler.transformSync(pathMockForScan, {
    plugins: [{
      visitor: {
        Other(node) {
          count++;
        },
      },
    }],
  });
  assert.equal(0, count, 'There should not be other nodes');
}

// Scan

function testScan() {
  const classSimpleNameSet = new Set();
  jaspiler.transformSync(pathMockForScan, {
    plugins: [{
      visitor: {
        Scan(node) {
          if (node) {
            classSimpleNameSet.add(node.classSimpleName);
          }
        },
      },
    }],
  });
  const expectedClassSimpleNameSet = new Set([
    'JTCompilationUnit', 'JTPackageDecl', 'JTFieldAccess', 'JTIdent', 'JTImport',
    'JTClassDecl', 'JTModifiers', 'JTMethodDecl', 'JTArrayType', 'JTNewArray',
    'JTPrimitiveType', 'JTBlock', 'JTAnnotation', 'JTLiteral', 'JTVariableDecl',
    'JTExpressionStatement', 'JTForLoop', 'JTBinary', 'JTUnary', 'JTReturn',
    'JTNewClass', 'JTIf', 'JTAssign',
    'JTSwitch', 'JTCase', 'JTBreak',
    'JTMethodInvocation', 'JTWhileLoop', 'JTDoWhileLoop', 'JTParens']);
  const unexpectedClassSimpleNames =
    [...classSimpleNameSet].filter(name => !expectedClassSimpleNameSet.has(name));
  assert.equal(0, unexpectedClassSimpleNames.length,
    'Unexpected [\'' + unexpectedClassSimpleNames.join('\',\'') + '\']');
}

// AST
testAstForFile();
testAstForString();
// Contract.Ignore
testContractIgnoreCompilationUnit();
testContractIgnoreClass();
testContractIgnoreMethod();
testContractIgnoreProperty();
testContractChangeMethod();
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
// Method
testMethod();
// Block
testBlock();
// Other
testOther();
// Scan
testScan();
