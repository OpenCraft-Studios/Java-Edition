package net.opencraft.util;

import static net.opencraft.LoggerConfig.LOG_FORMAT;
import static net.opencraft.LoggerConfig.handle;

import java.util.logging.Logger;

public class Utils {

	private static final Logger logger = Logger.getLogger("utils");

	static {
		// Set logging format
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
		handle(logger);
	}

	private Utils() {
	}

}
