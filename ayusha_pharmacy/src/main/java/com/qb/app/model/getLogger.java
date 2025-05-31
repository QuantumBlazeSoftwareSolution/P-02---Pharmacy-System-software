package com.qb.app.model;

import java.io.IOException;
import java.util.logging.*;

public class getLogger {

    private static Logger log;
    private static FileHandler handler;

    public static Logger logger() {
        try {
            log = Logger.getLogger("Logger");
            handler = new FileHandler("log.txt", 0, 1, true);
            handler.setFormatter(new SimpleFormatter());
            log.addHandler(handler);
        } catch (IOException | SecurityException e) {
            e.printStackTrace();
        }
        return log;
    }
}
