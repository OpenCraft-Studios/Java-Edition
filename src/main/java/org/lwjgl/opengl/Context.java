package org.lwjgl.opengl;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Optional;

public class Context {

	public static Canvas cv = null;

	private Context() {
	}

	public static void create() {
		if (cv != null)
			return;

		cv = new Canvas();
		cv.setBackground(Color.BLACK);
		makeDefault(Display.id());
	}

	public static void makeDefault(final long id) {
		Display.window.setLayout(new BorderLayout());
		Display.window.setPreferredSize(Display.window.getSize());
		Display.window.add(cv, BorderLayout.CENTER);
	}

	public static Graphics getGraphics() {
		BufferStrategy bs = cv.getBufferStrategy();
		if (bs == null) {
			cv.createBufferStrategy(2);
			return getGraphics();
		}

		return bs.getDrawGraphics();

	}

	public static boolean shouldRender() {
		boolean properlySize = Display.width() > 136 || Display.height() > 39;
		boolean showing = Display.isShowing();
		boolean notMinimized = !Display.isMinimized();

		return properlySize && showing && notMinimized;
	}

	public static void draw() {
		BufferStrategy bs = cv.getBufferStrategy();
		if (bs == null)
			return;

		bs.show();
	}

	public static boolean usesOpenGL() {
		return Boolean.parseBoolean(Optional.ofNullable(System.getProperty("sun.java2d.opengl")).orElse("false"));
	}

}
