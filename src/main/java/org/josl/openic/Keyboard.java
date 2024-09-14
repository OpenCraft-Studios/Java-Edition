package org.josl.openic;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class Keyboard implements KeyListener {

	private static HashSet<Integer> pressedKeys = new HashSet<>();
	private static Keyboard instance;
	
	private static int currentKeyCode = -1;
	
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
	
	public static boolean isKeyDown(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public static boolean isKeyClicked(int keyCode) {
        boolean down = isKeyDown(keyCode);
        if (down)
        	pressedKeys.remove(keyCode);
        
        return down;
    }
	
	public void keyTyped(KeyEvent e) {
	}

	@Override
    public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == currentKeyCode)
			return;
		
		pressedKeys.add(e.getKeyCode());
		currentKeyCode = keyCode;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        currentKeyCode = -1;
    }
	
	
}
