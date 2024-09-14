package net.opencraft.renderer.screens;

import java.awt.*;
import java.awt.event.KeyEvent;

import org.josl.openic.Keyboard;
import org.lwjgl.opengl.Display;

import lombok.Getter;
import lombok.Setter;
import net.opencraft.Locales;
import net.opencraft.SharedConstants;
import net.opencraft.renderer.Renderer;
import net.opencraft.sound.SoundManager;
import net.opencraft.spectoland.ILogger;
import net.opencraft.util.FontRenderer;

public class F3Screen {

	public static String status = null;
	
	@Getter @Setter
	private static boolean visible = false;
	
	private F3Screen() {
	}

	public static void draw(Graphics2D g2d) {
		if (Keyboard.isKeyClicked(KeyEvent.VK_I)) {
			try {
				ILogger.writeFile();
			} catch (Exception ignored) {
			}
			setStatus("Saved internal log file!");
		}
		
		Composite saveCMP = g2d.getComposite();
		AlphaComposite alphaCMP = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);

		FontRenderer font = FontRenderer.mojangles();

		g2d.setColor(Color.GRAY);
		int r = 7;
		g2d.setComposite(alphaCMP);
		{
			g2d.fillRoundRect(10, 10, 350, 30, r, r);
			g2d.fillRoundRect(10, 40, 390, 30, r, r);
			g2d.fillRoundRect(10, 70, 225, 30, r, r);
			g2d.fillRoundRect(10, 100, 250, 30, r, r);
			g2d.fillRoundRect(10, 130, 237, 30, r, r);
			g2d.fillRoundRect(10, 160, 190, 30, r, r);
			g2d.fillRoundRect(10, 190, 290, 30, r, r);
			if (status != null)
				g2d.fillRoundRect(10, Display.getHeight() - 80, 500, 30, r, r);
		}
		g2d.setComposite(saveCMP);

		font.color(Color.WHITE);
		font.size(20);
		font.drawShadow(g2d, "OpenCraft " + SharedConstants.VERSION_STRING + " (Vanilla)", 15, 30);
		font.drawShadow(g2d, "Actual screen: " + Screen.getCurrent().toString(), 15, 60);
		font.drawShadow(g2d, "OpenGL: " + (Renderer.usesOpenGL() ? "Enabled" : "Disabled"), 15, 90);
		font.drawShadow(g2d, "SoundAPI: " + (SoundManager.MUSIC ? "Active" : "Passive"), 15, 120);
		font.drawShadow(g2d, "Language: " + Locales.getLocale().toLanguageTag(), 15, 150);
		font.drawShadow(g2d, "UI Scale: " + System.getProperty("sun.java2d.uiScale"), 15, 180);
		font.drawShadow(g2d, "Resolution: " + Display.getWidth() + "x" + Display.getHeight(), 15, 210);

		if (status != null)
			font.drawShadow(g2d, status, 15, Display.getHeight() - 60);

		clearStatus();
	}

	public static void setStatus(final String status) {
		F3Screen.status = status;
	}
	
	public static void clearStatus() {
		status = null;
	}

	public static void toggleVisible() {
		visible = !visible;
	}

}
