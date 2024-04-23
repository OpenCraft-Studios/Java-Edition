package net.op.util;

import java.io.FileInputStream;
import java.io.InputStream;
import net.op.Config;
import net.op.InternalLogger;

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

        if (Config.GAME_DIRECTORY.isBlank() || Config.GAME_DIRECTORY.isEmpty()) {
            path = path.substring(1);
        }

        try {
            in = new FileInputStream(Config.GAME_DIRECTORY + path);
        } catch (Exception ex) {
            InternalLogger.out.println(ResourceGetter.class.getName() + " ->");
            ex.printStackTrace(InternalLogger.out);
            InternalLogger.out.println();
        }

        return in;
    }

}
