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

function testBasicTransform() {
  const result = jaspiler.transformSync(pathMockPublicAnnotation);
  // Assert result, result.ast, result.code
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
          }
        },
      },
    }],
  });
  console.info(result.code);
  assert.isTrue(isMockAnnotationFound, 'Class MockAnnotation should be found.');
  assert.include(result.code, '@NotInherited\n'
    + '@Retention(RetentionPolicy.RUNTIME, aaa.bbb)\n'
    + '@Target(ElementType.ANNOTATION_TYPE)\n'
    + '@Documented\n');
}

testBasicTransform();
// Package
testIgnorePackage();
testReplacePackage();
testReplacePackageName();
// Imports
testImports();
// Class
testClass();
