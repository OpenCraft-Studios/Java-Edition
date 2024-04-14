package net.op.render;

import static net.op.render.display.DisplayManager.createDisplay;
import static net.op.render.display.DisplayManager.getDisplay;
import static net.op.render.display.DisplayManager.setDisplayGraphics;
import static net.op.render.display.DisplayManager.showDisplay;
import static net.op.render.display.DisplayManager.updateDisplay;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import net.op.Client;
import net.op.Config;
import net.op.render.display.Display;
import net.op.render.screens.Screen;

/**
 * <h1>Render</h1> This class is used to draw into the display.
 *
 * @see Display
 */
public final class Render extends Canvas {

    public static final GraphicsConfiguration GFX_CONFIG = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice().getDefaultConfiguration();

    public static final DisplayMode GFX_DISPLAY_MODE = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();

    public static final Logger logger = Logger.getLogger(Render.class.getName());
    private static final long serialVersionUID = 1L;

    private static final boolean OPEN_GL;

    static {
        if (System.getProperty("sun.java2d.opengl") != null) {
            OPEN_GL = System.getProperty("sun.java2d.opengl").equalsIgnoreCase("true");
        } else {
            OPEN_GL = false;
        }
    }

    private Render() {
        setBackground(Color.BLACK);
    }

    public static Render create() {
        return new Render();
    }

    public static BufferedImage toCompatibleImage(final BufferedImage image) {
        if (image.getColorModel().equals(GFX_CONFIG.getColorModel())) {
            return image;
        }

        final BufferedImage new_image = GFX_CONFIG.createCompatibleImage(image.getWidth(), image.getHeight(),
                image.getTransparency());

        final Graphics2D g2d = (Graphics2D) new_image.getGraphics();

        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return new_image;
    }

    public void init() {
        // Configure display
        createDisplay();
        showDisplay();
        setDisplayGraphics(this);

        logger.info("Render system initialized!");
        logger.info("[OpenGL] Using OpenGL: %s".formatted(OPEN_GL ? "Yes" : "No"));

        // Do "VSync"
        try {
            Client.getClient().vsync();
        } catch (Exception ex) {
            logger.warning(ex.getMessage());
        }

        logger.info("FPS Rate: %d".formatted(Config.FPS_CAP));
    }

    public void update() {
        render();
        updateDisplay();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }

        Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();
        Screen.renderCurrent(g2d);
        g2d.dispose();

        bs.show();

    }

    public boolean shouldRender() {
        return getDisplay().isShowing() && !getDisplay().isMinimized();
    }

}
