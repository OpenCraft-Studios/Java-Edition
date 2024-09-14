package net.opencraft;

import static net.opencraft.OpenCraft.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import net.opencraft.renderer.screens.F3Screen;
import net.opencraft.sound.SoundManager;
import net.opencraft.spectoland.SpectoError;

import java.util.Properties;
import java.util.Set;

/**
 * <h1>GameSettings</h1> The GameSettings class contains every setting in the game. This
 * class can be converted to a {@code Properties} object that can be readed and
 * saved to a file.
 */
public class GameSettings {

	public static String DEF_CONFIG;
	public static boolean LEGACY_CONFIG = false;

	/**
	 * This method converts the settings to a {@code Properties} object.
	 *
	 * @return A properties object
	 */
	public static Properties toProperties() {
		Properties properties = new Properties();

		properties.setProperty("lang", Locales.getLocale().toLanguageTag());
		if (LEGACY_CONFIG)
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
		if (LEGACY_CONFIG)
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


}
