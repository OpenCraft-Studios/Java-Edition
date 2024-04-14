package net.op.render.textures;

import java.awt.Image;
import java.awt.image.BufferedImage;
import net.op.util.ResourceGetter;

public class Tilesheet {

    private final Texture tilesheet;

    public Tilesheet(Tilesheet other) {
        this.tilesheet = other.tilesheet;
    }
    
    public Tilesheet(Texture tilesheet) {
        this.tilesheet = tilesheet;
    }
    
    public Tilesheet(String path) {
        this(Texture.read(ResourceGetter.getInternal(path)));
    }

    public static Tilesheet forTexture(Texture tilesheet) {
        return new Tilesheet(tilesheet);
    }
    
    public static Tilesheet forImage(Image image) {
        return Tilesheet.forTexture(Texture.of(image));
    }

    public final BufferedImage get(int x, int y, int w, int h) {
        if (tilesheet.isNull())
            return Texture.empty().getImage();
        
        return tilesheet.getImage().getSubimage(x, y, w, h);
    }
    
}
