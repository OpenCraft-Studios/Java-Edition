package net.opencraft.language;

import java.util.Locale;

public interface Translatable {
	
	String toString(Locale language);
	
	String toString();

}
