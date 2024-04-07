package net.op;

import static net.op.render.display.DisplayManager.destroyDisplay;
import static net.op.render.display.DisplayManager.isDisplayAlive;
import static org.josl.openic.IC10.IC_TRUE;
import static org.josl.openic.IC13.icIsKeyPressed;
import static org.josl.openic.IC15.icBindDevice;
import static org.josl.openic.IC15.icGenDeviceId;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.josl.openic.input.Keyboard;

import net.java.games.input.Controller;
import net.op.crash.CrashReport;
import net.op.data.packs.Pack;
import net.op.language.Languages;
import net.op.logging.InternalLogger;
import net.op.logging.LoggerConfig;
import net.op.render.Render;
import net.op.render.scenes.Loadscreen;
import net.op.render.scenes.Screen;
import net.op.render.textures.Assets;
import net.op.sound.SoundManager;
import net.op.util.OCFont;

public final class Client implements Runnable {

	public static final String NAME = "OpenCraft";
	public static final String VERSION = "24r06";
	public static final String CODENAME = "Codename Starway";
	public static final String DISPLAY_NAME = NAME + ' ' + VERSION;

	public static final int NANOSECONDS = 1000000000;
	public static double NANO_PER_TICK = (double) NANOSECONDS / Config.FPS_CAP;

	private static final Client instance = new Client();
	public static final Logger logger = Logger.getLogger("net.op.Client");

	private final Thread thread;
	private boolean running = false;

	private Assets assets;
	private Render render;

	private Client() {
		this.assets = Assets.forTextures(getResourcePack());
		this.render = Render.create(this.assets);
		this.thread = new Thread(this);
		this.thread.setName("gameThread");
	}

	private void bindKeyboard() {
		long id = icGenDeviceId();
		Keyboard keyboard = new Keyboard();
		icBindDevice(Controller.Type.KEYBOARD, keyboard, IC_TRUE, id);
	}

	@Override
	public void run() {
		try {
			init();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Failed to initialize OpenCraft!", "Initialization Error",
					JOptionPane.ERROR_MESSAGE);

			CrashReport crash = CrashReport.create(e);
			crash.save(new File(CrashReport.generatePath()));

			crash.report();
		}

		logger.info(String.format("Selected language: %s", Languages.getDisplayName(getLanguage())));
		logger.info(Client.DISPLAY_NAME + " started!");

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
	}

	public void tick() {
		this.running = !(icIsKeyPressed(KeyEvent.VK_F3) && icIsKeyPressed(KeyEvent.VK_C)) && isDisplayAlive()
				&& running;
		this.render.update();
		SoundManager.update();
	}

	public void init() {
		LoggerConfig.init();
		InternalLogger.init();

		File gameSettingsFile = new File(getDirectory() + "/settings.yml");
		if (gameSettingsFile.exists()) {
			Properties gameSettings = new Properties();
			try {
				gameSettings.load(new FileInputStream(gameSettingsFile));
			} catch (Exception ignored) {
				logger.warning("Failed to load game settings!");
				// TODO Internal logger
			}
			Config.read(gameSettings);
		}

		Screen.setCurrent(Loadscreen.getInstance());
		this.render.init();
		OCFont.create(this.assets);
		bindKeyboard();

		this.running = true;
	}

	public Thread thread() {
		return this.thread;
	}

	public void start() {
		this.thread.start();
	}

	@SuppressWarnings("deprecation")
	public void stop() {
		// Save settings
		Properties gameSettings = Config.toProperties();
		try {
			gameSettings.store(new FileOutputStream(getDirectory() + "/settings.yml"), "Game Settings");
		} catch (Exception ignored) {
			logger.warning("Failed to save game settings!");
			// TODO Internal logger
		}

		this.running = false;
		SoundManager.stopSounds();
		destroyDisplay();

		try {
			thread().stop();
		} catch (Exception ignored) {
			System.out.println("error");
			ignored.printStackTrace();
			System.exit(0);
		}
	}

	public static Client getClient() {
		return Client.instance;
	}

	public static boolean isRunning() {
		return Client.instance.running;
	}

	public static void setLanguageIndex(int language_index) {
		Config.Language = language_index;
	}

	public static void setLanguage(Locale language) {
		setLanguageIndex(Languages.indexOf(language));
	}

	public static Locale getLanguage() {
		return Languages.getLocaleByIndex(Config.Language);
	}

	public static int getLanguageIndex() {
		return Config.Language;
	}

	public static void setDirectory(String gameDir) {
		Config.Directory = gameDir;
	}

	public static String getDirectory() {
		return Config.Directory;
	}

	public static Pack getResourcePack() {
		return Config.ResourcePack;
	}

	public static void setResourcePack(Pack resourcePack) {
		Config.ResourcePack = resourcePack;
	}

	public static void main(String[] args) {
		Client game = Client.getClient();
		Thread gameThread = game.thread();
		gameThread.start();

		int status = 0;
		try {
			gameThread.join();
		} catch (Exception ignored) {
			status = 3;
			logger.severe("The game has ended with errors");
		}

		System.exit(status);

	}

}
