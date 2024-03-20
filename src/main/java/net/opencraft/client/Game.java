package net.opencraft.client;

import static net.opencraft.logging.LoggerConfig.*;
import static net.opencraft.renderer.display.DisplayManager.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;

import net.opencraft.config.GameConfig;
import net.opencraft.config.GameExperiments;
import net.opencraft.data.packs.DefaultPack;
import net.opencraft.data.packs.Pack;
import net.opencraft.language.Languages;
import net.opencraft.logging.LoggerConfig;
import net.opencraft.renderer.RenderDragon;
import net.opencraft.renderer.scenes.Scene;
import net.opencraft.sound.SoundManager;

public class Game implements Runnable {

	public static final String NAME = "OpenCraft";
	public static final String VERSION = "24r05";
	public static final String TITLE = NAME + ((char) 0x20) + VERSION;

	public static final int NANOSECONDS = 1000000000;
	public static final double NANO_PER_TICK = (double) NANOSECONDS / GameConfig.TICK_RATE;

	private static final Game instance = new Game();
	private static final Logger logger = Logger.getLogger("main");

	private static Pack selected_pack = DefaultPack.getDefaultPack();

	private boolean running = false;

	static {
		LoggerConfig.clearLogDir();

		// Set logging format
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
		handle(logger, "/game.log");
	}

	public void init() {
		RenderDragon.init();

		if (GameExperiments.SKIP_LOAD_SCENE)
			Scene.setCurrent(Scene.MENU_SCENE);
		else
			Scene.setCurrent(Scene.LOAD_SCENE);

		System.out.println();
		running = true;
	}

	@Override
	public void run() {
		init();
		logger.info(String.format("Selected language: %s", Languages.getDisplayName(getLanguage())));

		logger.info(Game.TITLE + " started!");

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

	}

	public void render() {
		RenderDragon.update();
	}

	public void tick() {
		render();
		if (!GameExperiments.PLAY_SOUND_ONCE)
			SoundManager.update();
	}

	public static void stop() {
		logger.info("Stopping game...");
		destroyDisplay();
		LoggerConfig.clearLogDir();
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
		Scene.setCurrent(Scene.MENU_SCENE);
	}

	public static void selectPack(Pack pack) {
		selected_pack = pack;
	}

	public static void useDefaultPack() {
		selectPack(DefaultPack.getDefaultPack());
	}

	public static Pack getResourcePack() {
		return selected_pack;
	}

	public static Game getInstance() {
		return instance;
	}

	public static boolean isDefaultPackSelected() {
		return selected_pack instanceof DefaultPack;
	}

	public static BufferedImage screenshot() {
		return RenderDragon.getScreen().screenshot();
	}

	public static void main(String[] args) throws IOException {
		// Set system output to a file
		Thread gameThread = new Thread(instance);
		gameThread.setName("gameThread");
		gameThread.start();
		try {
			gameThread.join();
		} catch (InterruptedException e) {
			logger.warning("The game has not finished correctly!");
		}

		stop();
		System.exit(0);
	}
}
