package net.op.render.screens;

import java.awt.Graphics;

import net.op.render.Renderizable;
import net.op.util.Resource;

public abstract class Screen implements Renderizable {

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

    public static void renderCurrent(Graphics g) {
        if (current == null) {
            return;
        }

        current.render(g);
    }

    public final Resource getResource() {
        return this.resource;
    }

}