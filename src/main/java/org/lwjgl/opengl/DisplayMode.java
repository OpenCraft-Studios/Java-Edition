package org.lwjgl.opengl;

public class DisplayMode {

	private int width, height;
	
	public DisplayMode(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
