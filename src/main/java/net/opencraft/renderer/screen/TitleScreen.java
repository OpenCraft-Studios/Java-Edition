package net.opencraft.renderer.screen;

import static net.opencraft.util.Assets.NORMAL_BUTTON;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Locale;

import net.opencraft.client.Game;
import net.opencraft.config.GameConfig;
import net.opencraft.language.Translator;
import net.opencraft.renderer.Renderizable;
import net.opencraft.renderer.display.Display;
import net.opencraft.util.Assets;
import net.opencraft.util.Fonts;
import net.opencraft.util.Resource;
import net.opencraft.util.coords.Coordinates;
import net.opencraft.util.coords.Vec2;

public class TitleScreen implements Renderizable {

	public static final Resource RESOURCE = Resource.format("opencraft.screen:title");

	/**
	 * Don't use it, it's bugged for some reason
	 */
	public static final Screens SCREEN = Screens.TITLE_SCREEN;

	private static TitleScreen instance = new TitleScreen();

	@Override
	public void render(Graphics g) {

		final int width = Display.WIDTH;
		final int height = Display.HEIGHT;

		// Fill all the screen with white color
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		// Set font
		g.setFont(Fonts.MOJANGLES.getFont(Font.PLAIN, 18));

		// Draw panoramas
		g.drawImage(Assets.getPanoramas(), 0, 0, height * 3, height, null);

		// Draw Logo
		g.drawImage(Assets.getLogo(), (width - 583) / 2, 30, 583, 166, null);

		// Draw buttons
		int[] b1 = Coordinates.XYWHtoP4(Vec2.newTemp((width - 400) / 2, height / 2 - 50), new Dimension(400, 45));
		int[] b2 = Coordinates.XYWHtoP4(Vec2.newTemp((width - 400) / 2, height / 2), new Dimension(400, 45));

		int[] bb = Assets.getButtonBounds(NORMAL_BUTTON);
		g.drawImage(Assets.bindTexture("/gui/widgets.png"), b1[0], b1[1], b1[2], b1[3], bb[0], bb[1], bb[2], bb[3],
				null);
		g.drawImage(Assets.bindTexture("/gui/widgets.png"), b2[0], b2[1], b2[2], b2[3], bb[0], bb[1], bb[2], bb[3],
				null);

		// Legal terms
		g.setFont(Fonts.MOJANGLES.getFont(Font.PLAIN, 20));
		final String COPYLEFT = String.format("CopyLeft (-C) Sibermatica 2023-%d",
				Calendar.getInstance().get(Calendar.YEAR));

		g.drawString(COPYLEFT, width - 425, height - 70);

		// Draw Game Info
		g.drawString(Game.TITLE, 15, height - 70);
		g.drawString(Translator.translate("opencraft.buttons:singleplayer"), width / 2 - 69, height / 2 - 23);

		// In Spanish the words are a little bit large
		int settings_x = GameConfig.LANGUAGE == Locale.forLanguageTag("es-ES") ? width / 2 - 73 : width / 2 - 45;
		g.drawString(Translator.translate("opencraft.buttons:config"), settings_x, height / 2 + 27);
	}

	@Override
	public void render(Graphics g, int width, int height) {
		render(g);
	}

	@Override
	public void render(BufferedImage bi) {
		render(bi.getGraphics(), bi.getWidth(), bi.getHeight());
	}

	public static TitleScreen renewInstance() {
		return instance = new TitleScreen();
	}

	public static TitleScreen getInstance() {
		return instance;
	}

}
