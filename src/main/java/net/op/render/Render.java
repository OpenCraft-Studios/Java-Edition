package net.op.render;

import static net.op.render.display.DisplayManager.createDisplay;
import static net.op.render.display.DisplayManager.setDisplayGraphics;
import static net.op.render.display.DisplayManager.showDisplay;
import static net.op.render.display.DisplayManager.updateDisplay;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.util.logging.Logger;

import net.op.render.display.Display;
import net.op.render.screens.Screen;
import net.op.render.textures.Tilesheet;

/**
 * <h1>Render</h1> This class is used to draw into the display.
 * 
 * @see Display
 */
public final class Render extends Canvas {

	private static final long serialVersionUID = 1L;
	public static final Logger logger = Logger.getLogger(Render.class.getName());
	private Tilesheet assets;

	private Render(Tilesheet assets) {
		this.assets = assets;
	}

	public static Render create(Tilesheet assets) {
		return new Render(assets);
	}

	public void init() {
		// Configure display
		createDisplay();
		showDisplay();
		setDisplayGraphics(this);

		logger.info("Render system initialized!");
	}

	public void update() {
		render();
		updateDisplay();
		Toolkit.getDefaultToolkit().sync();
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

}
