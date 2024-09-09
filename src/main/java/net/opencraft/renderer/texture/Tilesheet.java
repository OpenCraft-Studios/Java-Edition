package net.opencraft.renderer.texture;

import java.awt.image.BufferedImage;

import net.opencraft.util.ResourceGetter;

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
    
    public static Tilesheet read(String path) {
    	return new Tilesheet(path);
    }
    
    public static Tilesheet forImage(BufferedImage image) {
        return Tilesheet.forTexture(Texture.of(image));
    }

    public final BufferedImage get(int x, int y, int w, int h) {
        if (tilesheet.isNull())
            return Texture.empty().getImage();
        
        return tilesheet.getImage().getSubimage(x, y, w, h);
    }
    
}
