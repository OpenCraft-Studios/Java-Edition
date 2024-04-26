package net.op;

import static net.op.Client.logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import net.op.language.Locales;
import net.op.sound.SoundManager;

/**
 * <h1>Config</h1>
 * The Config class contains every setting in the game. This class can be
 * converted to a {@code Properties} object that can be readed and saved to a
 * file.
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
     * This method converts the settings to a {@code Properties} object.
     *
     * @return A properties object
     */
    public static Properties toProperties() {
        Properties properties = new Properties();

        properties.setProperty("gameDir", GAME_DIRECTORY);
        properties.setProperty("language", Locales.getLocale().toLanguageTag());
        properties.setProperty("music", Boolean.toString(SoundManager.MUSIC));
        
        return properties;
    }

    /**
     * This method reads the configuration from a properties object
     *
     * @param properties The object to read
     */
    public static void read(Properties properties) {
        GAME_DIRECTORY = properties.getProperty("gameDir");
        Locales.setLocale(Locales.of(properties.getProperty("language")));
        SoundManager.MUSIC = Boolean.parseBoolean(properties.getProperty("music"));
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
