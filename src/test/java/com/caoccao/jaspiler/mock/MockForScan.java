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

package com.caoccao.jaspiler.mock;

import com.sun.source.tree.Tree;

import java.io.Serializable;
import java.util.Random;

@interface AA {
    String[] names() default {};
}

interface IA extends Serializable, AutoCloseable {
    default void a() {
    }
}

@AA
@SuppressWarnings("preview")
public class MockForScan implements IA {
    protected int a;

    public static void main(String[] args) {
        System.out.println(MockForScan.class.getName());
    }

    @Override
    public void close() throws Exception {

    }

    private void mockDoWhileLoop() {
        int i = 0;
        do {
            ++i;
            System.out.println(i);
        } while (i < 100);
    }

    private String mockForLoop() {
        for (int i = 0; i < 100; i++) {
            System.out.println(i);
        }
        return "abc";
    }

    private void mockIf() {
        Random random = new Random();
        if (random.nextBoolean()) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

    public void mockSwitch(String str, Tree.Kind kind) {
        Random random = new Random();
        switch (random.nextInt()) {
            case 1:
                System.out.println("1");
                break;
            default:
                System.out.println("default");
                break;
        }
        switch (str) {
            case "A" -> {
                System.out.println("A");
            }
            default -> {
                System.out.println("default");
            }
        }
        switch (kind) {
            case IF, CASE -> System.out.println("1");
            default -> System.out.println("2");
        }
    }

    private void mockWhileLoop() {
        int i = 0;
        while (i < 100) {
            ++i;
            System.out.println(i);
        }
    }
}
