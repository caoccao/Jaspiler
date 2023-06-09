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
import com.caoccao.jaspiler.options.JaspilerOptions;
import com.caoccao.jaspiler.utils.BaseLoggingObject;
import com.caoccao.jaspiler.utils.SystemUtils;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.NodeRuntime;
import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.values.reference.V8ValueObject;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Optional;

public class JaspilerMain extends BaseLoggingObject {
    protected static final String JS_VAR_JASPILER_OPTIONS = "jaspilerOptions";
    protected static final String OPTION_CONFIG = "config";
    protected static final String OPTION_HELP = "help";
    protected final Options options;
    protected File configFile;
    protected boolean helpRequested;

    public JaspilerMain() {
        super();
        configFile = null;
        helpRequested = false;
        options = new Options();
    }

    public static void main(String[] args) {
        var jaspilerMain = new JaspilerMain();
        jaspilerMain.buildOptions(args);
        jaspilerMain.parseOptions(args);
        System.exit(jaspilerMain.run().getExitCode());
    }

    protected void buildOptions(String[] args) {
        logger.info("Jaspiler is a Java to Java transpiler.\n");
        logger.info("Arguments: {}\n", StringUtils.join(args, " "));
        options.addOption(Option.builder(OPTION_HELP.substring(0, 1)).longOpt(OPTION_HELP)
                .desc("print the help")
                .required(false).optionalArg(true).type(String.class).build());
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            CommandLine commandLine = commandLineParser.parse(options, args);
            helpRequested = commandLine.hasOption(OPTION_HELP);
        } catch (Throwable ignored) {
        }
        options.addOption(Option.builder(OPTION_CONFIG.substring(0, 1)).longOpt(OPTION_CONFIG)
                .desc("config script path")
                .required(true).numberOfArgs(1).type(String.class).build());
    }

    protected void parseOptions(String[] args) {
        if (helpRequested) {
            printHelp(JaspilerExitCode.NoError);
        }
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            CommandLine commandLine = commandLineParser.parse(options, args);
            configFile = Optional.ofNullable(commandLine.getOptionValue(OPTION_CONFIG))
                    .map(config -> SystemUtils.getWorkingDirectory().resolve(config).toFile())
                    .orElse(null);
        } catch (Throwable t) {
            logger.error(t.getMessage());
            printHelp(JaspilerExitCode.OptionsInvalid);
        }
    }

    protected void printHelp(JaspilerExitCode jaspilerExitCode) {
        logger.info(StringUtils.EMPTY);
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(JaspilerContract.NAME, options);
        System.exit(jaspilerExitCode.getExitCode());
    }

    public JaspilerExitCode run() {
        if (configFile == null) {
            logger.error(JaspilerExitCode.ConfigInvalid.getMessageFormat());
            return JaspilerExitCode.ConfigInvalid;
        }
        if (!configFile.exists() || !configFile.isFile() || !configFile.canRead()) {
            logger.error(JaspilerExitCode.ConfigNotFound.getMessageFormat(), configFile.getAbsolutePath());
            return JaspilerExitCode.ConfigNotFound;
        }
        logger.info("Processing [{}]...", configFile.getPath());
        try (NodeRuntime nodeRuntime = V8Host.getNodeInstance().createV8Runtime()) {
            nodeRuntime.getExecutor(configFile).executeVoid();
            JaspilerOptions jaspilerOptions;
            try (V8ValueObject v8ValueObject = nodeRuntime.getExecutor(JS_VAR_JASPILER_OPTIONS).execute()) {
                jaspilerOptions = JaspilerOptions.deserialize(v8ValueObject);
            }
            if (!jaspilerOptions.isValid()) {
                logger.error(JaspilerExitCode.ConfigEmpty.getMessageFormat(), configFile.getPath());
                return JaspilerExitCode.ConfigEmpty;
            }
            logger.info("Processing [{}]...", JS_VAR_JASPILER_OPTIONS);
            logger.info(jaspilerOptions.toJson());
            return JaspilerExitCode.NoError;
        } catch (JavetException e) {
            logger.error(JaspilerExitCode.EngineUnknownError.getMessageFormat(), e.getMessage());
            return JaspilerExitCode.EngineUnknownError;
        } catch (Throwable t) {
            logger.error(JaspilerExitCode.UnknownError.getMessageFormat(), t.getMessage());
            return JaspilerExitCode.UnknownError;
        }
    }
}
