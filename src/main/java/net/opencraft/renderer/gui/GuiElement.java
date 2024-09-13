package net.opencraft.renderer.gui;

import java.awt.*;

import lombok.Getter;
import lombok.Setter;

public abstract class GuiElement {

	@Getter @Setter
	protected int x, y, width, height;

	public abstract void draw(Graphics2D g2d);
	
	/* Override-able event methods */
	
	public void mouseClicked(int x, int y, int button) {
	}
	
	public void mouseEntered(int x, int y, int dx, int dy) {
	}
	
	public void mouseExited(int x, int y, int dx, int dy) {
	}
	
	/* Setters & Getters */
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
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
	
	public void setBounds(int x, int y, int width, int height) {
		setSize(width, height);
		setLocation(x, y);
	}
	
	public void setBounds(Rectangle bounds) {
		setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
	}
	
}
