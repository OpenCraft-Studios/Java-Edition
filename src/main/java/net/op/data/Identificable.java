package net.op.data;

public class Identificable {

	protected final short id;
	
	protected Identificable(int id) {
		this.id = (short) id;
	}
	
	protected Identificable(int id, int data_value) {
		this((id & 0xFF) << 8 | (data_value & 0xFF));
	}
	
	public int getID() {
		return this.id >> 8;
	}
	
	public int getDataValue() {
		return this.id & 0xFF;
	}
	
}
