/*
 * Copyright (c) 2023-2025. caoccao.com Sam Cao
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

// Tutorial 04: Parse argv

/// <reference types="../jaspiler/index.d.ts"/>

process.argv = [process.argv[0], ...jaspiler.argv];
process.chdir('../')
const yargs = require('yargs');

const argv = yargs
  .option('input', {
    alias: 'i',
    type: 'string',
    describe: 'the input',
    demandOption: true,
  })
  .option('output', {
    alias: 'o',
    type: 'string',
    describe: 'the output',
    demandOption: true,
  })
  .version('1.0.0')
  .help()
  .argv;

console.info();
console.info(` Input: ${argv.input}`);
console.info(`Output: ${argv.output}`);