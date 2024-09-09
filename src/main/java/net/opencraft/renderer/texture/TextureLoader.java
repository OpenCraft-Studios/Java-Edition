package net.opencraft.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class TextureLoader {

	private TextureLoader() {
	}
	
	/**
	 * @param format intended to use after porting to MiniGL
	 */
	public static BufferedImage getTexture(String format, InputStream in) {
		BufferedInputStream bis = new BufferedInputStream(in);
		try {
			return ImageIO.read(in);
		} catch (IOException ignored) {
			// Typical missing texture image
			BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

	        img.setRGB(0, 0, 0xFF00FF); // Magenta
	        img.setRGB(0, 1, 0x000000); // Black
	        img.setRGB(1, 1, 0xFF00FF); // Magenta
	        img.setRGB(1, 0, 0x000000); // Black

	        return img;
		} finally {
			try {
				// Try to close stream
				bis.close();
			} catch (IOException e) {
			}
		}
	}
	
}
