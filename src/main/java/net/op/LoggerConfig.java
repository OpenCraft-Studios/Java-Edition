package net.op;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import net.op.crash.CrashReport;

public class LoggerConfig {

    public static final String LOG_FORMAT = "[%1$tH:%1$tM:%1$tS] [%3$s/%4$-4s]: %5$s%n";

    public static void init() {
        // Clear logs folder
        clearLogDir();

        // Set logging format
        System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);

        // Handle external loggers
        // handle(Client.logger, "/game.log");
        // handle(Render.logger, "/renderdragon.log");
        // handle(SoundManager.logger, "/soundmanager.log");
    }

    public static void handle(Logger logger, String filepath) {
        try {
            logger.addHandler(logFile(filepath));
        } catch (Exception ex) {
            String cinfo = CrashReport.create(ex).getInfo();
            System.err.println("\n" + cinfo);
        }
    }

    public static String getLogDir() {
        return Config.GAME_DIRECTORY + "/logs";
    }

    public static FileHandler logFile(String filepath) throws SecurityException, IOException {
        FileHandler fh = new FileHandler(getLogDir() + filepath);
        fh.setFormatter(new SimpleFormatter());
        return fh;
    }

    public static void clearLogDir() {
        File logDir = new File(getLogDir());

        if (!(logDir.exists() || logDir.isFile())) {
            return;
        }

        File[] logs = logDir.listFiles();
        for (File log : logs) {
            if (!log.getName().equalsIgnoreCase("crashes")) {
                log.delete();
            }
        }

    }

    private LoggerConfig() {
    }

}
