package xdman.ui.components;

import javax.swing.*;
import java.awt.*;

public class BarPanel extends JPanel {
	private static final long serialVersionUID = -5396480713429517585L;

	public BarPanel() {
		super();
		// Instead of a hardcoded image, we'll use a theme-aware background.
		// FlatLaf automatically handles background colors based on the theme.
		updateThemeColors();
	}

	private void updateThemeColors() {
		// Using a slightly different color to maintain the 'bar' aesthetic if needed,
		// but defaults are usually best for contrast.
		setBackground(UIManager.getColor("Separator.background"));
		if (getBackground() == null) {
			setBackground(UIManager.getColor("Panel.background"));
		}
	}

	@Override
	public void updateUI() {
		super.updateUI();
		updateThemeColors();
	}

	@Override
	protected void paintComponent(Graphics g) {
		// For modernization, we'll use a subtle gradient or solid color based on theme.
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		try {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// We can add a subtle bottom border for better separation
			g2.setColor(UIManager.getColor("Component.borderColor"));
			g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
		} finally {
			g2.dispose();
		}
	}
}
