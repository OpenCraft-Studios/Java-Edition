package net.opencraft.sound;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.opencraft.util.Resource;
import net.opencraft.util.Files;

public class Sound {

	private final Resource resource;
	private final String name;
	private final String author;
	private final String path;

	Sound(Resource resource, String name, String author, String path) {
		this.resource = resource;
		this.name = name;
		this.author = author;
		this.path = path;
	}

	Sound(String resource, String name, String author, String path) {
		this(Resource.format(resource), name, author, path);
	}

	public static Sound.Builder of() {
		return new Builder();
	}

	public Resource getResource() {
		return resource;
	}

	public String getName() {
		return name;
	}
	
	public String getAuthor() {
		return author;
	}

	public String getPath() {
		return path;
	}

	public void play() {
		SoundManager.playSound(this);
	}

	public AudioInputStream inputStream() throws UnsupportedAudioFileException, IOException {
		InputStream in;
		in = Files.external("/resources" + path);

		return getAudioInputStream(new BufferedInputStream(in));
	}

	public static class Builder {

		private Resource res = null;
		private String name = null;
		private String author = "Unknown";
		private String path = null;

		public Builder resource(Resource res) {
			this.res = res;
			return this;
		}

		public Builder resource(String res) {
			return resource(Resource.format(res));
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder author(String author) {
			this.author = author;
			return this;
		}

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public Sound build() {
			if (res == null || name == null || author == null || path == null) {
				throw new IllegalArgumentException(String.format("Invalid %s constructor", Sound.class.getName()));
			}

			return new Sound(res, name, author, path);
		}

	}

}
