package net.opencraft;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import net.opencraft.spectoland.SpectoError;
import net.opencraft.util.Files;

public class Locales {

	private static final Map<String, String> ENGLISH = new HashMap<>();
	private static final Map<String, String> GALICIAN = new HashMap<>();
	private static final Map<String, String> SPANISH = new HashMap<>();
	private static final Map<String, String> FRENCH = new HashMap<>();
	private static final Map<String, String> ITALIAN = new HashMap<>();
	private static final Map<String, String> CATALAN = new HashMap<>();
	private static final Map<String, String> PORTUGUESE = new HashMap<>();

	private static Locale locale;

	public static class Loader {

		public static void loadLocales() {
			parse(getCSVOnline().orElse(getInternalCSV()));
		}

		public static void parse(final InputStream in) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
				// Skip first line
				reader.readLine();

				String line;
				while ((line = reader.readLine()) != null) {
					try (Scanner sc = new Scanner(line)) {
						if (!sc.hasNext())
							continue;

						sc.useDelimiter(",");

						final String key = sc.next().toLowerCase();

						ENGLISH.put(key, next(sc));
						SPANISH.put(key, next(sc));
						ITALIAN.put(key, next(sc));
						FRENCH.put(key, next(sc));
						GALICIAN.put(key, next(sc));
						CATALAN.put(key, next(sc));
						PORTUGUESE.put(key, next(sc));
					}
				}

			} catch (Exception ex) {
				System.err.println("WARNING: Error loading game languages!");
				SpectoError.ignored(ex, Loader.class);
			}
		}

		private static String next(Scanner sc) {
			Optional<String> translation = Optional.empty();

			try {
				String raw = sc.next();
				if (!raw.isEmpty())
					translation = Optional.of(raw.trim());
			} catch (Exception ignored) {
			}

			return translation.orElse("???");
		}

		private static Optional<InputStream> getCSVOnline() {
			Optional<InputStream> opstream = Optional.empty();

			try {
				URI csvURI = new URI(GameSettings.ONLINE_LANGSHEET);
				InputStream in = csvURI.toURL().openStream();

				opstream = Optional.ofNullable(in);
			} catch (Exception ex) {
				SpectoError.ignored(ex, Loader.class);
			}

			return opstream;
		}

		private static InputStream getInternalCSV() {
			return Files.internal("/lang/langsheet.csv");
		}

	}

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

	public static void setLocale(Locale locale) {
		Locales.locale = locale;
	}

	public static Locale getLocale() {
		return locale;
	}

	public static String translate(String resource, final Locale locale) {
		final String _def = "???";
		final String lname = getGenericName(locale);
		
		resource = resource.trim().toLowerCase();
		Map<String, String> translations = ENGLISH;
		
		if (lname.equalsIgnoreCase("Spanish"))
			translations = SPANISH;
		else if (lname.equalsIgnoreCase("Italian"))
			translations = ITALIAN;
		else if (lname.equalsIgnoreCase("French"))
			translations = FRENCH;
		else if (lname.equalsIgnoreCase("Galician"))
			translations = GALICIAN;
		else if (lname.equalsIgnoreCase("Catalan"))
			translations = CATALAN;
		else if (lname.equalsIgnoreCase("Portuguese"))
			translations = PORTUGUESE;
		
		return translations.getOrDefault(resource, _def);
	}
	
	public static String translate(String resource) {
		return translate(resource, locale);
	}
	
}
