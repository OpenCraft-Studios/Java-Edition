package net.op.util;

import java.util.Objects;

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
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.origin);
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Resource other = (Resource) obj;
        if (!Objects.equals(this.origin, other.origin)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return origin + ':' + id;
    }

    public static boolean isValid(String resourceURL) {
        if (resourceURL == null) {
            return false;
        } else if (resourceURL.isEmpty()) {
            return false;
        }

        return true;

    }

}
