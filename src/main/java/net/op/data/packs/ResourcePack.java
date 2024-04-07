package net.op.data.packs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ResourcePack extends Pack {

	private ZipFile zipFile;

	public ResourcePack(ZipFile zipFile) {
		this.zipFile = zipFile;
	}
	
	public ResourcePack(File file) throws ZipException, IOException {
		this(new ZipFile(file));
	}
	
	public ResourcePack(String filepath) throws IOException {
		this(new ZipFile(filepath));
	}

	@Override
	public InputStream getResource(String resourceURL) {
		InputStream in = null;
		try {
			in = zipFile.getInputStream(zipFile.getEntry(resourceURL));
		} catch (Exception ignored) {
			// TODO Internal logger
		}
		
		return in;
	}

}
