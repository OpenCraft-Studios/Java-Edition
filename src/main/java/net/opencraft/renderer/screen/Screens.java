package net.opencraft.renderer.screen;

import net.opencraft.renderer.Renderizable;
import net.opencraft.util.Resource;

public enum Screens {

	LOAD_SCREEN(LoadScreen.RESOURCE),
	TITLE_SCREEN(TitleScreen.RESOURCE);

	private static Screens current = TITLE_SCREEN;
	private final Resource res;

	Screens(Resource res) {
		this.res = res;
	}

	public Renderizable getScene(Screens sc) {
		return switch (sc) {
			case TITLE_SCREEN -> TitleScreen.getInstance();
			case LOAD_SCREEN -> LoadScreen.getInstance();
			default -> null;
		};
	}

	public Renderizable getScene() {
		return getScene(this);
	}

	public static Screens getCurrent() {
		return current;
	}

	public void setCurrent() {
		setCurrent(this);
	}
	
	public static Screens setCurrent(Screens scn) {
		return current = scn;
	}

	public Resource getResource() {
		return res;
	}

}
