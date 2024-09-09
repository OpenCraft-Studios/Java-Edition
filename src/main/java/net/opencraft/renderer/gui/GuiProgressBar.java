package net.opencraft.renderer.gui;

import java.awt.*;

public class GuiProgressBar extends GuiElement {

	private float progress = 0F;
	
	private Color backgroundColor = new Color(0xE22837);
	private Color borderColor = Color.BLACK;
	
	public void setProgress(float progress) {
		this.progress = progress;
	}
	
	public void addProgress(float a) {
		this.progress += a;
	}
	
	public float getProgress() {
		return progress;
	}

	@Override
	public void draw(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(2F));
		// Draw borders
		{
			g2d.setColor(borderColor);
			g2d.drawRect(x, y, width, height - 2);
		}
		
		int filledWidth = (int) ((width - 6F) * 0.01f * progress);
		
		// Draw Background
		{
			g2d.setColor(backgroundColor);
			g2d.fillRect(x + 3, y + 3, filledWidth, height - 8);
		}
		
		
	}
	
}
