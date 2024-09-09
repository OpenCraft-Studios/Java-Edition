package net.opencraft.spectoland;

import static net.opencraft.OpenCraft.*;

import java.io.*;

public class ILogger {

	public static int iex = 0;

	public static OutputStream os;
	public static PrintStream out;

	static {
		os = new ByteArrayOutputStream();
		out = new PrintStream(os);
		
		out.println("# version 4");
		out.println();
	}

	private ILogger() {
	}

	public static void writeFile() throws IOException {
		File internalFile = new File(oc.directory, "logs/internal.log");
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

	public static byte[] getData() {
		return ((ByteArrayOutputStream) os).toByteArray();
	}

	public static String stackTrace() {
		return new String(getData());
	}

	public static void stopLogging() {
		try {
			out.close();
		} catch (Exception ignored) {
		}
	}

}