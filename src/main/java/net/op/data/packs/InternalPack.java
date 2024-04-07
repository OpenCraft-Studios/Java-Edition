package net.op.data.packs;

import java.io.InputStream;

import net.op.util.Resource;

public class InternalPack extends Pack {
	
	public static final InternalPack internalPack = new InternalPack();

	private InternalPack() {
	}
	
	@Override
	public InputStream getResource(String resourceURL) {
		if(!Resource.isValid(resourceURL))
			return null;
		
		return InternalPack.class.getResourceAsStream(resourceURL);
	}

}
