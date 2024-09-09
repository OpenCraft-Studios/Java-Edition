package net.opencraft.sound;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundManager {

	public static final Logger logger = LoggerFactory.getLogger(SoundManager.class.getName());
	public static final int TIMEOUT = (int) (60 * 1.5);

	public static boolean MUSIC = false;
	public static Thread cst = null;

	private SoundManager() {
	}

	public static void init() {
		logger.info("Sound manager started!");
		logger.info("[SoundAPI] Sound API status: " + (MUSIC ? "Active" : "Passive"));
	}

	public static void update() {
		if (!MUSIC) {
			if (cst != null)
				stopSounds();

			return;
		}

		if (cst != null) {
			if (cst.isAlive())
				return;
		}

		int r = (int) (System.currentTimeMillis() / 1000 % TIMEOUT);
		if (r == 0) {
			playRandomSound();
		}

	}

	public static void playRandomSound() {
		List<Sound> sounds = Tracks.get("Menu Sounds").getSounds().collect(Collectors.toList());
		int index = new Random().nextInt(sounds.size());
		playSound(sounds.get(index));
	}

	@SuppressWarnings("deprecation")
	public static void stopSounds() {
		if (cst == null)
			return;

		if (!cst.isAlive()) {
			cst = null;
			return;
		}

		try {
			cst.stop();
		} catch (Exception ignored) {
			try {
				cst.interrupt();
			} catch (Exception ignored2) {
			}
		}

		cst = null;
	}

	private static boolean mt_PlaySound(Sound sound) {
		try {
			Runnable soundRunnable = () -> {
				try {
					SoundPlayer.play(sound.inputStream());
				} catch (Exception ignored) {
				}
			};
			cst = new Thread(soundRunnable);
			cst.start();
		} catch (Exception ignored) {
			return false;
		}

		return true;
	}

	public static boolean playSound(Sound sound, boolean multithreading) {
		if (multithreading)
			return mt_PlaySound(sound);

		boolean state = true;
		try {
			state = SoundPlayer.play(sound.inputStream());
		} catch (Exception ignored) {
			state = false;
		}

		return state;
	}

	public static boolean playSound(Sound sound) {
		return playSound(sound, true);
	}

	public static void enable() {
		MUSIC = true;
		playRandomSound();
	}
	
	public static void disable() {
		MUSIC = false;
		stopSounds();
	}

	public static void shutdown() {
		disable();
		logger.info("Shutdowning sound system!");
	}

	public static double getVolume() {
		// Volume feature don't supported yet!
		return MUSIC ? 1.0 : 0.0;
	}

	public static void toggle() {
		if (MUSIC)
			disable();
		else
			enable();
	}
}
