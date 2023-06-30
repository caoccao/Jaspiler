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

// Tutorial 02: Play with Types

/// <reference types="../jaspiler/index.d.ts"/>

const { JTKind } = require('../jaspiler/jaspiler');

const result = jaspiler.transformSync(
  `package com.test;
  public class A {}
  public interface B {}
  public record C() {}
  public class D {}
  `,
  {
    plugins: [{
      visitor: {
        Class(node) {
          const simpleName = node.simpleName.value;
          switch (simpleName) {
            case 'A':
              node.kind = JTKind.INTERFACE;
              break;
            case 'B':
              node.kind = JTKind.CLASS;
              break;
            case 'C':
              node.kind = JTKind.ENUM;
              break;
            case 'D':
              node.kind = JTKind.RECORD;
              break;
          }
        },
      },
    }],
    sourceType: 'string',
  });
console.info(result.code);
