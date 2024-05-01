package net.op.render.screens;

import static net.op.language.Translations.MENU_OPTIONS;
import static net.op.language.Translations.MENU_QUIT;
import static net.op.language.Translations.MENU_SINGLEPLAYER;
import static net.op.render.textures.Assets.*;

import java.awt.Color;
import java.awt.Graphics;

import org.scgi.Display;

import net.op.Client;
import net.op.OpenCraft;
import net.op.input.MouseUtils;
import net.op.language.Locales;
import net.op.render.textures.Assets;
import net.op.spectoland.SpectoError;
import net.op.util.OCFont;
import net.op.util.Resource;

public class MenuScreen extends Screen {

	public static final Resource RESOURCE = Resource.format("opencraft:screens.menuscreen");
	private static MenuScreen instance = null;

	private MenuScreen() {
		super(MenuScreen.RESOURCE);
	}

	private static MenuScreen create() {
		return new MenuScreen();
	}

	@Override
	public void render(Graphics g, Assets assets) {
		int width = Display.width();
		int height = Display.height();
		OCFont font = OCFont.mojangles();

		g.setPaintMode();
		g.setColor(Color.BLACK);

		for (int x = 0; x < width; x += 64) {
			for (int y = 0; y < height; y += 64) {
				g.drawImage(assets.getBackground(), x, y, 64, 64, null);
			}
		}

		boolean quitsel, setsel;
		quitsel = false;
		setsel = false;

		if (getCurrent().equals(MenuScreen.getInstance())) {
			quitsel = MouseUtils.inRange(width / 2, height / 2 - 4, 200, 40);
			setsel = MouseUtils.inRange((width - 400) / 2, height / 2 - 4, 198, 40);
		}

		// Draw buttons
		g.drawImage(assets.getButton(BUTTON_DISABLED), (width - 400) / 2, height / 2 - 50, 400, 40, null);
		g.drawImage(assets.getButton(setsel ? BUTTON_HIGHLIGHTED : BUTTON), (width - 400) / 2, height / 2 - 4, 198, 40,
				null);
		g.drawImage(assets.getButton(quitsel ? BUTTON_HIGHLIGHTED : BUTTON), width / 2, height / 2 - 4, 200, 40, null);

		// Logo
		g.drawImage(assets.getLogo(), (width - 500) / 2, (480 > height) ? 10 : 30, 500, 87, null);
		g.setColor(Color.WHITE);

		int singlepy_x = width / 2 - 59;
		int settings_x = width / 2 - 150;
		int quitgame_x = width / 2 + 50;

		/* Center texts from another languages */
		String langName = Locales.getGenericName(Locales.getLocale());
		{
			if (langName.equalsIgnoreCase("French")) {
				quitgame_x = width / 2 + 30;
			} else if (langName.equalsIgnoreCase("Galician")) {
				quitgame_x = width / 2 + 34;
			} else if (langName.equalsIgnoreCase("Catalan")) {
				quitgame_x = width / 2 + 27;
			} else if (langName.equalsIgnoreCase("Italian")) {
				singlepy_x = width / 2 - 87;
				settings_x = width / 2 - 145;
				quitgame_x = width / 2 + 34;
			} else if (langName.equalsIgnoreCase("Spanish")) {
				settings_x = width / 2 - 155;
				quitgame_x = width / 2 + 21;
			}
		}

		// Draw credits
		font.size(17).drawShadow(g, "%s Codename %s".formatted(Client.NAME, Client.CODENAME), 3, 15, 0x808080);

		font.size(20);
		font.drawShadow(g, Locales.CURRENT[MENU_QUIT], quitgame_x, height / 2 + 20, quitsel ? 0xFFFFA0 : 0xFFFFFF);
		font.drawShadow(g, Locales.CURRENT[MENU_OPTIONS], settings_x, height / 2 + 20, setsel ? 0xFFFFA0 : 0xFFFFFF);
		font.drawShadow(g, Locales.CURRENT[MENU_SINGLEPLAYER], singlepy_x, height / 2 - 25, 0xA0A0A0);

		check(quitsel, setsel);
	}

	public void check(boolean quitsel, boolean setsel) {
		if (!MouseUtils.isButtonPressed(1))
			return;
		
		if (quitsel) {
			OpenCraft.getClient().stop();
			try {
				Thread.sleep(20);
			} catch (Exception ex) {
				SpectoError.ignored(ex, MenuScreen.class);
			}
			
		} else if (setsel) {
			Screen.setCurrent(SettingsScreen.class);
		}
	}

	public static MenuScreen getInstance() {
		if (instance == null) {
			return instance = create();
		}

		return instance;
	}

	public static void destroy() {
		instance = null;
	}

}
