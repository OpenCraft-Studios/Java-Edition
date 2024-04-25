package net.op.sound;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.op.util.Resource;
import net.op.util.ResourceGetter;

public class Sound {

	private final Resource resource;
	private final String name;
	private final String path;

	public Sound(Resource resource, String name, String path) {
		this.resource = resource;
		this.name = name;
		this.path = path;
	}

	public Sound(String resource, String name, String path) {
		this(Resource.format(resource), name, path);
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

	public String getPath() {
		return path;
	}

	public void play() {
		SoundManager.playSound(this);
	}

	public AudioInputStream inputStream() throws UnsupportedAudioFileException, IOException {
		InputStream in;
		in = ResourceGetter.getExternal("/resources/music" + path);

		return getAudioInputStream(new BufferedInputStream(in));
	}

	public static class Builder {

		private Resource res = null;
		private String name = null;
		private String path = null;

		public Builder resource(Resource res) {
			this.res = res;
			return this;
		}

		public Builder resource(String res) {
			resource(Resource.format(res));
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder path(String path) {
			this.path = path;
			return this;
		}

		public Sound build() {
			if (res == null || name == null || path == null) {
				throw new IllegalArgumentException("res or name or path can't be null!");
			}

			return new Sound(res, name, path);
		}

	}

}
