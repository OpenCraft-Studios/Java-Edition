package net.opencraft.renderer;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import org.lwjgl.opengl.Display;

import net.opencraft.SharedConstants;
import net.opencraft.renderer.texture.Texture;
import net.opencraft.spectoland.SpectoError;
import net.opencraft.util.Files;

public class DisplayManager {

	public static final List<String> iconsPath = new ArrayList<>();
	
	static {
		iconsPath.add("/resources/icons/icon_16x16.png");
		iconsPath.add("/resources/icons/icon_24x24.png");
		iconsPath.add("/resources/icons/icon_32x32.png");
		iconsPath.add("/resources/icons/icon_48x48.png");
		iconsPath.add("/resources/icons/icon_256x256.png");
	}
	
	public static void create(JPanel canvas, int width, int height) {
		Display.create(width, height, "OpenCraft ".concat(SharedConstants.VERSION_STRING));
		Display.setResizable(false);

		List<Texture> icons = iconsPath.stream()
				.map(path -> Files.external(path))
				.map(in -> Texture.getTexture("PNG", in))
				.collect(Collectors.toList());

		boolean someNull = icons.stream().anyMatch(tex -> tex.isNull());
		if (!someNull)
			Display.setIcons(icons.stream()
					.map(tex -> (Image) tex.getImage())
					.collect(Collectors.toList()));
		
		SpectoError.info("Display icons loaded!");
		Display.show();
	}
	
	public static void addCanvas(JPanel canvas) {
		Display.addChild(canvas);
	}

	public static int getMonitorFramerate() {
		return Renderer.GFX_DISPLAY_MODE.getRefreshRate();
	}

	
}
