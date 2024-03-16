package net.opencraft.client;

import static net.opencraft.LoggerConfig.LOG_FORMAT;
import static net.opencraft.LoggerConfig.handle;
import static net.opencraft.renderer.display.DisplayManager.destroyDisplay;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import net.opencraft.config.GameConfig;
import net.opencraft.config.GameExperiments;
import net.opencraft.config.Workspace;
import net.opencraft.data.packs.Pack;
import net.opencraft.renderer.RenderDragon;
import net.opencraft.renderer.Screen;
import net.opencraft.renderer.scenes.LoadScene;
import net.opencraft.renderer.scenes.Scenes;
import net.opencraft.sound.SoundManager;

public class Game implements Runnable {

	public static final String NAME = "OpenCraft";
	public static final String VERSION = "24r03";
	public static final String TITLE = NAME + ' ' + VERSION;

	public static final int NANOSECONDS = 1000000000;
	public static final double NANO_PER_TICK = NANOSECONDS / GameConfig.TICK_RATE;

	private static Game instance;
	private static Pack selected_pack;

	private static final Logger logger = Logger.getLogger("main");

	private boolean running = false;
	private Screen screen;

	static {
		// Set logging format
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
		handle(logger);
	}

	@Override
	public void run() {
		logger.info("Initializing the game...");
		init();
		logger.info("Game initializated!");

		long lastUpdate = System.nanoTime();

		double timePassed;
		double delta = 0;

		while (running) {
			final long loopStart = System.nanoTime();

			timePassed = loopStart - lastUpdate;
			lastUpdate = loopStart;

			delta += timePassed / NANO_PER_TICK;

			while (delta >= 1) {
				tick();
				delta--;
			}

		}

		stop();
		System.exit(0);

	}

	public void render() {
		Graphics g = this.screen.getGraphics();
		Scenes.renderCurrent(g);
		RenderDragon.update();
	}

	public void tick() {
		render();
		if (!GameExperiments.PLAY_SOUND_ONCE)
			SoundManager.update();
	}

	public void stop() {
		logger.info("Stopping game...");
		screen.getGraphics().dispose();
		destroyDisplay();
	}

	public void init() {
		Thread.currentThread().setName("main");

		Workspace.create();

		RenderDragon.init();
		screen = RenderDragon.getScreen();
		Scenes.setCurrent(LoadScene.SCENE);

		running = true;
	}

	public static Locale getLanguage() {
		return GameConfig.LANGUAGE;
	}

	public static void setLanguage(Locale language) {
		GameConfig.LANGUAGE = language;
	}

	public static void setTickRate(int tick_rate) {
		GameConfig.TICK_RATE = (byte) (tick_rate & 0xFF);
	}

	public static void enableUnicode() {
		GameConfig.UNICODE = true;
	}

	public static void disableUnicode() {
		GameConfig.UNICODE = false;
	}

	public static void skipIntro() {
		Scenes.setCurrent(Scenes.TITLE_SCENE);
	}

	public static void selectPack(Pack pack) {
		selected_pack = pack;
	}

	public static void useDefaultPack() {
		selectPack(null);
	}

	public static Pack getResourcePack() {
		return selected_pack;
	}

	public static Game getInstance() {
		return instance;
	}

	public static boolean isDefaultPackSelected() {
		return selected_pack == null;
	}

	public static BufferedImage screenshot() {
		Game game = getInstance();
		return game.screen.screenshot();
	}

	public static void main(String[] args) throws IOException {
		new Thread(instance = new Game()).start();
	}
}
