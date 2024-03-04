package net.opencraft.renderer.display;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Display extends JFrame {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 854;
	public static final int HEIGHT = 480;
	
	public static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);
	
	public Display() {
		super();
	}
	
	public Display(String title) {
		super(title);
	}
	
}
