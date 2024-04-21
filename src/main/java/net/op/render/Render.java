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
import java.util.Optional;
import java.util.logging.Logger;

import net.op.Client;
import net.op.Config;
import net.op.render.screens.Screen;

/**
 * <h1>Render</h1><br>
 * This class is used for manage drawing process and screen control. It can also
 * determine the best fps configuration and guide the OpenGL usage.
 */
public final class Render extends Canvas {

    public static final GraphicsConfiguration GFX_CONFIG = GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice().getDefaultConfiguration();

    public static final DisplayMode GFX_DISPLAY_MODE = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();

    public static final Logger logger = Logger.getLogger(Render.class.getName());
    private static final long serialVersionUID = 1L;

    private static final boolean OPEN_GL;

    static {
        // Checks if OpenGL-Based Pipeline is enabled
        OPEN_GL = Optional
                .ofNullable(System.getProperty("sun.java2d.opengl"))
                .orElse("false")
                .equalsIgnoreCase("true");
    }

    /**
     * Creates a new instance of this class.
     */
    private Render() {
        setBackground(Color.BLACK);
    }

    /**
     * Creates a new instance of this class.
     * @return That instance
     */
    public static Render create() {
        return new Render();
    }

    /**
     * Returns the preferred color model by your device. This possibly decrease
     * the CPU using.
     *
     * @param image The original image
     * @return The optimized image
     */
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

        // Set this object as the default drawing context
        setDisplayGraphics(this);

        // Show render details
        logger.info("Render system initialized!");
        logger.info("[OpenGL] Using OpenGL: %s".formatted(OPEN_GL ? "Yes" : "No"));

        // Do "VSync"
        try {
            Client.getClient().vsync();
        } catch (Exception ex) {
            logger.warning(ex.getMessage());
        }

        // Show FPS Rate
        logger.info("FPS Rate: %d".formatted(Config.FPS_CAP));
    }

    /**
     * Updates the display and renders the game.
     */
    public void update() {
        render();
        updateDisplay();
    }

    /**
     * Renders the game.
     */
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

    /**
     * @return true if you should renderize the game, otherwise false.
     */
    public boolean shouldRender() {
        return getDisplay().isShowing() && !getDisplay().isMinimized();
    }

}
