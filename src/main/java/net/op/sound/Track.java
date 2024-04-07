package net.op.sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Track {

	private final String name;
	private List<Sound> soundList = new ArrayList<>();

	public Track(String name) {
		this.name = name;
	}
	
	public Track(String name, List<Sound> soundList) {
		this(name);
		this.soundList = soundList;
	}

	public Stream<Sound> getSounds() {
		return soundList.stream();
	}

	public Optional<Sound> getSound(int si) {
		return Optional.ofNullable(this.soundList.get(si));
	}

	public String getName() {
		return this.name;
	}

	public void append(Sound sound) {
		soundList.add(sound);
	}

	@Override
	public String toString() {
		return Arrays.toString(soundList.toArray());
	}

}