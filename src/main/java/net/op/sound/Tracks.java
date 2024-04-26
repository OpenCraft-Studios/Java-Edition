package net.op.sound;

import java.util.ArrayList;
import java.util.List;
import net.op.util.Resource;

public class Tracks {

    private static Track menu;

    static {
        List<Sound> menu_sounds = new ArrayList<>();
        menu_sounds.add(Sound.of()
                .name("Menu 2")
                .resource("opencraft:sounds.menu_2")
                .path("/newmusic/menu2.ogg")
                .author("C418")
                .build());

        menu_sounds.add(Sound.of()
                .name("Menu 3")
                .resource("opencraft:sounds.menu_3")
                .path("/newmusic/menu3.ogg")
                .author("C418")
                .build());
        
        menu_sounds.add(Sound.of()
        		.name("Mission")
        		.resource("opencraft:sounds.mission")
        		.path("/music/mission.ogg")
        		.build());

        menu = new Track("Menu Sounds", menu_sounds);
    }

    public static Sound getByResource(Resource resource) {
        Track ost = new Track("OpenCraft OST");
        ost.appendTrack(menu);

        return ost.findSound(resource);
    }

    public static Sound getSoundByResource(String strResource) {
        return getByResource(Resource.format(strResource));
    }

    public static Track get(String track_name) {
        return track_name.equalsIgnoreCase("Menu Sounds") ? menu : null;
    }

}
