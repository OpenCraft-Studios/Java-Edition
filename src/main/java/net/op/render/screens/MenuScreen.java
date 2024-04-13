package net.op.render.screens;

import static net.op.language.Languages.translate;
import static net.op.render.display.DisplayManager.getDisplayHeight;
import static net.op.render.display.DisplayManager.getDisplayWidth;
import static net.op.render.textures.Tilesheet.BUTTON;
import static net.op.render.textures.Tilesheet.BUTTON_DISABLED;
import static net.op.render.textures.Tilesheet.BUTTON_HIGHLIGHTED;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Locale;

import org.josl.openic.IC;
import org.josl.openic.input.ComponentMouse;

import net.op.Client;
import net.op.render.display.Display;
import net.op.render.textures.Tilesheet;
import net.op.util.OCFont;
import net.op.util.MouseUtils;
import net.op.util.Resource;

public class MenuScreen extends Screen {

	public static final Resource RESOURCE = Resource.format("opencraft:screens.menuscreen");
	private static final MenuScreen instance = MenuScreen.create();

	private MenuScreen() {
		super(MenuScreen.RESOURCE);
	}

	private static MenuScreen create() {
		return new MenuScreen();
	}

	@Override
	public void render(Graphics g, Tilesheet assets) {
		int width = getDisplayWidth();
		int height = getDisplayHeight();
		OCFont font = OCFont.mojangles();

		g.setPaintMode();
		g.setColor(Color.BLACK);

		for (int x = 0; x < width; x += 64) {
			for (int y = 0; y < height; y += 64) {
				g.drawImage(assets.getBackground(), x, y, 64, 64, null); 
			}
		}

		boolean quitsel, setsel;
		quitsel = MouseUtils.inRange(width / 2, height / 2 - 4, 200, 40);
		setsel = MouseUtils.inRange((width - 400) / 2, height / 2 - 4, 198, 40);

		// Draw buttons
		g.drawImage(assets.getButton(BUTTON_DISABLED), (width - 400) / 2, height / 2 - 50, 400, 40, null);
		g.drawImage(assets.getButton(setsel ? BUTTON_HIGHLIGHTED : BUTTON), (width - 400) / 2, height / 2 - 4, 198, 40,
				null);
		g.drawImage(assets.getButton(quitsel ? BUTTON_HIGHLIGHTED : BUTTON), width / 2, height / 2 - 4, 200, 40, null);

		// Logo
		g.drawImage(assets.getLogo(), (width - 500) / 2, (Display.HEIGHT > height) ? 10 : 30, 500, 87, null);
		g.setColor(Color.WHITE);

		int singlepy_x = width / 2 - 59;
		int settings_x = width / 2 - 150;
		int quitgame_x = width / 2 + 50;

		/* Center texts from another languages */
		{
			if (Client.getLanguage().getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("French")) {
				quitgame_x = width / 2 + 30;
			}

			if (Client.getLanguage().getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Galician")) {
				quitgame_x = width / 2 + 34;
			}
			
			if (Client.getLanguage().getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Catalan")) {
				quitgame_x = width / 2 + 27;
			}
			
			if (Client.getLanguage().getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Italian")) {
				singlepy_x = width / 2 - 87;
				settings_x = width / 2 - 145;
				quitgame_x = width / 2 + 34;
			}

			if (Client.getLanguage().getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Spanish")) {
				settings_x = width / 2 - 155;
				quitgame_x = width / 2 + 21;
			}
		}

		// Draw credits
		font.size(15).draw(g, "%s Indev %s".formatted(Client.NAME, Client.VERSION), 3, 15, 0x505050);

		font.size(20);
		font.drawShadow(g, translate("menu.Quit"), quitgame_x, height / 2 + 20, quitsel ? 0xFFFFA0 : 0xFFFFFF);
		font.drawShadow(g, translate("menu.Options"), settings_x, height / 2 + 20, setsel ? 0xFFFFA0 : 0xFFFFFF);
		font.drawShadow(g, translate("menu.singleplayer"), singlepy_x, height / 2 - 25, 0xFFFFFF);

		check(quitsel, setsel);
	}

	public void check(boolean quitsel, boolean setsel) {
		ComponentMouse mouse = (ComponentMouse) IC.getDefaultMouse();
		if (mouse.isButtonPressed(1)) {
			if (quitsel)
				Client.getClient().stop();

			if (setsel)
				Screen.setCurrent(SettingsScreen.getInstance());
		}
	}

	public static MenuScreen getInstance() {
		return instance;
	}

}
