package net.op.util;

import static java.awt.Font.TRUETYPE_FONT;
import static java.awt.Font.createFont;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.InputStream;

import net.op.spectoland.SpectoError;

public class OCFont {

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

	private OCFont(Font font) {
		this.font = font;
	}

	public OCFont(OCFont other) {
		this(other.font);
	}

	public static void create(String fontDir) {
		MOJANGLES = getFont(fontDir + "/mojangles.ttf", TRUETYPE_FONT);
		MINECRAFT = getFont(fontDir + "/minecraft.ttf", TRUETYPE_FONT);
		TLRENDER = getFont(fontDir + "/tlrender.ttf", TRUETYPE_FONT);
	}

	public static OCFont monospace() {
		return new OCFont(MONOSPACE);
	}

	public static OCFont unicode() {
		return new OCFont(UNICODE);
	}

	public static OCFont mojangles() {
		return new OCFont(MOJANGLES);
	}
	
	public static OCFont minecraft() {
		return new OCFont(MINECRAFT);
	}

	public static OCFont tlrender() {
		return new OCFont(TLRENDER);
	}

	public static OCFont of(Font font) {
		return new OCFont(font);
	}

	public static Font getFont(String fontpath, int type) {
		Font font = MONOSPACE;
		try {
			InputStream in = new BufferedInputStream(ResourceGetter.getInternal(fontpath));
			font = createFont(type, in);
		} catch (Exception ex) {
			SpectoError.ignored(ex, OCFont.class);
		}

		return font;
	}

	public static Font getSystemFont(String fontname) {
		return new Font(fontname, Font.PLAIN, 12);
	}

	public OCFont size(int size) {
		this.size = size;
		return this;
	}

	public OCFont color(int color) {
		this.color = color;
		return this;
	}

	public OCFont color(Color c) {
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
