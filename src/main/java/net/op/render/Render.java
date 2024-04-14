package net.op.render;

import static net.op.render.display.DisplayManager.createDisplay;
import static net.op.render.display.DisplayManager.getDisplay;
import static net.op.render.display.DisplayManager.setDisplayGraphics;
import static net.op.render.display.DisplayManager.showDisplay;
import static net.op.render.display.DisplayManager.updateDisplay;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.logging.Logger;

import net.op.Client;
import net.op.Config;
import net.op.render.display.Display;
import net.op.render.screens.Screen;
import net.op.render.textures.Assets;

/**
 * <h1>Render</h1> This class is used to draw into the display.
 *
 * @see Display
 */
public final class Render extends Canvas {

	public static final Logger logger = Logger.getLogger(Render.class.getName());
	private static final long serialVersionUID = 1L;

	private static final boolean OPEN_GL;
	private Assets assets;

	static {
		if (System.getProperty("sun.java2d.opengl") != null) {
			OPEN_GL = System.getProperty("sun.java2d.opengl").equalsIgnoreCase("true");
		} else {
			OPEN_GL = false;
		}
	}

	private Render(Assets assets) {
		this.assets = assets;
		setBackground(Color.BLACK);
	}

	public static Render create(Assets assets) {
		return new Render(assets);
	}

	public void init() {
		// Configure display
		createDisplay();
		showDisplay();
		setDisplayGraphics(this);

		logger.info("Render system initialized!");
		logger.info("[OpenGL] Using OpenGL: %s".formatted(OPEN_GL ? "Yes" : "No"));

		// Do "VSync"
		try {
			Client.getClient().vsync();
		} catch (Exception ex) {
			logger.warning(ex.getMessage());
		}

		logger.info("FPS Rate: %d".formatted(Config.FPS_CAP));
	}

	public void update() {
		render();
		updateDisplay();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			return;
		}

		Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
		Screen.renderCurrent(g2d, this.assets);
		g2d.dispose();

		bs.show();

	}
	
	public boolean shouldRender() {
		return getDisplay().isShowing() && !getDisplay().isMinimized();
	}

}
