package net.op.sound;

import java.io.BufferedInputStream;
import java.io.InputStream;

import net.op.Config;
import net.op.render.textures.Assets;
import net.op.util.Resource;

public class Sound {

    private final Resource resource;
    private final String name;
    private final String path;
    private final boolean synthwave;

    public Sound(Resource resource, String name, String path, boolean synthwave) {
        this.resource = resource;
        this.name = name;
        this.path = path;
        this.synthwave = synthwave;
    }

    public Sound(String resource, String name, String path, boolean synthwave) {
        this(Resource.format(resource), name, path, synthwave);
    }

    public static Sound.Builder of() {
        return new Builder();
    }

    public Resource getResource() {
        return resource;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean isSynthwave() {
        return synthwave;
    }

    public void play() {
        SoundManager.playSound(this, false);
    }

    public void loop() {
        SoundManager.playSound(this, true);
    }

    public InputStream inputStream() {
        InputStream in;
        in = Assets.forResources(Config.PACK).bindOrDefault("/assets/opencraft/sounds" + path);

        return new BufferedInputStream(in);
    }

    public static class Builder {

        private Resource res = null;
        private String name = null;
        private String path = null;
        private boolean synthwave = false;

        public Builder resource(Resource res) {
            this.res = res;
            return this;
        }

        public Builder resource(String res) {
            resource(Resource.format(res));
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder synthwave(boolean synthwave) {
            this.synthwave = synthwave;
            return this;
        }

        public Sound build() {
            if (res == null || name == null || path == null) {
                throw new IllegalArgumentException("res or name or path can't be null!");
            }

            return new Sound(res, name, path, synthwave);
        }

    }

}
