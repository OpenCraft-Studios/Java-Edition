package net.opencraft.util.coords;

import java.awt.Point;

public final class Vec2 {

	public int x;
	public int y;
	
	public Vec2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2(Point p) {
		this(p.x, p.y);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public static Vec2 newTemp(int x, int y) {
		return new Vec2(x, y);
	}

	public Point toPoint() {
		return new Point(x, y);
	}

}
