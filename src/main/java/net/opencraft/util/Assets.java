package net.opencraft.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import net.opencraft.client.Game;
import net.opencraft.config.Workspace;
import net.opencraft.data.ButtonInfo;
import net.opencraft.renderer.Texture;

public class Assets {
	
	private Assets() {
	}
		
	public static ButtonInfo getButton(int button) {
		return switch (button) {
			case 0 -> ButtonInfo.of().vertices(new int[] { 0, 66, 200, 86 }).build();
			default -> ButtonInfo.EMPTY;
		};
	}

	public static Image getLogo() {
		return bindTexture("/gui/title/minecraft.png").getImage();
	}

	public static Image getLoadscreen() {
		return bindTexture("/gui/title/background/loadscreen.jpg").getImage();
	}

	public static Image getPanorama(int index) {
		return bindTexture(String.format("/gui/title/background/panorama_%d.png", index)).getImage();
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
	
	public static Image getIcon() {
		return bindInternalTexture("/icon.png").getImage();
	}

	public static Texture bindExternalTexture(String path) {
		Image img = null;
		try {
			img = ImageIO.read(Resource.bindExternalResource(path));
		} catch (Exception ignored) {
		}

		return Texture.of(img);
	}

	public static Texture bindInternalTexture(String path) {
		Image img = null;
		try {
			img = ImageIO.read(Resource.bindInternalResource(path));
		} catch (Exception ignored) {
		}

		return Texture.of(img);

	}
	
	public static Texture bindPackTexture(String path) {
		Image img = null;
		try {
			img = ImageIO.read(Game.getResourcePack().getResource(path));
		} catch (Exception ignored) {
		}
		
		return Texture.of(img);
	
	}

	public static Texture bindTexture(String path) {
		if (!Game.isDefaultPackSelected())
			return bindPackTexture("assets/opencraft/textures" + path);
			
		return bindExternalTexture(Workspace.ASSETS_DIR + "/opencraft/textures" + path);
	}
	

}
