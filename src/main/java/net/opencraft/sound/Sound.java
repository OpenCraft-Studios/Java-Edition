package net.opencraft.sound;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.opencraft.config.GameConfig;
import net.opencraft.util.Resource;

public enum Sound {
	NONE("opencraft.sounds", "none", null), MOOG_CITY("opencraft.sound", "title.moog_city", "MoogCity.wav"),
	ARIA_MATH("opencraft.sound", "ambient.aria_math", "AriaMath.wav");

	public static Sound PLAYING = null;

	final String origin;
	final String soundId;
	final String path;

	Sound(String origin, String soundTitle, String name) {
		this.origin = origin;
		this.soundId = soundTitle;
		this.path = name;
	}

	public static void setCurrent(Sound aures) {
		PLAYING = aures;
	}

	public String getOrigin() {
		return origin;
	}

	public String getSoundId() {
		return soundId;
	}

	public Optional<String> getRelativePath() {
		return Optional.ofNullable(path);
	}

	public Optional<String> getPath() {

		Optional<String> relativePath = getRelativePath();

		if (relativePath.isEmpty())
			return Optional.empty();

		return Optional.of(GameConfig.GAME_DIR + "/assets/opencraft/sounds/" + path);
	}

	public static Sound getCurrent() {
		return PLAYING;
	}

	public Sound fromResource(Resource res) {
		return switch (res.toString()) {
		case "opencraft.sound:title.moog_city" -> MOOG_CITY;
		case "opencraft.sound:ambient.aria_math" -> ARIA_MATH;
		default -> NONE;
		};
	}

	public static void play(Clip player, Sound sound) {
		if (player == null)
			return;

		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(getSound(sound).get());
			player.open(audioStream);
			player.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception ignored) {
		}
	}

	public void play(Clip player) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
		play(player, this);
	}

	public static Optional<InputStream> getSound(Sound snd) {
		Optional<String> path = snd.getPath();

		if (path.isEmpty())
			return Optional.empty();

		BufferedInputStream bis;
		try {
			var fis = new FileInputStream(path.get());
			bis = new BufferedInputStream(fis);
		} catch (Exception ignored) {
			return Optional.empty();
		}

		return Optional.of(bis);
	}

	public Resource toResource() {
		return Resource.of(origin, soundId);
	}

	@Override
	public String toString() {
		return toResource().toString();
	}

}
