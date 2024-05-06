package net.op.render.screens;

import java.awt.Graphics;
import java.awt.event.MouseListener;

import net.op.input.MouseUtils;
import net.op.render.textures.Assets;
import net.op.util.Resource;

public abstract class Screen {

	private static Screen current = Loadscreen.getInstance();
	private Resource resource;

	protected Screen(Resource resource) {
		this.resource = resource;
	}

	public abstract void render(Graphics g, Assets assets);

	public static void setCurrent(Class<? extends Screen> screenClass) {
		if (MenuScreen.class.equals(screenClass))
			Screen.setCurrent(MenuScreen.getInstance());
		else if (Loadscreen.class.equals(screenClass))
			Screen.setCurrent(Loadscreen.getInstance());
		else if (SettingsScreen.class.equals(screenClass))
			Screen.setCurrent(SettingsScreen.getInstance());
	}

	public static void setCurrent(Screen scene) {
		Screen.current = scene;
	}

	public static Screen getCurrent() {
		return current;
	}

	public static void renderCurrent(Graphics g, Assets assets) {
		if (current == null)
			return;
		if (current instanceof MouseListener)
			MouseUtils.makeCurrentListener((MouseListener) current);
		
		current.render(g, assets);
	}

	public final Resource getResource() {
		return this.resource;
	}
	
	@Override
	public String toString() {
		return "Screen(" + getResource().toString() + ")";
	}

}