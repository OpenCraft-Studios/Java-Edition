package net.op.render.screens;

import static net.op.Locales.translate;
import static net.op.render.display.DisplayManager.getDisplayHeight;
import static net.op.render.display.DisplayManager.getDisplayWidth;
import static net.op.render.textures.GUITilesheet.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Locale;

import org.josl.openic.IC;
import org.josl.openic.input.ComponentMouse;

import net.op.Client;
import net.op.Config;
import net.op.render.display.Display;
import net.op.render.textures.GUITilesheet;
import net.op.util.OCFont;
import net.op.input.MouseUtils;
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
    public void render(Graphics g) {
        GUITilesheet gts = GUITilesheet.getInstance();

        int width = getDisplayWidth();
        int height = getDisplayHeight();
        OCFont font = OCFont.mojangles();

        g.setPaintMode();
        g.setColor(Color.BLACK);

        for (int x = 0; x < width; x += 64) {
            for (int y = 0; y < height; y += 64) {
                g.drawImage(gts.getBackground(), x, y, 64, 64, null);
            }
        }

        boolean quitsel, setsel;
        quitsel = MouseUtils.inRange(width / 2, height / 2 - 4, 200, 40);
        setsel = MouseUtils.inRange((width - 400) / 2, height / 2 - 4, 198, 40);

        // Draw buttons
        g.drawImage(gts.getButton(BUTTON_DISABLED), (width - 400) / 2, height / 2 - 50, 400, 40, null);
        g.drawImage(gts.getButton(setsel ? BUTTON_HIGHLIGHTED : BUTTON), (width - 400) / 2, height / 2 - 4, 198, 40,
                null);
        g.drawImage(gts.getButton(quitsel ? BUTTON_HIGHLIGHTED : BUTTON), width / 2, height / 2 - 4, 200, 40, null);

        // Logo
        g.drawImage(gts.getLogo(), (width - 500) / 2, (Display.HEIGHT > height) ? 10 : 30, 500, 87, null);
        g.setColor(Color.WHITE);

        int singlepy_x = width / 2 - 59;
        int settings_x = width / 2 - 150;
        int quitgame_x = width / 2 + 50;

        /* Center texts from another languages */
        {
            if (Config.LOCALE.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("French")) {
                quitgame_x = width / 2 + 30;
            }

            if (Config.LOCALE.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Galician")) {
                quitgame_x = width / 2 + 34;
            }

            if (Config.LOCALE.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Catalan")) {
                quitgame_x = width / 2 + 27;
            }

            if (Config.LOCALE.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Italian")) {
                singlepy_x = width / 2 - 87;
                settings_x = width / 2 - 145;
                quitgame_x = width / 2 + 34;
            }

            if (Config.LOCALE.getDisplayLanguage(Locale.ENGLISH).equalsIgnoreCase("Spanish")) {
                settings_x = width / 2 - 155;
                quitgame_x = width / 2 + 21;
            }
        }

        // Draw credits
        font.size(17)
                .drawShadow(g, "%s %s".formatted(Client.NAME, Client.CODENAME), 3, 15, 0x808080);

        font.size(20);
        font.drawShadow(g, translate("menu.Quit"), quitgame_x, height / 2 + 20, quitsel ? 0xFFFFA0 : 0xFFFFFF);
        font.drawShadow(g, translate("menu.Options"), settings_x, height / 2 + 20, setsel ? 0xFFFFA0 : 0xFFFFFF);
        font.drawShadow(g, translate("menu.singleplayer"), singlepy_x, height / 2 - 25, 0xA0A0A0);

        check(quitsel, setsel);
    }

    public void check(boolean quitsel, boolean setsel) {
        ComponentMouse mouse = (ComponentMouse) IC.getDefaultMouse();
        if (mouse.isButtonPressed(1)) {
            if (quitsel) {
                Client.getClient().stop();
            }

            if (setsel) {
                Screen.setCurrent(SettingsScreen.getInstance());
            }
        }
    }

    public static MenuScreen getInstance() {
        return instance;
    }

}
