package net.opencraft.renderer.texture;

import java.awt.image.BufferedImage;

import io.vavr.Lazy;

public class LazyTextureLoader {
	
	private LazyTextureLoader() {
	}
	
	public static Lazy<BufferedImage> lazyLoad(String path) {
		return Lazy.of(() -> TextureLoader.getTexture("PNG", LazyTextureLoader.class.getResourceAsStream(path)));
	}

}
