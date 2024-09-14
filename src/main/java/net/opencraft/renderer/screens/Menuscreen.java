package net.opencraft.renderer.screens;

import static net.opencraft.OpenCraft.*;
import static net.opencraft.SharedConstants.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.function.Consumer;

import org.josl.openic.Mouse;

import io.vavr.Lazy;
import net.opencraft.Locales;
import net.opencraft.renderer.gui.GuiButton;
import net.opencraft.util.FontRenderer;

public class Menuscreen extends Screen {
	
	private static Menuscreen instance = null;
	private Lazy<BufferedImage> logo;

	private FontRenderer font;
	private GuiButton quitButton, singlepyButton, settingsButton;

	private Menuscreen() {
		logo = Lazy.of(oc.assets::getLogo);
		Locales.getListeners().add(this::translationChanged);
		
		this.font = FontRenderer.minecraft();
		initComponents();
		
		// Update button names
		translationChanged(null);
	}

	private void initComponents() {
		
		/* Quit Game Button */
		this.quitButton = new GuiButton(font) {
			public void mouseClicked(int x, int y, int button) {
				oc.running = false;
			}
		};
		quitButton.setSize(200, 40);
		
		/* Settings button */
		this.settingsButton = new GuiButton(font) {
			public void mouseClicked(int x, int y, int button) {
				goToConfigScreen();
			}
		};
		settingsButton.setSize(198, 40);
		
		/* Singleplayer button */
		
		this.singlepyButton = new GuiButton(font);
		singlepyButton.setSize(400, 40);
		singlepyButton.setEnabled(false);
		
	}
	
	public void translationChanged(Locale locale) {
		singlepyButton.setTranslatedText("menu.singleplayer");
		settingsButton.setTranslatedText("menu.Options");
		quitButton.setTranslatedText("menu.Quit");
	}

	@Override
	public void render(Graphics2D g2d) {
		pollEvents();
		final int width = oc.width;
		final int height = oc.height;

		super.drawDirtBackground(g2d);
		g2d.setColor(Color.BLACK);

		g2d.drawImage(logo.get(), (width - 500) / 2, (480 > height) ? 10 : 30, 500, 87, null);

		/*if (equals(getCurrent())) {
			setsel = MouseUtils.inRange((width - 400) / 2, height / 2 - 4, 198, 40);
		} else {
			setsel = false;
		}*/

		// Calculate buttons' position
		singlepyButton.setLocation((width - 400) / 2, height / 2 - 50);
		settingsButton.setLocation((width - 400) / 2, height / 2 - 4);
		quitButton.setLocation(width / 2, height / 2 - 4);

		if (equals(getCurrent())) {
			// TODO: singlepyButton.setHighlighted(Mouse::inRange);
			quitButton.setHighlighted(Mouse::inRange);
			settingsButton.setHighlighted(Mouse::inRange);
		}
		
		singlepyButton.draw(g2d);
		settingsButton.draw(g2d);
		quitButton.draw(g2d);
		
		FontMetrics metrics = font.size(14).metrics(g2d);
		String version_text = "OpenCraft " + TECHNICAL_NAME + " " + VERSION_STRING;
		int version_textH = metrics.getHeight() + metrics.getAscent();
		
		font.color(0xFFFFFF);
		font.drawShadow(g2d, version_text, 7, height - version_textH - 25);

	}

	private void pollEvents() {
		final int button = 1;
		if (!Mouse.isButtonClicked(button))
			return;
		
		if (quitButton.isHighlighted())
			quitButton.mouseClicked(Mouse.getX(), Mouse.getY(), button);
		if (settingsButton.isHighlighted())
			settingsButton.mouseClicked(Mouse.getX(), Mouse.getY(), button);
	}

	private void goToConfigScreen() {
		Screen.setCurrent(SettingsScreen.class);
	}

	public static Menuscreen getInstance() {
		if (instance == null)
			instance = new Menuscreen();

		return instance;
	}

	public static void destroyInstance() {
		try {
			Locales.getListeners().remove((Consumer<Locale>) instance::translationChanged);
		} catch (Exception ignored) {
			System.err.println("Listener not found!");
		}
		
		instance = null;
	}

}

