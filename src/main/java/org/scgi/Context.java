package org.scgi;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

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
		var bs = cv.getBufferStrategy();
		if (bs == null) {
			cv.createBufferStrategy(2);
			return getGraphics();
		}
			
		return bs.getDrawGraphics();
		
	}
	
	public static boolean shouldRender() {
		return !Display.isMinimized() && Display.isShowing();
	}
	
	public static void draw() {
		var bs = cv.getBufferStrategy();
		if (bs == null)
			return;
		
		bs.show();
	}

}
