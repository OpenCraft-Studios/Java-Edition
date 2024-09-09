package net.opencraft.renderer.gui;

import java.awt.*;

public abstract class GuiElement {

	protected int x, y, width, height;

	public abstract void draw(Graphics2D g2d);
	
	/* Setters & Getters */
	
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public Dimension getSize() {
		return new Dimension(width, height);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setSize(Dimension d) {
		setSize(d.width, d.height);
	}
	
	public Point getLocation() {
		return new Point(x, y);
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setLocation(Point p) {
		setLocation(p.x, p.y);
	}
	
	public void scale(float scalar) {
		this.width *= scalar;
		this.height *= scalar;
	}
	
}
