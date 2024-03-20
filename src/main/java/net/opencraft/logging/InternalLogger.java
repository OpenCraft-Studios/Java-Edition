package net.opencraft.logging;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class InternalLogger {

	private static FileOutputStream fos;
	public static PrintStream out;
	
	static {
		try {
			fos = new FileOutputStream(LoggerConfig.getLogDir() + "/internal.log");
			out = new PrintStream(fos);
		} catch (Exception ignored) {
			out = new PrintStream(OutputStream.nullOutputStream());
		}
	}
	
	private InternalLogger() {
	}
	
	
	
}
