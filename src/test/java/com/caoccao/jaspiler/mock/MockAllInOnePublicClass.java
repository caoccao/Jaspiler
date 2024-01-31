/*
 * Copyright (c) 2023-2024. caoccao.com Sam Cao
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

/* test */
package/* test */com./*1*/caoccao/*2*/.jaspiler.mock;

import com.caoccao.jaspiler.JaspilerContract;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@interface MockAnnotation {
    String[] names() default {"A", "B"};

    String value() default "value";
}

@SuppressWarnings("unchecked")
public abstract sealed class MockAllInOnePublicClass
        extends Object
        implements Serializable, AutoCloseable
        permits MockChild {
    @Deprecated
    @JaspilerContract.Ignore
    public int b;
    private String a;
    private List<Object> list;

    static {
        System.out.println("static block");
    }

    @Deprecated
    private Map<String, Object> map = new HashMap<>() {{
        put("a", 1);
        put("b", 2);
    }};

    /**
     * Test.
     */
    @SuppressWarnings("unchecked")
    public final <T> void Test(T x, @Deprecated int y) throws IOException, NoClassDefFoundError {
        int c = 5;
        String d;
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
            try {
                if (b > 100) {
                    throw new FileAlreadyExistsException("aaa");
                } else {
                    throw new FileNotFoundException("BBB");
                }
            } catch (FileAlreadyExistsException | FileNotFoundException e) {
                // Pass.
            }
        }
        ++b;
        b -= 1;
        System.out.println(StringUtils.isEmpty(a));
        assertEquals(1, b);
    }

    private int add(int left, int right) {
        return left + right;
    }

    @Override
    @JaspilerContract.Ignore
    public void close() throws Exception {

    }
}

final class MockChild extends MockAllInOnePublicClass {
    @SuppressWarnings("unchecked")
    public class MockChild1 {
    }
}
