package net.opencraft.renderer.gui.interfaces;

import java.awt.Rectangle;

@FunctionalInterface
public interface HighlightCalculator {

	boolean shouldHighlight(Rectangle elemBounds);
	
}
