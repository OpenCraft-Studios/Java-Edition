package net.opencraft.renderer.screens;

import static net.opencraft.OpenCraft.*;

import java.awt.*;
import java.awt.image.BufferedImage;

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
		
		current.render(g2d);
	}

	protected void clearScreen(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, oc.width, oc.height);
	}
	
	protected void drawOptionsBackground(Graphics2D g2d) {
		//clearScreen(g2d);
		BufferedImage dirtTexture = drawDirtBackground(g2d);
		
		Rectangle area = new Rectangle(0, 60, oc.width, oc.height - 160);
		
		Composite saveCMP = g2d.getComposite();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		g2d.setColor(Color.BLACK);
		g2d.fill(area);
		g2d.setPaint(new TexturePaint(dirtTexture, new Rectangle(0, 0, 64, 64)));
		g2d.fill(area);
		g2d.setComposite(saveCMP);
	}

	protected BufferedImage drawDirtBackground(Graphics2D g2d) {
		BufferedImage dirtTexture = oc.assets.getBackground();
		{
			g2d.setPaint(new TexturePaint(dirtTexture, new Rectangle(0, 0, 64, 64)));
			g2d.fillRect(0, 0, oc.width, oc.height);
		}
		return dirtTexture;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}