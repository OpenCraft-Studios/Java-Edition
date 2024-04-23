package net.op.language;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import net.op.InternalLogger;
import net.op.util.ResourceGetter;

public class LocalesLoader {

	private LocalesLoader() {
	}
	
	public static void load() {
		final String csvContents = readOnlineCSV().orElse(readInternalCSV().get());
		loadFromString(csvContents);
	}

	public static void loadFromString(final String csvContent) {
		List<String> english = new LinkedList<>();
		List<String> spanish = new LinkedList<>();
		List<String> italian = new LinkedList<>();
		List<String> french = new LinkedList<>();
		List<String> galician = new LinkedList<>();
		List<String> catalan = new LinkedList<>();

		try (BufferedReader reader = new BufferedReader(new StringReader(csvContent))) {
			reader.readLine(); // Skip first line

			String line;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty() || line.isBlank()) {
					continue;
				}

				String[] snippets = line.split(",");
				String key = snippets[0];

				english.add(get(snippets, 1, key));
				spanish.add(get(snippets, 2, key));
				italian.add(get(snippets, 3, key));
				french.add(get(snippets, 4, key));
				galician.add(get(snippets, 5, key));
				catalan.add(get(snippets, 6, key));
			}

		} catch (Exception ex) {
			InternalLogger.out.println(LocalesLoader.class.getName() + " ->");
			ex.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();

			System.err.println("WARNING: Error loading game languages!");
		}

		Locales.ENGLISH = english.toArray(new String[0]);
		Locales.SPANISH = spanish.toArray(new String[0]);
		Locales.ITALIAN = italian.toArray(new String[0]);
		Locales.FRENCH = french.toArray(new String[0]);
		Locales.GALICIAN = galician.toArray(new String[0]);
		Locales.CATALAN = catalan.toArray(new String[0]);
		
		Locales.CURRENT = Locales.ENGLISH;
	}

	private static String get(String[] snippets, int i, String key) {
		Optional<String> translation = Optional.empty();
		String rawTranslation;

		try {
			rawTranslation = snippets[i];

			if (!(rawTranslation.isBlank() || rawTranslation.isEmpty())) {
				translation = Optional.ofNullable(rawTranslation);
			}
		} catch (Exception ignored) {
		}

		return translation.orElse("???");
	}

	private static InputStream getCSVOnline() {
		InputStream in = null;

		try {
			URL csvURL = new URL("https://raw.githubusercontent.com/OpenCraftMC/OnlineResources/main/langsheet.csv");
			in = csvURL.openConnection().getInputStream();
		} catch (Exception ex) {
			InternalLogger.out.println(LocalesLoader.class.getName() + " ->");
			ex.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();
		}

		return in;
	}

	private static InputStream getInternalCSV() {
		return ResourceGetter.getInternal("/lang/langsheet.csv");
	}

	private static Optional<String> readCSV(InputStream in) {
		Optional<String> contents = Optional.empty();

		try {
			contents = Optional.of(new String(in.readAllBytes(), StandardCharsets.UTF_8));
		} catch (Exception ex) {
			InternalLogger.out.println(Locales.class.getName() + " ->");
			ex.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();
		}

		return contents;
	}

	private static Optional<String> readOnlineCSV() {
		return readCSV(getCSVOnline());
	}

	private static Optional<String> readInternalCSV() {
		return readCSV(getInternalCSV());
	}

}
