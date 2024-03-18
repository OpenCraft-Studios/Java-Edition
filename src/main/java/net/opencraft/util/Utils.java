package net.opencraft.util;

import net.opencraft.config.GameConfig;
import net.opencraft.data.ButtonInfo;

public class Utils {

	public static final int BUTTON = 0;
	public static final int BUTTON_SELECTED = 1;
	public static final int BUTTON_DISABLED = 2;

	private Utils() {
	}

	public static ButtonInfo getButton(int button) {
		return switch (button) {
			case BUTTON -> ButtonInfo.of().vertices(new int[] { 0, 66, 200, 86 }).build();
			case BUTTON_SELECTED -> ButtonInfo.of().vertices(new int[] { 0, 86, 200, 106 }).build();
			case BUTTON_DISABLED -> ButtonInfo.of().vertices(new int[] { 0, 45, 200, 66 }).build();
			default -> ButtonInfo.EMPTY;
		};
	}
	
	public static String getGameDir() {
		return GameConfig.GAME_DIR;
	}

	public static String getLatestFile() {
		return GameConfig.GAME_DIR + "/logs/latest.log";
	}
	
}
