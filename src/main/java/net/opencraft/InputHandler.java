package net.opencraft;


import static org.josl.openic.IC10.IC_TRUE;
import static org.josl.openic.IC15.icBindDevice;
import static org.josl.openic.IC15.icGenDeviceId;
import org.josl.openic.input.ComponentMouse;
import org.josl.openic.input.Device;
import org.josl.openic.input.Keyboard;
import org.josl.openic.input.Mouse;
import org.lwjgl.opengl.Context;

public class InputHandler {

    private InputHandler() {
    }
    
    public static void bindKeyboard() {
        long id = icGenDeviceId();
        Keyboard keyboard = new Keyboard();
        icBindDevice(Device.Type.KEYBOARD, keyboard, IC_TRUE, id);
    }

    public static void bindMouse() {
        long id = icGenDeviceId();
        Mouse mouse = new ComponentMouse(Context.cv);
        icBindDevice(Device.Type.MOUSE, mouse, IC_TRUE, id);
    }

}
