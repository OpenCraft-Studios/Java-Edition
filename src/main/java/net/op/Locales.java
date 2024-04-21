package net.op;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.op.logging.InternalLogger;
import net.op.util.ResourceGetter;

public class Locales {

    private static final Locale[] complete_locales = new Locale[]{Locales.get("en-US"), Locales.get("es-AR"),
        Locales.get("gl-ES")};

    private static final Map<String, String> ENGLISH = new HashMap<>();
    private static final Map<String, String> GALICIAN = new HashMap<>();
    private static final Map<String, String> SPANISH = new HashMap<>();
    private static final Map<String, String> FRENCH = new HashMap<>();
    private static final Map<String, String> ITALIAN = new HashMap<>();
    private static final Map<String, String> CATALAN = new HashMap<>();

    static {
        read(getCSVContent());
    }

    public static void read(final String csvContent) {
        try (BufferedReader reader = new BufferedReader(new StringReader(csvContent))) {
            reader.readLine(); // Skip first line

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty() || line.isBlank()) {
                    continue;
                }

                String[] snippets = line.split(",");
                String key = snippets[0].trim().toLowerCase();

                ENGLISH.put(key, get(snippets, 1, key));
                SPANISH.put(key, get(snippets, 2, key));
                ITALIAN.put(key, get(snippets, 3, key));
                FRENCH.put(key, get(snippets, 4, key));
                GALICIAN.put(key, get(snippets, 5, key));
                CATALAN.put(key, get(snippets, 6, key));

            }

        } catch (Exception ex) {
            InternalLogger.out.println(Locales.class.getName() + " ->");
            ex.printStackTrace(InternalLogger.out);
            InternalLogger.out.println();

            System.err.println("WARNING: Error loading game languages!");
        }

    }

    public static boolean isComplete(Locale locale) {
        return Arrays.stream(complete_locales).anyMatch(l -> l.equals(locale));
    }

    private static String get(String[] snippets, int i, String key) {
        String result = "???";

        try {
            result = snippets[i].trim();

            if (result == null) {
                result = "???";
            }

            if (result.isBlank() || result.isEmpty()) {
                result = "???";
            }
        } catch (Exception ignored) {
        }

        return result;
    }

    public static String translate(String property, Locale language) {
        property = property.toLowerCase();

        if (language.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Spanish")) {
            return SPANISH.getOrDefault(property, property);
        } else if (language.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Galician")) {
            return GALICIAN.getOrDefault(property, property);
        } else if (language.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("French")) {
            return FRENCH.getOrDefault(property, property);
        } else if (language.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Italian")) {
            return ITALIAN.getOrDefault(property, property);
        } else if (language.equals(Locales.get("ca-ES"))) {
            return CATALAN.getOrDefault(property, property);
        } else {
            return ENGLISH.getOrDefault(property, property);
        }
    }

    public static String translate(String property) {
        return translate(property, Config.LOCALE);
    }

    public static Locale get(String languageTag) {
        return Locale.forLanguageTag(languageTag);
    }

    public static String getDisplayName(Locale locale) {
        String text = locale.getDisplayName(Config.LOCALE);
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static String getLanguageName(Locale language) {
        String text = language.getDisplayLanguage(language);
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static String getCSVContent() {
        InputStream in = null;

        try {
            URL csvURL = new URL("https://raw.githubusercontent.com/OpenCraftMC/OnlineResources/main/langsheet.csv");
            in = csvURL.openConnection().getInputStream();
        } catch (Exception ex) {
            InternalLogger.out.println(Locales.class.getName() + " ->");
            ex.printStackTrace(InternalLogger.out);
            InternalLogger.out.println();
        }

        if (in == null) {
            in = ResourceGetter.getInternal("/lang/langsheet.csv");
        }

        String csvContent = "";
        try {
            csvContent = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            InternalLogger.out.println(Locales.class.getName() + " ->");
            ex.printStackTrace(InternalLogger.out);
            InternalLogger.out.println();

            System.err.println("WARNING: Could not read the 'langsheet.csv' file!");
        }

        return csvContent;
    }

}
