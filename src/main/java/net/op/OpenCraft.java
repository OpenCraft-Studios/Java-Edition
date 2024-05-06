package net.op;

import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;
import static org.josl.openic.IC15.icInit;
import static org.josl.openic.IC13.*;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.UIManager.LookAndFeelInfo;

import org.guppy4j.run.Startable;
import org.guppy4j.run.Stoppable;
import org.scgi.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.op.crash.CrashReport;
import net.op.input.InputManager;
import net.op.render.Render;
import net.op.render.textures.Assets;
import net.op.sound.SoundManager;
import net.op.spectoland.InternalLogger;
import net.op.spectoland.SpectoError;
import net.op.util.OCFont;

public final class OpenCraft implements Runnable, Startable, Stoppable {

	public static final String NAME = "OpenCraft";
	public static final String VERSION = "24r11";
	public static final String TECHNICAL_NAME = "System Update 1";
	public static final String DISPLAY_NAME = NAME + ' ' + VERSION;

	public static final int NANOSECONDS = 1000000000;
	public static final Logger logger = LoggerFactory.getLogger(OpenCraft.class);
	private static OpenCraft client;

	public final Thread thread;

	public boolean running = false;
	public Render render;
	public Assets assets;

	/**
	 * Creates a instance of the game. This method must be executed once. If you
	 * execute it more times, the game could crash or being completely unusable.
	 */
	OpenCraft() {
		this.assets = Assets.create("/gui.png");
		this.render = Render.create(this.assets);
		this.thread = new Thread(this);
		this.thread.setName("main");
	}

	/**
	 * This method just executes the game. The game will not finish until you have
	 * closed the game's main display or an exception has occurred.
	 */
	@Override
	public void run() {
		/* Initialize the game */
		try {
			init();
		} catch (Exception e) {
			// If any exception has been throwed, create a crash dump and try to report it

			JOptionPane.showMessageDialog(null, "Failed to initialize OpenCraft!", "Initialization Error",
					JOptionPane.ERROR_MESSAGE);

			CrashReport crash = CrashReport.create(e);
			crash.save(new File(CrashReport.generatePath()));

			crash.report();
		}

		// Game status info
		logger.info(String.format("Selected language: %s", Locales.getDisplayName(Locales.getLocale())));
		logger.info(OpenCraft.DISPLAY_NAME + " started!");

		/* The FPS Limiter */
		// (I could use a Timer class or something but is so difficult, I prefer doing
		// this into the run method)
		long lastUpdate = System.nanoTime();

		double timePassed;
		double delta = 0;

		while (running && !Display.shouldClose()) {
			try {
				final long loopStart = System.nanoTime();

				timePassed = loopStart - lastUpdate;
				lastUpdate = loopStart;

				delta += timePassed / this.getNanoPerTick();

				while (delta >= 1) {
					runStep();
					delta--;
				}

			} catch (Throwable tb) {
				SpectoError.process(tb);
			}
		}

		// Finally stops the game
		stop();
	}
	
	public void runStep() {
		boolean properlySize = Display.width() > 136 || Display.height() > 39;
		if (!properlySize)
			return;
		
		tick();
		this.render.render();
	}

	/**
	 * The {@code tick()} method is used to update the game. It can be called
	 * anywhere, just in case that the game is not updating properly.
	 */
	public void tick() {
		SoundManager.update();
		if (icIsKeyPressed(KeyEvent.VK_Q))
			System.out.println("W: " + Display.width() + "; H: " + Display.height());
	}

	/**
	 * The method {@code init()} is used to initialize the game and its
	 * dependencies.
	 *
	 * Is private because it must not be executed twice.
	 */
	private void init() {
		if (running) {
			logger.info("[INIT] Already running!");
			return;
		}

		if (!icInit())
			System.err.println("ERROR: Failed to start OpenIC");
		
		boolean windowsOS = false;
		String os_name = System.getProperty("os.name");
		
		if (os_name != null) {
			os_name = os_name.toLowerCase();
			if (os_name.contains("windows")) {
				windowsOS = true;
				logger.info("Detected OS Windows!");
			} else if (os_name.contains("linux"))
				logger.info("Detected Linux!");
			else if (os_name.contains("macos"))
				logger.info("Detected MacOS!");
		}
		
		if (windowsOS) {
			try {
				for (LookAndFeelInfo info : getInstalledLookAndFeels()) {
					if ("Windows".equals(info.getName())) {
						setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (Exception ex) {
				SpectoError.ignored(ex, getClass());
			}
		}
		
		// Load languages
		Locales.Loader.loadLocales();

		// Read config
		Config.read();

		// Create basics resources
		OCFont.create("/fonts");

		// Initialize sound and render
		SoundManager.init();
		this.render.init();

		// Bind keyboard
		InputManager.bindKeyboard();
		this.running = true;
	}

	public double getNanoPerTick() {
		return (double) NANOSECONDS / Config.FPS_CAP;
	}
	
	@Override
	public void start() {
		this.thread.start();
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
		
		this.running = false;

		// Disable soundManager
		SoundManager.shutdown();

		// Destroy display
		Display.destroy();

		// Stop logging and collect exceptions
		if (InternalLogger.ignoredExceptions > 0) {
			System.err.printf("\n(!): %d ignored exceptions were throwed!\n", InternalLogger.ignoredExceptions);
			try {
				InternalLogger.writeFile();
			} catch (Exception ignored) {
			}
		}

		InternalLogger.stopLogging();

		// Finish the thread
		try {
			thread.stop();
		} catch (Exception e1) {
			try {
				thread.interrupt();
			} catch (Exception e2) {
				e1.printStackTrace();
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
	@Override
	public void stop() {
		this.stop(false);
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
	public static OpenCraft getClient() {
		if (client == null)
			return client = new OpenCraft();
		
		return client;
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

		boolean playedForFirstTime = false;
		if (!new File(Config.DEFAULT_CONFIG_FILE).exists())
			playedForFirstTime = true;

		/* Start the game */

		OpenCraft client = OpenCraft.getClient();
		Thread gameThread = client.thread;
		gameThread.start();

		/* Wait the game to end */

		int status = 0;
		try {
			gameThread.join();
		} catch (Exception ignored) {
			status = 3;
			OpenCraft.logger.error("The game ended with errors!");
		}

		if (playedForFirstTime) {
			System.out.println();
			System.out.println(" ===== Thanks for playing OpenCraft =====");
			System.out.println("   We wish you the best experience with");
			System.out.println("  this game because we are putting all");
			System.out.println("     our efforts to make this game.");
			System.out.println();
			System.out.println("   If you want, you can share this game!");
			System.out.println(" =========== You're welcome!! ===========");
			System.out.println("   - OpenCraft's Developer Team " + Calendar.getInstance().get(Calendar.YEAR));
		}

		// Stops the game
		System.exit(status);
	}

}
