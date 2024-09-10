package org.lwjgl.opengl;

import java.awt.*;
import java.awt.event.ComponentListener;
import java.util.List;

import javax.swing.*;

public class Display {

	public static JFrame window = null;
	
	private Display() {
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
		
		window.setLayout(new BorderLayout());
	}
	
	public static void setResizable(boolean option) {
		window.setResizable(option);
	}
	
	public static void setLocation(int x, int y) {
		window.setLocation(x, y);
	}
	
	public static void setLocation(Point p) {
		window.setLocation(p);
	}
	
	public static void setSize(Dimension dim) {
		window.setSize(dim);
	}
	
	public static void setSize(int w, int h) {
		window.setSize(w, h);
	}
	
	public static void show() {
		window.setVisible(true);
	}
	
	public static void hide() {
		window.setVisible(false);
	}
	
	public static int getWidth() {
		return window.getWidth();
	}
	
	public static int getHeight() {
		return window.getHeight();
	}
	
	public static void destroy() {
		if (window == null)
			return;
		
		window.dispose();
		window = null;
	}
	
	public static void update() {
		if (window == null)
			return;
		
		window.repaint();
	}
	
	public static boolean isCloseRequested() {
		if (window == null)
			return true;
		
		return !window.isDisplayable();
	}
	
	public static void setIcon(Image img) {
		window.setIconImage(img);
	}
	
	public static void setIcon(ImageIcon img) {
		setIcon(img.getImage());
	}
	
	public static void setIcons(List<Image> icons) {
		window.setIconImages(icons);
	}
	
	public static boolean isMinimized() {
		return (window.getExtendedState() & Frame.ICONIFIED) != 0;
	}
	
	public static boolean isShowing() {
		return window.isShowing();
	}
	
	public static boolean isFocused() {
		return window.isFocused();
	}

	public static void addChild(JPanel canvas) {
		window.add(canvas, BorderLayout.CENTER);
		if (canvas instanceof ComponentListener)
			window.addComponentListener((ComponentListener) canvas);
	}
	
}
