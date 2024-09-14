package net.opencraft.renderer.texture;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.vavr.Lazy;

public class Assets {
	
	private final Tilesheet gui;
	private Lazy<Texture> loadingLogo;

	private Assets(Tilesheet gui) {
		this.gui = gui;
		this.loadingLogo = Texture.lazyLoad("/loading_logo.png");
	}
	
	private Assets(Tilesheet... tss) {
		this(tss[0]);
	}
	
	public static Assets create(Tilesheet... tss) {
		return new Assets(tss);
	}
	
	public static Assets create(String... tss) {
		List<Tilesheet> tilesheets = Arrays.stream(tss)
				.map(path -> Tilesheet.read(path))
				.collect(Collectors.toList());
		
		return create(tilesheets.toArray(new Tilesheet[0]));
	}
	
	/**
	 * This method returns a button.
	 * 
	 * Do not call this method directly! This method may use too much
	 * CPU. Instead, we recommend saving it to a {@link java.awt.image.BufferedImage}
	 * and retrieve it whenever.
	 */
	public BufferedImage getButton(int button_id) {
		return gui.get(0, button_id * 20, 200, 20);
	}

	/**
	 * This method returns an arrow.
	 * 
	 * Do not call this method directly! This method may use too much
	 * CPU. Instead, we recommend saving it to a {@link java.awt.image.BufferedImage}
	 * and retrieve it whenever.
	 */
	public BufferedImage getArrow(int arrow) {
		return gui.get(200 + 15 * arrow, 20, 14, 22);
	}

	/**
	 * This method returns the Minecraft Logo.
	 * 
	 * Do not call this method directly! This method may use too much
	 * CPU. Instead, we recommend saving it to a {@link java.awt.image.BufferedImage}
	 * and retrieve it whenever.
	 */
	public BufferedImage getLogo() {
		return gui.get(0, 61, 271, 44);
	}

	/**
	 * This method returns a dirt block but darker.
	 * 
	 * Do not call this method directly! This method may use too much
	 * CPU. Instead, we recommend saving it to a {@link java.awt.image.BufferedImage}
	 * and retrieve it whenever.
	 */
	public BufferedImage getBackground() {
		return gui.get(242, 0, 16, 16);
	}
	
	/**
	 * This method returns the OpenCraft HAECATOMBE logo.
	 * 
	 * Do not call this method directly! This method may use too much
	 * CPU. Instead, we recommend saving it to a {@link java.awt.image.BufferedImage}
	 * and retrieve it whenever.
	 */
	public BufferedImage getLoadingLogo() {
		return loadingLogo.get().getImage();
	}

}
