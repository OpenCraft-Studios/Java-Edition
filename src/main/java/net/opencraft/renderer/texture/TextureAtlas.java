package net.opencraft.renderer.texture;

import java.awt.image.BufferedImage;

import net.opencraft.util.Files;

public class TextureAtlas {

    private final Texture tilesheet;

    public TextureAtlas(TextureAtlas other) {
        this.tilesheet = other.tilesheet;
    }
    
    public TextureAtlas(Texture tilesheet) {
        this.tilesheet = tilesheet;
    }
    
    public TextureAtlas(String path) {
        this(Texture.getTexture("PNG", Files.internal(path)));
    }

    public static TextureAtlas forTexture(Texture tilesheet) {
        return new TextureAtlas(tilesheet);
    }
    
    public static TextureAtlas read(String path) {
    	return new TextureAtlas(path);
    }
    
    public static TextureAtlas forImage(BufferedImage image) {
        return TextureAtlas.forTexture(Texture.of(image));
    }

    public final BufferedImage get(int x, int y, int w, int h) {
        if (tilesheet.isNull())
            return Texture.empty().getImage();
        
        return tilesheet.getImage().getSubimage(x, y, w, h);
    }
    
}
