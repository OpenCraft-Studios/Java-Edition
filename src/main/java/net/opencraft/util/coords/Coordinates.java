package net.opencraft.util.coords;

import java.awt.Dimension;

public class Coordinates {

	private Coordinates() {
	}

	public static int[] XYWHtoP4(int x, int y, int width, int height) {
		int x1, x2, y1, y2;
		x1 = x;
		y1 = y;
		x2 = x + width;
		y2 = y + height;

		return new int[] { x1, y1, x2, y2 };
	}

	public static int[] XYWHtoP4(Vec2 v, Dimension d) {
		return XYWHtoP4(v.x, v.y, d.width, d.height);
	}

}