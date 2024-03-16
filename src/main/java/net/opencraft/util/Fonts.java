package net.opencraft.util;

import static net.opencraft.LoggerConfig.LOG_FORMAT;
import static net.opencraft.LoggerConfig.handle;

import java.awt.Font;
import java.io.InputStream;
import java.util.logging.Logger;

import net.opencraft.client.Game;
import net.opencraft.config.Workspace;

public class Fonts {

	private static final Logger logger = Logger.getLogger("fontManager");
	
	public static final Font MOJANGLES;

	static {
		// Set logging format
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
		handle(logger);
		
		Font mojangles;
		InputStream mojanglesIn = InputStream.nullInputStream();

		if (!Game.isDefaultPackSelected())
			mojanglesIn = Game.getResourcePack().getResource("assets/opencraft/fonts/Mojangles.ttf");
		else {
			try {
				mojanglesIn = Resource.bindExternalResource(Workspace.ASSETS_DIR + "/opencraft/fonts/Mojangles.ttf");
			} catch (Exception ignored) {
				logger.warning("Resource 'opencraft.font:mojangles' not found!");
			}
		}

		try {
			mojangles = Font.createFont(Font.TRUETYPE_FONT, mojanglesIn);
		} catch (Exception ignored) {
			mojangles = new Font("Mojangles", Font.PLAIN, 12);
		}

		MOJANGLES = mojangles;
	}

}
