package net.opencraft.renderer.texture;

import static net.opencraft.renderer.Renderer.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Optional;

import javax.imageio.ImageIO;

import io.vavr.Lazy;

public class Texture {

    private Optional<Image> opImg;

    public Texture(BufferedImage img) {
        if (img != null)
            img = toCompatibleImage(img);
        
        this.opImg = Optional.ofNullable(img);
    }

    public static Texture of(BufferedImage img) {
        return new Texture(img);
    }

    public static Texture empty() {
        return Texture.of(null);
    }
    
    /**
	 * Returns the preferred color model by your device. This possibly decrease the
	 * CPU using.
	 *
	 * @param image The original image
	 * @return The optimized image
	 */
	public static BufferedImage toCompatibleImage(final BufferedImage image) {
		if (image.getColorModel().equals(GFX_CONFIG.getColorModel())) {
			return image;
		}

		final BufferedImage new_image = GFX_CONFIG.createCompatibleImage(image.getWidth(), image.getHeight(),
				image.getTransparency());

		final Graphics2D g2d = (Graphics2D) new_image.getGraphics();

		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		return new_image;
	}

    public static Texture getTexture(String format, InputStream in) {
        if (in == null) {
            return Texture.empty();
        }

        BufferedImage img = null;
        try {
            img = ImageIO.read(in);
        } catch (Exception ignored) {
        }

        return Texture.of(img);
    }
    
    public static Lazy<Texture> lazyLoad(String path) {
    	return Lazy.of(() -> Texture.getTexture("PNG", Texture.class.getResourceAsStream(path)));
    }

    public boolean isNull() {
        return !opImg.isPresent();
    }

    public BufferedImage getImage() {
        return (BufferedImage) opImg.orElse(missignoImage());
    }
    
    private static BufferedImage missignoImage() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        img.setRGB(0, 0, Color.MAGENTA.getRGB());
        img.setRGB(0, 1, Color.BLACK.getRGB());
        img.setRGB(1, 0, Color.BLACK.getRGB());
        img.setRGB(1, 1, Color.MAGENTA.getRGB());

        return img;
    }

}
