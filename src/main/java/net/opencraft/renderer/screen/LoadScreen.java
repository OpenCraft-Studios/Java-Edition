package net.opencraft.renderer.screen;

import static net.opencraft.LoggerConfig.LOG_FORMAT;
import static net.opencraft.LoggerConfig.handle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.logging.Logger;

import net.opencraft.config.GameExperiments;
import net.opencraft.renderer.Renderizable;
import net.opencraft.renderer.display.Display;
import net.opencraft.util.Assets;
import net.opencraft.util.Resource;

public class LoadScreen implements Renderizable {

	public static final Resource RESOURCE = Resource.format("opencraft.screen:load");
	public static final Screens SCREEN = Screens.LOAD_SCREEN;

	private static final Logger logger = Logger.getLogger("loadscreen");
	private static final int I_MAX = Display.HEIGHT / 2;
	private static LoadScreen instance = new LoadScreen();

	private float i = Display.HEIGHT + 1;

	static {
		// Set logging format
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
		handle(logger);
	}

	public void render(Graphics g) {
		if (GameExperiments.SKIP_LOAD_SCREEN) {
			changeScreen(0);
			return;
		}
		
		if (GameExperiments.CLASSIC_LOAD_SCREEN) {
			classicLS(g);
			return;
		}
			
		animatedLS(g);
	}

	public void classicLS(Graphics g) {
		g.drawImage(Assets.getLoadscreen(), 0, 0, Display.WIDTH, Display.HEIGHT, null);
		i--;

		if (i < 100)
			changeScreen(1200);
	}

	public void animatedLS(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Display.WIDTH, Display.HEIGHT);

		// Draw OpenCraft Text
		g.setColor(Color.RED);
		g.setFont(new Font("SF Transrobotics", Font.ITALIC, 70));
		g.drawString("OpenCraft", Display.WIDTH / 2 - 190, (int) i);

		// Draw Rectangle
		g.setColor(Color.GREEN);
		g.drawRect(Display.WIDTH / 2 - 250, I_MAX - 70, 450, 100);

		/* OpenCraft Text Slide Down */
		if (i <= I_MAX) {
			i = I_MAX;
			changeScreen(3000);
		} else
			i -= 2;

	}

	public void changeScreen(long time) {
		if (time < 1) {
			try {
				Thread.sleep(time);
			} catch (Exception ignored) {
				logger.severe("Cannot wait for Thread.sleep()!");
			}
		}

		Screens.setCurrent(Screens.TITLE_SCREEN);
	}

	public static LoadScreen renewInstance() {
		return instance = new LoadScreen();
	}

	public static LoadScreen getInstance() {
		return instance;
	}

}
