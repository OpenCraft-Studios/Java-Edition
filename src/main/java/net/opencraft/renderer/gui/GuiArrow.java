package net.opencraft.renderer.gui;

import static net.opencraft.OpenCraft.*;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.joml.Vector2f;

public class GuiArrow extends GuiElement implements IState, IUpdateable, IHighlight {

	public static final int ARROW_LEFT        = 0b01,
							ARROW_RIGHT       = 0b00,
							ARROW_HIGHLIGHTED = 0b10,
							ARROW_NORMAL      = 0b00;
	
	public static final Dimension DEFAULT_SIZE = new Dimension(14, 22);
	
	private boolean right = true;
	private boolean highlighted = false;
	
	private BufferedImage tex = null;

	public GuiArrow() {
		setSize(DEFAULT_SIZE);
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		if (tex == null)
			update();
		
		g2d.drawImage(tex, x, y, width, height, null);
	}
	
	@Override
	public int getState() {
		int state = 0;
		state |= highlighted ? ARROW_HIGHLIGHTED : ARROW_NORMAL;
		state |= right       ? ARROW_RIGHT       : ARROW_LEFT;
		
		return state;
	}
	
	public void setDirection(int direction) {
		this.right = direction == ARROW_RIGHT;
		this.update();
	}
	
	public int getDirection() {
		return right ? ARROW_RIGHT : ARROW_LEFT;
	}
	
	@Override
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
		this.update();
	}
	
	@Override
	public boolean isHighlighted() {
		return highlighted;
	}
	
	@Override
	public void update() {
		this.tex = oc.assets.getArrow(getState());
	}
	
	@Override
	public void mouseEntered(Vector2f pos, Vector2f delta) {
		setHighlighted(true);
	}
	
	@Override
	public void mouseExited(Vector2f pos, Vector2f delta) {
		setHighlighted(false);
	}
	
}
