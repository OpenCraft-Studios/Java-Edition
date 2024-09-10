package net.opencraft.renderer.gui;

import static net.opencraft.OpenCraft.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GuiButton extends GuiElement implements IUpdateable, IState, IHighlight{

	public static final int BUTTON_DISABLED = 0,
							BUTTON_NORMAL = 1,
							BUTTON_HIGHLIGHTED = 2;
	
	private BufferedImage tex = null;
	private boolean highlighted;
	
	@Override
	public void draw(Graphics2D g2d) {
		if (tex == null)
			this.update();
		
	}
	
	@Override
	public void update() {
		this.tex = oc.assets.getButton(getState());
	}

	@Override
	public int getState() {
		return BUTTON_NORMAL;
	}

	@Override
	public boolean isHighlighted() {
		return highlighted;
	}

	@Override
	public void setHighlighted(boolean h) {
		this.highlighted = h;
		
	}

}
