package net.opencraft.util;

import net.opencraft.config.Workspace;

public class Utils {

	private Utils() {
	}

	public static String getLatestFile() {
		return Workspace.LOGS_DIR + "/latest.log";
	}
	
}
