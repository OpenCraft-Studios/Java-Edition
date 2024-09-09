package net.opencraft.renderer.screens;

import static net.opencraft.Locales.*;
import static net.opencraft.OpenCraft.*;
import static net.opencraft.renderer.texture.Assets.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.util.Locale;

import org.lwjgl.opengl.Display;

import net.opencraft.GameSettings;
import net.opencraft.Locales;
import net.opencraft.renderer.texture.Assets;
import net.opencraft.sound.SoundManager;
import net.opencraft.spectoland.SpectoError;
import net.opencraft.util.FontRenderer;
import net.opencraft.util.MouseUtils;

public class SettingsScreen extends Screen implements MouseListener {

	private static final SettingsScreen instance = new SettingsScreen();

	private String currentTab = "options.generalTab";

	public boolean arrow2 = false;
	public boolean arrow1 = false;
	public boolean donesel = false;
	public boolean ofcpage = false;
	public boolean musicBtn = false;

	private SettingsScreen() {
	}

	@Override
	public void render(Graphics2D g2d) {
		int width = Display.width();
		int height = Display.height();

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
		ofcpage = MouseUtils.inRange(30, height - 90, 257, 40);
		g2d.drawImage(oc.assets.getButton(donesel ? BUTTON_HIGHLIGHTED : BUTTON), (width - 175) / 2, height - 90, 175, 40,
				null);
		g2d.drawImage(oc.assets.getButton(ofcpage ? BUTTON_HIGHLIGHTED : BUTTON), 30, height - 90, 257, 40, null);

		FontRenderer font = FontRenderer.mojangles();
		font.size(20);
		font.drawShadow(g2d, translate("gui.Done"), (width - 175) / 2 + 62, height - 66, donesel ? 0xFFFFA0 : 0xFFFFFF);

		String strGotoOfcPage = translate("gui.gotoOfcPage");
		if (strGotoOfcPage.length() >= 24)
			strGotoOfcPage = strGotoOfcPage.substring(0, 21) + "...";

		font.drawShadow(g2d, strGotoOfcPage, 45, height - 66, ofcpage ? 0xFFFFA0 : 0xFFFFFF);

		arrow1 = MouseUtils.inRange((width - 400) / 2 + 100, 20, 21, 33);
		arrow2 = MouseUtils.inRange((width - 400) / 2 + 273, 20, 21, 33);

		arrow2 &= currentTab.equals("options.generalTab");
		
		g2d.drawImage(oc.assets.getArrow(arrow1 ? 3 : 1), (width - 400) / 2 + 100, 15, 21, 33, null);
		g2d.drawImage(oc.assets.getArrow(arrow2 ? 2 : 0), (width - 400) / 2 + 273, 15, 21, 33, null);

		font.drawShadow(g2d, translate(currentTab), (width - 400) / 2 + 155, 37, 0xFFFFFF);

		g2d.setColor(Color.WHITE);
		g2d.drawLine(0, 60, width, 60);
		g2d.drawLine(0, height - 100, width, height - 100);
	}

	private void drawGeneralTab(Graphics g, Assets assets) {
		FontRenderer font = FontRenderer.mojangles();

		musicBtn = MouseUtils.inRange(30, 80, 200, 40);
		g.drawImage(assets.getButton(musicBtn ? BUTTON_HIGHLIGHTED : BUTTON), 30, 80, 200, 40, null);

		font.size(20);
		font.color(musicBtn ? 0xFFFFA0 : 0xFFFFFF);
		font.drawShadow(g,
				translate("options.Music") + ": " + translate(SoundManager.MUSIC ? "options.ON" : "options.OFF"), 45,
				105);

		font.color(Color.WHITE);
	}

	private void drawLocalesTab(Graphics g) {
		// Draw buttons
		final int width = Display.width();
		final int height = Display.height();
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

		if (lang != null && MouseUtils.isButtonPressed(1))
			Locales.setLocale(lang);
	}

	public static SettingsScreen getInstance() {
		return instance;
	}

	@Override
	public void mouseReleased(MouseEvent mouse) {
		if (musicBtn) {
			SoundManager.toggle();
		} else if (arrow2)
			currentTab = "options.localesTab";
		else if (arrow1) {
			if (currentTab.equals("options.generalTab"))
				donesel = true;
			
			currentTab = "options.generalTab";
		}
		else if (ofcpage) {
			try {
				Desktop.getDesktop().browse(new URI(GameSettings.OFFICIAL_WEBPAGE));
			} catch (Exception ex) {
				SpectoError.ignored(ex, SettingsScreen.class);
			}
		}
		
		if (donesel) {
			currentTab = "options.generalTab";
			GameSettings.save();
			Screen.setCurrent(Menuscreen.class);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
