package net.opencraft.renderer.scenes;

import java.awt.image.BufferedImage;

import net.opencraft.config.GameExperiments;
import net.opencraft.renderer.Renderizable;
import net.opencraft.sound.Sound;
import net.opencraft.sound.SoundManager;
import net.opencraft.util.Resource;

public abstract class Scene implements Renderizable {

	public static final Scene LOAD_SCENE = LoadScene.getInstance();
	public static final Scene TITLE_SCENE = TitleScene.getInstance();
	
//	LOAD_SCREEN(LoadScene.RESOURCE, Sound.NONE),
//	TITLE_SCENE(TitleScene.RESOURCE, Sound.MOOG_CITY);

	protected static Scene current = TITLE_SCENE;
	protected final Resource res;
	protected final Sound snd;

	public Scene(Resource res, Sound snd) {
		this.res = res;
		this.snd = snd;
	}
	
	public Sound getSound() {
		return this.snd;
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
