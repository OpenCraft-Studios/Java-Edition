package net.opencraft.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import net.opencraft.config.GameConfig;
import net.opencraft.renderer.Texture;

public class Assets {

	public final static Image LOGO;
	public final static Image WIDGETS;
	public final static Image PANORAMAS;

	public static String TEXTURE_DIR = GameConfig.GAME_DIR + "/assets/opencraft/textures";
	
	static {
		LOGO = getLogo().getImage();
		WIDGETS = getWidgets().getImage();
		PANORAMAS = getPanoramas().getImage();
	}

	private Assets() {
	}

	private static Texture getLogo() {
		return bindTexture("/gui/title/minecraft.png");
	}

	private static Image getPanorama(int index) {
		return bindTexture(String.format("/gui/title/background/panorama_%d.png", index)).getImage();
	}

	private static Texture getPanoramas() {
		BufferedImage bi = new BufferedImage(3072, 1024, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();

		g.setColor(Color.PINK);
		g.fillRect(0, 0, 3072, 1024);
		g.drawImage(getPanorama(0), 0, 0, null);
		g.drawImage(getPanorama(1), 1024, 0, null);
		g.drawImage(getPanorama(2), 2048, 0, null);
		g.dispose();

		return Texture.of(bi);
	}

	private static Texture getWidgets() {
		return bindTexture("/gui/widgets.png");
	}

	public static Image getIcon() {
		return Texture.of(Resource.bindInternalResource("/icon.png")).getImage();
	}

	public static Image getLoadscene() {
		return bindTexture("/gui/title/background/loadscreen.jpg").getImage();
	}

	public static Texture bindTexture(String path) {
		return Texture.of(Resource.bindResource(Assets.TEXTURE_DIR + path));
	}
}