package net.opencraft.renderer.screens;

import static java.awt.AlphaComposite.*;
import static java.awt.image.BufferedImage.*;
import static net.opencraft.OpenCraft.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.lwjgl.opengl.Display;

import net.opencraft.renderer.gui.GuiProgressBar;

public class Loadscreen extends Screen {

	private static Menuscreen nextScreen = Menuscreen.getInstance();
	private static final double TRANSITION_DURATION = 1e3; // Original value: 3250

	public static Loadscreen instance = getInstance();
	private GuiProgressBar progressBar = new GuiProgressBar();
	private long start = -1;
	
	private Loadscreen() {
	}

	@Override
	public void render(Graphics2D g2d) {
		animatedLS(g2d, true);
	}

	public void animatedLS(Graphics2D g2d, boolean updateProgress) {
		staticLS(g2d);
		
		// Update progress bar
		if (!updateProgress)
			return;
		
		if (progressBar.getProgress() > 99)
			attemptToChange(g2d);
		else
			progressBar.addProgress(0.42f);
	}

	private void staticLS(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 854, 480);

		// Draw OpenCraft Text

		BufferedImage loadingLogo = oc.assets.getLoadingLogo();
		{	
			int logoW = 840;
			int logoH = 445;
			
			int logoX = 0;
			int logoY = 0;
			
			g2d.drawImage(loadingLogo, logoX, logoY, logoW, logoH, null);
		}
		
		progressBar.setSize(604, 24);
		progressBar.setLocation(124, 358);
		progressBar.draw(g2d);
	}

	private void attemptToChange(Graphics2D g2d) {
		final long current = System.currentTimeMillis();

		if (start == -1)
			start = current;
		
		if (current - start <= TRANSITION_DURATION) {
			double alpha = 1 - (current - start) / TRANSITION_DURATION;

			BufferedImage bi = new BufferedImage(854, 480, TYPE_INT_ARGB);

			Graphics2D gbi = bi.createGraphics();
			nextScreen.render(g2d);

			AlphaComposite ac = AlphaComposite.getInstance(SRC_OVER, (float) alpha);
			gbi.setComposite(ac);
			animatedLS(gbi, false);

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
