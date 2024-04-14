package net.op;

import static net.op.render.display.DisplayManager.destroyDisplay;
import static net.op.render.display.DisplayManager.isDisplayAlive;
import static org.josl.openic.IC10.IC_TRUE;
import static org.josl.openic.IC13.icIsKeyPressed;
import static org.josl.openic.IC15.icBindDevice;
import static org.josl.openic.IC15.icGenDeviceId;

import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.josl.openic.input.Keyboard;

import net.java.games.input.Controller;
import net.op.crash.CrashReport;
import net.op.logging.InternalLogger;
import net.op.logging.LoggerConfig;
import net.op.render.Render;
import net.op.render.screens.Loadscreen;
import net.op.render.screens.Screen;
import net.op.render.textures.Assets;
import net.op.render.textures.GUITilesheet;
import net.op.sound.SoundManager;
import net.op.util.OCFont;

public final class Client implements Runnable {

	public static final String NAME = "OpenCraft";
	public static final String VERSION = "24r07";
	public static final String CODENAME = "CODENAME STYLEFML";
	public static final String DISPLAY_NAME = NAME + ' ' + VERSION;

	public static final int NANOSECONDS = 1000000000;

	private static final Client instance = new Client();
	public static final Logger logger = Logger.getLogger(Client.class.getName());

	private final Thread thread;
	private boolean running = false;

	private Assets assets;
	private Render render;

	private Client() {
		this.assets = Assets.forTextures(Config.getPack());
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

		logger.info(String.format("Selected language: %s", Locales.getDisplayName(Config.LOCALE)));
		logger.info(Client.DISPLAY_NAME + " started!");

		long lastUpdate = System.nanoTime();

		double timePassed;
		double delta = 0;

		byte exceptionCounter = 0;

		try {
			do {
				final long loopStart = System.nanoTime();

				timePassed = loopStart - lastUpdate;
				lastUpdate = loopStart;

				delta += timePassed / this.getNanoPerTick();

				while (delta >= 1) {
					tick();
					render();

					delta--;
				}
			} while (running);
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
		SoundManager.update();
	}

	public void render() {
		if (this.render.shouldRender())
			this.render.update();
	}

	/**
	 * The method {@code init()} is used to initialize the game and its
	 * dependencies.
	 *
	 * Is private because it shouldn't be executed twice.
	 */
	private void init() {
		if (running) {
			System.err.println("(!) This message shouldn't be displayed!");
			return;
		}
		GUITilesheet.create("/gui.png");

		LoggerConfig.init();
		InternalLogger.init();

		File gameSettingsFile = new File(Config.DIRECTORY + "/settings.yml");
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

	public void vsync() throws Exception {
		int fpsRate;
		fpsRate = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode()
				.getRefreshRate();

		if (fpsRate != DisplayMode.REFRESH_RATE_UNKNOWN)
			Config.FPS_CAP = fpsRate;
		else
			throw new Exception("Imposible to determinate the refresh rate!");
	}

	public double getNanoPerTick() {
		return (double) NANOSECONDS / Config.FPS_CAP;
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
			gameSettings.store(new FileOutputStream(Config.DIRECTORY + "/settings.yml"), "Game Settings");
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
	 * This method checks if the game is currently running.
	 */
	public boolean isRunning() {
		return this.running;
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
	 * <b>Main method</b><br>
	 * Is the principal method that guides the: initialization, running and the stop
	 * of the game.
	 */
	public static void main(String[] args) {
		// Starting game
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
