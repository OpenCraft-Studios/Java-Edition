package net.opencraft.renderer.screens;

import java.awt.Graphics2D;
import java.awt.event.MouseListener;

import net.opencraft.util.MouseUtils;

public abstract class Screen {

	private static Screen current = Loadscreen.getInstance();

	public abstract void render(Graphics2D g2d);

	public static void setCurrent(Class<? extends Screen> screenClass) {
		if (Menuscreen.class.equals(screenClass))
			Screen.setCurrent(Menuscreen.getInstance());
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

	public static void renderCurrent(Graphics2D g2d) {
		if (current == null)
			return;
		if (current instanceof MouseListener)
			MouseUtils.makeCurrentListener((MouseListener) current);
		
		current.render(g2d);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName().concat("(unknown_id)");
	}

}