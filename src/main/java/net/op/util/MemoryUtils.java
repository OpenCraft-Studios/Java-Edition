package net.op.util;

public class MemoryUtils {

    private static Runtime runtime = Runtime.getRuntime();

    public static long getMemoryUsedInBytes() {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static String getMemoryUsed() {
        return unityConversor10(getMemoryUsedInBytes());
    }

    public static String getMemoryUsedInPercentage() {
        long usedMemory = getMemoryUsedInBytes();
        long freeMemory = runtime.maxMemory() - usedMemory;
        long percentage = 0;
        if (usedMemory > freeMemory) {
            percentage = freeMemory * 100 / usedMemory;
        } else {
            percentage = usedMemory * 100 / freeMemory;
        }

        percentage /= 10;
        percentage *= 10;

        return Long.toString(percentage);
    }

    public static String unityConversor10(long bytes) {
        int m;

        for (m = 0; bytes > 1024; m++) {
            bytes /= 1024;
        }

        String append = switch (m) {
            case 1 ->
                "KB";
            case 2 ->
                "MB";
            case 3 ->
                "GB";
            case 4 ->
                "TB";

            default ->
                "";
        };

        bytes /= 10;
        bytes *= 10;

        return Long.toString(bytes) + append;
    }

    public static String unityConversor(long bytes) {
        int m;

        for (m = 0; bytes > 1024; m++) {
            bytes /= 1024;
        }

        String append = switch (m) {
            case 1 ->
                "KB";
            case 2 ->
                "MB";
            case 3 ->
                "GB";
            case 4 ->
                "TB";

            default ->
                "";
        };

        return Long.toString(bytes) + append;
    }

}
