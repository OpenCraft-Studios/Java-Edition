package net.opencraft.util;

import static net.opencraft.OpenCraft.*;

import java.io.*;

import net.opencraft.spectoland.SpectoError;

public class Files {

	private Files() {
	}

	public static InputStream internal(String path) {
		if (!Resource.isValid(path)) {
			return null;
		}

		return Files.class.getResourceAsStream(path);
	}

	public static InputStream external(String path) {
		InputStream in = null;
		if (!Resource.isValid(path))
			return in;

		try {
			in = new FileInputStream(new File(oc.directory, path));
		} catch (Exception ex) {
			SpectoError.ignored(ex, Files.class);
		}

		return in;
	}

}
