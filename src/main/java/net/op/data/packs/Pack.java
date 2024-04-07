package net.op.data.packs;

import java.io.InputStream;

public abstract class Pack {

	public static DefaultPack getDefaultPack() {
		return DefaultPack.defaultPack;
	}

	public static Pack getInternalPack() {
		return InternalPack.internalPack;
	}

	public abstract InputStream getResource(String resourceURL);

}
