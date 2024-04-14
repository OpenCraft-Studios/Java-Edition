package net.op.util;

import java.io.FileInputStream;
import java.io.InputStream;
import net.op.Config;

public class ResourceGetter {

    private ResourceGetter() {
    }

    public static InputStream getInternal(String path) {
        if (!Resource.isValid(path)) {
            return null;
        }

        return ResourceGetter.class.getResourceAsStream(path);
    }

    public static InputStream getExternal(String path) {
        InputStream in = null;
        if (!Resource.isValid(path)) {
            return in;
        }

        if (Config.DIRECTORY.isBlank() || Config.DIRECTORY.isEmpty()) {
            path = path.substring(1);
        }

        try {
            in = new FileInputStream(Config.DIRECTORY + path);
        } catch (Exception ignored) {
            // TODO Internal logger
        }

        return in;
    }

}
