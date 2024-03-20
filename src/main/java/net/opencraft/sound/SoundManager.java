package net.opencraft.sound;

import static net.opencraft.LoggerConfig.LOG_FORMAT;
import static net.opencraft.LoggerConfig.handle;
import static net.opencraft.sound.Sound.getCurrent;
import static net.opencraft.sound.Sound.setCurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import net.opencraft.config.GameConfig;
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
			clip = null;
			supported = false;
		}

		player = clip;
		SUPPORTED = supported;
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
		
		int rand = (int) (Math.random() * Math.pow(10, 5));
		rand = rand >> 5;
		rand = rand * 2;
		rand = (int) (rand ^ System.nanoTime());
		rand = rand >> 8;
		rand = ++rand & usedSounds.size() - 1;
		
		try {
			int sndIndex = rand;
			System.out.println(rand);
			sound = usedSounds.get(sndIndex);
		} catch (Exception ignored) {
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
