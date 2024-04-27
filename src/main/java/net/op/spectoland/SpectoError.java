package net.op.spectoland;

import static net.op.spectoland.SpectoUtils.ErrorTemplates.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.zip.GZIPOutputStream;

import net.op.Config;
import net.op.crash.CrashReport;

public class SpectoError {

	private static int exception_counter = 0;

	public static class InternalLogger {

		public static int ignoredExceptions = 0;

		private static OutputStream os;
		private static PrintStream out;

		static {
			os = new ByteArrayOutputStream();
			out = new PrintStream(os);
		}

		private InternalLogger() {
		}

		public static void writeFile() throws IOException {
			File internalFile = new File(Config.GAME_DIRECTORY + "/logs/internal.log");
			if (!internalFile.getParentFile().exists()) {
				internalFile.getParentFile().mkdirs();
			}
			if (!internalFile.exists()) {
				internalFile.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(internalFile);
			fos.write(getData());
			fos.close();
		}

		private static byte[] getData() {
			return ((ByteArrayOutputStream) os).toByteArray();
		}

		public static String stackTrace() {
			return new String(getData());
		}

		public static void stopLogging() {
			try {
				os.close();
			} catch (Exception ignored) {
			}

			out = new PrintStream(OutputStream.nullOutputStream());
		}

	}

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

	public static byte[] reportResult(CrashReport crash) {
		var baos = new ByteArrayOutputStream();
		
		try {
			var gzos = new GZIPOutputStream(baos);
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
		InternalLogger.out.println(clazz.getName() + " ->");
		tb.printStackTrace(InternalLogger.out);
		InternalLogger.out.println();
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
