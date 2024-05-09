package net.op.render.screens;

import static org.josl.openic.IC13.*;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import org.scgi.Context;
import org.scgi.Display;

import com.sun.glass.events.KeyEvent;

import net.op.Locales;
import net.op.OpenCraft;
import net.op.sound.SoundManager;
import net.op.spectoland.ILogger;
import net.op.util.OCFont;

public class F3Screen {

	private F3Screen() {
	}

	public static String status = null;

	public static void drawF3(Graphics2D g2d) {
		if (icIsKeyPressed(KeyEvent.VK_I)) {
			try {
				ILogger.writeFile();
			} catch (Exception ignored) {
			}
			setStatus("Saving internal log file");
		}
		
		Composite comp = g2d.getComposite();
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);

		OCFont font = OCFont.tlrender();

		g2d.setColor(Color.GRAY);
		g2d.setComposite(ac);
		{
			g2d.fillRect(10, 10, 500, 30);
			g2d.fillRect(10, 40, 510, 30);
			g2d.fillRect(10, 70, 225, 30);
			g2d.fillRect(10, 100, 250, 30);
			g2d.fillRect(10, 130, 237, 30);
			g2d.fillRect(10, 160, 190, 30);
			g2d.fillRect(10, 190, 290, 30);
			if (status != null)
				g2d.fillRect(10, Display.height() - 80, 320, 30);
		}
		g2d.setComposite(comp);

		font.color(Color.WHITE);
		font.size(20);
		font.drawShadow(g2d, OpenCraft.NAME + " " + OpenCraft.VERSION + " (Vanilla)", 15, 30);
		font.drawShadow(g2d, "Actual screen: " + Screen.getCurrent().getResource().getId(), 15, 60);
		font.drawShadow(g2d, "OpenGL: " + (Context.usesOpenGL() ? "Enabled" : "Disabled"), 15, 90);
		font.drawShadow(g2d, "SoundAPI: " + (SoundManager.MUSIC ? "Active" : "Passive"), 15, 120);
		font.drawShadow(g2d, "Language: " + Locales.getLocale().toLanguageTag(), 15, 150);
		font.drawShadow(g2d, "UI Scale: " + System.getProperty("sun.java2d.uiScale"), 15, 180);
		font.drawShadow(g2d, "Resolution: " + Display.width() + "x" + Display.height(), 15, 210);

		if (status != null)
			font.drawShadow(g2d, status, 15, Display.height() - 60);

		clearStatus();
	}

	public static void setStatus(final String status) {
		F3Screen.status = status;
	}
	
	public static void clearStatus() {
		status = null;
	}

}
