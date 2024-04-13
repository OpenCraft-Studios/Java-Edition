package net.op.render.textures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.op.data.packs.Pack;

public class Tilesheet {

	private static final GraphicsConfiguration GFX_CONFIG = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getDefaultScreenDevice().getDefaultConfiguration();

	public static final int BUTTON_DISABLED = 0;
	public static final int BUTTON = 1;
	public static final int BUTTON_HIGHLIGHTED = 2;
	public static final Logger logger = Logger.getLogger(Tilesheet.class.getName());

	private final BufferedImage tilesheet;

	private Pack pack;

	public Tilesheet(Pack pack, boolean loadTextures) {
		this.pack = pack;

		if (loadTextures)
			tilesheet = toCompatibleImage((BufferedImage) this.bindTexture("/gui/everything.png").getImage());
		else
			tilesheet = null;
	}

	public Tilesheet(boolean loadTextures) {
		this(Pack.getDefaultPack(), loadTextures);
	}

	public static BufferedImage toCompatibleImage(final BufferedImage image) {
		if (image.getColorModel().equals(GFX_CONFIG.getColorModel()))
			return image;

		final BufferedImage new_image = GFX_CONFIG.createCompatibleImage(image.getWidth(), image.getHeight(),
				image.getTransparency());

		final Graphics2D g2d = (Graphics2D) new_image.getGraphics();

		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();

		return new_image;
	}

	public static Tilesheet forTextures(Pack resourcePack) {
		return new Tilesheet(resourcePack, true);
	}

	public static Tilesheet forResources(Pack resourcePack) {
		return new Tilesheet(resourcePack, false);
	}

	public static InputStream bindExternalResource(String resourceURL) {
		return Pack.getDefaultPack().getResource(resourceURL);
	}

	public static InputStream bindInternalResource(String resourceURL) {
		return Pack.getInternalPack().getResource(resourceURL);
	}

	public static List<Image> getIcons() {
		List<Texture> icons = new ArrayList<>();
		icons.add(bindInternalTexture("/icons/icon-1.png"));
		icons.add(bindInternalTexture("/icons/icon-2.png"));
		icons.add(bindInternalTexture("/icons/icon-3.png"));
		icons.add(bindInternalTexture("/icons/icon-4.png"));
		icons.add(bindInternalTexture("/icons/icon-5.png"));

		boolean nullImg = icons.stream().anyMatch(tex -> tex.isNull());
		if (nullImg)
			return null;

		return icons.stream().map(tex -> tex.getImage()).toList();
	}

	public InputStream bindResource(String resourceURL) {
		return this.pack.getResource(resourceURL);
	}

	public InputStream bindOrDefault(String resourceURL) {
		InputStream pkgIN = bindResource(resourceURL);
		InputStream in = bindExternalResource(resourceURL);

		if (pkgIN == null)
			return in;
		else
			return pkgIN;
	}

	public static Texture bindExternalTexture(String texturePath) {
		InputStream in = bindExternalResource("/assets/opencraft/textures" + texturePath);
		return Texture.read(in);
	}

	public static Texture bindInternalTexture(String texturePath) {
		InputStream in = bindInternalResource(texturePath);
		return Texture.read(in);
	}

	public Texture bindTexture(String texturePath) {
		InputStream in = bindOrDefault("/assets/opencraft/textures" + texturePath);
		return Texture.read(in);
	}

	public Image getButton(int button_id) {
		int y = switch (button_id) {
			case BUTTON -> 20;
			case BUTTON_HIGHLIGHTED -> 40;

			default -> 0;
		};
		return tilesheet.getSubimage(0, y, 200, 20);
	}

	public Image getArrow(int arrow) {
		int x = switch (arrow) {
			case 0 -> 200;
			case 1 -> 215;
			case 2 -> 229;
			case 3 -> 244;

			default -> 0;
		};

		return tilesheet.getSubimage(x, 20, 14, 22);

	}

	public Image getLogo() {
		return tilesheet.getSubimage(0, 61, 271, 44);
	}

	public Image getBackground() {
		return tilesheet.getSubimage(242, 0, 16, 16);
	}

	public static BufferedImage missignoImage() {
		BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

		img.setRGB(0, 0, Color.MAGENTA.getRGB());
		img.setRGB(0, 1, Color.BLACK.getRGB());
		img.setRGB(1, 0, Color.BLACK.getRGB());
		img.setRGB(1, 1, Color.MAGENTA.getRGB());

		return img;
	}
}
