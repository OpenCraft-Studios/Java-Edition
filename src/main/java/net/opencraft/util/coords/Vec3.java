package net.opencraft.util.coords;

public final class Vec3 {

	public int x;
	public int y;
	public int z;

	public Vec3(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public static Vec3 newTemp(int x, int y, int z) {
		return new Vec3(x, y, z);
	}

}
