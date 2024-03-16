package net.opencraft.renderer.scenes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.opencraft.config.GameExperiments;
import net.opencraft.renderer.Renderizable;
import net.opencraft.sound.SoundManager;
import net.opencraft.sound.Sounds;
import net.opencraft.util.Resource;

public enum Scenes {

	LOAD_SCREEN(LoadScene.RESOURCE, Sounds.NONE),
	TITLE_SCENE(TitleScene.RESOURCE, Sounds.MOOG_CITY);

	private static Scenes current = TITLE_SCENE;
	private final Resource res;
	private final Sounds snd;

	Scenes(Resource res, Sounds snd) {
		this.res = res;
		this.snd = snd;
	}

	public Renderizable getScene(Scenes sc) {
		return switch (sc) {
			case TITLE_SCENE -> TitleScene.getInstance();
			case LOAD_SCREEN -> LoadScene.getInstance();
			default -> null;
		};
	}

	public Renderizable getScene() {
		return getScene(this);
	}
	
	public Sounds getSound() {
		return this.snd;
	}

	public static void renderCurrent(BufferedImage bi) {
		getCurrent().getScene().render(bi);
	}
	
	public static void renderCurrent(Graphics g, int width, int height) {
		getCurrent().getScene().render(g, width, height);
	}
	
	public static void renderCurrent(Graphics g) {
		getCurrent().getScene().render(g);
	}
	
	public static Scenes getCurrent() {
		return current;
	}

	public void setCurrent() {
		setCurrent(this);
	}
	
	public static Scenes setCurrent(Scenes scn) {
		current = scn;

		if (GameExperiments.PLAY_SOUND_ONCE)
			SoundManager.update();
		
		return current;
	}

	public Resource getResource() {
		return res;
	}

}
