package org.scgi;

import java.util.Optional;

public class DisplayExtra {

	public static Optional<Object> get(String key) {
		Optional<Object> result = Optional.empty();
		
		if (key.equals("@int _width"))
			result = Optional.of(Display.window.getWidth());
		else if (key.equals("@int _height"))
			result = Optional.of(Display.window.getHeight());
		else if (key.equals("@java.lang.String _title"))
			result = Optional.of(Display.window.getTitle());
		
		return result;
	}
	
}
