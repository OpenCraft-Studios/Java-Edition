package net.op.util;

import static java.awt.Font.TRUETYPE_FONT;
import static java.awt.Font.createFont;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;

import net.op.render.textures.Tilesheet;

public class OCFont {

	private static OCFont MONOSPACE;
	private static OCFont MOJANGLES;

	private int size = 12;
	private int color = 0;
	private Font font;

	static {
		MONOSPACE = new OCFont("Dialog");
	}

	public OCFont(Font font) {
		this.font = font;
	}

	public OCFont(OCFont other) {
		this(other.font);
	}

	public OCFont(String fontname) {
		this(getSystemFont(fontname));
	}

	public static void create(Tilesheet assets) {
		MOJANGLES = getFont(assets, "/assets/opencraft/fonts/Mojangles.ttf");
	}

	public static OCFont monospace() {
		return MONOSPACE;
	}

	public static OCFont mojangles() {
		return MOJANGLES;
	}

	public static OCFont of(Font font) {
		return new OCFont(font);
	}

	public static OCFont read(InputStream in) throws FontFormatException, IOException {
		return OCFont.of(createFont(TRUETYPE_FONT, in));
	}

	public static OCFont getFont(Tilesheet assets, String fontpath) {
		InputStream in = assets.bindOrDefault(fontpath);
		OCFont font = OCFont.monospace();
		try {
			font = read(in);
		} catch (Exception ignored) {
			// TODO Internal logger
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

	public Font getFont() {
		return this.font;
	}

}
