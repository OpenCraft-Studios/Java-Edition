package net.opencraft.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import net.opencraft.renderer.Renderizable;

public class Screen implements Renderizable {

	private final BufferedImage screenImg;

	public Screen(final int width, final int height) {
		screenImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

	public BufferedImage getImage() {
		return screenImg;
	}

	public Graphics getGraphics() {
		return getImage().getGraphics();
	}

	@Override
	public void render(Graphics g, final int width, final int height) {
		g.drawImage(getImage(), 0, 0, width, height, null);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(getImage(), 0, 0, null);
	}

	@Override
	public void render(BufferedImage bi) {
		if (bi.equals(screenImg))
			return;

		render(bi.getGraphics(), bi.getWidth(), bi.getHeight());
	}

	public void draw(Image img) {
		Graphics g = getGraphics();
		g.drawImage(img, 0, 0, null);
	}

	public void draw(Image img, int x, int y) {
		Graphics g = getGraphics();
		g.drawImage(img, x, y, null);
	}

	public void draw(Image img, int x, int y, int width, int height) {
		Graphics g = getGraphics();
		g.drawImage(img, x, y, width, height, null);
	}

	public void draw(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2) {
		Graphics g = getGraphics();
		g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
	}

}
