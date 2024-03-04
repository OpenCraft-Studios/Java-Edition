package net.opencraft.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public interface Renderizable {

	void render(Graphics g);

	public default void render(Graphics g, int width, int height) {
		render(g);
	}

	public default void render(BufferedImage bi) {
		render(bi.getGraphics(), bi.getWidth(), bi.getHeight());
	}

}
