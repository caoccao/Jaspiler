const result = jaspiler.transformSync(
  `package com.test;
  public class A {
  }
  `,
  {
    plugins: [{
      visitor: {
        Class(node) {
          node.simpleName = jaspiler.createName('B');
        },
      },
    }],
    sourceType: 'string',
  });
console.info(result.code);
