package net.opencraft.util;

import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.input.Mouse;

import net.opencraft.renderer.gui.GuiElement;

public class MouseUtils {

	private MouseUtils() {
	}
	
	public static boolean inRange(Rectangle bounds) {
		Point mousePos = new Point(Mouse.getX(), Mouse.getY());
		return bounds.contains(mousePos);
	}

	public static boolean inRange(int x, int y, int w, int h) {
		Rectangle bounds = new Rectangle(x, y, w, h);
		return inRange(bounds);
	}
	
	public static boolean inRange(GuiElement element) {
		return inRange(element.getBounds());
	}

}
