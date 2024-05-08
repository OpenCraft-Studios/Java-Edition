package net.op.spectoland;

import static net.op.spectoland.ILogger.*;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import net.op.crash.CrashReport;

public class SpectoError {

	private static int exception_counter = 0;

	private SpectoError() {
	}

	public static void warn(final String message) {
		iex++;
		out.println(" WARN: " + message);
	}

	public static void error(final String message, Class<?> clazz) {
		iex++;
		out.println(" ERROR: " + message);
	}
	
	public static void info(final String message) {
		out.println(" INFO: " + message);
	}
	
	public static void possibleCorruption(Throwable tb, Class<?> clazz) {
		iex++;
		
		String strTime;
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		strTime = formatter.format(new Date());
		
		out.println();
		out.println(" ...DUMP...: at {" + strTime + "} in class (" + clazz.getName() + "):");
		out.println("    '' An error was marked as unrecognized.");
		out.println("    '' We are trying to identify the possible cause of this.");
		out.println("    '' === STACK TRACE ===");
		tb.printStackTrace(out);
		out.println();
	}

	public static byte[] reportResult(CrashReport crash) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			GZIPOutputStream gzos = new GZIPOutputStream(baos);
			
			crash.write(gzos);
			gzos.write((byte) '\n');
			gzos.write(ILogger.getData());
			
			gzos.close();
		} catch (Exception ex) {
			possibleCorruption(ex, SpectoError.class);
			ex.printStackTrace();
		}
		
		return baos.toByteArray();
	}

	public static void ignored(Throwable tb, Class<?> clazz) {
		iex++;
		
		String strTime;
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		strTime = formatter.format(new Date());
		
		String strCause;
		if (tb.getCause() != null)
			strCause = tb.getCause().getMessage();
		else
			strCause = "unknown";
		
		out.println();
		out.println(" ERROR: at {" + strTime + "} in class (" + clazz.getName() + "):");
		out.println("   * type: exception." + tb.getClass().getSimpleName());
		out.println("   * cause: " + strCause);
		out.println("   * message: " + tb.getMessage());
		out.println("   === STACK TRACE ===");
		tb.printStackTrace(out);
		out.println();
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
			 * FIXME Implement that
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
