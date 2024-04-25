package net.op;

import static net.op.render.display.DisplayManager.destroyDisplay;
import static net.op.render.display.DisplayManager.isDisplayAlive;
import static org.josl.openic.IC13.icIsKeyPressed;

import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Calendar;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import net.op.crash.CrashReport;
import net.op.input.InputManager;
import net.op.language.Locales;
import net.op.language.LocalesLoader;
import net.op.render.Render;
import net.op.render.textures.GUITilesheet;
import net.op.sound.SoundManager;
import net.op.util.OCFont;

public final class Client implements Runnable {

	public static final String NAME = "OpenCraft";
	public static final String VERSION = "24r09";
	public static final String CODENAME = "StyleFML";
	public static final String DISPLAY_NAME = NAME + ' ' + VERSION;

	public static final int NANOSECONDS = 1000000000;

	private static final Client instance = new Client();
	public static final Logger logger = Logger.getLogger(Client.class.getName());

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
		this.thread.setName("gameThread");
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

		// This is the "exception counter", if this reach 5, this will stop the game.
		byte exceptionCounter = 0;
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
				 * We have a variable called "exceptionCounter" that counts the exceptions that
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

		// Initialize loggers & Render System
		LoggerConfig.init();
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

	public void vsync() throws Exception {
		int fpsRate;
		fpsRate = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode()
				.getRefreshRate();

		
		if (fpsRate != DisplayMode.REFRESH_RATE_UNKNOWN) {
			Config.FPS_CAP = fpsRate;
		} else {
			throw new Exception("Imposible to determinate the refresh rate!");
		}
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
		if (force) {
			System.exit(0);
		}

		System.out.println(InternalLogger.getOutputData());

		// Stop
		this.running = false;

		// Save settings
		Config.save();

		// Destroy display
		destroyDisplay();

		// Stop logging and collect traces
		InternalLogger.stopLogging();

		// Finish the thread
		try {
			// InternalLogger.writeFile();
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
		// Starting game
		Client game = Client.getClient();
		Thread gameThread = game.thread();
		gameThread.start();

		int status = 0;
		try {
			gameThread.join();
		} catch (Exception ignored) {
			status = 3;
			logger.severe("The game ended with errors!");
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
