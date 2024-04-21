package net.op.render.screens;

import static net.op.render.display.DisplayManager.getDisplay;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.op.Client;
import net.op.render.display.Display;
import net.op.util.OCFont;
import net.op.util.Resource;

public class Loadscreen extends Screen {

    public static final Resource RESOURCE = Resource.format("opencraft:screens.loadscreen");

    private static final int TIMEOUT = 3250;
    private static int I_MAX = Display.HEIGHT / 2;
    public static Loadscreen instance = create();

    private float i = Display.HEIGHT - 1;
    private long start = -1;

    private Loadscreen() {
        super(RESOURCE);
    }

    public static Loadscreen create() {
        return new Loadscreen();
    }

    @Override
    public void render(Graphics g) {
        animatedLS((Graphics2D) g, true);
    }

    public void animatedLS(Graphics2D g2d, boolean slideUp) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, Display.WIDTH, Display.HEIGHT);

        // Draw OpenCraft Text
        g2d.setColor(Color.MAGENTA);
        g2d.setFont(OCFont.getSystemFont("SF Transrobotics").deriveFont(Font.BOLD, 26));
        g2d.drawString(Client.CODENAME.toUpperCase(), (Display.WIDTH - 333) / 2, (Display.HEIGHT + 135) / 2);

        // Draw OpenCraft Text
        g2d.setColor(Color.RED);
        g2d.setFont(g2d.getFont().deriveFont(Font.ITALIC, 70));
        g2d.drawString("OpenCraft", (Display.WIDTH - 376) / 2, (int) i);

        // Draw Rectangle
        g2d.setColor(Color.GREEN);
        g2d.drawRoundRect((Display.WIDTH - 451) / 2, (Display.HEIGHT - 164) / 2, 421, 112, 15, 15);

        /* OpenCraft Text Slide Up */
        if (slideUp) {
            if (i <= I_MAX) {
                attemptToChange(g2d);
            } else {
                i -= 2;
            }
        }

    }

    public void attemptToChange(Graphics g) {
        final long current = System.currentTimeMillis();

        if (start == -1) {
            start = current;
        }

        if (current - start <= TIMEOUT) {
            double alpha = 1 - (current - start) / (double) TIMEOUT;

            BufferedImage bi = new BufferedImage(Display.WIDTH, Display.HEIGHT, BufferedImage.TYPE_INT_ARGB);

            Graphics gbi = bi.getGraphics();
            MenuScreen.getInstance().render(g);

            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha);
            ((Graphics2D) gbi).setComposite(ac);
            animatedLS((Graphics2D) gbi, false);

            g.drawImage(bi, 0, 0, null);
            return;
        }

        instance = null;
        System.gc();
        getDisplay().setResizable(true);
        Screen.setCurrent(MenuScreen.getInstance());
    }

    public static Loadscreen getInstance() {
        return instance;
    }

}
