package net.op.crash;

import static net.op.GameSettings.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Optional;

public class CrashReport {

	private final Throwable error;

	/**
	 * Instances the CrashReport class.
	 * @see #create(Throwable)
	 */
	public CrashReport(Throwable error) {
		this.error = error;
	}

	/**
	 * This method instances the CrashReport class.
	 * It's the same as {@code new CrashReport(error)}
	 * 
	 * @param error The throwable/error
	 * @return A CrashReport instance
	 */
	public static CrashReport create(Throwable error) {
		return new CrashReport(error);
	}

	/**
	 * This method generates a random path to save
	 * the crash report using {@code Calendar} info.
	 * 
	 * @return A random file path
	 */
	public static String generatePath() {
		Calendar cInstance = Calendar.getInstance();

		String filepath = ((Integer) (cInstance.get(Calendar.YEAR))).toString() + "-" + cInstance.get(Calendar.MONTH)
				+ "-" + cInstance.get(Calendar.DAY_OF_MONTH) + "_" + cInstance.get(Calendar.HOUR) + "-"
				+ cInstance.get(Calendar.MINUTE) + "-" + cInstance.get(Calendar.SECOND);

		return getDirectory() + "/logs/crashes/crash-" + filepath + ".log";
	}

	/**
	 * This method attempts to save crash information
	 * to a {@code File}
	 * 
	 * @param file The file
	 */
	public void save(File file) {
		try {
			System.err.println("[CRASH_INFO]: Saved report file to: " + file.getAbsolutePath());
			if (!file.exists())
				file.createNewFile();

			this.write(new FileOutputStream(file));
		} catch (Exception ignored) {
			System.err.println("FATAL ERROR: Cannot save crash report to file!");
		}
	}
	
	/**
	 * This method attempts to save crash information
	 * to a {@code File}
	 * 
	 * @param filepath The file path to save
	 */
	public void save(String filepath) {
		this.save(new File(filepath));
	}

	/**
	 * This method returns the crash data
	 * @return Crash data 
	 */
	public String getInfo() {
		Optional<Throwable> cause = Optional.ofNullable(error.getCause());
		String causeMsg = cause.isPresent() ? cause.get().getMessage() : "Unknown";
		StringBuilder sb = new StringBuilder();

		sb.append("------ BEGIN CRASH REPORT ------");
		sb.append(String.format("\n  Exception %s\n", error.getClass().getCanonicalName()));
		sb.append(String.format("\n  Cause: %s\n", causeMsg));
		sb.append(String.format("\n  Message: %s\n",error.getMessage()));
		sb.append(String.format("\n  Exception Pointer: 0x%h", error));
		sb.append("\n");
		sb.append("\n(Not allowed to show stack trace)");
		sb.append("\n");
		sb.append("\n------- END CRASH REPORT -------");

		return sb.toString();
	}

	/**
	 * This method writes crash data to a {@code PrintStream}. This could be used
	 * for report crashes or show crash info to the user.
	 * 
	 * @throws IOException
	 * @param out The PrintStream to write crash report
	 * @see #write(OutputStream)
	 * @see #report()
	 */
	public void write(PrintWriter out) throws IOException {
		Optional<Throwable> cause = Optional.ofNullable(error.getCause());
		String causeMsg = cause.isPresent() ? cause.get().getMessage() : "Unknown";

		out.println("------ BEGIN CRASH REPORT ------");
		out.printf("  Exception %s\n", error.getClass().getCanonicalName());
		out.printf("  Cause: %s\n", causeMsg);
		out.printf("  Message: %s\n", error.getMessage());
		out.printf("  Exception Pointer: 0x%h", error);
		out.println();
		out.println("Stack trace:");
		error.printStackTrace(out);
		out.println();
		out.println("------- END CRASH REPORT -------");
		out.close();

		if (out.checkError())
			throw new IOException("Error when writing crash info!");
	}

	/**
	 * This method writes crash data to a {@code OutputStream}. This could be used
	 * for report crashes or show crash info to the user.
	 * 
	 * @throws IOException
	 * @param out The stream to write crash report
	 * @see #write(PrintStream)
	 * @see #report()
	 */
	public void write(OutputStream out) throws IOException {
		this.write(new PrintWriter(out));
	}

	/**
	 * This method is supposed to send crash information to Sibermatica (Owner of
	 * OpenCraft). And then identify the posible cause and release another update to
	 * fix that.
	 */
	public void report() {
		// Any report code
	}

}