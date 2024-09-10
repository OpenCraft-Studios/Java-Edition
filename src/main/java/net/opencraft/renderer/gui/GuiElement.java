package net.opencraft.renderer.gui;

import java.awt.*;

import org.joml.Vector2f;

import lombok.Getter;
import lombok.Setter;

public abstract class GuiElement {

	@Getter @Setter
	protected int x, y, width, height;

	public abstract void draw(Graphics2D g2d);
	
	/* Override-able event methods */
	
	public void mouseClicked(Vector2f pos, int button) {
	}
	
	public void mouseEntered(Vector2f pos, Vector2f delta) {
	}
	
	public void mouseExited(Vector2f pos, Vector2f delta) {
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
	
}
