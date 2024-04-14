package net.op.util;

import java.io.FileInputStream;
import java.io.InputStream;
import net.op.Config;

public class ResourceGetter {
    
    private ResourceGetter() {
    }
    
    public static InputStream getInternal(String path) {
        return ResourceGetter.class.getResourceAsStream(path);
    }
    
    public static InputStream getExternal(String path) {
        InputStream in = null;
        try {
            in = new FileInputStream(Config.getDirectory() + path);
        } catch (Exception ignored) {
            // TODO Internal logger
        }
        
        return in;
    }
    
    

}
