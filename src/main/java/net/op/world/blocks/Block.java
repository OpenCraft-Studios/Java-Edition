package net.op.world.blocks;

import net.op.data.Identificable;
import net.op.util.Resource;

public class Block extends Identificable {

	public static Block DIRT_BLOCK = new Block("opencraft:dirt", 3, 1);
	private final Resource res;
	
	public Block(Resource res, int id, int data_value) {
		super(id, data_value);
		this.res = res;
	}
	
	public Block(String res, int id, int data_value) {
		this(Resource.format(res), id, data_value);
	}
	
	public Resource getResource() {
		return this.res;
	}
	
}
