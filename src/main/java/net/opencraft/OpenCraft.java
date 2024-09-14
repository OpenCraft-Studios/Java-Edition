package net.opencraft;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.*;

import org.guppy4j.run.Startable;
import org.guppy4j.run.Stoppable;
import org.josl.openic.Keyboard;
import org.josl.openic.Mouse;
import org.lwjgl.opengl.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import joptsimple.*;
import net.opencraft.crash.CrashReport;
import net.opencraft.renderer.DisplayManager;
import net.opencraft.renderer.Renderer;
import net.opencraft.renderer.texture.Assets;
import net.opencraft.sound.SoundManager;
import net.opencraft.spectoland.ILogger;
import net.opencraft.util.FontRenderer;

public final class OpenCraft extends JPanel
		implements Runnable, Startable, Stoppable, ComponentListener {

	/* Static variables... */
	static final Logger logger = LoggerFactory.getLogger(OpenCraft.class);
	public static OpenCraft oc;

	public final Thread thread;

	// Objects
	public Renderer renderer;
	public Assets assets;

	// Settings
	public final File directory;
	public boolean running = false;
	
	// Size
	public int width, height;
	
	// Timers
	public Timer renderTimer;
	public Timer tickTimer;

	/**
	 * Creates a instance of the game. This method must be executed once. If you
	 * execute it more times, the game could crash or being completely unusable.
	 */
	OpenCraft(int width, int height, File directory) {
		this.width = width;
		this.height = height;
		
		this.renderTimer = new Timer(1000 / DisplayManager.getMonitorFramerate(), e -> Display.update());
		this.tickTimer   = new Timer(1000 / 20, e -> tick());
		
		this.directory = directory;
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
		logger.info("Selected language: {}", Locales.getDisplayName(Locales.getLocale()));
		logger.info("OpenCraft {} started!", SharedConstants.VERSION_STRING);

		renderTimer.start();
		tickTimer.start();
	}

	/**
	 * The {@code tick()} method is used to update the game. It can be called
	 * anywhere, just in case that the game is not updating properly.
	 */
	public void tick() {
		running &= !Display.isCloseRequested();
		if (!running) {
			this.stop();
			return;
		}
		
		SoundManager.update();
	}

	/**
	 * The method {@code init()} is used to initialize the game and its
	 * dependencies.
	 *
	 * Is private because it must not be executed twice externally.
	 */
	private void init() {
		// Read config
		GameSettings.read();

		// Configure font and translations
		Locales.Loader.loadLocales();
		FontRenderer.create("/fonts");

		// Create basics resources
		this.assets = Assets.create("/gui.png");
		this.renderer = Renderer.create();

		DisplayManager.create(this, width, height);
		DisplayManager.addCanvas(this);
		Mouse.create(this);
		Keyboard.create(this);
		
		// Initialize sound and render
		this.renderer.init();
		SoundManager.init();

		this.running = true;
	}

	@Override
	public void start() {
		this.thread.start();
	}

	/**
	 * This method is used to stop the game. Normally this is executed at the end of
	 * the game, but you can still use it for stop it whenever you want.
	 */
	public void stop() {
		this.running = false;

		tickTimer.stop();
		renderTimer.stop();
		
		// Disable soundManager
		SoundManager.shutdown();

		// Destroy display
		Display.destroy();

		// Stop logging and collect exceptions
		if (ILogger.iex > 0)
			System.err.printf("\n(!): %d ignored exceptions were throwed!\n", ILogger.iex);

		ILogger.stopLogging();
		System.exit(0);

	}
	
	/**
	 * Renders the game.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		renderer.render(g2d);
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		oc.width = Display.getWidth();
		oc.height = Display.getHeight();
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
		GameSettings.LEGACY_CONFIG = argSet.has(legacyFlag);

		if (argSet.has(uiScaleArgument)) {
			logger.warn("[CUSTOM UI SCALE] Use custom ui scales can cause the game to display wrong some objects!");
			System.setProperty("sun.java2d.uiScale", (String) argSet.valueOf(uiScaleArgument));
		} else
			System.setProperty("sun.java2d.uiScale", "1.0"); // Default

		if (argSet.has(usernameArgument))
			logger.info("Setting user {}", argSet.valueOf(usernameArgument));

		if (argSet.has(gameDirArgument))
			gameDir = new File((String) argSet.valueOf(gameDirArgument));

		if (argSet.has(configFileArgument))
			GameSettings.DEF_CONFIG = (String) argSet.valueOf(configFileArgument);
		else
			GameSettings.DEF_CONFIG = new File(gameDir, "options.txt").getPath();
		
		/* Start the game */
		OpenCraft.oc = new OpenCraft(854, 480, gameDir);
		oc.thread.start();
	}

	public void componentMoved(ComponentEvent e)  {}
	public void componentShown(ComponentEvent e)  {}
	public void componentHidden(ComponentEvent e) {}

}
