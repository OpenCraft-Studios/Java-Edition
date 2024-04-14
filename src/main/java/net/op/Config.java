package net.op;

import java.util.Locale;
import java.util.Properties;

import net.op.data.packs.Pack;

public class Config {

    public static Locale LOCALE = Locale.ENGLISH;
    public static int FPS_CAP = 70;

    public static String DIRECTORY = "opcraft";
    public static Pack PACK = Pack.getDefaultPack();

    public static Properties toProperties() {
        Properties properties = new Properties();

        properties.setProperty("gameDir", DIRECTORY);
        properties.setProperty("lang", LOCALE.toLanguageTag());

        String strPack = "another";
        if (PACK.equals(Pack.getDefaultPack())) {
            strPack = "default";
        } else if (PACK.equals(Pack.getInternalPack())) {
            strPack = "internal";
        }

        properties.setProperty("respack", strPack);

        return properties;
    }

    public static void read(Properties properties) {
        DIRECTORY = properties.getProperty("gameDir");
        LOCALE = Locales.get(properties.getProperty("lang"));
        String strPack = properties.getProperty("respack");

        if (strPack.equalsIgnoreCase("default")) {
            PACK = Pack.getDefaultPack();
        } else if (strPack.equalsIgnoreCase("internal")) {
            PACK = Pack.getInternalPack();
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

    /**
     * Returns the current resource pack used by the game.
     *
     * @return The resource pack used by the game
     */
    public static Pack getPack() {
        return Config.PACK;
    }

    /**
     * Sets the resource pack to a specific one.
     *
     * @param pack the resource pack
     */
    public static void setPack(Pack pack) {
        Config.PACK = pack;
    }

}
