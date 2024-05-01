package net.op.input;

import net.java.games.input.Controller;
import static org.josl.openic.IC10.IC_TRUE;
import static org.josl.openic.IC15.icBindDevice;
import static org.josl.openic.IC15.icGenDeviceId;
import org.josl.openic.input.ComponentMouse;
import org.josl.openic.input.Keyboard;
import org.josl.openic.input.Mouse;
import org.scgi.Context;

public class InputManager {

    private InputManager() {
    }
    
    public static void bindKeyboard() {
        long id = icGenDeviceId();
        Keyboard keyboard = new Keyboard();
        icBindDevice(Controller.Type.KEYBOARD, keyboard, IC_TRUE, id);
    }

    public static void bindMouse() {
        long id = icGenDeviceId();
        Mouse mouse = new ComponentMouse(Context.cv);
        icBindDevice(Controller.Type.MOUSE, mouse, IC_TRUE, id);
    }

}
