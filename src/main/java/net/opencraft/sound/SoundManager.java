package net.opencraft.sound;

import static net.opencraft.logging.LoggerConfig.LOG_FORMAT;
import static net.opencraft.logging.LoggerConfig.handle;
import static net.opencraft.sound.Sound.getCurrent;
import static net.opencraft.sound.Sound.setCurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import net.opencraft.config.GameConfig;
import net.opencraft.logging.InternalLogger;
import net.opencraft.renderer.scenes.Scene;

public class SoundManager {

	private static final Logger logger = Logger.getLogger("soundManager");

	private static final Clip player;
	public static final boolean SUPPORTED;

	static {
		/* Set logging format */
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
		handle(logger, "/soundmanager.log");

		// Create clip instance
		Clip clip = null;
		boolean supported = true;
		try {
			clip = AudioSystem.getClip();
		} catch (Exception ignored) {
			InternalLogger.out.printf("[%s] Ignored exception:\n", SoundManager.class.getName());
			ignored.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();
			clip = null;
			supported = false;
		}

		player = clip;
		SUPPORTED = supported;
		logger.info("Sound manager started!");
		if (!SUPPORTED)
			System.err.println("\t(!) It seems like your computer doesn't allows sound!");
	}

	private SoundManager() {
	}

	public static void update() {
		List<Sound> usedSounds = new ArrayList<>();

		for (Sound sound : Scene.getCurrent().getSounds()) {
			if (!sound.isSynthwave() && !GameConfig.SYNTHWAVE)
				usedSounds.add(sound);
			else if (sound.isSynthwave() && GameConfig.SYNTHWAVE)
				usedSounds.add(sound);
		}

		Sound sound = Sound.NONE;
		
		int sndIndex = (int) (Math.random() * Math.pow(10, 5));
		sndIndex = sndIndex >> 5;
		sndIndex = sndIndex * 2;
		sndIndex = (int) (sndIndex ^ System.nanoTime());
		sndIndex = sndIndex >> 8;
		sndIndex = ++sndIndex & usedSounds.size() - 1;
		
		if (sndIndex >= usedSounds.size())
			System.err.printf("WARNING: Wrong sound index detected! -> %d\n", sndIndex);
		
		try {
			sound = usedSounds.get(sndIndex);
		} catch (Exception ignored) {
			InternalLogger.out.printf("[%s] Ignored exception:\n", SoundManager.class.getName());
			ignored.printStackTrace(InternalLogger.out);
			InternalLogger.out.println();
		}

		if (!isSupported())
			return;

		if (getCurrent() != sound) {
			resetPlayer();

			Sound.play(player, sound);
			setCurrent(sound);
		}

	}

	public static void resetPlayer() {
		player.loop(0);
		player.stop();
		player.close();
	}

	public static boolean isSupported() {
		return SUPPORTED;
	}
}
