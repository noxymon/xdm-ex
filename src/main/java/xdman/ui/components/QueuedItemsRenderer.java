package xdman.ui.components;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import xdman.DownloadEntry;
import xdman.XDMApp;
import xdman.ui.res.ColorResource;
import xdman.ui.res.FontResource;
import xdman.util.FormatUtilities;
import static xdman.util.XDMUtils.getScaledInt;
public class QueuedItemsRenderer extends JLabel implements ListCellRenderer<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1484239790859356321L;

	public QueuedItemsRenderer() {
		setForeground(ColorResource.getDeepFontColor());
		setFont(FontResource.getNormalFont());
		setOpaque(true);
		setPreferredSize(new Dimension(getScaledInt(100), getScaledInt(30)));
		setBorder(new EmptyBorder(0, getScaledInt(5), 0, 0));
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
			boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(ColorResource.getSelectionColor());
			setForeground(ColorResource.getSelectionForeground());
		} else {
			setBackground(ColorResource.getTableBackground());
			setForeground(ColorResource.getDeepFontColor());
		}
		DownloadEntry ent = XDMApp.getInstance().getEntry(value);
		String str = "";
		if (ent != null) {
			str += ent.getFile();
			if (ent.getSize() > 0) {
				str += " [ " + FormatUtilities.formatSize(ent.getSize()) + " ]";
			}
		}
		setText(str);
		return this;
	}

}
