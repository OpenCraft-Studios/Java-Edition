package net.opencraft.data.packs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import net.opencraft.logging.InternalLogger;

public class ResourcePack extends Pack {

	private ZipFile zfile;

	public ResourcePack(ZipFile zfile) {
		this.zfile = zfile;
	}

	public ResourcePack(File file) throws ZipException, IOException {
		this(new ZipFile(file));
	}

	public ResourcePack(String filename) throws ZipException, IOException {
		this(new File(filename));
	}

	public static ResourcePack get(final String filename) {
		ResourcePack pack = null;

		try {
			pack = new ResourcePack(filename);
		} catch (Exception ignored) {
			InternalLogger.out.printf("[%s] Ignored exception:\n", ResourcePack.class.getName());
			ignored.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();
		}

		return pack;
	}

	@Override
	public InputStream getResource(String resource) {
		InputStream stream = InputStream.nullInputStream();
		
		try {
			stream = zfile.getInputStream(zfile.getEntry(resource));
		} catch (Exception ignored) {
			InternalLogger.out.printf("[%s] Ignored exception:\n", getClass().getName());
			ignored.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();
		}
		
		return stream;
	}

}
