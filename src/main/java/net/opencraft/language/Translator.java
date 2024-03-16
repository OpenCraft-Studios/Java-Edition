package net.opencraft.language;

import java.util.Locale;

import net.opencraft.config.GameConfig;
import net.opencraft.util.Resource;

public class Translator {
	
	public static String translate(String property, Locale language) {
		if (language.equals(Locale.forLanguageTag("es-ES"))) {
			return switch(property) {
				case "opencraft.buttons:singleplayer" -> "Un jugador";
				case "opencraft.buttons:multiplayer" -> "Multijugador";
				case "opencraft.buttons:config" -> "Configuración";
				case "opencraft.buttons:quit" -> "Salir";
				
				default -> property;
			};
		}
		
		return switch(property) {
			case "opencraft.buttons:singleplayer" -> "Singleplayer";
			case "opencraft.buttons:multiplayer" -> "Multiplayer";
			case "opencraft.buttons:config" -> "Settings";
			case "opencraft.buttons:quit" -> "Quit Game";
		
			default -> property;
		};
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
