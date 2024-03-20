package net.opencraft.renderer;

import static net.opencraft.logging.LoggerConfig.LOG_FORMAT;
import static net.opencraft.logging.LoggerConfig.handle;
import static net.opencraft.renderer.display.DisplayManager.createDisplay;
import static net.opencraft.renderer.display.DisplayManager.getDisplay;
import static net.opencraft.renderer.display.DisplayManager.getDisplayHeight;
import static net.opencraft.renderer.display.DisplayManager.getDisplayWidth;
import static net.opencraft.renderer.display.DisplayManager.showDisplay;
import static net.opencraft.renderer.display.DisplayManager.updateDisplay;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.logging.Logger;

import net.opencraft.renderer.display.Display;
import net.opencraft.renderer.scenes.Scene;
import net.opencraft.renderer.scenes.SceneListener;
import net.opencraft.util.Resource;

public class RenderDragon extends Canvas implements SceneListener {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger("renderThread");

	private static boolean initializated = false;
	private static RenderDragon instance;

	private static Screen screen = new Screen(Display.WIDTH, Display.HEIGHT);

	static {
		// Set logging format
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
		handle(logger, "/renderdragon.log");
	}
	
	private RenderDragon() {
	}

	public static void init() {
		if (isInitializated())
			return;

		instance = new RenderDragon();
		Scene.addListener(instance);
		initializated = true;

		createDisplay();
		showDisplay();
		getDisplay().add(instance);

		update();
		
		logger.info("Render system initialized!");

	}

	public static void update() {
		Scene.renderCurrent(screen.getImage());
		instance.render();
		updateDisplay();
	}

	public static Screen getScreen() {
		return screen;
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			logger.info("Buffered rendering not configured, creating new framework.");
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		screen.render(g, getDisplayWidth(), getDisplayHeight());
		g.dispose();
		bs.show();
	}

	public static boolean isInitializated() {
		return initializated;
	}


	@Override
	public void onSceneChanged(Resource scene1, Resource scene2) {
		logger.info(String.format("Rendered scene %s", scene2));
		
	}
	@Override
	public void onSceneUpdated(Resource scene_res) {
	}

}
