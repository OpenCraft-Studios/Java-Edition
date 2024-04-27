package net.op;

import static net.op.Client.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import net.op.language.Locales;
import net.op.sound.SoundManager;

/**
 * <h1>Config</h1> The Config class contains every setting in the game. This
 * class can be converted to a {@code Properties} object that can be readed and
 * saved to a file.
 */
public class Config {

	/**
	 * The game's directory
	 */
	public static String GAME_DIRECTORY = "opcraft";

	/**
	 * Maximum FPS that the game will consume.
	 */
	public static int FPS_CAP = 70;

	/**
	 * Indicates whether or not save config files with the legacy format.
	 */
	public static boolean LEGACY = false;

	/**
	 * This method converts the settings to a {@code Properties} object.
	 *
	 * @return A properties object
	 */
	public static Properties toProperties() {
		Properties properties = new Properties();

		properties.setProperty("lang", Locales.getLocale().toLanguageTag());
		if (LEGACY)
			properties.setProperty("music", Boolean.toString(SoundManager.MUSIC));
		else
			properties.setProperty("soundCategory_music", Double.toString(SoundManager.getVolume()));

		return properties;
	}

	/**
	 * This method reads the configuration from a properties object
	 *
	 * @param properties The object to read
	 */
	public static void read(Properties properties) {
		Locales.setLocale(Locales.of((String) properties.getOrDefault("lang", "en-US")));
		if (LEGACY)
			SoundManager.MUSIC = Boolean.parseBoolean((String) properties.getOrDefault("music", "false"));
		else
			SoundManager.MUSIC = Double.parseDouble((String) properties.getOrDefault("soundCategory_music", "0.0")) > 0;
	}

	/**
	 * This method reads the configuration written in the default file to apply it.
	 */
	public static void read(File gameSettingsFile) {
		if (!gameSettingsFile.exists())
			return;

		Properties gameSettings = new Properties();
		try {
			gameSettings.load(new FileInputStream(gameSettingsFile));
		} catch (Exception ex) {
			logger.warning("Failed to load game settings!");
			InternalLogger.out.println(Config.class.getName() + " ->");
			ex.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();
		}

		Config.read(gameSettings);
	}

	/**
	 * This method reads the configuration written in the default file to apply it.
	 */
	public static void read(String filepath) {
		read(new File(filepath));
	}

	/**
	 * This method reads the configuration written in the default file to apply it.
	 */
	public static void read() {
		read(GAME_DIRECTORY + "/options.txt");
	}

	/**
	 * This method saves the actual configuration to the default file.
	 */
	public static void save(BufferedWriter writer) throws IOException {
		var entries = Config.toProperties().entrySet();

		for (Entry<Object, Object> entry : entries) {
			writer.write((String) entry.getKey() + ":" + (String) entry.getValue());
			writer.write("\n");
		}

		writer.close();
	}

	/**
	 * This method saves the actual configuration to the default file.
	 */
	public static void save(File file) {
		try {
			save(new BufferedWriter(new FileWriter(file)));
		} catch (Exception ex) {
			logger.warning("Failed to save game settings!");
			InternalLogger.out.println(Config.class.getName() + " ->");
			ex.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();
		}
	}

	/**
	 * This method saves the actual configuration to the default file.
	 */
	public static void save(String filepath) {
		save(new File(filepath));
	}

	/**
	 * This method saves the actual configuration to the default file.
	 */
	public static void save() {
		save(GAME_DIRECTORY + "/options.txt");
	}

	/**
	 * It's used to set the current game directory.
	 *
	 * @param gameDir Game's directory
	 */
	public static void setDirectory(String gameDir) {
		Config.GAME_DIRECTORY = gameDir;
	}

	/**
	 * Returns the game directory
	 *
	 * @return The game directory
	 *
	 * @see #setDirectory(String)
	 */
	public static String getDirectory() {
		return Config.GAME_DIRECTORY;
	}

}
