package org.lwjgl.input;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class Keyboard implements KeyListener {

	private static HashSet<Integer> currentKeys = new HashSet<>();
    private static HashSet<Integer> prevKeys = new HashSet<>();
	
	private static Keyboard instance;
	
	private Keyboard(Component parent) {
		parent.addKeyListener(this);
		parent.setFocusable(true);
		parent.requestFocusInWindow();
	}

	public static void create(Component parent) {
		instance = new Keyboard(parent);
	}
	
	public static void destroy() {
		instance = null;
	}
	
	public static boolean isCreated() {
		return instance != null;
	}
	
	public static boolean isKeyPressed(int keyCode) {
        return currentKeys.contains(keyCode);
    }

    public static boolean isKeyJustPressed(int keyCode) {
        return currentKeys.contains(keyCode) && !prevKeys.contains(keyCode);
    }
	
	public static void poll() {
		prevKeys.clear();
        prevKeys.addAll(currentKeys);	
	}
	
	public void keyTyped(KeyEvent e) {}

	@Override
    public void keyPressed(KeyEvent e) {
        currentKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentKeys.remove(e.getKeyCode());
    }
	
	
}
