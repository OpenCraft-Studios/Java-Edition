package net.op.render.display;

import static org.josl.openic.IC10.IC_TRUE;
import static org.josl.openic.IC15.icBindDevice;
import static org.josl.openic.IC15.icGenDeviceId;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.josl.openic.input.ComponentMouse;
import org.josl.openic.input.Mouse;

import net.java.games.input.Controller;
import net.op.Client;
import net.op.render.Render;
import net.op.render.textures.Tilesheet;

public final class Display extends JFrame {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 854;
	public static final int HEIGHT = 480;

	public static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);

	public Display() {
		super(Client.DISPLAY_NAME);
	}

	public void defaultConfig() {
		// When display is closed, stop the game
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Set display size
		setSize(SIZE);
		setMinimumSize(new Dimension(618, 315));
		setPreferredSize(SIZE);

		setResizable(false); // Make not resizable (this will change after the load screen has been loaded)
		setLayout(new BorderLayout()); // Set layout to a border layout
		setLocationRelativeTo(null); // Center display

		// Set icons
		setIconImages(Tilesheet.getIcons());
		requestFocus();

	}

	public void update() {
		repaint();
		revalidate();
	}

	public void setGraphics(Render renderDragon) {
		long id = icGenDeviceId();
		Mouse mouse = new ComponentMouse(renderDragon);
		icBindDevice(Controller.Type.MOUSE, mouse, IC_TRUE, id);
		add(renderDragon);
	}

}
