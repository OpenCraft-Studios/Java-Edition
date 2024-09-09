package net.opencraft.util;

import static java.awt.Font.TRUETYPE_FONT;
import static java.awt.Font.createFont;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.InputStream;

import net.opencraft.spectoland.SpectoError;

public class FontRenderer {

	private static Font MONOSPACE;
	private static Font UNICODE;
	private static Font MOJANGLES;
	private static Font TLRENDER;
	private static Font MINECRAFT;

	private int size = 12;
	private int color = 0;
	private Font font;

	static {
		MONOSPACE = getSystemFont("Consolas");
		UNICODE = getSystemFont("Dialog");
	}

	private FontRenderer(Font font) {
		this.font = font;
	}

	public FontRenderer(FontRenderer other) {
		this(other.font);
	}

	public static void create(String fontDir) {
		MOJANGLES = getFont(fontDir + "/mojangles.ttf", TRUETYPE_FONT);
		MINECRAFT = getFont(fontDir + "/minecraft.ttf", TRUETYPE_FONT);
		TLRENDER = getFont(fontDir + "/tlrender.ttf", TRUETYPE_FONT);
	}

	public static FontRenderer monospace() {
		return new FontRenderer(MONOSPACE);
	}

	public static FontRenderer unicode() {
		return new FontRenderer(UNICODE);
	}

	public static FontRenderer mojangles() {
		return new FontRenderer(MOJANGLES);
	}
	
	public static FontRenderer minecraft() {
		return new FontRenderer(MINECRAFT);
	}

	public static FontRenderer tlrender() {
		return new FontRenderer(TLRENDER);
	}

	public static FontRenderer of(Font font) {
		return new FontRenderer(font);
	}

	public static Font getFont(String fontpath, int type) {
		Font font = MONOSPACE;
		try {
			InputStream in = new BufferedInputStream(Files.internal(fontpath));
			font = createFont(type, in);
		} catch (Exception ex) {
			SpectoError.ignored(ex, FontRenderer.class);
		}

		return font;
	}

	public static Font getSystemFont(String fontname) {
		return new Font(fontname, Font.PLAIN, 12);
	}

	public FontRenderer size(int size) {
		this.size = size;
		return this;
	}

	public FontRenderer color(int color) {
		this.color = color;
		return this;
	}

	public FontRenderer color(Color c) {
		return color(c.getRGB());
	}

	public int getColor() {
		return this.color;
	}

	public int getSize() {
		return this.size;
	}

	public void draw(Graphics g, String text, int x, int y, int color) {
		g.setFont(this.font.deriveFont(Font.PLAIN, size));
		g.setColor(new Color(color));
		g.drawString(text, x, y);
	}

	public void draw(Graphics g, String text, int x, int y) {
		draw(g, text, x, y, this.color);
	}

	public void drawShadow(Graphics g, String text, int x, int y, int color) {
		draw(g, text, x + 2, y + 2, (color & 0xFCFCFC) >> 2);
		draw(g, text, x, y, color);
	}

	public void drawShadow(Graphics g, String text, int x, int y) {
		drawShadow(g, text, x, y, this.color);
	}

	public String getName() {
		return toFont().getName();
	}

	public Font toFont() {
		return this.font;
	}

}
