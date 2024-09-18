package net.opencraft.renderer.texture;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.vavr.Lazy;
import net.opencraft.annotations.MayConsumeCPU;

public class Assets {
	
	private final TextureAtlas gui;
	private Lazy<Texture> loadingLogo;

	private Assets(TextureAtlas gui) {
		this.gui = gui;
		this.loadingLogo = Texture.lazyLoad("/loading_logo.png");
	}
	
	private Assets(TextureAtlas... tss) {
		this(tss[0]);
	}
	
	public static Assets create(TextureAtlas... tss) {
		return new Assets(tss);
	}
	
	public static Assets create(String... tss) {
		List<TextureAtlas> tilesheets = Arrays.stream(tss)
				.map(path -> TextureAtlas.read(path))
				.collect(Collectors.toList());
		
		return create(tilesheets.toArray(new TextureAtlas[0]));
	}
	
	/**
	 * @return a button.
	 */
	@MayConsumeCPU
	public BufferedImage getButton(int button_id) {
		return gui.get(0, button_id * 20, 200, 20);
	}

	/**
	 * @return an arrow.
	 */
	@MayConsumeCPU
	public BufferedImage getArrow(int arrow) {
		return gui.get(200 + 15 * arrow, 20, 14, 22);
	}

	/**
	 * @return the Minecraft Logo.
	 */
	@MayConsumeCPU
	public BufferedImage getLogo() {
		return gui.get(0, 61, 271, 44);
	}

	/**
	 * @return a dirt block but darker.
	 */
	@MayConsumeCPU
	public BufferedImage getBackground() {
		return gui.get(242, 0, 16, 16);
	}
	
	/**
	 * @return the OpenCraft HAECATOMBE logo.
	 */
	@MayConsumeCPU
	public BufferedImage getLoadingLogo() {
		return loadingLogo.get().getImage();
	}

}
