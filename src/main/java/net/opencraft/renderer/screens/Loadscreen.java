package net.opencraft.renderer.screens;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.lwjgl.opengl.Display;

import net.opencraft.renderer.gui.GuiProgressBar;
import net.opencraft.renderer.texture.Assets;
import net.opencraft.util.FontRenderer;

public class Loadscreen extends Screen {

	private static Menuscreen nextScreen = Menuscreen.getInstance();
	private static final double TRANSITION_DURATION = 1e3; // Original value: 3250

	public static Loadscreen instance = getInstance();
	private GuiProgressBar progressBar = new GuiProgressBar();
	private long start = -1;
	

	private Loadscreen() {
	}

	@Override
	public void render(Graphics2D g, Assets assets) {
		animatedLS((Graphics2D) g, true, assets);
	}

	public void animatedLS(Graphics2D g2d, boolean slideUp, Assets assets) {
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 854, 480);

		// Draw OpenCraft Text

		g2d.setColor(Color.BLACK);
		g2d.setFont(FontRenderer.getSystemFont("Microgramma D Extended").deriveFont(Font.BOLD, 70));
		
		FontMetrics metrics = g2d.getFontMetrics();
		String str = "OPENCRAFT";
		{	
			Rectangle textBounds = new Rectangle();
			textBounds.width = metrics.stringWidth(str);
			textBounds.height = metrics.getHeight();
		
			textBounds.x = (854 - textBounds.width) / 2;
			textBounds.y = (480 - textBounds.height) / 2 + metrics.getAscent();
		
			g2d.drawString(str, textBounds.x, textBounds.y);
		}

		g2d.setFont(FontRenderer.getSystemFont("Consolas").deriveFont(Font.BOLD, 23));
		g2d.drawString(Math.round(progressBar.getProgress()) + "%", 50, 50);
		
		progressBar.setSize(604, 24);
		progressBar.setLocation(124, 358);
		progressBar.draw(g2d);
		
		/* OpenCraft Text Slide Up */
		if (!slideUp)
			return;
		
		if (progressBar.getProgress() > 99)
			attemptToChange(g2d, assets);
		else
			progressBar.addProgress(0.42f);
	}

	private void attemptToChange(Graphics2D g2d, Assets assets) {
		final long current = System.currentTimeMillis();

		if (start == -1)
			start = current;
		
		if (current - start <= TRANSITION_DURATION) {
			double alpha = 1 - (current - start) / TRANSITION_DURATION;

			BufferedImage bi = new BufferedImage(854, 480, BufferedImage.TYPE_INT_ARGB);

			Graphics2D gbi = bi.createGraphics();
			nextScreen.render(g2d, assets);

			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha);
			gbi.setComposite(ac);
			animatedLS(gbi, false, null);

			g2d.drawImage(bi, 0, 0, null);
			return;
		}
		
		destroyInstance();
		Display.setResizable(true);
		Screen.setCurrent(nextScreen);
	}

	public static Loadscreen getInstance() {
		if (instance == null) {
			instance = new Loadscreen();
			// TODO: make logger
		}
		
		return instance;
	}
	
	public static void destroyInstance() {
		instance = null;
		System.gc();
	}

}
