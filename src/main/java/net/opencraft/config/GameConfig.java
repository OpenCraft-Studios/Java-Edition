package net.opencraft.config;

import java.util.Locale;

import net.opencraft.data.packs.Pack;

public class GameConfig {

	public static Locale LANGUAGE = Locale.getDefault();
	public static byte TICK_RATE = 60;
	public static boolean UNICODE = false;
	public static Pack PACK_SELECTED = null;
	public static String GAME_DIR = "opcraft";

}
