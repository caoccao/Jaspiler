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

package com.caoccao.jaspiler;

import com.caoccao.jaspiler.enums.JaspilerExitCode;
import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.caoccao.jaspiler.v8.V8Jaspiler;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.converters.JavetProxyConverter;
import picocli.CommandLine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = JaspilerContract.NAME,
        mixinStandardHelpOptions = true,
        version = JaspilerContract.VERSION,
        description = JaspilerContract.DESCRIPTION)
public final class JaspilerMain extends BaseLoggingObject implements Callable<Integer> {
    @CommandLine.Parameters(hidden = true)
    private List<String> argv;
    @CommandLine.Parameters(index = "0", description = "The JavaScript file to be executed.")
    private File file;

    public JaspilerMain() {
        super();
        argv = new ArrayList<>();
        file = null;
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new JaspilerMain()).execute(args));
    }

    @Override
    public Integer call() throws Exception {
        JaspilerExitCode jaspilerExitCode = JaspilerExitCode.NoError;
        if (file == null) {
            logger.error(JaspilerExitCode.ConfigInvalid.getMessageFormat());
            jaspilerExitCode = JaspilerExitCode.ConfigInvalid;
        } else if (!file.exists() || !file.isFile() || !file.canRead()) {
            logger.error(JaspilerExitCode.ConfigNotFound.getMessageFormat(), file.getAbsolutePath());
            jaspilerExitCode = JaspilerExitCode.ConfigNotFound;
        } else {
            // Covert the file to an absolute file to avoid the impact from the working directory changes.
            file = file.getAbsoluteFile();
            logger.info("Executing [{}]...", file.getPath());
            try (NodeRuntime nodeRuntime = V8Host.getNodeInstance().createV8Runtime()) {
                var javetProxyConverter = new JavetProxyConverter();
                nodeRuntime.setConverter(javetProxyConverter);
                try (V8Jaspiler v8Jaspiler = new V8Jaspiler(argv, nodeRuntime)) {
                    nodeRuntime.getGlobalObject().set(V8Jaspiler.NAME, v8Jaspiler);
                    nodeRuntime.getExecutor(file).executeVoid();
                } finally {
                    nodeRuntime.getGlobalObject().delete(V8Jaspiler.NAME);
                    nodeRuntime.lowMemoryNotification();
                }
            } catch (JavetException e) {
                logger.error(JaspilerExitCode.EngineUnknownError.getMessageFormat(), e.getMessage());
                jaspilerExitCode = JaspilerExitCode.EngineUnknownError;
            } catch (Throwable t) {
                logger.error(JaspilerExitCode.UnknownError.getMessageFormat(), t.getMessage());
                jaspilerExitCode = JaspilerExitCode.UnknownError;
            }
        }
        return jaspilerExitCode.getExitCode();
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
