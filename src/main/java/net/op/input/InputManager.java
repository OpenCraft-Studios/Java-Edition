package net.op.input;

import net.java.games.input.Controller;
import net.op.render.Render;
import static org.josl.openic.IC10.IC_TRUE;
import static org.josl.openic.IC15.icBindDevice;
import static org.josl.openic.IC15.icGenDeviceId;
import org.josl.openic.input.ComponentMouse;
import org.josl.openic.input.Keyboard;
import org.josl.openic.input.Mouse;

public class InputManager {

    private InputManager() {
    }
    
    public static void bindKeyboard() {
        long id = icGenDeviceId();
        Keyboard keyboard = new Keyboard();
        icBindDevice(Controller.Type.KEYBOARD, keyboard, IC_TRUE, id);
    }

    public static void bindMouse(Render renderSystem) {
        long id = icGenDeviceId();
        Mouse mouse = new ComponentMouse(renderSystem);
        icBindDevice(Controller.Type.MOUSE, mouse, IC_TRUE, id);
    }

}
