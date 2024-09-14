package net.opencraft.renderer.gui;

import static net.opencraft.Locales.*;
import static net.opencraft.OpenCraft.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

import net.opencraft.renderer.gui.interfaces.*;
import net.opencraft.util.FontRenderer;

public class GuiButton extends GuiElement
	implements IUpdateable, IState, IHighlight, IEnabled {

	public static final int BUTTON_DISABLED    = 0,
							BUTTON_NORMAL      = 1,
							BUTTON_HIGHLIGHTED = 2;
	
	private BufferedImage tex = null;
	private int state = BUTTON_NORMAL;
	
	private FontRenderer font;
	private String text;
	
	public GuiButton(String text, FontRenderer font) {
		setText(text);
		setFont(font);
	}
	
	public GuiButton(FontRenderer font) {
		this("", font);
	}
	
	public void setFont(FontRenderer font) {
		this.font = Objects.requireNonNull(font);
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		if (tex == null)
			this.update();
		
		// Draw button
		g2d.drawImage(tex, x, y, width, height, null);
		
		// Draw text
		font.color(isHighlighted() ? 0xFFFFA0 : 0xFFFFFF);
		font.size(16);
		FontMetrics metrix = font.metrics(g2d);

		int textX = (width - metrix.stringWidth(text)) / 2 + x;
		int textY = (height - metrix.getHeight()) / 2 + metrix.getAscent() + y;
		
		if (isEnabled())
			font.drawShadow(g2d, text, textX, textY);
		else
			font.draw(g2d, text, textX, textY, 0xA0A0A0);
	}
	
	@Override
	public void update() {
		this.tex = oc.assets.getButton(getState());
	}

	@Override
	public int getState() {
		return state;
	}

	@Override
	public boolean isHighlighted() {
		return state == BUTTON_HIGHLIGHTED;
	}

	@Override
	public void setHighlighted(boolean highlighted) {
		if (!isEnabled())
			return;
		if (state == BUTTON_HIGHLIGHTED && highlighted)
			return;
		
		setState(highlighted ? BUTTON_HIGHLIGHTED : BUTTON_NORMAL);
	}

	@Override
	public boolean isEnabled() {
		return state != BUTTON_DISABLED;
	}

	@Override
	public void setState(int state) {
		this.state = state;
		this.update();
	}
	
	public void setText(String text) {
		this.text = Objects.requireNonNull(text);
	}

	@Override
	public void setHighlighted(HighlightCalculator calc) {
		setHighlighted(calc.shouldHighlight(getBounds()));
	}

	@Override
	public void setEnabled(boolean enabled) {
		setState(enabled ? BUTTON_NORMAL : BUTTON_DISABLED);
	}

	public void setTranslatedText(String component) {
		this.text = translate(component);
	}

}
