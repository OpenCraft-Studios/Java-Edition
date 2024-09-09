package net.opencraft;

import static org.josl.openic.IC15.*;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.JOptionPane;

import org.guppy4j.run.Startable;
import org.guppy4j.run.Stoppable;
import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import joptsimple.*;
import net.opencraft.crash.CrashReport;
import net.opencraft.renderer.Renderer;
import net.opencraft.renderer.texture.Assets;
import net.opencraft.sound.SoundManager;
import net.opencraft.spectoland.ILogger;
import net.opencraft.spectoland.SpectoError;
import net.opencraft.util.FontRenderer;

public final class OpenCraft implements Runnable, Startable, Stoppable {

	/* Static variables... */

	public static final String NAME = "OpenCraft";
	public static final String VERSION = "24r11";
	public static final String TECHNICAL_NAME = "System Update 3";
	public static final String DISPLAY_NAME = NAME + ' ' + VERSION;

	public static final int NANOSECONDS = 1000000000;
	public static final Logger logger = LoggerFactory.getLogger(OpenCraft.class);
	public static OpenCraft oc;

	public final Thread thread;

	// Objects
	public Renderer render;
	public Assets assets;

	// Settings
	public boolean running = false;
	public boolean legacyCnf = false;
	public int fpsCap = 70;
	public final File directory;

	/**
	 * Creates a instance of the game. This method must be executed once. If you
	 * execute it more times, the game could crash or being completely unusable.
	 */
	OpenCraft(File directory, boolean legacyCnf) {
		this.directory = directory;
		this.legacyCnf = legacyCnf;

		this.thread = new Thread(this);
		this.thread.setName("oc-thread");
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
	}

	/**
	 * The method {@code init()} is used to initialize the game and its
	 * dependencies.
	 *
	 * Is private because it must not be executed twice.
	 */
	private void init() {
		if (running) {
			logger.info("#inittask Already running!");
			return;
		}

		if (!icInit())
			throw new IllegalStateException("Failed to start OpenIC");

		// Read config
		GameSettings.read();

		// Configure font and translations
		Locales.Loader.loadLocales();
		FontRenderer.create("/fonts");

		// Create basics resources
		this.assets = Assets.create("/gui.png");
		this.render = Renderer.create(this.assets);

		// Initialize sound and render
		this.render.init();
		SoundManager.init();

		// Bind keyboard
		InputHandler.bindKeyboard();

		this.running = true;
	}

	public double getNanoPerTick() {
		return (double) NANOSECONDS / (double) fpsCap;
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
		if (ILogger.iex > 0)
			System.err.printf("\n(!): %d ignored exceptions were throwed!\n", ILogger.iex);

		ILogger.stopLogging();

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
	 * <b>Main method</b><br>
	 * Is the principal method that guides the: initialization, running and the stop
	 * of the game. If you want to create your own OpenCraft launcher, we recommend
	 * delete this method and compile this game without it because you can guide the
	 * executing and monitoring of the game.
	 */
	public static void main(String[] args) {
		// Parse arguments
		OptionParser parser = new OptionParser();
		
		File gameDir = new File("opcraft");
		boolean legacyCnf;
		
		

		/*
		 * Compatibility for Minecraft launchers. I will not convert this into a pay
		 * game
		 */
		parser.accepts("demo");
		parser.accepts("sessionId");
		parser.accepts("tokenId");
		parser.accepts("token");
		parser.accepts("authsessionid");

		OptionSpec<?> legacyFlag = parser.accepts("legacy");
		OptionSpec<?> gameDirArgument = parser.accepts("gameDir").withRequiredArg();
		OptionSpec<?> configFileArgument = parser.acceptsAll(Arrays.asList("cnf", "conf", "config")).withRequiredArg();
		OptionSpec<?> uiScaleArgument = parser.acceptsAll(Arrays.asList("scale", "uiscale")).withRequiredArg();
		OptionSpec<?> usernameArgument = parser.accepts("username");

		OptionSet argSet = parser.parse(args);
		legacyCnf = argSet.has(legacyFlag);

		if (argSet.has(uiScaleArgument)) {
			logger.warn("[CUSTOM UI SCALE] Use custom ui scales can cause the game to display wrong some objects!");
			System.setProperty("sun.java2d.uiScale", (String) argSet.valueOf(uiScaleArgument));
		} else
			System.setProperty("sun.java2d.uiScale", "1.0"); // Default

		if (argSet.has(usernameArgument))
			logger.info("Setting user {}", argSet.valueOf(usernameArgument));

		if (argSet.has(gameDirArgument))
			gameDir = new File((String) argSet.valueOf(gameDirArgument));

		GameSettings.DEF_CONFIG = new File(gameDir, "options.txt").getPath();

		if (argSet.has(configFileArgument))
			GameSettings.DEF_CONFIG = (String) argSet.valueOf(configFileArgument);

		/* Start the game */
		OpenCraft.oc = new OpenCraft(gameDir, legacyCnf);
		oc.thread.start();

		/* Wait the game to end */

		int status = 0;
		try {
			oc.thread.join();
		} catch (Exception ignored) {
			status = 3;
			logger.error("The game ended with errors!");
		}

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
