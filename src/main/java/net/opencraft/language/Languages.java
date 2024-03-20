package net.opencraft.language;

import java.util.Locale;

public class Languages {

	public static Locale get(String languageTag) {
		return Locale.forLanguageTag(languageTag);
	}
	
	public static String getDisplayName(Locale locale) {
		String name = locale.getDisplayName(locale);
		
		if (locale.equals(Languages.get("du-DU")))
			name = "Dullutiano (Dulutia)";
		
		return name;
	}

}
