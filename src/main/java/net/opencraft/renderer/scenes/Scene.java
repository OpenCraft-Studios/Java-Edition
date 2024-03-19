package net.opencraft.renderer.scenes;

import java.awt.image.BufferedImage;

import net.opencraft.config.GameExperiments;
import net.opencraft.renderer.Renderizable;
import net.opencraft.sound.Sound;
import net.opencraft.sound.SoundManager;
import net.opencraft.util.Resource;

public abstract class Scene implements Renderizable {

	public static final Scene LOAD_SCENE = LoadScene.getInstance();
	public static final Scene MENU_SCENE = MenuScene.getInstance();

	protected static Scene current = MENU_SCENE;
	protected final Resource res;
	protected final Sound[] sounds;

	public Scene(Resource res, Sound[] sounds) {
		this.res = res;
		this.sounds = sounds;
	}
	
	public Sound[] getSounds() {
		return this.sounds;
	}

	public static void renderCurrent(BufferedImage bi) {
		getCurrent().render(bi);
	}
	
	public static Scene getCurrent() {
		return current;
	}

	public void setCurrent() {
		setCurrent(this);
	}
	
	public static Scene setCurrent(Scene scn) {
		current = scn;

		if (GameExperiments.PLAY_SOUND_ONCE)
			SoundManager.update();
		
		return current;
	}

	public Resource getResource() {
		return res;
	}
	
}
