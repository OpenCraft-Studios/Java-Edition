package net.op;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class OpenCraft {

	private static final Client client = new Client();

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

		Client game = OpenCraft.getClient();
		Thread gameThread = game.thread;
		gameThread.start();

		/* Wait the game to end */

		int status = 0;
		try {
			gameThread.join();
		} catch (Exception ignored) {
			status = 3;
			Client.logger.error("The game ended with errors!");
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
