package net.op.util;

public final class Resource {

    private String origin;
    private String id;

    private Resource(String origin, String id) {
        this.origin = origin;
        this.id = id;
    }

    private Resource(Resource other) {
        this.origin = other.origin;
        this.id = other.id;
    }

    public static Resource of(String origin, String id) {
        return new Resource(origin, id);
    }

    public static Resource of(Resource other) {
        return new Resource(other);
    }

    public static Resource of(String formated_res) {
        return Resource.of(Resource.format(formated_res));
    }

    public static Resource format(String formated_res) {
        String origin = formated_res.split(":")[0];
        String id = formated_res.split(":")[1];

        return Resource.of(origin, id);
    }

    public String getOrigin() {
        return this.origin;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return origin + ':' + id;
    }

    public static boolean isValid(String resourceURL) {
        if (resourceURL == null) {
            return false;
        }

        if (resourceURL.isBlank() || resourceURL.isEmpty()) {
            return false;
        }

        return true;

    }

}
