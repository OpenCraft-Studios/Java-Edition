package net.op.render.textures;

import java.awt.Image;

public final class GUITilesheet extends Tilesheet {

    private static GUITilesheet instance = null;
    
    public static final int BUTTON_DISABLED = 0;
    public static final int BUTTON = 1;
    public static final int BUTTON_HIGHLIGHTED = 2;

    public GUITilesheet(Texture tilesheet) {
        super(tilesheet);
    }

    public GUITilesheet(Tilesheet other) {
        super(other);
    }

    public GUITilesheet(String path) {
        super(path);
    }
    
    public static GUITilesheet create(String path) {
        return instance = new GUITilesheet(path);
    }
    
    public static GUITilesheet getInstance() {
        return instance;
    }

    public Image getButton(int button_id) {
        return get(0, button_id * 20, 200, 20);
    }

    public Image getArrow(int arrow) {
        int x = switch (arrow) {
            case 0 ->
                200;
            case 1 ->
                215;
            case 2 ->
                229;
            case 3 ->
                244;

            default ->
                0;
        };

        return get(x, 20, 14, 22);
    }

    public Image getLogo() {
        return get(0, 61, 271, 44);
    }

    public Image getBackground() {
        return get(242, 0, 16, 16);
    }

}
