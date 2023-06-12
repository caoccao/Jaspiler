const assert = require('chai').assert;
const path = require('path');
const process = require('process');
const workingDirectory = process.cwd();

function testWithoutOptions() {
  const result = jaspiler.transformSync(
    path.join(
      workingDirectory,
      '../../../src/test/java/com/caoccao/jaspiler/mock/MockPublicAnnotation.java'));
  // Assert result, result.ast, result.code
  assert.isObject(result);
  assert.isObject(result.ast);
  const expectedLine = 'public @interface MockPublicAnnotation {';
  assert.include(result.code, expectedLine, 'The code string should be generated.');
  assert.include(result.ast.toString(), expectedLine, 'The ast.toString() should work.');
  assert.include('' + result.ast, expectedLine, 'The ast[Symbol.toPrimitive]() should work.');
  // Assert ast
  const ast = result.ast;
}

testWithoutOptions();
