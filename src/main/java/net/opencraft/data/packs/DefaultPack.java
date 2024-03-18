package net.opencraft.data.packs;

import java.io.FileInputStream;
import java.io.InputStream;

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
		
		System.out.println("Resource adquired: " + resource);
		try {
			in = new FileInputStream(resource);
		} catch (Exception ignored) {
		}
		
		return in;
	}

}
