const assert = require('chai').assert;
const path = require('path');
const process = require('process');

const workingDirectory = process.cwd();
const pathMockPublicAnnotation = path.join(
  workingDirectory,
  '../../../src/test/java/com/caoccao/jaspiler/mock/MockPublicAnnotation.java');

function testWithoutOptions() {
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
}

function testReplacePackageName() {
  const result = jaspiler.transformSync(pathMockPublicAnnotation, {
    plugins: [{
      visitor: {
        Package(node) {
          node.packageName = jaspiler.createFieldAccess('abc', 'def', 'ghi');
        },
      },
    }],
  });
  console.info(result.code);
  assert.include(result.code, 'package abc.def.ghi;');
}

testWithoutOptions();
testReplacePackageName();
