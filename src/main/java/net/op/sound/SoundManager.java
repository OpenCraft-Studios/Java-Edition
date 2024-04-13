package net.op.sound;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {

	private static final Clip player;
	public static final Logger logger = Logger.getLogger(SoundManager.class.getName());
	public static boolean ENABLED = false;

	static {
		// Create clip instance
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
		} catch (Exception ignored) {
			// TODO Internal logger
			clip = null;
			ENABLED = false;
		}

		player = clip;
		logger.info("Sound manager started!");
		logger.info("[SoundAPI] Sound API status: %s".formatted(ENABLED ? "Active" : "Passive"));
	}

	private SoundManager() {
	}

	public static void update() {
		if (!ENABLED) {
			stopSounds();
			return;
		}
		
		if (player.isActive())
			return;

		int r = (int) (System.currentTimeMillis() / 1000 % 10);
		if (r == 0) {
			Stream<Sound> sounds = Tracks.get("Menu Sounds").getSounds().filter(sound -> !sound.isSynthwave());
			List<Sound> soundList = sounds.toList();
			int index = new Random().nextInt(soundList.size());
			playSound(soundList.get(index), false);
		}

	}

	public static void playSound(Sound sound, boolean loop) {
		if (player == null || sound == null)
			return;

		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(sound.inputStream());
			player.open(audioStream);
			player.loop(loop ? Clip.LOOP_CONTINUOUSLY : 0);
		} catch (Exception ignored) {
			ignored.printStackTrace();
			// TODO Internal logger
		}
	}

	public static void stopSounds() {
		player.loop(0);
		player.stop();
		player.close();
	}
	
	public static void enable() {
		ENABLED = true;
	}
	
	public static void disable() {
		ENABLED = false;
	}

	public static boolean isSupported() {
		return ENABLED;
	}

}
