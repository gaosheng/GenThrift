package com.gs.thrift.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ShellProcess {

    private static final Logger logger = LoggerFactory.getLogger(ShellProcess.class);

    public static Process process;

    /**
     * 运行shell命令
     * @param shell
     */
    public static void runShell(String shell) {
        try {
            process = Runtime.getRuntime().exec(shell);
            logger.info(shell);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
