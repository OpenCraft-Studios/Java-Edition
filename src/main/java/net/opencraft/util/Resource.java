package net.opencraft.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
		return Resource.class.getResourceAsStream(respath);
	}
	
	public static URL getResourceURL(String respath) {
		return Resource.class.getResource(respath);
	}
	
	public static InputStream bindExternalResource(String respath) throws IOException {
		return new FileInputStream(respath);
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
