package net.opencraft.client;

import static net.opencraft.renderer.display.DisplayManager.destroyDisplay;

import java.awt.Graphics;

import net.opencraft.config.Workspace;
import net.opencraft.renderer.RenderDragon;
import net.opencraft.renderer.screen.LoadScreen;
import net.opencraft.renderer.screen.Screens;
import net.opencraft.util.Screen;

public class Game implements Runnable {

	public static final String NAME = "OpenCraft";
	public static final String VERSION = "24r01";
	public static final VersionType VERSION_TYPE = VersionType.RELEASE;
	public static final String TITLE = NAME + " " + VERSION + VERSION_TYPE.menuScreenTitle();

	public static enum VersionType {
		RELEASE("release"), INDEV("indev"), ALPHA("alpha"), BETA("beta"), PRECLASSIC("pre-classic"),
		SNAPSHOT("snapshot");

		private String name;

		VersionType(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
		
		public String menuScreenTitle() {
			if (this.equals(RELEASE))
				return "";
			
			return " " + this.name;
		}

		public VersionType fromString(String str) {
			return switch (str.toUpperCase()) {
				case "RELEASE" -> VersionType.RELEASE;
				case "INDEV" -> VersionType.INDEV;
				case "ALPHA" -> VersionType.ALPHA;
				case "BETA" -> VersionType.BETA;
				case "PRE-CLASSIC" -> VersionType.PRECLASSIC;
				case "SNAPSHOT" -> VersionType.SNAPSHOT;
				default -> null;
			};
		}

	}
	
	public static final int NANOSECONDS = 1000000000;
	public static byte TICK_RATE = 60;
	public static final double NANO_PER_TICK = NANOSECONDS / TICK_RATE;

	private static boolean running = false;
	private Screen screen;

	@Override
	public void run() {
		init();

		long lastUpdate = System.nanoTime();

		double timePassed;
		double delta = 0;

		while (running) {
			final long loopStart = System.nanoTime();

			timePassed = loopStart - lastUpdate;
			lastUpdate = loopStart;

			delta += timePassed / NANO_PER_TICK;

			while (delta >= 1) {
				runStep();
				delta--;
			}

		}

	}

	public void tick() {
	}

	public void render() {
		Graphics g = screen.getGraphics();
		Screens.getCurrent().getScene().render(g);
		RenderDragon.update();

	}

	public void runStep() {
		tick();
		render();
	}

	public void stop() {
		screen.getGraphics().dispose();
		destroyDisplay();
	}

	public void init() {
		Thread.currentThread().setName("Game Thread");

		Workspace.create();

		RenderDragon.init();
		screen = RenderDragon.getScreen();
		Screens.setCurrent(LoadScreen.SCREEN);

		running = true;
	}

}
