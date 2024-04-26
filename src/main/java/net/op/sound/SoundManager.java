package net.op.sound;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class SoundManager {

	public static final Logger logger = Logger.getLogger(SoundManager.class.getName());
	public static final int TIMEOUT = 12;

	public static boolean MUSIC = false;
	private static Thread currentSoundThread = null;

	private SoundManager() {
	}

	public static void init() {
		logger.info("Sound manager started!");
		logger.info("[SoundAPI] Sound API status: %s".formatted(MUSIC ? "Active" : "Passive"));
	}

	public static void update() {
		if (!MUSIC) {
			if (currentSoundThread != null)
				stopSounds();
			
			return;
		}

		if (currentSoundThread != null) {
			if (currentSoundThread.isAlive())
				return;
		}

		int r = (int) (System.currentTimeMillis() / 1000 % TIMEOUT);
		if (r == 0) {
			playRandomSound();
		}

	}

	public static void playRandomSound() {
		List<Sound> sounds = Tracks.get("Menu Sounds").getSounds().toList();
		int index = new Random().nextInt(sounds.size());
		playSound(sounds.get(index));
	}
	
	@SuppressWarnings("deprecation")
	public static void stopSounds() {
		if (currentSoundThread == null)
			return;
		
		if (!currentSoundThread.isAlive()) {
			currentSoundThread = null;
			return;
		}

		try {
			currentSoundThread.stop();
		} catch (Exception ignored) {
			try {
				currentSoundThread.interrupt();
			} catch (Exception ignored2) {
			}
		}
		
		currentSoundThread = null;
	}

	private static boolean mt_PlaySound(Sound sound) {
		try {
			Runnable soundRunnable = () -> {
				try {
					SoundPlayer.play(sound.inputStream());
				} catch (Exception ignored) {
				}
			};
			currentSoundThread = new Thread(soundRunnable);
			currentSoundThread.start();
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
		SoundManager.playRandomSound();
	}

	public static void shutdown() {
		MUSIC = false;
		SoundManager.stopSounds();
	}

}
