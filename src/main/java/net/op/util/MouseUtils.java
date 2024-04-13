package net.op.util;

import static org.josl.openic.IC12.icGetMouseDX;
import static org.josl.openic.IC12.icGetMouseDY;

import org.josl.openic.IC;
import org.josl.openic.input.ComponentMouse;

public class MouseUtils {

	private MouseUtils() {
	}

	public static boolean inRange(int[] bounds) {
		double mouseX = icGetMouseDX();
		double mouseY = icGetMouseDY();

		return mouseX >= bounds[0] && mouseY >= bounds[1] && mouseX <= bounds[2] && mouseY <= bounds[3];
	}

	public static boolean inRange(int x, int y, int w, int h) {
		return inRange(new int[] { x, y, x + w, y + h });
	}

	public static boolean isButtonPressed(int button) {
		return ((ComponentMouse) IC.getDefaultMouse()).isButtonPressed(button);
	}

}
