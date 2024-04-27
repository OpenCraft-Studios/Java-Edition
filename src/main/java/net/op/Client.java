package net.op;

import static net.op.render.display.DisplayManager.destroyDisplay;
import static net.op.render.display.DisplayManager.isDisplayAlive;
import static org.josl.openic.IC13.icIsKeyPressed;

import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.op.crash.CrashReport;
import net.op.input.InputManager;
import net.op.language.Locales;
import net.op.language.LocalesLoader;
import net.op.render.Render;
import net.op.render.textures.GUITilesheet;
import net.op.sound.SoundManager;
import net.op.spectoland.SpectoError;
import net.op.spectoland.SpectoError.InternalLogger;
import net.op.util.OCFont;

public final class Client implements Runnable {

	public static final String NAME = "OpenCraft";
	public static final String VERSION = "24r10";
	public static final String CODENAME = "StyleFML";
	public static final String DISPLAY_NAME = NAME + ' ' + VERSION;

	public static final int NANOSECONDS = 1000000000;

	private static final Client instance = new Client();
	public static final Logger logger = LoggerFactory.getLogger(Client.class);

	private final Thread thread;
	private boolean running = false;

	private Render render;

	/**
	 * Creates a instance of the game. This method must be executed once. If you
	 * execute it more times, the game could crash or being completely unusable.
	 */
	private Client() {
		this.render = Render.create();
		this.thread = new Thread(this);
		this.thread.setName("main");
	}

	/**
	 * This method just executes the game. The game will not finish until you have
	 * closed the game's main display or an exception has occurred.
	 */
	@Override
	public void run() {
		try {
			// Initialize the game
			init();
		} catch (Exception e) {
			/*
			 * If any exception has been throwed, create a crash dump and try to report it
			 */
			JOptionPane.showMessageDialog(null, "Failed to initialize OpenCraft!", "Initialization Error",
					JOptionPane.ERROR_MESSAGE);

			CrashReport crash = CrashReport.create(e);
			crash.save(new File(CrashReport.generatePath()));

			crash.report();
		}

		// Game status info
		logger.info(String.format("Selected language: %s", Locales.getDisplayName(Locales.getLocale())));
		logger.info(Client.DISPLAY_NAME + " started!");

		/* The FPS Limiter */
		// (I could use a Timer class or something but is so difficult, I prefer doing
		// this into the run method)
		long lastUpdate = System.nanoTime();

		double timePassed;
		double delta = 0;

		do {
			try {
				final long loopStart = System.nanoTime();

				timePassed = loopStart - lastUpdate;
				lastUpdate = loopStart;

				delta += timePassed / this.getNanoPerTick();

				while (delta >= 1) {
					tick();
					render();

					delta--;
				}

			} catch (Throwable tb) {
				SpectoError.process(tb);
			}
		} while (running);

		// Finally stops the game
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
		if (this.render.shouldRender()) {
			this.render.update();
		}
	}

	/**
	 * The method {@code init()} is used to initialize the game and its
	 * dependencies.
	 *
	 * Is private because it shouldn't be executed twice.
	 */
	private void init() {
		if (running) {
			System.err.println("(!) This message should not be displayed!");
			return;
		}

		// Load languages
		LocalesLoader.load();

		// Read config
		Config.read();

		// Create basics resources
		GUITilesheet.create("/gui.png");
		OCFont.create("/fonts");

		// Initialize sound and render
		SoundManager.init();
		this.render.init();

		InputManager.bindKeyboard();

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

	public boolean vsync() {
		int fpsRate;
		fpsRate = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode()
				.getRefreshRate();

		if (fpsRate != DisplayMode.REFRESH_RATE_UNKNOWN)
			Config.FPS_CAP = fpsRate;
		
		return fpsRate != DisplayMode.REFRESH_RATE_UNKNOWN;
	}

	public double getNanoPerTick() {
		return (double) NANOSECONDS / Config.FPS_CAP;
	}

	/**
	 * This method is used to stop the game. Normally this is executed at the end of
	 * the game, but you can still use it for stop it whenever you want.
	 *
	 * @param force This is used for stopping the game suddenly
	 */
	@SuppressWarnings("deprecation")
	public void stop(boolean force) {
		if (force)
			System.exit(0);
		if (!running)
			return;

		// Stop
		this.running = false;

		// Disable soundManager
		SoundManager.shutdown();

		// Destroy display
		destroyDisplay();

		// Stop logging and collect exceptions
		if (InternalLogger.ignoredExceptions == 0)
			logger.info("No ignored exceptions are detected!");
		else
			logger.error("{} ignored exceptions were throwed!\n", InternalLogger.ignoredExceptions);
		
		InternalLogger.stopLogging();

		// Finish the thread
		try {
			InternalLogger.writeFile();
			thread.stop();
		} catch (Exception e1) {
			try {
				thread.interrupt();
			} catch (Exception e2) {
				e2.printStackTrace();
				System.exit(0);
			}
		}
	}

	/**
	 * This method is effectively the same as {@code stop(boolean force)} but it
	 * takes as default argument 'force' for value 'false': So the game ends
	 * correctly and it saves everything.
	 */
	public void stop() {
		this.stop(false);
	}

	/**
	 * This method checks if the game is currently running.
	 *
	 * @return the game status
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
	 * @return The current client instance
	 */
	public static Client getClient() {
		return Client.instance;
	}

	/**
	 * <b>Main method</b><br>
	 * Is the principal method that guides the: initialization, running and the stop
	 * of the game. If you want to create your own OpenCraft launcher, we recommend
	 * delete this method and compile this game without it because you can guide the
	 * executing and monitoring of the game.
	 */
	public static void main(String[] args) {
		// Parse arguments
		OptionParser parser = new OptionParser();

		/* Support for Minecraft launchers. I will not convert this into a pay game */
		parser.accepts("demo");

		OptionSpec<?> legacyFlag = parser.accepts("legacy");
		OptionSpec<?> gameDirArgument = parser.accepts("gameDir").withRequiredArg();
		OptionSpec<?> configFileArgument = parser.acceptsAll(Arrays.asList("cnf", "conf", "config")).withRequiredArg();

		OptionSet argSet = parser.parse(args);
		Config.LEGACY = argSet.has(legacyFlag);

		if (argSet.has(gameDirArgument))
			Config.GAME_DIRECTORY = (String) argSet.valueOf(gameDirArgument);

		Config.DEFAULT_CONFIG_FILE = Config.GAME_DIRECTORY + "/options.txt";

		if (argSet.has(configFileArgument))
			Config.DEFAULT_CONFIG_FILE = (String) argSet.valueOf(configFileArgument);

		// Starting game
		Client game = Client.getClient();
		Thread gameThread = game.thread();
		gameThread.start();

		int status = 0;
		try {
			gameThread.join();
		} catch (Exception ignored) {
			status = 3;
			logger.error("The game ended with errors!");
		}

		// TODO: Implement this if the game is played by first time
		System.out.println();
		System.out.println(" ===== Thanks for playing OpenCraft =====");
		System.out.println("   We wish you the best experience with");
		System.out.println("  this game because we are putting all");
		System.out.println("     our efforts to make this game.");
		System.out.println();
		System.out.println("   If you want, you can share this game!");
		System.out.println(" =========== You're welcome!! ===========");
		System.out.println("   - OpenCraft's Developer Team " + Calendar.getInstance().get(Calendar.YEAR));

		// Stops the game
		System.exit(status);
	}

}
