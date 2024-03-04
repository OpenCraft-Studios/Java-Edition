package net.opencraft.util;

import java.awt.Font;
import java.io.File;

import net.opencraft.config.Workspace;

public enum Fonts {
	MOJANGLES("Mojangles", "/fonts/Mojangles.ttf");
	
	final String name;
	final String path;
	
	Fonts(String name, String path) {
		this.name = name;
		this.path = path;
	}
	
	public String getPath() {
		return Workspace.ASSETS_DIR + "/opencraft" + path;
	}
	
	public String getRelativePath() {
		return path;
	}
	
	public String getName() {
		return name;
	}

	public Font getFont(int style, int fontSize) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, new File(getPath())).deriveFont(style, fontSize);
		} catch (Exception ignored) {
			return new Font("Mojangles", style, fontSize);
		}
	}
	
}
