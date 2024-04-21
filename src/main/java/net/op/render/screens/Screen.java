package net.op.render.screens;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import net.op.render.Renderizable;
import net.op.render.display.Display;
import static net.op.render.display.DisplayManager.getDisplayHeight;
import static net.op.render.display.DisplayManager.getDisplaySize;
import static net.op.render.display.DisplayManager.getDisplayWidth;
import net.op.util.Resource;

public abstract class Screen implements Renderizable {

    public static final List<Dimension> RESOLUTIONS = new ArrayList() {

        {
            add(null); // Flexible dimension
            add(new Dimension(640, 480)); // VGA Resolution
            add(new Dimension(1024, 720)); // Horizontal phone resolution
            add(new Dimension(1080, 720)); // Some kind of stretch resolution
            add(Display.SIZE); // The default dimension
            add(new Dimension(1920, 1080)); // FHD Resolution
        }

    };

    /**
     * TODO: Add documentation pls
     */
    private static Dimension globalResolution = RESOLUTIONS.get(0);

    private static Screen current = Loadscreen.getInstance();
    private Resource resource;

    protected Screen(Resource resource) {
        this.resource = resource;
    }

    public static void setCurrent(Screen scene) {
        Screen.current = scene;
    }

    public static Screen getCurrent() {
        return current;
    }

    public static Dimension getResolution() {
        return globalResolution;
    }

    public static Point getOffset() {
        final int width = getDisplayWidth();
        final int height = getDisplayHeight();

        int x, y, w, h;
        w = globalResolution.width;
        h = globalResolution.height;

        x = (width - w) / 2;
        y = (height - h) / 2;

        return new Point(x, y);
    }

    public static Dimension getDrawSize() {
        return globalResolution == null ? getDisplaySize() : globalResolution;
    }

    public static void setResolution(Dimension resolution) {
        globalResolution = resolution;
    }

    public static void setResolution(int resolution_id) {
        setResolution(RESOLUTIONS.get(resolution_id));
    }

    public static void renderCurrent(Graphics g) {
        if (current == null) {
            return;
        }

        if (globalResolution != null) {

            int x, y, w, h;
            w = globalResolution.width;
            h = globalResolution.height;

            x = getOffset().x;
            y = getOffset().y;
            
            g.clipRect(x, y, w, h);
        }

        current.render(g);
    }

    public final Resource getResource() {
        return this.resource;
    }

}
