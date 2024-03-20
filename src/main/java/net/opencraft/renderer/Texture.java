package net.opencraft.renderer;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Optional;

import javax.imageio.ImageIO;

import net.opencraft.logging.InternalLogger;

public class Texture {
	
	public final Optional<Image> img;

	public Texture(Image img) {
		this.img = Optional.ofNullable(img);
	}
	
	public static Texture empty() {
		return new Texture(null);
	}

	public Texture(int width, int height, boolean alpha) {
		this(new BufferedImage(width, height, (alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB)));
	}
	
	public static Texture of(InputStream in) {
		Image img = null;
		
		try {
			img = ImageIO.read(in);
		} catch (Exception ignored) {
			InternalLogger.out.printf("[%s] Ignored exception:\n", Texture.class.getName());
			ignored.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();
		}
		
		return Texture.of(img);
	}
	
	public static Texture of(Image img) {
		return new Texture(img);
	}
	
	public Image getImage() {
		if (img.isPresent())
			return img.get();
		
		BufferedImage bi = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		bi.setRGB(0, 0, Color.MAGENTA.getRGB());
		bi.setRGB(0, 1, Color.BLACK.getRGB());
		bi.setRGB(1, 0, Color.BLACK.getRGB());
		bi.setRGB(1, 1, Color.MAGENTA.getRGB());
		
		return bi;
	}

}
