package net.op.render;

import java.awt.Graphics;

import net.op.render.textures.Assets;

/**
 * <h1>Renderizable</h1>
 * This interface can be used to represent a object
 * that can be drawed with graphics.
 */
public interface Renderizable {

	/**
	 * It's used for rendering the current object.
	 * @param g The graphics to write to
	 * @param assets The assets
	 */
	void render(Graphics g, Assets assets);
	
}
