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
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.op.Config;
import net.op.render.screens.Screen;

/**
 * <h1>Render</h1><br>
 * This class is used for manage drawing process and screen control. It can also
 * determine the best fps configuration and guide the OpenGL usage.
 */
public final class Render extends Canvas {

	public static final GraphicsDevice DEF_GRAPHICS_DEVICE = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	
    public static final GraphicsConfiguration GFX_CONFIG = DEF_GRAPHICS_DEVICE.getDefaultConfiguration();

    public static final DisplayMode GFX_DISPLAY_MODE = DEF_GRAPHICS_DEVICE.getDisplayMode();

    public static final Logger logger = LoggerFactory.getLogger(Render.class);
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

        // Get the DPI info
        final int DPI = Toolkit.getDefaultToolkit().getScreenResolution();

        // Show render details
        logger.info("Render system initialized!");
        logger.info("[OpenGL] Using OpenGL: %s".formatted(OPEN_GL ? "Yes" : "No"));
        
        logger.info("DPI is set to {}", DPI);
        if (DPI != 96)
        	logger.info(" \u2514\u2500 (Recommended 96)", DPI);

        // Do "VSync"
        if (!vsync())
        	logger.warn("[VSync] Imposible to determinate the refresh rate!");

        // Show FPS Rate
        logger.info("FPS Rate: %d".formatted(Config.FPS_CAP));
    }
    
    public boolean vsync() {
		int fpsRate;
		fpsRate = Render.GFX_DISPLAY_MODE.getRefreshRate();
		
		if (fpsRate != DisplayMode.REFRESH_RATE_UNKNOWN)
			Config.FPS_CAP = fpsRate;

		return fpsRate != DisplayMode.REFRESH_RATE_UNKNOWN;
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
