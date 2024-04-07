package net.op.sound;

import java.util.ArrayList;
import java.util.List;

public class Tracks {

	private static Track menu;
	
	static {
		List<Sound> menu_sounds = new ArrayList<>();
		menu_sounds.add(Sound.of()
				.name("Menu 2")
				.resource("opencraft.sound:menu_2")
				.path("/menu/menu2.wav")
				.synthwave(false)
				.build());
		
		menu_sounds.add(Sound.of()
				.name("Menu 3")
				.resource("opencraft.sound:menu_3")
				.path("/menu/menu3.wav")
				.synthwave(false)
				.build());
		
		menu_sounds.add(Sound.of()
				.name("Menu 3")
				.resource("opencraft.sound:menu_3.synthwave")
				.path("/menu/menu3.swav")
				.synthwave(true)
				.build());
		
		
		menu = new Track("Menu Sounds", menu_sounds);
	}
	
	public static Track get(String track_name) {
		if (track_name.equalsIgnoreCase("Menu Sounds"))
			return menu;
		
		return null;
	}
	
}
