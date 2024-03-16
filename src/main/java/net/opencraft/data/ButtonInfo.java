package net.opencraft.data;

import java.nio.IntBuffer;
import java.util.Objects;

public class ButtonInfo {

	public static final ButtonInfo EMPTY = null;
	
	private final int[] vertices;

	public ButtonInfo(int[] vertices) {
		this.vertices = vertices;
	}

	public ButtonInfo(IntBuffer vertices) {
		this(vertices.array());
	}
	
	public int[] getBounds() {
		return this.vertices;
	}
	
	public IntBuffer getBufferedBounds() {
		return IntBuffer.wrap(getBounds());
	}

	public static Builder of() {
		return new Builder();
	}
	
	public static class Builder {

		private int[] vertices;

		public Builder vertices(int[] vertices) {
			this.vertices = vertices;
			return this;
		}

		public Builder vertices(IntBuffer vertices) {
			return this.vertices(vertices.array());
		}

		public ButtonInfo build() {
			if (Objects.isNull(vertices))
				throw new IllegalArgumentException("Cannot build ButtonInfo from null vertices!");
			
			return new ButtonInfo(vertices);
		}
		
	}

}
