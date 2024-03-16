package net.opencraft.sound;

import static net.opencraft.LoggerConfig.LOG_FORMAT;
import static net.opencraft.LoggerConfig.handle;
import static net.opencraft.sound.Sound.getCurrent;
import static net.opencraft.sound.Sound.setCurrent;

import java.util.Objects;
import java.util.logging.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import net.opencraft.renderer.scenes.Scene;

public class SoundManager {

	private static final Logger logger = Logger.getLogger("soundManager");

	private static final Clip player;
	public static final boolean SUPPORTED;

	static {
		// Set logging format
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
		handle(logger);

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
		Sound sound = Scene.getCurrent().getSound();
		if (!isSupported() || Objects.isNull(sound.getPath()))
			return;
		
		if (getCurrent() != sound) {
			resetPlayer();
			try {
				Sound.play(player, sound);
			} catch (Exception e) {
				logger.severe(e.getMessage());
			}
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
