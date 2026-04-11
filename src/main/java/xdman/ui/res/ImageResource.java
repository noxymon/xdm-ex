package xdman.ui.res;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageResource {
//	private final static String ICON_FOLDER = "icons";
//
//	static Map<String, ImageIcon> iconMap = new HashMap<String, ImageIcon>();
//
//	public static ImageIcon get(String id) {
//		return get(id, true);
//	}
//
//	public static ImageIcon get(String id, boolean cacheResult) {
//		ImageIcon icon = iconMap.get(id);
//		if (icon == null) {
//			icon = getIcon(id);
//			if (icon != null && cacheResult) {
//				iconMap.put(id, icon);
//			}
//		}
//		return icon;
//	}

	public static Image getImage(String name) {
		try {
			URL url=ImageResource.class.getResource("/icons/xxhdpi/" + name);
			System.out.println("Loading image from url: "+url);
			return ImageIO.read(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Icon getIcon(String name, int width, int height) {
		// Try SVG first
		String svgName = name.replace(".png", ".svg");
		Icon svgIcon = getSVGIcon(svgName, width, height);
		if (svgIcon != null) {
			return svgIcon;
		}

		try {
			URL resource = ImageResource.class.getResource("/icons/xxhdpi/" + name);
			if (resource == null)
				return new ImageIcon();

			BufferedImage image = ImageIO.read(resource);
			BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2 = scaledImage.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2.drawImage(image, 0, 0, width, height, null);
			g2.dispose();
			image.flush();

			// Auto-invert if light mode is active (since source icons are white/light)
			if (!com.formdev.flatlaf.FlatLaf.isLafDark()) {
				for (int y = 0; y < scaledImage.getHeight(); y++) {
					for (int x = 0; x < scaledImage.getWidth(); x++) {
						int rgba = scaledImage.getRGB(x, y);
						int a = (rgba >> 24) & 0xFF;
						if (a > 0) {
							int r = 255 - ((rgba >> 16) & 0xFF);
							int g = 255 - ((rgba >> 8) & 0xFF);
							int b = 255 - (rgba & 0xFF);
							scaledImage.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
						}
					}
				}
			}

			return new ImageIcon(scaledImage);
		} catch (Exception e) {
			return new ImageIcon();
		}
	}

	public static Icon getSVGIcon(String name, int width, int height) {
		try {
			// Expected path: /icons/svg/name.svg
			String path = "icons/svg/" + name;
			URL url = ImageResource.class.getResource("/" + path);
			if (url != null) {
				return new FlatSVGIcon(path, width, height);
			}
		} catch (Exception e) {
			// SVG not found or error loading
		}
		return null;
	}

//	private static ImageIcon getIcon(String name) {
//		try {
//
//			java.net.URL urlHdpi = ImageResource.class.getResource("/" + ICON_FOLDER + "/hdpi/" + name);
//			java.net.URL urlXhdpi = ImageResource.class.getResource("/" + ICON_FOLDER + "/xhdpi/" + name);
//			java.net.URL urlXxhdpi = ImageResource.class.getResource("/" + ICON_FOLDER + "/xxhdpi/" + name);
//
//			BaseMultiResolutionImage img = new BaseMultiResolutionImage(0, ImageIO.read(urlHdpi),
//					ImageIO.read(urlXhdpi), ImageIO.read(urlXxhdpi));
//			return new ImageIcon(img);
//		} catch (Exception e) {
//			return new ImageIcon();
//		}
//
////		int screenType = XDMUtils.detectScreenType();
////		String folder = "hdpi";
////		if (screenType == XDMConstants.XHDPI) {
////			folder = "xxhdpi";
////		} else if (screenType == XDMConstants.HDPI) {
////			folder = "xhdpi";
////		} else {
////			folder = "hdpi";
////		}
////		System.out.println("icon type:"+folder);
////		try {
////			java.net.URL url = ImageResource.class.getResource("/" + ICON_FOLDER + "/" + folder + "/" + name);
////			if (url == null)
////				throw new Exception();
////			return new ImageIcon(url);
////		} catch (Exception e) {
////			return new ImageIcon(ICON_FOLDER + "/" + folder + "/" + name);
////		}
//	}
}