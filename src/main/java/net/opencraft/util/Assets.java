package net.opencraft.util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import net.opencraft.config.Workspace;
import net.opencraft.raster.Texture;

public class Assets {

	public static final int NORMAL_BUTTON = 0;

	private Assets() {
	}
	
	public static int[] getButtonBounds(int button) {
		return switch (button) {
			case NORMAL_BUTTON -> new int[] { 0, 66, 200, 86 };
			default -> new int[] { 0, 0, 0, 0 };
		};
	}

	public static Image getLogo() {
		return Assets.bindTexture("/gui/title/minecraft.png");
	}

	public static Image getLoadscreen() {
		return Assets.bindTexture("/gui/title/background/loadscreen.jpg");
	}

	public static Image getPanorama(int index) {
		return bindTexture(String.format("/gui/title/background/panorama_%d.png", index));
	}

	public static Image getPanoramas() {
		BufferedImage bi = new BufferedImage(3072, 1024, BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.getGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 3072, 1024);
		g.drawImage(getPanorama(0), 0, 0, null);
		g.drawImage(getPanorama(1), 1024, 0, null);
		g.drawImage(getPanorama(2), 2048, 0, null);
		g.dispose();

		return bi;
	}

	public static Cursor getCursor() {
		BufferedImage bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();

		g.setColor(Color.BLACK);
		g.drawLine(0, 16, 32, 16);
		g.drawLine(16, 0, 16, 32);
	
		Toolkit tk = Toolkit.getDefaultToolkit();
		return tk.createCustomCursor(bi.getSubimage(8, 8, 16, 16), new Point(0, 0), "dg_cursor");

	}
	
	public static Image getIcon() {
		return bindTexture("/gui/release_icon.png");
	}

	public static Image bindExternalTexture(String path) {
		Image img = null;
		try {
			img = ImageIO.read(new FileInputStream(path));
		} catch (Exception ignored) {
		}

		return Texture.of(img).getImage();
	}

	public static Image bindInternalTexture(String path) {
		Image img = null;
		try {
			img = ImageIO.read(Assets.class.getResourceAsStream(path));
		} catch (Exception ignored) {
		}

		return Texture.of(img).getImage();

	}

	public static Image bindTexture(String path) {
		return bindExternalTexture(Workspace.ASSETS_DIR + "/opencraft/textures" + path);
	}

}
