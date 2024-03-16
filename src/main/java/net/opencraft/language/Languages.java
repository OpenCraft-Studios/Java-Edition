package net.opencraft.language;

import java.util.Locale;

public class Languages {

	public static Locale get(String languageTag) {
		return Locale.forLanguageTag(languageTag);
	}

}
