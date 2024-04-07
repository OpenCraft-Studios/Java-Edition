package net.op.render.scenes;

import static net.op.render.display.DisplayManager.getDisplay;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import net.op.Client;
import net.op.render.display.Display;
import net.op.render.textures.Assets;
import net.op.util.OCFont;
import net.op.util.Resource;

public class Loadscreen extends Screen {

	public static final Resource RESOURCE = Resource.format("opencraft:screens.loadscreen");

	private static int I_MAX = Display.HEIGHT / 2;
	public static Loadscreen instance = create();

	private float i = Display.HEIGHT - 1;
	private long start = -1;

	private Loadscreen() {
		super(RESOURCE);
	}

	public static Loadscreen create() {
		return new Loadscreen();
	}

	@Override
	public void render(Graphics g, Assets assets) {
		if (start == -1)
			start = System.currentTimeMillis();

		animatedLS(g);
	}

	public void animatedLS(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Display.WIDTH, Display.HEIGHT);

		// Draw OpenCraft Text
		g.setColor(Color.MAGENTA);
		g.setFont(OCFont.getSystemFont("SF Transrobotics").deriveFont(Font.BOLD, 26));
		g.drawString(Client.CODENAME.toUpperCase(), (Display.WIDTH - 335) / 2, (Display.HEIGHT + 135) / 2);

		// Draw OpenCraft Text
		g.setColor(Color.RED);
		g.setFont(g.getFont().deriveFont(Font.ITALIC, 70));
		g.drawString("OpenCraft", (Display.WIDTH - 376) / 2, (int) i);

		// Draw Rectangle
		g.setColor(Color.GREEN);
		g.drawRoundRect((Display.WIDTH - 451) / 2, (Display.HEIGHT - 164) / 2, 421, 112, 15, 15);

		/* OpenCraft Text Slide Up */
		if (i <= I_MAX) {
			attemptToChange();
		} else
			i -= 2;

	}

	public void attemptToChange() {
		if (System.currentTimeMillis() < (start + 3000))
			return;

		instance = null;
		System.gc();
		getDisplay().setResizable(true);
		Screen.setCurrent(MenuScreen.getInstance());
	}

	public static Loadscreen getInstance() {
		return instance;
	}

}
