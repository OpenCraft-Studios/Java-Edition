package net.op.render.screens;

import static net.op.language.Translations.GOTO_OFC_PAGE;
import static net.op.language.Translations.GUI_DONE;
import static net.op.language.Translations.OPTIONS_GENERALTAB;
import static net.op.language.Translations.OPTIONS_LOCALESTAB;
import static net.op.language.Translations.OPTIONS_MUSIC;
import static net.op.language.Translations.OPTIONS_OFF;
import static net.op.language.Translations.OPTIONS_ON;
import static net.op.render.display.DisplayManager.getDisplayHeight;
import static net.op.render.display.DisplayManager.getDisplayWidth;
import static net.op.render.textures.GUITilesheet.BUTTON;
import static net.op.render.textures.GUITilesheet.BUTTON_HIGHLIGHTED;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.net.URI;
import java.util.Locale;

import net.op.InternalLogger;
import net.op.input.MouseUtils;
import net.op.language.Locales;
import net.op.render.textures.GUITilesheet;
import net.op.sound.SoundManager;
import net.op.util.OCFont;
import net.op.util.Resource;

public class SettingsScreen extends Screen {

	public static final Resource RESOURCE = Resource.format("opencraft:screens.settings_screen");
	private static final SettingsScreen instance = SettingsScreen.create();

	private String currentTab = "options.generalTab";

	private SettingsScreen() {
		super(RESOURCE);
	}

	private static SettingsScreen create() {
		return new SettingsScreen();
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		GUITilesheet gts = GUITilesheet.getInstance();

		int width = getDisplayWidth();
		int height = getDisplayHeight();

		OCFont font = OCFont.mojangles();

		g.setPaintMode();
		g.setColor(Color.WHITE);

		Composite defcomposite = g2d.getComposite();
		for (int x = 0; x < width; x += 64) {
			for (int y = 0; y < height; y += 64) {
				boolean maximumY = (y / 64) >= Math.floor(height / 64) - 1.5;

				if (y == 0 || maximumY)
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				else
					g2d.setComposite(defcomposite);

				g2d.setColor(Color.BLACK);
				g2d.fillRect(x, y, 64, 64);
				g2d.drawImage(gts.getBackground(), x, y, 64, 64, null);
			}
		}
		g2d.setComposite(defcomposite);

		if (currentTab.equalsIgnoreCase("options.generalTab")) {
			drawGeneralTab(g);
		} else if (currentTab.equalsIgnoreCase("options.localesTab")) {
			drawLocalesTab(g);
		}

		/*-------------------------------*/
		boolean donesel = MouseUtils.inRange((width - 175) / 2, height - 90, 175, 40);
		boolean ofcpage = MouseUtils.inRange(30, height - 90, 257, 40);
		g.drawImage(gts.getButton(donesel ? BUTTON_HIGHLIGHTED : BUTTON), (width - 175) / 2, height - 90, 175, 40,
				null);
		g.drawImage(gts.getButton(ofcpage ? BUTTON_HIGHLIGHTED : BUTTON), 30, height - 90, 257, 40, null);

		font.drawShadow(g, Locales.CURRENT[GUI_DONE], (width - 175) / 2 + 62, height - 66,
				donesel ? 0xFFFFA0 : 0xFFFFFF);

		String strGotoOfcPage = Locales.CURRENT[GOTO_OFC_PAGE];
		if (strGotoOfcPage.length() >= 24)
			strGotoOfcPage = strGotoOfcPage.substring(0, 21) + "...";

		font.drawShadow(g, strGotoOfcPage, 45, height - 66, ofcpage ? 0xFFFFA0 : 0xFFFFFF);

		boolean arrow1 = MouseUtils.inRange((width - 400) / 2 + 100, 20, 21, 33);
		boolean arrow0 = MouseUtils.inRange((width - 400) / 2 + 273, 20, 21, 33);

		g.drawImage(gts.getArrow(arrow1 ? 3 : 1), (width - 400) / 2 + 100, 15, 21, 33, null);
		g.drawImage(gts.getArrow(arrow0 ? 2 : 0), (width - 400) / 2 + 273, 15, 21, 33, null);

		font.drawShadow(g, Locales.CURRENT[currentTab.equalsIgnoreCase("options.generalTab") ? OPTIONS_GENERALTAB
				: OPTIONS_LOCALESTAB], (width - 400) / 2 + 155, 37, 0xFFFFFF);

		g.setColor(Color.WHITE);
		g.drawLine(0, 60, width, 60);
		g.drawLine(0, height - 100, width, height - 100);

		check(1, donesel, () -> {
			currentTab = "options.generalTab";
			Screen.setCurrent(MenuScreen.getInstance());
		});

		check(1, arrow0, () -> {
			currentTab = "options.localesTab";
		});

		check(1, arrow1, () -> {
			currentTab = "options.generalTab";
		});

		check(1, ofcpage, () -> {
			try {
				Desktop.getDesktop().browse(new URI("https://opencraftmc.github.io"));
				Thread.sleep(150);
			} catch (Exception ex) {
				InternalLogger.out.println(SettingsScreen.class.getName() + " ->");
				ex.printStackTrace(InternalLogger.out);
				InternalLogger.out.println();
			}
		});

	}

	private void drawGeneralTab(Graphics g) {
		GUITilesheet gts = GUITilesheet.getInstance();
		OCFont font = OCFont.mojangles();

		boolean musicBtn = MouseUtils.inRange(30, 80, 200, 40);
		g.drawImage(gts.getButton(musicBtn ? BUTTON_HIGHLIGHTED : BUTTON), 30, 80, 200, 40, null);

		font.size(20);
		font.color(musicBtn ? 0xFFFFA0 : 0xFFFFFF);
		font.drawShadow(g,
				Locales.CURRENT[OPTIONS_MUSIC] + ": " + Locales.CURRENT[SoundManager.MUSIC ? OPTIONS_ON : OPTIONS_OFF],
				45, 105);

		font.color(Color.WHITE);

		check(1, musicBtn, () -> {
			SoundManager.toggle();
			try {
				Thread.sleep(90);
			} catch (Exception ex) {
				InternalLogger.out.println(SettingsScreen.class.getName() + " ->");
				ex.printStackTrace(InternalLogger.out);
				InternalLogger.out.println();
			}
		});
	}

	private void drawLocalesTab(Graphics g) {
		// Draw buttons
		final int width = getDisplayWidth();
		final String strLang = Locales.getGenericName(Locales.getLocale());

		OCFont font = OCFont.mojangles();

		boolean enLang, spLang, itLang, frLang, glLang, caLang;
		enLang = MouseUtils.inRange((width - 350) / 2 - 1, 80 - 1, 350 + 1, 40 + 1);
		spLang = MouseUtils.inRange((width - 350) / 2 - 1, 80 + 50 - 1, 350 + 1, 40 + 1);
		itLang = MouseUtils.inRange((width - 350) / 2 - 1, 80 + 50 * 2 - 1, 350 + 1, 40 + 1);
		frLang = MouseUtils.inRange((width - 350) / 2 - 1, 80 + 50 * 3 - 1, 350 + 1, 40 + 1);
		glLang = MouseUtils.inRange((width - 350) / 2 - 1, 80 + 50 * 4 - 1, 350 + 1, 40 + 1);
		caLang = MouseUtils.inRange((width - 350) / 2 - 1, 80 + 50 * 5 - 1, 350 + 1, 40 + 1);

		g.setColor(Color.BLACK);
		g.fillRect((width - 350) / 2, 80, 350, 40);
		g.setColor(new Color((strLang.equalsIgnoreCase("English") || enLang) ? 0xFFFFFF : 0x454545));
		g.drawRect((width - 350) / 2 - 1, 80 - 1, 350 + 1, 40 + 1);

		font.color(Color.WHITE);
		font.draw(g, "English (United States)", (width - 237) / 2, 80 + 25);

		g.setColor(Color.BLACK);
		g.fillRect((width - 350) / 2, 80 + 50, 350, 40);
		g.setColor(new Color((strLang.equalsIgnoreCase("Spanish") || spLang) ? 0xFFFFFF : 0x454545));
		g.drawRect((width - 350) / 2 - 1, 80 + 50 - 1, 350 + 1, 40 + 1);

		font.draw(g, "Español (Argentina)", (width - 207) / 2, 80 + 50 + 25);

		g.setColor(Color.BLACK);
		g.fillRect((width - 350) / 2, 80 + 50 * 2, 350, 40);
		g.setColor(new Color((strLang.equalsIgnoreCase("Italian") || itLang) ? 0xFFFFFF : 0x454545));
		g.drawRect((width - 350) / 2 - 1, 80 + 50 * 2 - 1, 350 + 1, 40 + 1);

		font.draw(g, "Italiano (Italia)", (width - 175) / 2, 80 + 50 * 2 + 25);

		g.setColor(Color.BLACK);
		g.fillRect((width - 350) / 2, 80 + 50 * 3, 350, 40);
		g.setColor(new Color((strLang.equalsIgnoreCase("French") || frLang) ? 0xFFFFFF : 0x454545));
		g.drawRect((width - 350) / 2 - 1, 80 + 50 * 3 - 1, 350 + 1, 40 + 1);

		font.draw(g, "Français (France)", (width - 190) / 2, 80 + 50 * 3 + 25);

		g.setColor(Color.BLACK);
		g.fillRect((width - 350) / 2, 80 + 50 * 4, 350, 40);
		g.setColor(new Color((strLang.equalsIgnoreCase("Galician") || glLang) ? 0xFFFFFF : 0x454545));
		g.drawRect((width - 350) / 2 - 1, 80 + 50 * 4 - 1, 350 + 1, 40 + 1);

		font.draw(g, "Galego (España)", (width - 178) / 2, 80 + 50 * 4 + 25);

		g.setColor(Color.BLACK);
		g.fillRect((width - 350) / 2, 80 + 50 * 5, 350, 40);
		g.setColor(new Color((strLang.equalsIgnoreCase("Catalan") || caLang) ? 0xFFFFFF : 0x454545));
		g.drawRect((width - 350) / 2 - 1, 80 + 50 * 5 - 1, 350 + 1, 40 + 1);

		font.draw(g, "Catalá (Espanya)", (width - 178) / 2, 80 + 50 * 5 + 25);

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

		if (lang != null && MouseUtils.isButtonPressed(1))
			Locales.setLocale(lang);
	}

	private void check(int button, boolean condition, Runnable lambda) {
		if (MouseUtils.isButtonPressed(button) && condition) {
			lambda.run();
		}
	}

	public static SettingsScreen getInstance() {
		return instance;
	}

}
