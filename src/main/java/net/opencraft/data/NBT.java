package net.opencraft.data;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

// Named Binary Tag
public class NBT {

	private Map<String, Object> smap = new HashMap<>();

	public NBT() {
	}

	public NBT(Map<String, Object> smap) {
		this.smap = smap;
	}

	public void write(OutputStream out) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(out);
		ObjectOutputStream oos = new ObjectOutputStream(bos);

		Iterator<Entry<String, Object>> i = smap.entrySet().iterator();
		Entry<String, Object> entry;
		while (i.hasNext()) {
			entry = i.next();
			oos.writeUTF(entry.getKey());
			oos.writeObject(entry.getValue());
		}

		oos.close();
	}
	
	public PipedInputStream read() throws IOException {
		PipedOutputStream pos = new PipedOutputStream();
		write(pos);
		return new PipedInputStream(pos);
	}

}
