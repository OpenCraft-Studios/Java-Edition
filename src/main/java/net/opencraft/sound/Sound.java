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
import net.opencraft.logging.InternalLogger;
import net.opencraft.util.Resource;

public enum Sound {
	NONE("opencraft.sounds:none", null, false),
	MENU2("opencraft.sound:menu.moog_city", "menu/menu2.wav", false),
	MENU3("opencraft.sound:menu.beginning_2", "menu/menu3.wav", false),
	MENU3_SYNTHWAVE("opencraft.sound:menu.beginning_2.synthwave", "menu/menu3.swav", true),
	CREATIVE4("opencraft.sound:ambient.creative4", "creative/creative4.wav", false),
	CREATIVE4_SYNTHWAVE("opencraft.sound:ambient.creative4.synthwave", "creative/creative4.swav", true);

	public static Sound PLAYING = null;

	final Resource resource;
	final String path;

	final boolean synthwave;

	Sound(String id, String path, boolean synthwave) {
		this.resource = Resource.format(id);
		this.path = path;
		this.synthwave = synthwave;
	}

	public static void setCurrent(Sound aures) {
		PLAYING = aures;
	}

	public boolean isSynthwave() {
		return this.synthwave;
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
			case "opencraft.sound:menu.moog_city" -> MENU2;
			case "opencraft.sound:ambient.aria_math" -> CREATIVE4;
			case "opencraft.sound:menu.beginning_2" -> MENU3;
			case "opencraft.sound:menu.beginning_2.synthwave" -> MENU3_SYNTHWAVE;
			case "opencraft.sound:ambient.creative4" -> CREATIVE4;
			case "opencraft.sound:ambient.creative4.synthwave" -> CREATIVE4_SYNTHWAVE;
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
			InternalLogger.out.printf("[%s] Ignored exception:\n", Sound.class.getName());
			ignored.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();
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
			InternalLogger.out.printf("[%s] Ignored exception:\n", Sound.class.getName());
			ignored.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();
			return Optional.empty();
		}

		return Optional.of(bis);
	}

	public Resource toResource() {
		return resource;
	}

	@Override
	public String toString() {
		return resource.getName();
	}

}
