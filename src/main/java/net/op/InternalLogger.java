package net.op;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class InternalLogger {

    private static OutputStream os;
    public static PrintStream out;

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

    public static byte[] getData() {
        return ((ByteArrayOutputStream) os).toByteArray();
    }

    public static String getOutputData() {
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
