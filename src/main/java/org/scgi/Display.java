package org.scgi;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Display {

	static JFrame window = null;
	
	private Display() {
	}
	
	private static void ensureCreated() {
		if (window == null)
			throw new RuntimeException("Display not created!");
	}
	
	public static void create(int width, int height, String title) {
		if (title == null)
			window = new JFrame();
		else
			window = new JFrame(title);
		
		window.setVisible(false);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setSize(width, height);
		window.setLocationRelativeTo(null);
	}
	
	public static void setResizable(boolean option) {
		ensureCreated();
		window.setResizable(option);
	}
	
	public static void setLocation(int x, int y) {
		ensureCreated();
		window.setLocation(x, y);
	}
	
	public static void setLocation(Point p) {
		ensureCreated();
		window.setLocation(p);
	}
	
	public static void setSize(Dimension dim) {
		ensureCreated();
		window.setSize(dim);
	}
	
	public static void setSize(int w, int h) {
		ensureCreated();
		window.setSize(w, h);
	}
	
	public static void show() {
		ensureCreated();
		window.setVisible(true);
	}
	
	public static void hide() {
		ensureCreated();
		window.setVisible(false);
	}
	
	public static int width() {
		ensureCreated();
		return window.getWidth();
	}
	
	public static int height() {
		ensureCreated();
		return window.getHeight();
	}
	
	public static void destroy() {
		if (window == null)
			return;
		
		window.dispose();
		window = null;
	}
	
	public static void update() {
		ensureCreated();
		window.repaint();
	}
	
	public static boolean shouldClose() {
		if (window == null)
			return true;
		
		return !window.isDisplayable();
	}
	
	public static void setIcon(Image img) {
		ensureCreated();
		window.setIconImage(img);
	}
	
	public static void setIcon(ImageIcon img) {
		setIcon(img.getImage());
	}
	
	public static void setIcons(List<Image> icons) {
		ensureCreated();
		window.setIconImages(icons);
	}
	
	public static boolean isMinimized() {
		ensureCreated();
		return (window.getExtendedState() & Frame.ICONIFIED) != 0;
	}
	
	public static boolean isShowing() {
		ensureCreated();
		return window.isShowing();
	}
	
	public static boolean isFocused() {
		ensureCreated();
		return window.isFocused();
	} 
	
	public static long id() {
		if (window == null)
			return 0;
		
		return (long) (window.hashCode() & Long.MAX_VALUE);
	}
	
}
