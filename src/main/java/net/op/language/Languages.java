package net.op.language;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import net.op.Client;
import net.op.render.textures.Assets;

public class Languages {

	private static final Locale[] locales = new Locale[] { Languages.get("en-US"), Languages.get("es-AR"),
			Languages.get("it-IT"), Languages.get("fr-FR"), Languages.get("gl-ES"), Languages.get("ca-ES") };
	
	private static final Map<String, String> ENGLISH = new HashMap<>();
	private static final Map<String, String> GALICIAN = new HashMap<>();
	private static final Map<String, String> SPANISH = new HashMap<>();
	private static final Map<String, String> FRENCH = new HashMap<>();
	private static final Map<String, String> ITALIAN = new HashMap<>();
	private static final Map<String, String> CATALAN = new HashMap<>();

	static {
		read("/lang/es_AR.lang", SPANISH);
		read("/lang/gl_ES.lang", GALICIAN);
		read("/lang/en_US.lang", ENGLISH);
		read("/lang/it_IT.lang", ITALIAN);
		read("/lang/fr_FR.lang", FRENCH);
		read("/lang/ca_ES.lang", CATALAN);
	}

	public static void read(String filepath, Map<String, String> lang) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				Assets.forResources(Client.getResourcePack()).bindOrDefault("/assets/opencraft" + filepath)));

		try {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty() || line.isBlank())
					continue;
				
				String key = line.split("=")[0].trim();
				String value = line.split("=")[1].trim();

				lang.put(key.toLowerCase(), StringEscapeUtils.unescapeJava(value));
			}
		} catch (Exception ignored) {
			// TODO Internal logger
		}

	}

	public static String translate(String property, Locale language) {
		property = property.toLowerCase();

		if (language.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Spanish"))
			return SPANISH.getOrDefault(property, property);
		else if (language.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Galician"))
			return GALICIAN.getOrDefault(property, property);
		else if (language.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("French"))
			return FRENCH.getOrDefault(property, property);
		else if (language.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Italian"))
			return ITALIAN.getOrDefault(property, property);
		else if (language.equals(Languages.get("ca-ES")))
			return CATALAN.getOrDefault(property, property);
		else
			return ENGLISH.getOrDefault(property, property);

	}

	public static String translate(String property) {
		return translate(property, Client.getLanguage());
	}

	public static Locale get(String languageTag) {
		return Locale.forLanguageTag(languageTag);
	}

	public static String getDisplayName(Locale locale) {
		return StringUtils.capitalize(locale.getDisplayName(Client.getLanguage()));
	}

	public static Locale getLocaleByIndex(int index) {
		return locales[index];
	}

	public static int indexOf(Locale language) {
		return Arrays.asList(locales).indexOf(language);
	}
	
	public static int getLocaleIndexByLanguage(String language) {
		if (language.equalsIgnoreCase("Spanish"))
			return 1;
		else if (language.equalsIgnoreCase("Italian"))
			return 2;
		else if (language.equalsIgnoreCase("French"))
			return 3;
		else if (language.equalsIgnoreCase("Galician"))
			return 4;
		else
			return 0;
	}
	
	public static Locale getLocaleByLanguage(String language) {
		return getLocaleByIndex(getLocaleIndexByLanguage(language));
	}

	public static String getLanguageName(Locale language) {
		return StringUtils.capitalize(language.getDisplayLanguage(language));
	}

}
