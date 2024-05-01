package net.op.render.screens;

import java.awt.Graphics;

import net.op.util.Resource;

public abstract class Screen {

	private static Screen current = Loadscreen.getInstance();
	private Resource resource;

	protected Screen(Resource resource) {
		this.resource = resource;
	}

	public abstract void render(Graphics g);

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

	public static void renderCurrent(Graphics g) {
		if (current == null) {
			return;
		}

		current.render(g);
	}

	public final Resource getResource() {
		return this.resource;
	}

}