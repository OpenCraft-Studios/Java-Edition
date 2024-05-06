package net.op.spectoland;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.zip.GZIPOutputStream;

import net.op.crash.CrashReport;

public class SpectoError {

	private static int exception_counter = 0;

	private SpectoError() {
	}

	public static void warn(final String message, Class<?> clazz) {
		InternalLogger.out.println();
		InternalLogger.out.println("WARNING: Caused from class " + clazz.getName() + ":");
		InternalLogger.out.println(" | Message: " + message);
		InternalLogger.out.println(" +");
		InternalLogger.out.println();
	}

	public static void error(final String message, Class<?> clazz) {
		InternalLogger.out.println();
		InternalLogger.out.println("ERROR: Caused from class " + clazz.getName() + ":");
		InternalLogger.out.println(" | Error Message: " + message);
		InternalLogger.out.println(" +");
		InternalLogger.out.println();
	}
	
	public static void info(final String message) {
		InternalLogger.out.printf("\n INFO: %s\n", message);
	}
	
	public static void thisIsStrange(Throwable tb, Class<?> clazz) {
		InternalLogger.out.println();
	}

	public static byte[] reportResult(CrashReport crash) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			GZIPOutputStream gzos = new GZIPOutputStream(baos);
			PrintStream out = new PrintStream(gzos);
			
			crash.write(out);
			out.println();
			out.println(InternalLogger.stackTrace());
			
			out.close();
			gzos.close();
			baos.close();
		} catch (Exception ex) {
			// wTf
			ignored(ex, SpectoError.class);
		}
		
		return baos.toByteArray();
	}

	public static void ignored(Throwable tb, Class<?> clazz) {
		InternalLogger.ignoredExceptions++;
		InternalLogger.out.println("[IGNORED EXCEPTION] " + clazz.getName() + " ->");
		tb.printStackTrace(InternalLogger.out);
		InternalLogger.out.println();
	}

	public static void process(Throwable tb) {
		try {
			throw tb;
		} catch (OutOfMemoryError ignored) {
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
