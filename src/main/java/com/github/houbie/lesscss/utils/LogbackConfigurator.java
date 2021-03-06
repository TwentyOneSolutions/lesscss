/*
 * Copyright (c) 2013 Houbrechts IT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.houbie.lesscss.utils;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.github.houbie.lesscss.LessCompilerImpl;
import com.github.houbie.lesscss.builder.CompilationTask;
import com.github.houbie.lesscss.engine.LessCompilationEngine;
import org.slf4j.LoggerFactory;

/**
 * Logback configuration utility for switching compiler logging to the console on/off
 *
 * @author Ivo Houbrechts
 */
public class LogbackConfigurator {

    /**
     * Configure the LessCompilerImpl logger with a ConsoleAppender
     *
     * @param verbose set logger level to Level.ALL if true, otherwise Level.OFF
     * @param daemon  set the level of the compilationTaskLogger to Level.INFO if true, otherwise Level.OFF
     */
    public static void configure(boolean verbose, boolean daemon) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<ILoggingEvent>();
        ca.setContext(lc);
        ca.setName("less console");
        PatternLayoutEncoder pl = new PatternLayoutEncoder();
        pl.setContext(lc);
        pl.setPattern("%msg%n");
        pl.start();

        ca.setEncoder(pl);
        ca.start();

        //prevent double log output
        lc.getLogger(Logger.ROOT_LOGGER_NAME).detachAndStopAllAppenders();
        configureCompilerLogger(verbose, lc, ca);
        configureCompilationTaskLogger(daemon, lc, ca);
        configureLessCompilationEngineLogger(verbose, lc, ca);
    }

    private static void configureCompilerLogger(boolean verbose, LoggerContext lc, ConsoleAppender<ILoggingEvent> ca) {
        Logger compilerLogger = lc.getLogger(LessCompilerImpl.class);
        compilerLogger.detachAndStopAllAppenders();
        compilerLogger.addAppender(ca);
        compilerLogger.setLevel(verbose ? Level.ALL : Level.OFF);
    }

    private static void configureCompilationTaskLogger(boolean daemon, LoggerContext lc, ConsoleAppender<ILoggingEvent> ca) {
        Logger compilationTaskLogger = lc.getLogger(CompilationTask.class);
        compilationTaskLogger.detachAndStopAllAppenders();
        compilationTaskLogger.addAppender(ca);
        compilationTaskLogger.setLevel(daemon ? Level.INFO : Level.OFF);
    }

    private static void configureLessCompilationEngineLogger(boolean verbose, LoggerContext lc, ConsoleAppender<ILoggingEvent> ca) {
        Logger engineLogger = lc.getLogger(LessCompilationEngine.class.getPackage().getName());
        engineLogger.detachAndStopAllAppenders();
        engineLogger.addAppender(ca);
        engineLogger.setLevel(verbose ? Level.ALL : Level.OFF);
    }
}
