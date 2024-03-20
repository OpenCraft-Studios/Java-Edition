package net.opencraft.data.packs;

import java.io.FileInputStream;
import java.io.InputStream;

import net.opencraft.logging.InternalLogger;

public final class DefaultPack extends Pack {

	private static final DefaultPack defaultPack = new DefaultPack();
	
	public DefaultPack() {
	}
	
	public static Pack getDefaultPack() {
		return DefaultPack.defaultPack;
	}
	
	@Override
	public InputStream getResource(String resource) {
		InputStream in = InputStream.nullInputStream();
		
		try {
			in = new FileInputStream(resource);
		} catch (Exception ignored) {
			InternalLogger.out.printf("[%s] Ignored exception:\n", getClass().getName());
			ignored.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();
		}
		
		return in;
	}

}
