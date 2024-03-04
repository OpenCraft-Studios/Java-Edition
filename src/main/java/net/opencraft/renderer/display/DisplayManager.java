package net.opencraft.renderer.display;

import java.awt.BorderLayout;

import net.opencraft.client.Game;
import net.opencraft.util.Assets;

public class DisplayManager {

	public static Display display = null;

	private DisplayManager() {
	}

	public static void createDisplay() {
		display = new Display(Game.TITLE);
		display.setDefaultCloseOperation(Display.EXIT_ON_CLOSE);
		display.setSize(Display.SIZE);
		display.setPreferredSize(Display.SIZE);
		display.setLocationRelativeTo(null);
		display.setResizable(true);
		display.setLayout(new BorderLayout());
		display.setCursor(Assets.getCursor());
		display.setIconImage(Assets.getIcon());
		display.pack();
	}

	public static void showDisplay() {
		display.setVisible(true);
	}

	public static void destroyDisplay() {
		display.setVisible(false);
		display.dispose();
		display = null;
	}

	public static boolean existDisplay() {
		return display != null;
	}

	public static Display getDisplay() {
		return display;
	}

	public static int getDisplayWidth() {
		return display.getWidth();
	}

	public static int getDisplayHeight() {
		return display.getHeight();
	}

	public static void updateDisplay() {
		display.repaint();
	}

}
