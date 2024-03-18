package net.opencraft.language;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.opencraft.config.GameConfig;
import net.opencraft.util.Resource;

public class Translator {

	private static final Map<String, String> ENGLISH = new HashMap<>();
	private static final Map<String, String> GALICIAN = new HashMap<>();
	private static final Map<String, String> SPANISH = new HashMap<>();
	
	static {
		
		// English translation
		ENGLISH.put("opencraft.button:singleplayer", "Singleplayer");
		ENGLISH.put("opencraft.button:multiplayer", "Multiplayer");
		ENGLISH.put("opencraft.button:settings", "Settings");
		ENGLISH.put("opencraft.button:quit", "Quit Game");
		
		// Galician translation
		GALICIAN.put("opencraft.button:singleplayer", "Un xogador");
		GALICIAN.put("opencraft.button:multiplayer", "Multixogador");
		GALICIAN.put("opencraft.button:settings", "Configuración");
		GALICIAN.put("opencraft.button:quit", "Saír");
		
		// Spanish translation
		SPANISH.put("opencraft.button:singleplayer", "Un jugador");
		SPANISH.put("opencraft.button:multiplayer", "Multijugador");
		SPANISH.put("opencraft.button:settings", "Configuración");
		SPANISH.put("opencraft.button:quit", "Salir");
		
	}
	
	public static String translate(String property, Locale language) {
		
		if (language.equals(Languages.get("es-ES")))
			return SPANISH.getOrDefault(property, property);
		else if (language.equals(Languages.get("gl-GL")))
			return GALICIAN.getOrDefault(property, property);
		else
			return ENGLISH.getOrDefault(property, property);
		
	}

	public static String translate(String property) {
		return Translator.translate(property, GameConfig.LANGUAGE);
	}

	public static String translate(Resource resource, Locale language) {
		return Translator.translate(resource.toString(), language);
	}

	public static String translate(Resource resource) {
		return Translator.translate(resource, GameConfig.LANGUAGE);
	}
}
