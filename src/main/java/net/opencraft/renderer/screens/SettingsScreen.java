package net.opencraft.renderer.screens;

import static net.opencraft.Locales.*;
import static net.opencraft.OpenCraft.*;
import static net.opencraft.renderer.gui.GuiArrow.*;
import static net.opencraft.renderer.gui.GuiButton.*;

import java.awt.*;
import java.util.Locale;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import net.opencraft.GameSettings;
import net.opencraft.Locales;
import net.opencraft.renderer.gui.GuiArrow;
import net.opencraft.renderer.texture.Assets;
import net.opencraft.sound.SoundManager;
import net.opencraft.util.FontRenderer;
import net.opencraft.util.MouseUtils;

public class SettingsScreen extends Screen {

	private static final SettingsScreen instance = new SettingsScreen();

	private String currentTab = "options.generalTab";

	private GuiArrow back_arrow, next_arrow;
	
	public boolean donesel = false;
	public boolean musicBtn = false;

	private SettingsScreen() {
		back_arrow = new GuiArrow();
		back_arrow.setSize(21, 33);
		back_arrow.setDirection(ARROW_LEFT);
		
		next_arrow = new GuiArrow();
		next_arrow.setSize(21, 33);
		next_arrow.setDirection(ARROW_RIGHT);
		
		
	}

	@Override
	public void render(Graphics2D g2d) {
		pollEvents();
		int width = Display.getWidth();
		int height = Display.getHeight();

		g2d.setPaintMode();
		g2d.setColor(Color.WHITE);

		Composite defcomposite = g2d.getComposite();
		for (int x = 0; x < width; x += 64) {
			for (int y = 0; y < height; y += 64) {
				if (y <= 60 || y >= (height - 100))
					g2d.setComposite(defcomposite);
				else
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

				g2d.setColor(Color.BLACK);
				g2d.fillRect(x, y, 64, 64);
				g2d.drawImage(oc.assets.getBackground(), x, y, 64, 64, null);
			}
		}
		g2d.setComposite(defcomposite);

		if (currentTab.equalsIgnoreCase("options.generalTab")) {
			drawGeneralTab(g2d, oc.assets);
		} else if (currentTab.equalsIgnoreCase("options.localesTab")) {
			drawLocalesTab(g2d);
		}

		/*-------------------------------*/
		donesel = MouseUtils.inRange((width - 175) / 2, height - 90, 175, 40);
		g2d.drawImage(oc.assets.getButton(donesel ? BUTTON_HIGHLIGHTED : BUTTON_NORMAL), (width - 175) / 2, height - 90, 175, 40,
				null);

		FontRenderer font = FontRenderer.mojangles();
		font.size(20);
		font.drawShadow(g2d, translate("gui.Done"), (width - 175) / 2 + 62, height - 66, donesel ? 0xFFFFA0 : 0xFFFFFF);
		
		next_arrow.setLocation((width - 400) / 2 + 273, 15);
		next_arrow.setHighlighted(MouseUtils.inRange(next_arrow)
				&& currentTab.equals("options.generalTab"));
		next_arrow.draw(g2d);
		
		back_arrow.setLocation((width - 400) / 2 + 100, 15);
		back_arrow.setHighlighted(MouseUtils.inRange(back_arrow));
		back_arrow.draw(g2d);
		
		//g2d.drawImage(oc.assets.getArrow(arrow1 ? ARROW_HIGHLIGHTED | ARROW_LEFT  : ARROW_NORMAL | ARROW_LEFT), (width - 400) / 2 + 100, 15, 21, 33, null);
		//g2d.drawImage(oc.assets.getArrow(arrow2 ? ARROW_HIGHLIGHTED | ARROW_RIGHT : ARROW_NORMAL | ARROW_RIGHT), (width - 400) / 2 + 273, 15, 21, 33, null);

		font.drawShadow(g2d, translate(currentTab), (width - 400) / 2 + 155, 37, 0xFFFFFF);

		g2d.setColor(Color.WHITE);
		g2d.setStroke(new BasicStroke(2F));
		g2d.drawLine(0, 60, width, 60);
		g2d.drawLine(0, height - 100, width, height - 100);
	}

	private void drawGeneralTab(Graphics g, Assets assets) {
		FontRenderer font = FontRenderer.mojangles();

		musicBtn = MouseUtils.inRange(30, 80, 200, 40);
		g.drawImage(assets.getButton(musicBtn ? BUTTON_HIGHLIGHTED : BUTTON_NORMAL), 30, 80, 200, 40, null);

		font.size(20);
		font.color(musicBtn ? 0xFFFFA0 : 0xFFFFFF);
		font.drawShadow(g,
				translate("options.Music") + ": " + translate(SoundManager.MUSIC ? "options.ON" : "options.OFF"), 45,
				105);

	}

	private void drawLocalesTab(Graphics g) {
		// Draw buttons
		final int width = Display.getWidth();
		final int height = Display.getHeight();
		final String strLang = Locales.getGenericName(Locales.getLocale());

		FontRenderer font = FontRenderer.mojangles();
		font.size(20);

		boolean enLang, spLang, itLang, frLang, glLang, caLang, ptLang;
		enLang = false;
		spLang = false;
		itLang = false;
		frLang = false;
		glLang = false;
		caLang = false;
		ptLang = false;

		if (height >= 220) {
			enLang = MouseUtils.inRange((width - 350) / 2 - 1, 80 - 1, 350 + 1, 40 + 1);

			g.setColor(Color.BLACK);
			g.fillRect((width - 350) / 2, 80, 350, 40);
			g.setColor(new Color((strLang.equalsIgnoreCase("English") || enLang) ? 0xFFFFFF : 0x454545));
			g.drawRect((width - 350) / 2 - 1, 80 - 1, 350 + 1, 40 + 1);

			font.color(Color.WHITE);
			font.draw(g, "English (United States)", (width - 237) / 2, 80 + 25);
		}

		if (height >= 270) {
			spLang = MouseUtils.inRange((width - 350) / 2 - 1, 80 + 50 - 1, 350 + 1, 40 + 1);

			g.setColor(Color.BLACK);
			g.fillRect((width - 350) / 2, 80 + 50, 350, 40);
			g.setColor(new Color((strLang.equalsIgnoreCase("Spanish") || spLang) ? 0xFFFFFF : 0x454545));
			g.drawRect((width - 350) / 2 - 1, 80 + 50 - 1, 350 + 1, 40 + 1);

			font.color(Color.WHITE);
			font.draw(g, "Español (Argentina)", (width - 207) / 2, 80 + 50 + 25);
		}

		if (height >= 320) {
			itLang = MouseUtils.inRange((width - 350) / 2 - 1, 80 + 50 * 2 - 1, 350 + 1, 40 + 1);

			g.setColor(Color.BLACK);
			g.fillRect((width - 350) / 2, 80 + 50 * 2, 350, 40);
			g.setColor(new Color((strLang.equalsIgnoreCase("Italian") || itLang) ? 0xFFFFFF : 0x454545));
			g.drawRect((width - 350) / 2 - 1, 80 + 50 * 2 - 1, 350 + 1, 40 + 1);

			font.color(Color.WHITE);
			font.draw(g, "Italiano (Italia)", (width - 175) / 2, 80 + 50 * 2 + 25);
		}

		if (height >= 370) {
			frLang = MouseUtils.inRange((width - 350) / 2 - 1, 80 + 50 * 3 - 1, 350 + 1, 40 + 1);

			g.setColor(Color.BLACK);
			g.fillRect((width - 350) / 2, 80 + 50 * 3, 350, 40);
			g.setColor(new Color((strLang.equalsIgnoreCase("French") || frLang) ? 0xFFFFFF : 0x454545));
			g.drawRect((width - 350) / 2 - 1, 80 + 50 * 3 - 1, 350 + 1, 40 + 1);

			font.color(Color.WHITE);
			font.draw(g, "Français (France)", (width - 200) / 2, 80 + 50 * 3 + 25);
		}

		if (height >= 420) {
			glLang = MouseUtils.inRange((width - 350) / 2 - 1, 80 + 50 * 4 - 1, 350 + 1, 40 + 1);

			g.setColor(Color.BLACK);
			g.fillRect((width - 350) / 2, 80 + 50 * 4, 350, 40);
			g.setColor(new Color((strLang.equalsIgnoreCase("Galician") || glLang) ? 0xFFFFFF : 0x454545));
			g.drawRect((width - 350) / 2 - 1, 80 + 50 * 4 - 1, 350 + 1, 40 + 1);

			font.color(Color.WHITE);
			font.draw(g, "Galego (España)", (width - 178) / 2, 80 + 50 * 4 + 25);
		}

		if (height >= 470) {
			caLang = MouseUtils.inRange((width - 350) / 2 - 1, 80 + 50 * 5 - 1, 350 + 1, 40 + 1);

			g.setColor(Color.BLACK);
			g.fillRect((width - 350) / 2, 80 + 50 * 5, 350, 40);
			g.setColor(new Color((strLang.equalsIgnoreCase("Catalan") || caLang) ? 0xFFFFFF : 0x454545));
			g.drawRect((width - 350) / 2 - 1, 80 + 50 * 5 - 1, 350 + 1, 40 + 1);

			font.color(Color.WHITE);
			font.draw(g, "Catalá (Espanya)", (width - 184) / 2, 80 + 50 * 5 + 25);
		}

		if (height >= 512) {
			ptLang = MouseUtils.inRange((width - 350) / 2 - 1, 80 + 50 * 6 - 1, 350 + 1, 40 + 1);

			g.setColor(Color.BLACK);
			g.fillRect((width - 350) / 2, 80 + 50 * 6, 350, 40);
			g.setColor(new Color((strLang.equalsIgnoreCase("Portuguese") || ptLang) ? 0xFFFFFF : 0x454545));
			g.drawRect((width - 350) / 2 - 1, 80 + 50 * 6 - 1, 350 + 1, 40 + 1);

			font.color(Color.WHITE);
			font.draw(g, "Português (Portugal)", (width - 215) / 2, 80 + 50 * 6 + 25);
		} else {
			font.size(25);
			font.drawShadow(g, "...", (width - 20) / 2, height - 111);
		}

		Locale lang = null;
		if (enLang)
			lang = Locales.of("en-US");
		else if (spLang)
			lang = Locales.of("es-AR");
		else if (itLang)
			lang = Locales.of("it-IT");
		else if (frLang)
			lang = Locales.of("fr-FR");
		else if (glLang)
			lang = Locales.of("gl-ES");
		else if (caLang)
			lang = Locales.of("ca-ES");
		else if (ptLang)
			lang = Locales.of("pt-PT");

		if (lang != null && Mouse.isButtonDown(1))
			Locales.setLocale(lang);
	}

	public static SettingsScreen getInstance() {
		return instance;
	}

	public void pollEvents() {
		if (!Mouse.isButtonDown(1)) {
			Mouse.poll();
			return;
		}
		Mouse.poll();
		
		if (musicBtn) {
			SoundManager.toggleSound();
		} else if (next_arrow.isHighlighted()) {
			currentTab = "options.localesTab";
		} else if (back_arrow.isHighlighted()) {
			if (currentTab.equals("options.generalTab"))
				donesel = true;
			
			currentTab = "options.generalTab";
		}
		
		if (donesel) {
			currentTab = "options.generalTab";
			GameSettings.save();
			Screen.setCurrent(Menuscreen.class);
		}
	}
	
	@Override
	public String toString() {
		return super.toString(); //+ "#" + currentTab.substring(8, 15); lateeer...
	}

}
