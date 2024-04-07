package net.op;

import java.util.Properties;

import net.op.data.packs.Pack;

public class Config {
	public static int Language = 0;
	public static int FPS_CAP = 60;
	public static String Directory = "opcraft";
	public static Pack ResourcePack = Pack.getDefaultPack();

	public static Properties toProperties() {
		Properties properties = new Properties();

		properties.setProperty("gameDir", Directory);
		properties.setProperty("lang", Integer.toString(Language));

		String strPack = "another";
		if (ResourcePack.equals(Pack.getDefaultPack()))
			strPack = "default";
		else if (ResourcePack.equals(Pack.getInternalPack()))
			strPack = "internal";

		properties.setProperty("respack", strPack);

		return properties;
	}

	public static void read(Properties properties) {
		Directory = properties.getProperty("gameDir");
		Language = Integer.parseInt(properties.getProperty("lang"));
		String strPack = properties.getProperty("respack");

		if (strPack.equalsIgnoreCase("default"))
			ResourcePack = Pack.getDefaultPack();
		else if (strPack.equalsIgnoreCase("internal"))
			ResourcePack = Pack.getInternalPack();
	}

}
