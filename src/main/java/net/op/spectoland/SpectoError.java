package net.op.spectoland;

import static net.op.spectoland.SpectoUtils.ErrorTemplates.*;

public class SpectoError {
	
	private static int exception_counter = 0;
	
	private SpectoError() {
	}
	
	public static String serverNotFound(String server_name) {
		return String.format(SERVER_NOT_FOUND, server_name);
	}
	
	public static String warn(final String message) {
		return "WARNING: " + message;
	}
	
	public static String error(final String message) {
		return "ERROR: " + message;
	}
	
	public static void process(Throwable tb) {
		try {
			throw tb;
		} catch (OutOfMemoryError error) {
			/*
			 * If any OutOfMemoryError occurs while the game is in execution we can catch it
			 * and run the Java Garbage Collector.
			 * 
			 * But if "that" overflows 2 times the memory the program will exit for safety.
			 * Because is better to prevent the game crashing by anything, but if it's
			 * repetitive is better to stop.
			 * 
			 * TODO Implement that
			 */
			System.gc();
		} catch (ArithmeticException ignored) {
			/*
			 * ATTENTION: This exception ignoring is probably not the best option. But it is
			 * not a very very important error so we just ignore it. It could be likely
			 * dangerous but we like to take risks.
			 */
		} catch (Throwable any) {
			/*
			 * We have a variable called "exceptionCounter" that counts the exceptions that
			 * the game has at the moment, if it reaches 5 it will print the stack trace and
			 * exit the game.
			 * 
			 * This is that way because we don't want the game crash for anything, just for
			 * important. And it's considered important if the error repeats by five.
			 */
			if (exception_counter++ >= 5) {
				any.printStackTrace();
				System.exit(1);
			}
		}
	}

}
