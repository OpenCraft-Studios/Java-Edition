package net.opencraft.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

public class ZipUtils {

	private ZipUtils() {
	}
	
	public static InputStream getResource(String name, ZipFile zfile) throws IOException {
		return zfile.getInputStream(zfile.getEntry(name));
	}
	
}
