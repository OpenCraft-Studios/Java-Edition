package net.op.render.screens;

import java.awt.Graphics;

import net.op.render.Renderizable;
import net.op.render.textures.Tilesheet;
import net.op.util.Resource;

public abstract class Screen implements Renderizable {

	private static Screen current = null;
	private Resource resource;

	protected Screen(Resource resource) {
		this.resource = resource;
	}
	
	public static void setCurrent(Screen scene) {
		Screen.current = scene;
	}
	
	public static Screen getCurrent() {
		return current;
	}
	
	public static void renderCurrent(Graphics g, Tilesheet assets) {
		if (current == null)
			return;
		
		current.render(g, assets);
	}
	
	public final Resource getResource() {
		return this.resource;
	}
	
}
