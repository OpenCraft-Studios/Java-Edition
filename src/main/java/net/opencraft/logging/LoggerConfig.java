package net.opencraft.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

import net.opencraft.config.GameConfig;

public class LoggerConfig {

	public static final String LOG_FORMAT = "[%1$tH:%1$tM:%1$tS] [%3$s/%4$-4s]: %5$s%n";

	static {
		// Set logging format
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
	}

	public static void handle(Logger logger, String filepath) {
		try {
			logger.addHandler(logFile(filepath));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Cannot save logging traces to file! More information in console.",
					"FATAL SEVERE ERROR!", JOptionPane.ERROR_MESSAGE);
			System.err.println("Cannot save logging traces to file!");
			System.err.println("Exception -> class " + e.getClass().getCanonicalName());
			System.err.println("  * Cause: " + e.getCause());
			System.err.println("  * Message: " + e.getMessage());
			System.err.println("  * Message (in your language): " + e.getLocalizedMessage());
			System.err.println("  * Exception Pointer: 0x" + Integer.toString(e.hashCode(), 16).toUpperCase());
		}
	}

	public static String getLogDir() {
		return GameConfig.GAME_DIR + "/logs";
	}
	
	public static FileHandler logFile(String filepath) throws SecurityException, IOException {
		FileHandler fh = new FileHandler(getLogDir() + filepath);
		fh.setFormatter(new SimpleFormatter());
		return fh;
	}
	
	public static void clearLogDir() {
		File logDir = new File(getLogDir());
		
		if (!(logDir.exists() || logDir.isFile()))
			return;
		
		File[] logs = logDir.listFiles();
		for (File log : logs)
			log.delete();
		
	}

}
