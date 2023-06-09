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

import com.caoccao.jaspiler.utils.BaseLoggingObject;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class JaspilerMain extends BaseLoggingObject {
    protected static final int EXIT_CODE_INVALID_OPTIONS = 1;
    protected static final int EXIT_CODE_NO_ERROR = 0;
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
        System.exit(jaspilerMain.run());
    }

    protected void buildOptions(String[] args) {
        logger.info("Jaspiler is a Java to Java transpiler.\n");
        logger.info("Arguments: {}", StringUtils.join(args, " "));
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
                .required(true).optionalArg(false).type(String.class).build());
    }

    protected void parseOptions(String[] args) {
        if (helpRequested) {
            printHelp(EXIT_CODE_NO_ERROR);
        }
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            CommandLine commandLine = commandLineParser.parse(options, args);
            configFile = new File(commandLine.getOptionValue(OPTION_CONFIG));
        } catch (Throwable t) {
            logger.error(t.getMessage());
            printHelp(EXIT_CODE_INVALID_OPTIONS);
        }
    }

    protected void printHelp(int exitCode) {
        logger.info(StringUtils.EMPTY);
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp(JaspilerContract.NAME, options);
        System.exit(exitCode);
    }

    public int run() {
        return EXIT_CODE_NO_ERROR;
    }
}
