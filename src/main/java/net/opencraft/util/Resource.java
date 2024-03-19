package net.opencraft.util;

import static net.opencraft.LoggerConfig.LOG_FORMAT;
import static net.opencraft.LoggerConfig.handle;

import java.io.InputStream;
import java.net.URL;
import java.util.logging.Logger;

import net.opencraft.client.Game;

public class Resource {

	private static final Logger logger = Logger.getLogger("resourceBinder");

	private final String origin;
	private final String name;

	static {
		// Set logging format
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);

		handle(logger, "/resbinder.log");
	}

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

		logger.finest(String.format("Internal resource adquired: %s!", respath));
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

		logger.finest(String.format("Resource adquired: %s!", respath));
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
