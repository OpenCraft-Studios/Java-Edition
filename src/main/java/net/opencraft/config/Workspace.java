package net.opencraft.config;

public class Workspace {

	public static String GAME_DIR = ".";

	public static String ASSETS_DIR = GAME_DIR + "/assets";
	public static String WORLD_DIR = GAME_DIR + "/worlds";
	public static String LOGS_DIR = GAME_DIR + "/logs";

	private Workspace() {
	}

	private static void create(String gameDir, String assetsDir, String worldDir, String logsDir) {
		GAME_DIR = gameDir;
		ASSETS_DIR = assetsDir;
		WORLD_DIR = worldDir;
		LOGS_DIR = logsDir;
	}

	private static void create(String gameDir, String assetsDir) {
		create(gameDir, assetsDir, autoWorldDir(), autoLogsDir());
	}

	private static void create(String gameDir) {
		create(gameDir, autoAssetsDir());
	}

	public static void create() {
		create(autoGameDir());
	}

	public static void create(String... dirs) {
		int len = dirs.length;
		if (len == 0) {
			create();
		} else if (len == 1) {
			create(dirs[0]);
		} else if (len > 1 && len < 4) {
			create(dirs[0], dirs[1]);
		} else if (len > 3) {
			create(dirs[0], dirs[1], dirs[2], dirs[3]);
		}
	}

	private static String autoGameDir() {
		return ".";
	}

	private static String autoAssetsDir() {
		return GAME_DIR + "/assets";
	}

	private static String autoWorldDir() {
		return GAME_DIR + "/worlds";
	}

	private static String autoLogsDir() {
		return GAME_DIR + "/logs";
	}

}
