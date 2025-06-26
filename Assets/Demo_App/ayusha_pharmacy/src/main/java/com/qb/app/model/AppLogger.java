package com.qb.app.model;

import java.io.IOException;
import java.util.logging.*;

public class AppLogger {

    public static Logger getLogger() {
        Logger logger = Logger.getLogger("AppLogger");
        try {
            FileHandler fh = new FileHandler("application_log.txt", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false); // Stop logging to console
        } catch (IOException e) {
            e.printStackTrace();
            getLogger.logger().warning(e.toString());
        }
        return logger;
    }
}
