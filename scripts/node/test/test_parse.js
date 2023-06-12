const assert = require('chai').assert;

const result = jaspiler.parseSync('../../../src/test/java/com/caoccao/jaspiler/mock/MockAllInOnePublicClass.java');
assert.isObject(result);
console.info(result.ast)
