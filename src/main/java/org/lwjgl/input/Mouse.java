package org.lwjgl.input;

import java.awt.Component;
import java.awt.event.*;
import java.util.HashSet;

import javax.swing.JFrame;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

	private static HashSet<Integer> currentMouseButtons = new HashSet<>();
    private static HashSet<Integer> prevMouseButtons = new HashSet<>();
    private static Mouse instance;
    private static int x, y, xo, yo, xd, yd;
    private static int dwheel;
    
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
    
    public static void poll() {
    	prevMouseButtons.clear();
        prevMouseButtons.addAll(currentMouseButtons);
    }
    
	public static boolean isButtonDown(int button) {
        return currentMouseButtons.contains(button);
    }

    public static boolean isButtonJustPressed(int button) {
        return currentMouseButtons.contains(button) && !prevMouseButtons.contains(button);
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
        currentMouseButtons.add(e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentMouseButtons.remove(e.getButton());
    }

	public void mouseEntered(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseExited(MouseEvent e)  {}
	
}
