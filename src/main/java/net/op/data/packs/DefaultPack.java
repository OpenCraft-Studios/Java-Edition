package net.op.data.packs;

import java.io.FileInputStream;
import java.io.InputStream;

import net.op.Client;
import net.op.util.Resource;

public class DefaultPack extends Pack {

	public static final DefaultPack defaultPack = new DefaultPack();

	private DefaultPack() {
	}

	@Override
	public InputStream getResource(String resourceURL) {
		InputStream in = null;
		if (!Resource.isValid(resourceURL))
			return in;
		
		if (Client.getDirectory().isBlank() || Client.getDirectory().isEmpty())
			resourceURL = resourceURL.substring(1);
		
		try {
			in = new FileInputStream(Client.getDirectory() + resourceURL);
		} catch (Exception ignored) {
			// TODO Internal logger
		}

		return in;
	}

}
