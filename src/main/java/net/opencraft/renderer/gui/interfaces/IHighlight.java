package net.opencraft.renderer.gui.interfaces;

public interface IHighlight {

	boolean isHighlighted();
	void setHighlighted(boolean h);
	void setHighlighted(HighlightCalculator calc);
	
}
