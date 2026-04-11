package xdman.ui.res;

import xdman.util.Logger;
import xdman.util.XDMUtils;

import java.awt.*;

public class FontResource {
	static {
		Logger.log("Loading fonts");
	}

	private static final String MODERN_FONT = "Inter";
	private static final String FALLBACK_FONT = "Segoe UI Variable Text";

	private static Font createFont(int style, int size) {
		int scaledSize = XDMUtils.getScaledInt(size);
		Font font = new Font(MODERN_FONT, style, scaledSize);
		if (font.getFamily().equalsIgnoreCase("Dialog") || font.getFamily().equalsIgnoreCase("SansSerif")) {
			font = new Font(FALLBACK_FONT, style, scaledSize);
		}
		// Add CJK fallback if the font cannot display CJK characters
		if (font.canDisplayUpTo("\u4e2d\u6587") >= 0) {
			int os = XDMUtils.detectOS();
			String[] cjkFonts;
			if (os == XDMUtils.WINDOWS) {
				cjkFonts = new String[]{"Microsoft YaHei", "SimHei"};
			} else if (os == XDMUtils.LINUX) {
				cjkFonts = new String[]{"Noto Sans CJK SC", "WenQuanYi Micro Hei"};
			} else {
				cjkFonts = new String[]{};
			}
			Font cjkFont = null;
			for (String fontName : cjkFonts) {
				Font candidate = new Font(fontName, style, scaledSize);
				if (!candidate.getFamily().equalsIgnoreCase("Dialog")
						&& candidate.canDisplayUpTo("\u4e2d\u6587") < 0) {
					cjkFont = candidate;
					break;
				}
			}
			if (cjkFont == null) {
				// Use system composite font as final fallback
				cjkFont = new Font(Font.SANS_SERIF, style, scaledSize);
			}
			font = cjkFont;
		}
		return font;
	}

	public static Font getNormalFont() {
		if (plainFont == null) {
			plainFont = createFont(Font.PLAIN, 12);
		}
		return plainFont;
	}

	public static Font getBoldFont() {
		if (boldFont == null) {
			boldFont = createFont(Font.BOLD, 12);
		}
		return boldFont;
	}

	public static Font getBigFont() {
		if (plainFontBig == null) {
			plainFontBig = createFont(Font.PLAIN, 14);
		}
		return plainFontBig;
	}

	public static Font getBigBoldFont() {
		if (boldFont2 == null) {
			boldFont2 = createFont(Font.BOLD, 14);
		}
		return boldFont2;
	}

	public static Font getItemFont() {
		if (itemFont == null) {
			itemFont = createFont(Font.PLAIN, 16);
		}
		return itemFont;
	}

	public static Font getBiggerFont() {
		if (plainFontBig1 == null) {
			plainFontBig1 = createFont(Font.PLAIN, 18);
		}
		return plainFontBig1;
	}

	public static Font getBiggestFont() {
		if (plainFontBig2 == null) {
			plainFontBig2 = createFont(Font.PLAIN, 24);
		}
		return plainFontBig2;
	}

	private static Font plainFont;
	private static Font boldFont;
	private static Font boldFont2;

	private static Font plainFontBig;
	private static Font plainFontBig1;
	private static Font plainFontBig2;
	private static Font itemFont;
}