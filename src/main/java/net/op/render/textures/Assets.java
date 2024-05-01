package net.op.render.textures;

import java.awt.Image;
import java.util.Arrays;
import java.util.stream.Stream;

public class Assets {

	public static final int BUTTON_DISABLED = 0;
    public static final int BUTTON = 1;
    public static final int BUTTON_HIGHLIGHTED = 2;
	
	private final Tilesheet gui;

	private Assets(Tilesheet gui) {
		this.gui = gui;
	}
	
	private Assets(Tilesheet... tss) {
		this(tss[0]);
	}

	public static Assets create(Tilesheet... tss) {
		return new Assets(tss);
	}
	
	public static Assets create(String... tss) {
		Stream<String> tilesheetsPath = Arrays.stream(tss);
		Stream<Tilesheet> tilesheets = tilesheetsPath.map(path -> Tilesheet.read(path));
		return create(tilesheets.toList().toArray(new Tilesheet[0]));
	}

	public Image getButton(int button_id) {
		return gui.get(0, button_id * 20, 200, 20);
	}

	public Image getArrow(int arrow) {
		int x = switch (arrow) {
			case 0 -> 200;
			case 1 -> 215;
			case 2 -> 229;
			case 3 -> 244;

			default -> 0;
		};

		return gui.get(x, 20, 14, 22);
	}

	public Image getLogo() {
		return gui.get(0, 61, 271, 44);
	}

	public Image getBackground() {
		return gui.get(242, 0, 16, 16);
	}

}
