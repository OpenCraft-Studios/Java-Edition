package net.op.render;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Optional;

import org.scgi.Context;
import org.scgi.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.op.Client;
import net.op.Config;
import net.op.input.InputManager;
import net.op.render.screens.Screen;

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

	private static final boolean OPEN_GL;

	static {
		// Checks if OpenGL-Based Pipeline is enabled
		OPEN_GL = Boolean.parseBoolean(Optional.ofNullable(System.getProperty("sun.java2d.opengl")).orElse("false"));
	}

	/**
	 * Creates a new instance of this class.
	 */
	private Render() {
	}

	/**
	 * Creates a new instance of this class.
	 * 
	 * @return That instance
	 */
	public static Render create() {
		return new Render();
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
		Display.create(854, 480, Client.DISPLAY_NAME);
		Display.setResizable(false);
		Display.show();

		Context.create();
		InputManager.bindMouse();

		// Get the DPI info
		final int DPI = Toolkit.getDefaultToolkit().getScreenResolution();

		// Show render details
		logger.info("Render system initialized!");
		logger.info("[OpenGL] Using OpenGL: %s".formatted(OPEN_GL ? "Yes" : "No"));

		logger.info("DPI is set to {} ({})", DPI, Math.round(DPI * 1.0417d) + "%");
		if (DPI != 96)
			logger.info(" \u2514\u2500 (Recommended 96)", DPI);

		// Do "VSync"
		if (!vsync())
			logger.warn("[VSync] Imposible to determinate the refresh rate!");

		// Show FPS Rate
		logger.info("FPS Rate: %d".formatted(Config.FPS_CAP));
	}

	public boolean vsync() {
		int fpsRate;
		fpsRate = Render.GFX_DISPLAY_MODE.getRefreshRate();

		if (fpsRate != DisplayMode.REFRESH_RATE_UNKNOWN) {
			Config.FPS_CAP = fpsRate;
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
		Screen.renderCurrent(g2d);
		g2d.dispose();

		Context.draw();
		Display.update();
	}

}
