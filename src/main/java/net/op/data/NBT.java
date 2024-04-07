package net.op.data;

import java.util.HashMap;
import java.util.Map;

public class NBT {

	public Map<String, String> properties = new HashMap<>();

	public NBT() {
	}

	public static NBT format(String nbt) {

		NBT objNBT = new NBT();

		int cbrackets = 0;
		int brackets = 0;
		int parenthesis = 0;
		boolean qmarks = false;
		boolean apostrophe = false;

		if (!(nbt.startsWith("[") && nbt.endsWith("]")))
			throw new NBTFormatException();

		nbt = nbt.substring(1, nbt.length() - 1) + ',';

		char[] chArray = nbt.toCharArray();

		String key = "";
		String value = "";
		boolean keyIterated = false;
		for (char c : chArray) {
			switch (c) {
			case '=':
				if (keyIterated)
					throw new NBTFormatException();
				keyIterated = true;
				continue;
			case ',':
				if (brackets > 0 || cbrackets > 0 || parenthesis > 0 || apostrophe || qmarks)
					break;

				objNBT.properties.put(key.trim(), value.trim());

				key = "";
				value = "";
				keyIterated = false;
				continue;
			case '(':
				parenthesis++;
				break;
			case ')':
				parenthesis--;
				break;
			case '[':
				brackets++;
				break;
			case ']':
				brackets--;
				break;
			case '{':
				cbrackets++;
				break;
			case '}':
				cbrackets--;
				break;
			case '"':
				qmarks = !qmarks;
				break;
			case '\'':
				apostrophe = !apostrophe;
				break;
			}
			if (keyIterated)
				value += c;
			else
				key += c;
		}

		if (!(cbrackets == 0 && brackets == 0 && parenthesis == 0 && !qmarks && !apostrophe))
			throw new NBTFormatException();

		return objNBT;
	}

}
