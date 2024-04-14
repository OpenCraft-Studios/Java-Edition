package net.op;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Properties;
import static net.op.Client.logger;

public class Config {

    public static Locale LOCALE = Locale.ENGLISH;
    public static int FPS_CAP = 70;

    public static String DIRECTORY = "opcraft";

    public static Properties toProperties() {
        Properties properties = new Properties();

        properties.setProperty("gameDir", DIRECTORY);
        properties.setProperty("lang", LOCALE.toLanguageTag());

        return properties;
    }

    public static void read(Properties properties) {
        DIRECTORY = properties.getProperty("gameDir");
        LOCALE = Locales.get(properties.getProperty("lang"));
    }

    public static void read() {
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
    }

    public static void save() {
        Properties gameSettings = Config.toProperties();
        try {
            gameSettings.store(new FileOutputStream(Config.DIRECTORY + "/settings.yml"), "Game Settings");
        } catch (Exception ignored) {
            logger.warning("Failed to save game settings!");
            // TODO Internal logger
        }
    }

    /**
     * This method sets the language of the game to a defined one.
     */
    public static void setLocale(Locale locale) {
        Config.LOCALE = locale;
    }

    /**
     * The {@code getLanguage()} method is used to get the current language used
     * by the game.
     */
    public static Locale getLocale() {
        return Config.LOCALE;
    }

    /**
     * It's used to set the current game directory.
     *
     * @param gameDir Game's directory
     */
    public static void setDirectory(String gameDir) {
        Config.DIRECTORY = gameDir;
    }

    /**
     * Returns the game directory
     *
     * @return The game directory
     *
     * @see #setDirectory(String)
     */
    public static String getDirectory() {
        return Config.DIRECTORY;
    }

}
