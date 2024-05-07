package net.op.render;

import static net.op.OpenCraft.oc;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.scgi.Context;
import org.scgi.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.op.OpenCraft;
import net.op.input.InputManager;
import net.op.render.screens.Screen;
import net.op.render.textures.Assets;
import net.op.render.textures.Texture;
import net.op.util.ResourceGetter;

/**
 * <h1>Render</h1><br>
 * This class is used for manage drawing process and screen control. It can also
 * determine the best fps configuration and guide the OpenGL usage.
 */
public final class Render {

	public static final GraphicsDevice DEF_GRAPHICS_DEVICE = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice();

	public static final GraphicsConfiguration GFX_CONFIG = DEF_GRAPHICS_DEVICE.getDefaultConfiguration();
	public static final DisplayMode GFX_DISPLAY_MODE = DEF_GRAPHICS_DEVICE.getDisplayMode();
	public static final Logger logger = LoggerFactory.getLogger(Render.class);

	private final Assets assets;

	/**
	 * Creates a new instance of this class.
	 */
	private Render(Assets assets) {
		this.assets = assets;
	}

	/**
	 * @return a new instance of this class.
	 */
	public static Render create(Assets assets) {
		return new Render(assets);
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

	public void init() {
		// Config display
		configDisplay();

		Context.create();
		InputManager.bindMouse();

		// Get the DPI info
		final int DPI = Toolkit.getDefaultToolkit().getScreenResolution();

		// Show render details
		logger.info("Render system initialized!");
		logger.info("[OpenGL] Using OpenGL: " + (Context.usesOpenGL() ? "Yes" : "No"));

		logger.info("DPI is set to {} ({})", DPI, Math.round(DPI * 1.0417d) + "%");
		if (DPI != 96)
			logger.info(" \u2514\u2500 (Recommended 96)", DPI);

		// Do "VSync"
		if (!vsync())
			logger.warn("[VSync] Imposible to determinate the refresh rate!");

		// Show FPS Rate
		logger.info("FPS Rate: " + OpenCraft.getClient().fpsCap);
	}

	private void configDisplay() {
		Display.create(854, 480, OpenCraft.DISPLAY_NAME);
		Display.setResizable(false);

		List<String> iconsPath = new ArrayList<>();
		iconsPath.add("/resources/icons/icon_16x16.png");
		iconsPath.add("/resources/icons/icon_24x24.png");
		iconsPath.add("/resources/icons/icon_32x32.png");
		iconsPath.add("/resources/icons/icon_48x48.png");
		iconsPath.add("/resources/icons/icon_256x256.png");

		List<Texture> icons = iconsPath.stream().map(path -> ResourceGetter.getExternal(path))
				.map(in -> Texture.read(in)).collect(Collectors.toList());

		boolean someNull = icons.stream().anyMatch(tex -> tex.isNull());
		if (!someNull)
			Display.setIcons(icons.stream().map(tex -> (Image) tex.getImage()).collect(Collectors.toList()));

		Display.show();
	}

	public boolean vsync() {
		int fpsRate;
		fpsRate = Render.GFX_DISPLAY_MODE.getRefreshRate();

		if (fpsRate != DisplayMode.REFRESH_RATE_UNKNOWN) {
			oc.fpsCap = fpsRate;
			return true;
		}

		return false;
	}

	/**
	 * Renders the game.
	 */
	public void render() {
		if (!Context.shouldRender())
			return;

		Graphics2D g2d = (Graphics2D) Context.getGraphics();
		Screen.renderCurrent(g2d, this.assets);
		g2d.dispose();

		Context.draw();
		Display.update();
	}

}
