package net.op;

import static net.op.OpenCraft.logger;
import static net.op.OpenCraft.oc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import net.op.render.screens.F3Screen;
import net.op.sound.SoundManager;
import net.op.spectoland.SpectoError;

/**
 * <h1>Config</h1> The Config class contains every setting in the game. This
 * class can be converted to a {@code Properties} object that can be readed and
 * saved to a file.
 */
public class GameSettings {

	public static final String OFFICIAL_WEBPAGE = "https://opencraftstudios.github.io";
	public static final String ONLINE_LANGSHEET = "https://raw.githubusercontent.com/OpenCraftStudios/piston-data/main/langsheet.csv";
	
	public static String DEF_CONFIG;

	/**
	 * This method converts the settings to a {@code Properties} object.
	 *
	 * @return A properties object
	 */
	public static Properties toProperties() {
		Properties properties = new Properties();

		properties.setProperty("lang", Locales.getLocale().toLanguageTag());
		if (oc.legacyCnf)
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
		F3Screen.setStatus("Reading settings...");
		Locales.setLocale(Locales.of((String) properties.getOrDefault("lang", "en-US")));
		if (oc.legacyCnf)
			SoundManager.MUSIC = Boolean.parseBoolean((String) properties.getOrDefault("music", "false"));
		else
			SoundManager.MUSIC = Double.parseDouble((String) properties.getOrDefault("soundCategory_music", "0.0")) > 0;
		F3Screen.setStatus("Settings readed!");
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
			logger.warn("Failed to load game settings!");
			SpectoError.ignored(ex, GameSettings.class);
		}

		GameSettings.read(gameSettings);
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
		read(DEF_CONFIG);
	}

	/**
	 * This method saves the actual configuration to the default file.
	 */
	public static void save(BufferedWriter writer) throws IOException {
		Set<Entry<Object, Object>> entries = GameSettings.toProperties().entrySet();

		for (Entry<Object, Object> entry : entries) {
			writer.write((String) entry.getKey() + ":" + (String) entry.getValue());
			writer.write("\n");
		}

		writer.close();
		F3Screen.setStatus("Saving settings...");
	}

	/**
	 * This method saves the actual configuration to the default file.
	 */
	public static void save(File file) {
		try {
			save(new BufferedWriter(new FileWriter(file)));
		} catch (Exception ex) {
			logger.warn("Failed to save game settings!");
			SpectoError.ignored(ex, GameSettings.class);
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
		save(DEF_CONFIG);
	}

	/**
	 * It's used to set the current game directory.
	 *
	 * @param gameDir Game's directory
	 */
	public static void setDirectory(String gameDir) {
		oc.directory = gameDir;
	}

	/**
	 * Returns the game directory
	 *
	 * @return The game directory
	 *
	 * @see #setDirectory(String)
	 */
	public static String getDirectory() {
		return oc.directory;
	}

}
