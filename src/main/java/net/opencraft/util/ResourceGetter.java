package net.opencraft.util;

import static net.opencraft.GameSettings.*;

import java.io.FileInputStream;
import java.io.InputStream;

import net.opencraft.spectoland.SpectoError;

public class ResourceGetter {

	private ResourceGetter() {
	}

	public static InputStream getInternal(String path) {
		if (!Resource.isValid(path)) {
			return null;
		}

		return ResourceGetter.class.getResourceAsStream(path);
	}

	public static InputStream getExternal(String path) {
		InputStream in = null;
		if (!Resource.isValid(path)) {
			return in;
		}

		if (getDirectory().isEmpty()) {
			path = path.substring(1);
		}

		try {
			in = new FileInputStream(getDirectory() + path);
		} catch (Exception ex) {
			SpectoError.ignored(ex, ResourceGetter.class);
		}

		return in;
	}

}
