package xdman.ui.components;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.JButton;
import com.formdev.flatlaf.FlatClientProperties;

public class CustomButton extends JButton {
	private static final long serialVersionUID = 6378409011977437191L;

	public CustomButton() {
		super();
		init();
	}

	public CustomButton(Icon icon) {
		super(icon);
		init();
	}

	public CustomButton(String text) {
		super(text);
		init();
	}

	private void init() {
		// Use FlatLaf default styling for Kinetic Flux theme
		setBorderPainted(false);
		setFocusPainted(false);
		setContentAreaFilled(true);
		
		// Set default arc for rounded corners
		putClientProperty(FlatClientProperties.STYLE, "arc: 8;");
	}

	public void setArc(int arc) {
		putClientProperty(FlatClientProperties.STYLE, "arc: " + arc + ";");
	}

	public void setRolloverBackground(Color rolloverBackground) {
		putClientProperty(FlatClientProperties.STYLE, 
			(String)getClientProperty(FlatClientProperties.STYLE) + "; hoverBackground: #" + Integer.toHexString(rolloverBackground.getRGB()).substring(2));
	}

    // Keep setter-like methods for compatibility, but map them to FlatLaf style where possible
	public void setPressedBackground(Color pressedBackground) {
		// Handle via style if necessary, but FlatLaf's default is usually better
	}
}
