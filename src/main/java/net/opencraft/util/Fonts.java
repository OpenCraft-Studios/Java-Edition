package net.opencraft.util;

import java.awt.Font;
import java.io.InputStream;

import net.opencraft.config.GameConfig;
import net.opencraft.logging.InternalLogger;

public class Fonts {
	
	public static final Font MOJANGLES;

	static {
		Font mojangles;
		InputStream mojanglesIn = Resource.bindResource(GameConfig.GAME_DIR + "/assets/opencraft/fonts/Mojangles.ttf");


		try {
			mojangles = Font.createFont(Font.TRUETYPE_FONT, mojanglesIn);
		} catch (Exception ignored) {
			mojangles = new Font("Mojangles", Font.PLAIN, 12);
			InternalLogger.out.printf("[%s] Ignored exception:\n", Fonts.class.getName());
			ignored.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();
		}

		MOJANGLES = mojangles;
	}

}
