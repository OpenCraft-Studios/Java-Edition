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
import net.op.render.screens.Loadscreen;
import net.op.render.screens.Screen;
import net.op.render.textures.Tilesheet;
import net.op.sound.SoundManager;
import net.op.util.OCFont;

public final class Client implements Runnable {

	public static final String NAME = "OpenCraft";
	public static final String VERSION = "24r07";
	public static final String CODENAME = "CODENAME STYLEFML";
	public static final String DISPLAY_NAME = NAME + ' ' + VERSION;

	public static final int NANOSECONDS = 1000000000;
	public static double NANO_PER_TICK = (double) NANOSECONDS / Config.FPS_CAP;

	private static final Client instance = new Client();
	public static final Logger logger = Logger.getLogger(Client.class.getName());

	private final Thread thread;
	private boolean running = false;

	private Tilesheet assets;
	private Render render;

	private Client() {
		this.assets = Tilesheet.forTextures(getResourcePack());
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

		byte exceptionCounter = 0;

		try {
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
		} catch (OutOfMemoryError error) {
			/*
			 * If any OutOfMemoryError occurs while the game is in execution we can catch it
			 * and run the Java Garbage Collector.
			 * 
			 * But if "that" overflows 2 times the memory the program will exit for safety.
			 * Because is better to prevent the game crashing by anything, but if it's
			 * repetitive is better to stop.
			 * 
			 * TODO Implement that
			 */
			System.gc();
		} catch (ArithmeticException ignored) {
			/*
			 * ATTENTION: This exception ignoring is probably not the best option. But it is
			 * not a very very important error so we just ignore it. It could be likely
			 * dangerous but we like to take risks.
			 */
		} catch (Exception any) {
			/*
			 * We have a variable called exceptionCounter that counts the exceptions that
			 * the game has at the moment, if it reaches 5 it will print the stack trace and
			 * exit the game.
			 * 
			 * This is that way because we don't want the game crash for anything, just for
			 * important. And it's considered important if the error repeats by five.
			 */
			if (exceptionCounter++ >= 5) {
				any.printStackTrace();
				System.exit(1);
			}
		}

		stop();
	}

	/**
	 * The {@code tick()} method is used to update the game. It can be called
	 * anywhere, just in case that the game is not updating properly.
	 */
	public void tick() {
		this.running = !(icIsKeyPressed(KeyEvent.VK_F3) && icIsKeyPressed(KeyEvent.VK_C)) && isDisplayAlive()
				&& running;
		this.render.update();
		SoundManager.update();
	}

	/**
	 * The method {@code init()} is used to initialize the game and its
	 * dependencies.
	 * 
	 * Is private because it shouldn't be executed twice.
	 */
	private void init() {
		if (running) {
			System.err.println("(1) This message shouldn't be displayed!");
			return;
		}

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

	/**
	 * This method is used to get the game's current thread.
	 * 
	 * @return The thread
	 */
	public Thread thread() {
		return this.thread;
	}

	/**
	 * The {@code start()} method runs the game thread so the game starts.
	 */
	public void start() {
		this.thread.start();
	}

	/**
	 * This method is used to stop the game. It could be a little bit dangerous
	 * because of some deprecated methods but it still the default.
	 */
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
		} catch (Exception ex) {
			System.exit(0);
		}
	}

	/**
	 * The {@code getClient()} method is often used for getting the current client
	 * instance.
	 * 
	 * The client is a <i>singleton</i> because is more easy to have a non-static
	 * client and access it by a static method.
	 * 
	 * @return The client
	 */
	public static Client getClient() {
		return Client.instance;
	}

	/**
	 * This method checks if the game is currently running.
	 */
	public static boolean isRunning() {
		return Client.instance.running;
	}

	/**
	 * This method sets the language of the game to a defined one.
	 */
	public static void setLanguage(Locale language) {
		Config.Language = language;
	}

	/**
	 * The {@code getLanguage()} method is used to get the current language used by
	 * the game.
	 */
	public static Locale getLanguage() {
		return Config.Language;
	}

	/**
	 * It's used to set the current game directory.
	 * 
	 * @param gameDir Game's directory
	 */
	public static void setDirectory(String gameDir) {
		Config.Directory = gameDir;
	}

	/**
	 * Returns the game directory
	 * 
	 * @return The game directory
	 * 
	 * @see #setDirectory(String)
	 */
	public static String getDirectory() {
		return Config.Directory;
	}

	/**
	 * Returns the current resource pack used by the game.
	 * 
	 * @return The resource pack used by the game
	 */
	public static Pack getResourcePack() {
		return Config.ResourcePack;
	}

	/**
	 * Sets the resource pack to a specific one.
	 * 
	 * @param resourcePack the resource pack
	 */
	public static void setResourcePack(Pack resourcePack) {
		Config.ResourcePack = resourcePack;
	}

	/**
	 * <b>Main method</b><br>
	 * Is the principal method that guides the: initialization, running and the stop
	 * of the game.
	 */
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

		// TODO: Implement this, but only if the game is played by first time
		/*
		 * // System.out.println(); //
		 * System.out.println(" ===== Thanks for playing OpenCraft ====="); //
		 * System.out.println("     We wish you a hopeful experience"); //
		 * System.out.println("  With this game and we are pushing our"); //
		 * System.out.println("   Efforts to make you play this game."); //
		 * System.out.println(); //
		 * System.out.println("  If you want, you can share this game"); //
		 * System.out.println(" =========== You're WELCOME!! ==========="); //
		 * System.out.println("  - OpenCraft's Developer Team " +
		 * Calendar.getInstance().get(Calendar.YEAR));
		 */
		
		// Stops the game
		System.exit(status);

	}

}
