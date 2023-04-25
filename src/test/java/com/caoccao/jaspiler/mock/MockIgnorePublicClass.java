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

/* test */package/* test */com./*1*/caoccao/*2*/.jaspiler.mock;

import com.caoccao/*1*/./*2*/jaspiler.JaspilerContract;

import java.util./* test */ArrayList;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JaspilerContract.Ignore
public class MockIgnorePublicClass {
    public int b;
    private String a;
    private List<Object> list;

    public void Test() {
        a = "abc";
        b = 1;
        list = new ArrayList<>();
    }
}
