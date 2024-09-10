package net.opencraft.renderer;

import static net.opencraft.OpenCraft.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.opencraft.renderer.screens.F3Screen;
import net.opencraft.renderer.screens.Screen;
import net.opencraft.spectoland.SpectoError;

/**
 * <h1>Render</h1><br>
 * This class is used for manage drawing process and screen control. It can also
 * determine the best fps configuration and guide the OpenGL usage.
 */
public final class Renderer {

	public static final GraphicsDevice DEF_GRAPHICS_DEVICE = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice();

	public static final GraphicsConfiguration GFX_CONFIG = DEF_GRAPHICS_DEVICE.getDefaultConfiguration();
	static final DisplayMode GFX_DISPLAY_MODE = DEF_GRAPHICS_DEVICE.getDisplayMode();
	private static final Logger logger = LoggerFactory.getLogger(Renderer.class);
	
	/**
	 * Creates a new instance of this class.
	 */
	private Renderer() {
	}

	/**
	 * @return a new instance of this class.
	 */
	public static Renderer create() {
		return new Renderer();
	}

	public void init() {
		// Show render details
		logger.info("Render system initialized!");
		SpectoError.info("Render System initialized!");
		logger.info("[OpenGL] Using OpenGL: " + (usesOpenGL() ? "Yes" : "No"));
	}

	public void render(Graphics2D g2d) {
		Screen.renderCurrent(g2d);

		if (Keyboard.isKeyClicked(KeyEvent.VK_F3))
			F3Screen.toggleVisible();
		
		if (F3Screen.isVisible())
			F3Screen.draw(g2d);

		g2d.dispose();
	}

	public void takeScreenshot() {
		BufferedImage bi = new BufferedImage(Display.getWidth(), Display.getHeight(), BufferedImage.TYPE_INT_RGB);
		Screen.renderCurrent(bi.createGraphics());
		try {
			ImageIO.write(bi, "PNG", new FileOutputStream(new File(oc.directory, "screenshot.png")));
			F3Screen.setStatus("Taking screenshot...");
		} catch (Exception ex) {
			SpectoError.ignored(ex, getClass());
		}
	}

	public static boolean usesOpenGL() {
		return Boolean.parseBoolean(System.getProperty("sun.java2d.opengl", "false"));		
	}	

}
