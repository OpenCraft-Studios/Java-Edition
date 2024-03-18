package net.opencraft.util;

import java.io.InputStream;
import java.net.URL;

import net.opencraft.client.Game;

public class Resource {

	private final String origin;
	private final String name;

	public Resource(String origin, String name) {
		this.origin = origin;
		this.name = name;
	}

	public Resource(Resource other) {
		this.origin = other.origin;
		this.name = other.name;
	}

	public Resource(String complete_name) {
		this(Resource.format(complete_name));
	}

	public static Resource of(String origin, String name) {
		return new Resource(origin, name);
	}

	public static Resource of(Resource other) {
		return new Resource(other);
	}

	public static Resource of(String complete_name) {
		return Resource.of(Resource.format(complete_name));
	}

	public static Resource format(String complete_name) {
		String[] sources = complete_name.split(":");
		String origin = sources[0];
		String name = sources[1];

		return Resource.of(origin, name);
	}

	public static InputStream bindInternalResource(String respath) {
		InputStream in = InputStream.nullInputStream();
		try {
			in = Resource.class.getResourceAsStream(respath);
		} catch (Exception ignored) {
		}

		return in;
	}

	public static URL getResourceURL(String respath) {
		return Resource.class.getResource(respath);
	}

	public static InputStream bindResource(String respath) {
		InputStream in = InputStream.nullInputStream();
		try {
			in = Game.getResourcePack().getResource(respath);
		} catch (Exception ignored) {
		}
	
		return in;
	}

	public String getOrigin() {
		return origin;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return origin + ":" + name;
	}

}
