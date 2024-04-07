package net.op.render.scenes;

import static net.op.language.Languages.translate;
import static net.op.render.display.DisplayManager.getDisplayHeight;
import static net.op.render.display.DisplayManager.getDisplayWidth;
import static net.op.render.textures.Assets.BUTTON;
import static net.op.render.textures.Assets.BUTTON_HIGHLIGHTED;

import java.awt.Color;
import java.awt.Graphics;

import org.josl.openic.IC;
import org.josl.openic.input.ComponentMouse;

import net.op.Client;
import net.op.render.textures.Assets;
import net.op.sound.SoundManager;
import net.op.util.OCFont;
import net.op.util.MouseUtils;
import net.op.util.Resource;

public class SettingsScreen extends Screen {

	public static final Resource RESOURCE = Resource.format("opencraft:screens.settings_screen");
	private static final SettingsScreen instance = SettingsScreen.create();

	private boolean donesel = false;

	private String ctab = "options.music";

	private SettingsScreen() {
		super(RESOURCE);
	}

	private static SettingsScreen create() {
		return new SettingsScreen();
	}

	@Override
	public void render(Graphics g, Assets assets) {
		int width = getDisplayWidth();
		int height = getDisplayHeight();

		OCFont font = OCFont.mojangles();

		g.setPaintMode();
		g.setColor(Color.WHITE);

		for (int x = 0; x < width; x += 64) {
			for (int y = 0; y < height; y += 64) {
				g.drawImage(assets.getBackground(), x, y, 64, 64, null);
			}
		}

		if (ctab.equalsIgnoreCase("options.music"))
			drawSoundTab(g, assets);
		else if (ctab.equalsIgnoreCase("options.localesTab"))
			drawLocalesTab(g, assets);

		/*-------------------------------*/

		donesel = MouseUtils.inRange(30, 15, 175, 40);
		g.drawImage(assets.getButton(donesel ? BUTTON_HIGHLIGHTED : BUTTON), 30, 15, 175, 40, null);

		// Draw buttons content

		g.setColor(Color.WHITE);
		font.drawShadow(g, translate("gui.Done"), 90, 40, donesel ? 0xFFFFA0 : 0xFFFFFF);

		boolean arrow1 = MouseUtils.inRange((width - 400) / 2 + 100, 20, 21, 33);
		boolean arrow0 = MouseUtils.inRange((width - 400) / 2 + 273, 20, 21, 33);

		g.drawImage(assets.getArrow(arrow1 ? 3 : 1), (width - 400) / 2 + 100, 20, 21, 33, null);
		g.drawImage(assets.getArrow(arrow0 ? 2 : 0), (width - 400) / 2 + 273, 20, 21, 33, null);

		font.drawShadow(g, translate(ctab), (width - 400) / 2 + 155, 42, 0xFFFFFF);

		check(1, donesel, () -> {
			exitScreen();
		});

		check(1, arrow0, () -> {
			ctab = "options.localesTab";
		});
		check(1, arrow1, () -> {
			ctab = "options.music";
		});

	}

	private void drawSoundTab(Graphics g, Assets assets) {
		OCFont font = OCFont.mojangles();

		boolean musicBtn = MouseUtils.inRange(30, 80, 200, 40);
		g.drawImage(assets.getButton(musicBtn ? BUTTON_HIGHLIGHTED : BUTTON), 30, 80, 200, 40, null);

		font.color(Color.WHITE);
		font.size(20);
		font.drawShadow(g,
				translate("options.music") + ": " + translate("options." + (SoundManager.ENABLED ? "on" : "off")), 45,
				105);

		check(1, musicBtn, () -> {
			SoundManager.ENABLED = !SoundManager.ENABLED;
			try {
				Thread.sleep(250);
			} catch (Exception ignored) {
				// TODO Internal logger
			}
		});
	}

	private void drawLocalesTab(Graphics g, Assets assets) {
		// Draw buttons
		final int width = getDisplayWidth();
		final int lIndex = Client.getLanguageIndex();
		int lang = -1;

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
		g.setColor(new Color((lIndex == 0 || enLang) ? 0xFFFFFF : 0x454545));
		g.drawRect((width - 350) / 2 - 1, 80 - 1, 350 + 1, 40 + 1);

		font.color(Color.WHITE);
		font.draw(g, "English (United States)", (width - 237) / 2, 80 + 25);

		g.setColor(Color.BLACK);
		g.fillRect((width - 350) / 2, 80 + 50, 350, 40);
		g.setColor(new Color((lIndex == 1 || spLang) ? 0xFFFFFF : 0x454545));
		g.drawRect((width - 350) / 2 - 1, 80 + 50 - 1, 350 + 1, 40 + 1);

		font.draw(g, "Español (Argentina)", (width - 207) / 2, 80 + 50 + 25);

		g.setColor(Color.BLACK);
		g.fillRect((width - 350) / 2, 80 + 50 * 2, 350, 40);
		g.setColor(new Color((lIndex == 2 || itLang) ? 0xFFFFFF : 0x454545));
		g.drawRect((width - 350) / 2 - 1, 80 + 50 * 2 - 1, 350 + 1, 40 + 1);

		font.draw(g, "Italiano (Italia)", (width - 175) / 2, 80 + 50 * 2 + 25);

		g.setColor(Color.BLACK);
		g.fillRect((width - 350) / 2, 80 + 50 * 3, 350, 40);
		g.setColor(new Color((lIndex == 3 || frLang) ? 0xFFFFFF : 0x454545));
		g.drawRect((width - 350) / 2 - 1, 80 + 50 * 3 - 1, 350 + 1, 40 + 1);

		font.draw(g, "Français (France)", (width - 190) / 2, 80 + 50 * 3 + 25);

		g.setColor(Color.BLACK);
		g.fillRect((width - 350) / 2, 80 + 50 * 4, 350, 40);
		g.setColor(new Color((lIndex == 4 || glLang) ? 0xFFFFFF : 0x454545));
		g.drawRect((width - 350) / 2 - 1, 80 + 50 * 4 - 1, 350 + 1, 40 + 1);

		font.draw(g, "Galego (España)", (width - 178) / 2, 80 + 50 * 4 + 25);

		g.setColor(Color.BLACK);
		g.fillRect((width - 350) / 2, 80 + 50 * 5, 350, 40);
		g.setColor(new Color((lIndex == 5 || caLang) ? 0xFFFFFF : 0x454545));
		g.drawRect((width - 350) / 2 - 1, 80 + 50 * 5 - 1, 350 + 1, 40 + 1);

		font.draw(g, "Catalá (Espanya)", (width - 178) / 2, 80 + 50 * 5 + 25);

		if (enLang)
			lang = 0;
		else if (spLang)
			lang = 1;
		else if (itLang)
			lang = 2;
		else if (frLang)
			lang = 3;
		else if (glLang)
			lang = 4;
		else if (caLang)
			lang = 5;

		switchLanguage(lang);
	}

	private void exitScreen() {
		ctab = "options.music";
		Screen.setCurrent(MenuScreen.getInstance());
	}

	private void check(int button, boolean condition, Runnable lambda) {
		if (MouseUtils.isButtonPressed(button) && condition)
			lambda.run();
	}

	public void switchLanguage(int lang) {
		if (lang == -1)
			return;

		ComponentMouse mouse = (ComponentMouse) IC.getDefaultMouse();
		if (mouse.isButtonPressed(1))
			Client.setLanguageIndex(lang);
	}

	public static SettingsScreen getInstance() {
		return instance;
	}

}
