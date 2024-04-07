package net.op.render;

import static net.op.render.display.DisplayManager.createDisplay;
import static net.op.render.display.DisplayManager.getDisplayHeight;
import static net.op.render.display.DisplayManager.getDisplayWidth;
import static net.op.render.display.DisplayManager.setDisplayGraphics;
import static net.op.render.display.DisplayManager.showDisplay;
import static net.op.render.display.DisplayManager.updateDisplay;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.logging.Logger;

import net.op.render.scenes.Screen;
import net.op.render.textures.Assets;

/**
 * <h1>Render</h1>
 * This class is used to draw into the display.
 * @see Display
 */
public final class Render extends Canvas {

	private static final long serialVersionUID = 1L;
	public static final Logger logger = Logger.getLogger("net.op.render.Render");
	private Assets assets;

	private Render(Assets assets) {
		this.assets = assets;
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
	}

	public void update() {
		render();
		updateDisplay();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getDisplayWidth(), getDisplayHeight());

		Screen.renderCurrent(g, this.assets);
		g.dispose();

		bs.show();
	}

}
