package net.op.render.textures;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Optional;

import javax.imageio.ImageIO;
import static net.op.render.textures.Assets.toCompatibleImage;

public class Texture {

    private Optional<Image> opImg;

    public Texture(Image img) {
        Image img1 = null;
        
        if (img != null) {
            img1 = toCompatibleImage((BufferedImage) img);
        }
        
        this.opImg = Optional.ofNullable(img1);
    }

    public static Texture of(Image img) {
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
        return (BufferedImage) opImg.orElse(Assets.missignoImage());
    }

}
