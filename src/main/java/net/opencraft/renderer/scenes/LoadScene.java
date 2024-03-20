package net.opencraft.renderer.scenes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.opencraft.config.GameExperiments;
import net.opencraft.logging.InternalLogger;
import net.opencraft.renderer.display.Display;
import net.opencraft.sound.Sound;
import net.opencraft.util.Assets;
import net.opencraft.util.Resource;

public class LoadScene extends Scene {

	public static final Resource RESOURCE = Resource.format("opencraft.scene:load");
	public static final Sound[] SOUNDS = { Sound.NONE };

	private static final int I_MAX = Display.HEIGHT / 2;
	private static final LoadScene instance = new LoadScene();

	private float i = Display.HEIGHT - 1;

	public LoadScene() {
		super(RESOURCE, SOUNDS);
	}

	@Override
	public void render(BufferedImage img) {
		Graphics g = img.getGraphics();

		if (GameExperiments.CLASSIC_LOAD_SCENE) {
			classicLS(g);
			return;
		}

		animatedLS(g);
	}

	public void classicLS(Graphics g) {
		if (i == Display.HEIGHT - 1)
			g.drawImage(Assets.getLoadscene(), 0, 0, Display.WIDTH, Display.HEIGHT, null);

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
				InternalLogger.out.printf("[%s] Ignored exception:\n", getClass().getName());
				ignored.printStackTrace(InternalLogger.out);
				InternalLogger.out.println();
			}
		}

		Scene.setCurrent(Scene.MENU_SCENE);
	}

	public static LoadScene getInstance() {
		return instance;
	}

}
