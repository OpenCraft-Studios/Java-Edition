package net.op.render.display;

import java.awt.Dimension;
import net.op.render.Render;

public class DisplayManager {

    private static Display display;

    private DisplayManager() {
    }

    public static void createDisplay() {
        display = new Display();
        display.defaultConfig();
    }

    public static void showDisplay() {
        display.setVisible(true);
    }

    public static void hideDisplay() {
        display.setVisible(false);
    }

    public static void destroyDisplay() {
        display = null;
    }

    public static void updateDisplay() {
        display.update();
    }

    public static void setDisplayGraphics(Render renderDragon) {
        display.setGraphics(renderDragon);
    }

    public static int getDisplayWidth() {
        return display.getWidth();
    }

    public static int getDisplayHeight() {
        return display.getHeight();
    }
    
    public static Dimension getDisplaySize() {
        return display.getSize();
    }

    public static Display getDisplay() {
        return display;
    }

    public static boolean isDisplayAlive() {
        if (display == null) {
            return false;
        }

        boolean notDisposed = display.isDisplayable();
        boolean visible = display.isVisible();

        return notDisposed && visible;
    }

}
