package net.op.language;

import java.util.Arrays;
import java.util.Locale;

public class Locales {

	public static String[] ENGLISH;
	public static String[] SPANISH;
	public static String[] ITALIAN;
	public static String[] FRENCH;
	public static String[] GALICIAN;
	public static String[] CATALAN;
	
	public static String[] CURRENT;

	public static Locale of(String localeTag) {
		return Locale.forLanguageTag(localeTag);
	}

	public static String getDisplayName(Locale locale) {
		String text = locale.getDisplayName(locale);
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}

	public static String getLanguageName(Locale locale) {
		String text = locale.getDisplayLanguage(locale);
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}
	
	public static String getGenericName(Locale locale) {
		return locale.getDisplayLanguage(Locale.ENGLISH);
	}
	
	public static String[] getLocaleTranslations(Locale locale) {
		String langName = getGenericName(locale);
		
		if (langName.equalsIgnoreCase("Spanish")) {
			return SPANISH;
		} else if (langName.equalsIgnoreCase("Italian")) {
			return ITALIAN;
		} else if (langName.equalsIgnoreCase("French")) {
			return FRENCH;
		} else if (langName.equalsIgnoreCase("Galician")) {
			return GALICIAN;
		} else if (langName.equalsIgnoreCase("Catalan")) {
			return CATALAN;
		}
		
		return ENGLISH;
	}
	
	public static void setLocale(Locale locale) {
		CURRENT = getLocaleTranslations(locale);
	}
	
	public static Locale getLocale() {
		if (Arrays.equals(CURRENT, SPANISH))
			return Locales.of("es-AR");
		else if (Arrays.equals(CURRENT, FRENCH))
			return Locales.of("fr-FR");
		else if (Arrays.equals(CURRENT, ITALIAN))
			return Locales.of("it-IT");
		else if (Arrays.equals(CURRENT, GALICIAN))
			return Locales.of("gl-ES");
		else if (Arrays.equals(CURRENT, CATALAN))
			return Locales.of("ca-ES");
		
		return Locales.of("en-US");
	}

}
