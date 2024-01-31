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

package com.caoccao.jaspiler;

import com.caoccao.jaspiler.enums.JaspilerExitCode;
import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.caoccao.jaspiler.v8.V8Jaspiler;
import com.caoccao.jaspiler.v8.V8PatchedFileExecutor;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.converters.JavetProxyConverter;

import java.io.File;

public final class JaspilerMain extends BaseLoggingObject {
    public static void main(String[] args) {
        JaspilerExitCode jaspilerExitCode = new JaspilerMain().execute(args);
        System.exit(jaspilerExitCode.getExitCode());
    }

    public JaspilerExitCode execute(String[] args) {
        JaspilerExitCode jaspilerExitCode = JaspilerExitCode.NoError;
        if (args.length == 0) {
            printHelp();
            jaspilerExitCode = JaspilerExitCode.ScriptAbsent;
            logger.error(jaspilerExitCode.getMessageFormat());
        } else {
            File file = new File(args[0]);
            if (!file.exists() || !file.isFile() || !file.canRead()) {
                jaspilerExitCode = JaspilerExitCode.ScriptNotFound;
                logger.error(jaspilerExitCode.getMessageFormat(), file.getAbsolutePath());
            } else {
                // Covert the file to an absolute file to avoid the impact from the working directory changes.
                file = file.getAbsoluteFile();
                logger.info("Executing [{}]...", file.getPath());
                try (NodeRuntime nodeRuntime = V8Host.getNodeInstance().createV8Runtime()) {
                    var javetProxyConverter = new JavetProxyConverter();
                    nodeRuntime.setConverter(javetProxyConverter);
                    try (V8Jaspiler v8Jaspiler = new V8Jaspiler(args, nodeRuntime)) {
                        nodeRuntime.getGlobalObject().set(V8Jaspiler.NAME, v8Jaspiler);
                        var executor = new V8PatchedFileExecutor(nodeRuntime, file);
                        executor.executeVoid();
                        nodeRuntime.await();
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
        }
        return jaspilerExitCode;
    }

    private void printHelp() {
        logger.info("{} v{}", JaspilerContract.NAME, JaspilerContract.VERSION);
        logger.info("{}\n", JaspilerContract.DESCRIPTION);
        logger.info("Usage:");
        logger.info("  java -jar jaspiler.*.jar <scriptFilePath> args...");
    }
}
