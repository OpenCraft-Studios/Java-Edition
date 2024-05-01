package net.op.render.textures;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Optional;

import javax.imageio.ImageIO;
import static net.op.render.Render.toCompatibleImage;

public class Texture {

    private Optional<Image> opImg;

    public Texture(BufferedImage img) {
        Image img1 = null;
        
        if (img != null) {
            img1 = toCompatibleImage(img);
        }
        
        this.opImg = Optional.ofNullable(img1);
    }

    public static Texture of(BufferedImage img) {
        return new Texture(img);
    }

    public static Texture empty() {
        return Texture.of(null);
    }

    public static Texture read(InputStream in) {
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

    public boolean isNull() {
        return opImg.isEmpty();
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
