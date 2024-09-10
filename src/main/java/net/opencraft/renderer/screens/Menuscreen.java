package net.opencraft.renderer.screens;

import static net.opencraft.Locales.*;
import static net.opencraft.OpenCraft.*;
import static net.opencraft.renderer.gui.GuiButton.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.lwjgl.input.Mouse;

import io.vavr.Lazy;
import net.opencraft.Locales;
import net.opencraft.util.FontRenderer;
import net.opencraft.util.MouseUtils;

public class Menuscreen extends Screen {
	
	private static Menuscreen instance = null;

	private boolean quitsel = false;
	private boolean setsel = false;
	private Lazy<BufferedImage> logo;

	private Menuscreen() {
		logo = Lazy.of(oc.assets::getLogo);
	}

	@Override
	public void render(Graphics2D g2d) {
		pollEvents();
		final int width = oc.width;
		final int height = oc.height;

		g2d.setColor(Color.BLACK);
		g2d.setPaint(new TexturePaint(oc.assets.getBackground(), new Rectangle(0, 0, 64, 64)));
		g2d.fillRect(0, 0, width, height);

		g2d.drawImage(logo.get(), (width - 500) / 2, (480 > height) ? 10 : 30, 500, 87, null);

		if (getCurrent().equals(Menuscreen.getInstance())) {
			quitsel = MouseUtils.inRange(width / 2, height / 2 - 4, 200, 40);
			setsel = MouseUtils.inRange((width - 400) / 2, height / 2 - 4, 198, 40);
		} else {
			quitsel = false;
			setsel = false;
		}

		// Draw buttons
		g2d.drawImage(oc.assets.getButton(BUTTON_DISABLED), (width - 400) / 2, height / 2 - 50, 400, 40, null);
		g2d.drawImage(oc.assets.getButton(setsel ? BUTTON_HIGHLIGHTED : BUTTON_NORMAL), (width - 400) / 2,
				height / 2 - 4, 198, 40, null);
		g2d.drawImage(oc.assets.getButton(quitsel ? BUTTON_HIGHLIGHTED : BUTTON_NORMAL), width / 2, height / 2 - 4, 200,
				40, null);

		g2d.setColor(Color.WHITE);

		int singlepy_x = width / 2 - 59;
		int settings_x = width / 2 - 150;
		int quitgame_x = width / 2 + 50;

		/* Center texts from another languages */
		String langName = Locales.getGenericName(Locales.getLocale());
		{
			if (langName.equalsIgnoreCase("French")) {
				quitgame_x = width / 2 + 30;
			} else if (langName.equalsIgnoreCase("Galician") || langName.equalsIgnoreCase("Portuguese")) {
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

		FontRenderer font = FontRenderer.minecraft();

		font.size(16);
		font.drawShadow(g2d, translate("menu.Quit"), quitgame_x, height / 2 + 20, quitsel ? 0xFFFFA0 : 0xFFFFFF);
		font.drawShadow(g2d, translate("menu.Options"), settings_x, height / 2 + 20, setsel ? 0xFFFFA0 : 0xFFFFFF);
		font.drawShadow(g2d, translate("menu.singleplayer"), singlepy_x, height / 2 - 25, 0xA0A0A0);
	}

	private void pollEvents() {
		if (!Mouse.isButtonClicked(1))
			return;
		
		if (setsel)
			Screen.setCurrent(SettingsScreen.class);
		else if (quitsel)
			oc.running = false;
		
	}

	public static Menuscreen getInstance() {
		if (instance == null)
			instance = new Menuscreen();

		return instance;
	}

	public static void destroy() {
		instance = null;
	}

}
