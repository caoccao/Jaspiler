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

// Tutorial 03: Built-in Annotations

/// <reference types="../jaspiler/index.d.ts"/>

const { PluginContractIgnore, PluginContractChangeMethod } = require('../jaspiler/jaspiler');

const result = jaspiler.transformSync(
  `package com.test;

  public class A {
    @JaspilerContract.Ignore
    private int a; // This property is ignored.

    @JaspilerContract.Ignore(condition = "x == 1")
    private int b; // This property is ignored.

    @JaspilerContract.Ignore(condition = "x == 2")
    private int c; // This property is not ignored.

    @JaspilerContract.Ignore
    public void d() {
      // This method is ignored.
    }

    @JaspilerContract.Change(instruction = "options = { type: 'clear' }")
    public void e(String str) {
      System.out.println(str); // This method is cleared.
    }

    @JaspilerContract.Change(condition = "x == 1", instruction = "options = { type: 'clear' }")
    public int f(int x, int y) {
      return x + y; // This method is cleared.
    }

    @JaspilerContract.Change(condition = "x == 2", instruction = "options = { type: 'clear' }")
    public int g(int x, int y) {
      return x + y; // This method is not cleared.
    }
  }

  @JaspilerContract.Ignore
  interface B {} // This interface is ignored.
  `,
  {
    context: { x: 1, options: {} },
    plugins: [PluginContractIgnore, PluginContractChangeMethod],
    sourceType: 'string',
  });
console.info(result.code);
