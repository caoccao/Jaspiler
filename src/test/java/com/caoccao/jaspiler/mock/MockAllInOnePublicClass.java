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

import com.caoccao.jaspiler.JaspilerContract;

import java.io.IOException;
import java.lang.annotation.*;
import java.util./* test */ArrayList;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.apache.commons.lang3.StringUtils;

public class MockAllInOnePublicClass {
    public int b;
    private String a;
    private List<Object> list;

    /**
     * Test.
     */
    public void Test() throws IOException {
        a = "abc";
        b = new Random().nextInt();
        list = new ArrayList<>();
        if (b > 0) {
            a = "def";
        } else {
            throw new IOException("abc");
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
    }
}

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@interface Annotation1 {
    String value() default "value";
}

