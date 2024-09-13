package net.opencraft.renderer.gui;

import net.opencraft.renderer.gui.interfaces.HighlightCalculator;

public interface IHighlight {

	boolean isHighlighted();
	void setHighlighted(boolean h);
	void setHighlighted(HighlightCalculator calc);
	
}
