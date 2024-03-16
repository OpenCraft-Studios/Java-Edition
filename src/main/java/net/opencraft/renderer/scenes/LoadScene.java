package net.opencraft.renderer.scenes;

import static net.opencraft.LoggerConfig.LOG_FORMAT;
import static net.opencraft.LoggerConfig.handle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.logging.Logger;

import net.opencraft.config.GameExperiments;
import net.opencraft.renderer.display.Display;
import net.opencraft.sound.Sound;
import net.opencraft.util.Assets;
import net.opencraft.util.Resource;

public class LoadScene extends Scene {

	public static final Resource RESOURCE = Resource.format("opencraft.scene:load");
	public static final Sound SOUND = Sound.NONE;

	private static final Logger logger = Logger.getLogger("loadscene");
	private static final int I_MAX = Display.HEIGHT / 2;
	private static LoadScene instance = new LoadScene();

	private float i = Display.HEIGHT - 1;

	static {
		// Set logging format
		System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
		handle(logger);
	}

	public LoadScene() {
		super(RESOURCE, SOUND);
	}

	public void render(Graphics g) {
		if (GameExperiments.SKIP_LOAD_SCENE) {
			changeScreen(0);
			return;
		}

		if (GameExperiments.CLASSIC_LOAD_SCENE) {
			classicLS(g);
			return;
		}

		animatedLS(g);
	}

	public void classicLS(Graphics g) {
		if (i == Display.HEIGHT - 1)
			g.drawImage(Assets.getLoadscreen(), 0, 0, Display.WIDTH, Display.HEIGHT, null);

		i--;

		if (i < 100)
			changeScreen(1000);
	}

	public void animatedLS(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Display.WIDTH, Display.HEIGHT);

		// Draw OpenCraft Text
		g.setColor(Color.RED);
		g.setFont(new Font("SF Transrobotics", Font.ITALIC, 70));
		g.drawString("OpenCraft", (Display.WIDTH - 376) / 2, (int) i);

		// Draw Rectangle
		g.setColor(Color.GREEN);
		g.drawRect((Display.WIDTH - 451) / 2, (Display.HEIGHT - 122) / 2, 421, 89);

		/* OpenCraft Text Slide Up */
		if (i <= I_MAX) {
			i = I_MAX;
			changeScreen(3000);
		} else
			i -= 2;
	}

	public void changeScreen(long time) {
		if (time > 1) {
			try {
				Thread.sleep(time);
			} catch (Exception ignored) {
				logger.severe("Cannot wait for Thread.sleep()!");
			}
		}

		Scene.setCurrent(Scene.TITLE_SCENE);
	}

	public static LoadScene renewInstance() {
		return instance = new LoadScene();
	}

	public static LoadScene getInstance() {
		return instance;
	}

}
