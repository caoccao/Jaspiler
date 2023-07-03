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

package com.caoccao.jaspiler.v8;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.executors.IV8Executor;
import com.caoccao.javet.interop.executors.V8FileExecutor;
import com.caoccao.javet.node.modules.NodeModuleModule;
import com.caoccao.javet.node.modules.NodeModuleProcess;

import java.io.File;

public class V8PatchedFileExecutor extends V8FileExecutor {
    public V8PatchedFileExecutor(V8Runtime v8Runtime, File scriptFile) throws JavetException {
        super(v8Runtime, scriptFile);
    }

    @Override
    public IV8Executor setResourceName(String resourceName) throws JavetException {
        getV8ScriptOrigin().setResourceName(resourceName);
        V8Runtime v8Runtime = getV8Runtime();
        if (v8Runtime.getJSRuntimeType().isNode()) {
            NodeRuntime nodeRuntime = (NodeRuntime) v8Runtime;
            File resourceFile = new File(resourceName);
            File parentFile = resourceFile.getParentFile();
            nodeRuntime.getGlobalObject().set(NodeRuntime.PROPERTY_DIRNAME, parentFile.getAbsolutePath());
            nodeRuntime.getGlobalObject().set(NodeRuntime.PROPERTY_FILENAME, resourceFile.getAbsolutePath());
            nodeRuntime.getNodeModule(NodeModuleModule.class).setRequireRootDirectory(parentFile.getAbsoluteFile() + File.separator);
            nodeRuntime.getNodeModule(NodeModuleProcess.class).setWorkingDirectory(parentFile.getAbsolutePath());
        }
        return this;
    }
}
