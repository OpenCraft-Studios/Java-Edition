package org.josl.openic;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

	private static Set<Integer> pressed = new HashSet<>();
    private static Mouse instance;
    private static int x, y, xo, yo, xd, yd;
    private static int dwheel;
    
    private static int currentButton = -1;
    
    private Mouse(Component parent) {
    	if (parent instanceof JFrame)
    		System.err.println("We don't recommend using JFrame for Mouse detection!");
    	
    	parent.addMouseListener(this);
    	parent.addMouseMotionListener(this);
    	parent.addMouseWheelListener(this);
    	parent.setFocusable(true);
    	parent.requestFocusInWindow();
    }
    
    public static void create(Component parent) {
    	instance = new Mouse(parent);
    }
    
    public static void destroy() {
    	instance = null;
    }
    
    public static boolean isCreated() {
    	return instance != null;
    }
    
	public static boolean isButtonDown(int button) {
        return pressed.contains(button);
    }

    public static boolean isButtonClicked(int button) {
        boolean down = isButtonDown(button);
        if (down)
        	pressed.remove(button);
        
        return down;
    }

    public static int getX() {
    	return x;
    }

    public static int getY() {
        return y;
    }
    
    public static int getDX() {
    	return xd;
    }
    
    public static int getDY() {
    	return yd;
    }
    
    public static int getDWheel() {
    	return dwheel;
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
    	x = e.getX();
        y = e.getY();
        
        xd = x - xo;
        yd = y - yo;
        xo = x;
        yo = y;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		dwheel = e.getWheelRotation();
	}
	
	@Override
    public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		if (button == currentButton)
			return;
		
        pressed.add(e.getButton());
        currentButton = button;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressed.remove(e.getButton());
        currentButton = -1;
    }
    
    public static boolean inRange(Rectangle bounds) {
		Point mousePos = getPos();
		return bounds.contains(mousePos);
	}

	private static Point getPos() {
		return new Point(x, y);
	}

	public static boolean inRange(int x, int y, int w, int h) {
		Rectangle bounds = new Rectangle(x, y, w, h);
		return inRange(bounds);
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseExited(MouseEvent e)  {}
	
}
