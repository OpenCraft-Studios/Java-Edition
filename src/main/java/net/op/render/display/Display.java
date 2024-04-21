package net.op.render.display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import net.op.Client;
import net.op.input.InputManager;
import net.op.render.Render;
import net.op.render.textures.Texture;
import static net.op.util.ResourceGetter.getInternal;

public final class Display extends JFrame {

    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 854;
    public static final int HEIGHT = 480;

    public static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);

    public Display() {
        super(Client.DISPLAY_NAME);
    }

    public void defaultConfig() {
        // When display is closed, stop the game
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Set display size
        setSize(SIZE);
        setMinimumSize(new Dimension(618, 315));
        setPreferredSize(SIZE);

        setResizable(false); // Make not resizable (this will change after the load screen has been loaded)
        setLayout(new BorderLayout()); // Set layout to a border layout
        setLocationRelativeTo(null); // Center display

        // Set icons
        List<InputStream> texStrm = new ArrayList<>();
        texStrm.add(getInternal("/icons/icon-1.png"));
        texStrm.add(getInternal("/icons/icon-2.png"));
        texStrm.add(getInternal("/icons/icon-3.png"));
        texStrm.add(getInternal("/icons/icon-4.png"));
        texStrm.add(getInternal("/icons/icon-5.png"));
        
        List<Image> icons = texStrm.stream().map(in -> (Image) Texture.read(in).getImage()).toList();
        if (icons.stream().anyMatch(icon -> icon == null))
            icons = null;
        
        setIconImages(icons);
        requestFocus();

    }

    public void update() {
        repaint();
        revalidate();
    }

    public boolean isMinimized() {
        return (getExtendedState() & Frame.ICONIFIED) != 0;
    }

    public void setGraphics(Render renderSystem) {
        InputManager.bindMouse(renderSystem);
        add(renderSystem);
    }

}
