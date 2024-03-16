package net.opencraft.data.packs;

import java.io.InputStream;

public interface Pack {
	InputStream getResource(String resource);
}
