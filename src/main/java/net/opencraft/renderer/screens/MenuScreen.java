package net.opencraft.renderer.screens;

import static net.opencraft.Locales.*;
import static net.opencraft.OpenCraft.*;
import static net.opencraft.renderer.texture.Assets.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.lwjgl.opengl.Display;

import net.opencraft.Locales;
import net.opencraft.OpenCraft;
import net.opencraft.renderer.texture.Assets;
import net.opencraft.util.FontRenderer;
import net.opencraft.util.MouseUtils;

public class MenuScreen extends Screen implements MouseListener {

	public static final int TIMEOUT = 1500;
	
	private static MenuScreen instance = null;

	private boolean quitsel = false;
	private boolean setsel = false;
	//private long start = -1;

	private MenuScreen() {
	}

	@Override
	public void render(Graphics g, Assets assets) {
		int width = Display.width();
		int height = Display.height();

		g.setColor(Color.BLACK);

		for (int x = 0; x < width; x += 64) {
			for (int y = 0; y < height; y += 64) {
				g.drawImage(assets.getBackground(), x, y, 64, 64, null);
			}
		}
		
		g.drawImage(assets.getLogo(), (width - 500) / 2, (480 > height) ? 10 : 30, 500, 87, null);

		if (getCurrent().equals(MenuScreen.getInstance())) {
			quitsel = MouseUtils.inRange(width / 2, height / 2 - 4, 200, 40);
			setsel = MouseUtils.inRange((width - 400) / 2, height / 2 - 4, 198, 40);
		} else {
			quitsel = false;
			setsel = false;
		}

		// Draw buttons
		g.drawImage(assets.getButton(BUTTON_DISABLED), (width - 400) / 2, height / 2 - 50, 400, 40, null);
		g.drawImage(assets.getButton(setsel ? BUTTON_HIGHLIGHTED : BUTTON), (width - 400) / 2, height / 2 - 4, 198, 40,
				null);
		g.drawImage(assets.getButton(quitsel ? BUTTON_HIGHLIGHTED : BUTTON), width / 2, height / 2 - 4, 200, 40, null);

		g.setColor(Color.WHITE);

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
		font.drawShadow(g, translate("menu.Quit"), quitgame_x, height / 2 + 20, quitsel ? 0xFFFFA0 : 0xFFFFFF);
		font.drawShadow(g, translate("menu.Options"), settings_x, height / 2 + 20, setsel ? 0xFFFFA0 : 0xFFFFFF);
		font.drawShadow(g, translate("menu.singleplayer"), singlepy_x, height / 2 - 25, 0xA0A0A0);

		// Draw game name
		font = FontRenderer.tlrender().size(14);
		font.drawShadow(g, OpenCraft.NAME + " " + OpenCraft.TECHNICAL_NAME, 3, 15, 0x808080);
	}

	public static MenuScreen getInstance() {
		if (instance == null)
			instance = new MenuScreen();
		
		return instance;
	}

	public static void destroy() {
		instance = null;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (setsel)
			Screen.setCurrent(SettingsScreen.class);
		else if (quitsel)
			oc.running = false;
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