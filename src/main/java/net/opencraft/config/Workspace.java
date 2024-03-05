package net.opencraft.config;

import java.io.File;

public class Workspace {

	public static String GAME_DIR = "opcraft";

	public static String ASSETS_DIR = GAME_DIR + "/assets";
	public static String WORLDS_DIR = GAME_DIR + "/worlds";
	public static String LOGS_DIR = GAME_DIR + "/logs";

	private Workspace() {
	}

	public static void createFolders() {
		File gameDir = new File(GAME_DIR);
		File logsDir = new File(LOGS_DIR);
		File assetsDir = new File(ASSETS_DIR);
		File worldsDir = new File(WORLDS_DIR);

		gameDir.mkdirs();
		logsDir.mkdirs();
		assetsDir.mkdirs();
		worldsDir.mkdirs();
	}
	
	private static void create(String gameDir, String assetsDir, String worldsDir, String logsDir) {
		GAME_DIR = gameDir;
		ASSETS_DIR = assetsDir;
		WORLDS_DIR = worldsDir;
		LOGS_DIR = logsDir;
		createFolders();
	}

	private static void create(String gameDir, String assetsDir) {
		create(gameDir, assetsDir, autoWorldsDir(), autoLogsDir());
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
		return "opcraft";
	}

	private static String autoAssetsDir() {
		return GAME_DIR + "/assets";
	}

	private static String autoWorldsDir() {
		return GAME_DIR + "/worlds";
	}

	private static String autoLogsDir() {
		return GAME_DIR + "/logs";
	}

}
