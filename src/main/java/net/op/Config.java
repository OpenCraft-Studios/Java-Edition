package net.op;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Properties;
import static net.op.Client.logger;
import net.op.logging.InternalLogger;

/**
 * <h1>Config</h1>
 * The Config class contains every setting in the game. This class can be
 * converted to a {@code Properties} object that can be readed and saved to a
 * file.
 */
public class Config {

    /**
     * The game's language
     */
    public static Locale LOCALE = Locale.ENGLISH;

    /**
     * The game's directory
     */
    public static String GAME_DIRECTORY = "opcraft";

    /**
     * Maximum FPS that the game will consume.
     */
    public static int FPS_CAP = 70;

    /**
     * This method converts the settings to a {@code Properties} object.
     *
     * @return A properties object
     */
    public static Properties toProperties() {
        Properties properties = new Properties();

        properties.setProperty("gameDir", GAME_DIRECTORY);
        properties.setProperty("lang", LOCALE.toLanguageTag());

        return properties;
    }

    /**
     * This method reads the configuration from a properties object
     *
     * @param properties The object to read
     */
    public static void read(Properties properties) {
        GAME_DIRECTORY = properties.getProperty("gameDir");
        LOCALE = Locales.get(properties.getProperty("lang"));
    }

    /**
     * This method reads the configuration written in the default file to apply
     * it.
     */
    public static void read() {
        File gameSettingsFile = new File(Config.GAME_DIRECTORY + "/settings.yml");
        if (gameSettingsFile.exists()) {
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
    }

    /**
     * This method saves the actual configuration to the default file.
     */
    public static void save() {
        Properties gameSettings = Config.toProperties();
        try {
            gameSettings.store(new FileOutputStream(Config.GAME_DIRECTORY + "/settings.yml"), "Game Settings");
        } catch (Exception ex) {
            logger.warning("Failed to save game settings!");
            InternalLogger.out.println(Config.class.getName() + " ->");
            ex.printStackTrace(InternalLogger.out);
            InternalLogger.out.println();
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
     *
     * @return Game's language
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
