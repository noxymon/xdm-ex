package xdman.ui.components;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import xdman.ui.res.ColorResource;
import xdman.ui.res.FontResource;
import xdman.ui.res.StringResource;
import xdman.util.FormatUtilities;
import static xdman.util.XDMUtils.getScaledInt;
public class PropertiesPage extends Page {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9192969191740177178L;
	private static PropertiesPage propPage;
	private JTextField txtDefFile, txtDefFolder, txtUrl, lblReferer;
	private JLabel lblSize, lblDate, lblType;
	JTextArea txtCookie;

	public void setDetails(String file, String folder, long size, String url, String referer, String date,
			String cookies, String type) {
		this.txtDefFile.setText(file);
		this.txtDefFolder.setText(folder);
		this.txtUrl.setText(url);
		this.lblSize.setText(FormatUtilities.formatSize(size));
		this.lblDate.setText(date);
		this.txtCookie.setText(cookies);
		this.lblType.setText(type);
		this.lblReferer.setText(referer);
	}

	private PropertiesPage(XDMFrame xframe) {
		super(StringResource.get("TITLE_PROP"), getScaledInt(350), xframe);
		initUI();
	}

	public static PropertiesPage getPage(XDMFrame xframe) {
		if (propPage == null) {
			propPage = new PropertiesPage(xframe);
		}
		return propPage;
	}

	private void initUI() {
		int y = 0;
		int h = 0;
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setOpaque(false);
		y += getScaledInt(10);
		h = getScaledInt(30);
		JLabel lblFileTitle = new JLabel(StringResource.get("ND_FILE"));
		lblFileTitle.setForeground(ColorResource.getDeepFontColor());
		lblFileTitle.setFont(FontResource.getNormalFont());
		lblFileTitle.setBounds(getScaledInt(15), y, getScaledInt(350) - getScaledInt(30), h);
		panel.add(lblFileTitle);
		y += h;
		h = getScaledInt(25);
		txtDefFile = new JTextField();
		txtDefFile.setBounds(getScaledInt(15), y, getScaledInt(350) - getScaledInt(50), h);
		txtDefFile.setBorder(new LineBorder(ColorResource.getBorderColor()));
		txtDefFile.setEditable(false);
		txtDefFile.setForeground(ColorResource.getDeepFontColor());
		txtDefFile.setOpaque(false);
		panel.add(txtDefFile);
		y += h;

		h = getScaledInt(30);
		JLabel lblFolderTitle = new JLabel(StringResource.get("CD_LOC"));
		lblFolderTitle.setForeground(ColorResource.getDeepFontColor());
		lblFolderTitle.setFont(FontResource.getNormalFont());
		lblFolderTitle.setBounds(getScaledInt(15), y, getScaledInt(350) - getScaledInt(50), h);
		panel.add(lblFolderTitle);
		y += h;
		h = getScaledInt(25);
		txtDefFolder = new JTextField();
		txtDefFolder.setBounds(getScaledInt(15), y, getScaledInt(350) - getScaledInt(50), h);
		txtDefFolder.setBorder(new LineBorder(ColorResource.getBorderColor()));
		txtDefFolder.setEditable(false);
		txtDefFolder.setForeground(ColorResource.getDeepFontColor());
		txtDefFolder.setOpaque(false);
		panel.add(txtDefFolder);
		y += h;

		h = getScaledInt(30);
		JLabel lblUrlTitle = new JLabel(StringResource.get("ND_ADDRESS"));
		lblUrlTitle.setForeground(ColorResource.getDeepFontColor());
		lblUrlTitle.setFont(FontResource.getNormalFont());
		lblUrlTitle.setBounds(getScaledInt(15), y, getScaledInt(350) - getScaledInt(50), h);
		panel.add(lblUrlTitle);
		y += h;
		h = getScaledInt(25);
		txtUrl = new JTextField();
		txtUrl.setBounds(getScaledInt(15), y, getScaledInt(350) - getScaledInt(50), h);
		txtUrl.setBorder(new LineBorder(ColorResource.getBorderColor()));
		txtUrl.setEditable(false);
		txtUrl.setForeground(ColorResource.getDeepFontColor());
		txtUrl.setOpaque(false);
		panel.add(txtUrl);
		y += h;

		h = getScaledInt(30);
		JLabel lblSizeLabel = new JLabel(StringResource.get("PROP_SIZE"));
		lblSizeLabel.setForeground(ColorResource.getDeepFontColor());
		lblSizeLabel.setFont(FontResource.getNormalFont());
		lblSizeLabel.setBounds(getScaledInt(15), y, getScaledInt(100), h);
		panel.add(lblSizeLabel);

		lblSize = new JLabel();
		lblSize.setForeground(ColorResource.getDeepFontColor());
		lblSize.setFont(FontResource.getNormalFont());
		lblSize.setBounds(getScaledInt(115), y, getScaledInt(200), h);
		panel.add(lblSize);
		y += h;

		h = getScaledInt(30);
		JLabel lblDateLabel = new JLabel(StringResource.get("PROP_DATE"));
		lblDateLabel.setForeground(ColorResource.getDeepFontColor());
		lblDateLabel.setFont(FontResource.getNormalFont());
		lblDateLabel.setBounds(getScaledInt(15), y, getScaledInt(100), h);
		panel.add(lblDateLabel);

		lblDate = new JLabel();
		lblDate.setForeground(ColorResource.getDeepFontColor());
		lblDate.setFont(FontResource.getNormalFont());
		lblDate.setBounds(getScaledInt(115), y, getScaledInt(200), h);
		panel.add(lblDate);
		y += h;

		h = getScaledInt(30);
		JLabel lblTypeLabel = new JLabel(StringResource.get("PROP_TYPE"));
		lblTypeLabel.setForeground(ColorResource.getDeepFontColor());
		lblTypeLabel.setFont(FontResource.getNormalFont());
		lblTypeLabel.setBounds(getScaledInt(15), y, getScaledInt(100), h);
		panel.add(lblTypeLabel);

		lblType = new JLabel();
		lblType.setForeground(ColorResource.getDeepFontColor());
		lblType.setFont(FontResource.getNormalFont());
		lblType.setBounds(getScaledInt(115), y, getScaledInt(200), h);
		panel.add(lblType);
		y += h;

		h = getScaledInt(30);
		JLabel lblRefererLabel = new JLabel(StringResource.get("PROP_REFERER"));
		lblRefererLabel.setForeground(ColorResource.getDeepFontColor());
		lblRefererLabel.setFont(FontResource.getNormalFont());
		lblRefererLabel.setBounds(getScaledInt(15), y, getScaledInt(100), h);
		panel.add(lblRefererLabel);

		lblReferer = new JTextField();
		lblReferer.setForeground(ColorResource.getDeepFontColor());
		lblReferer.setFont(FontResource.getNormalFont());
		lblReferer.setEditable(false);
		lblReferer.setBorder(null);
		lblReferer.setOpaque(false);
		lblReferer.setBounds(getScaledInt(115), y, getScaledInt(200), h);
		panel.add(lblReferer);
		y += h;
		h = getScaledInt(30);
		JLabel lblCookieTitle = new JLabel(StringResource.get("PROP_COOKIE"));
		lblCookieTitle.setForeground(ColorResource.getDeepFontColor());
		lblCookieTitle.setFont(FontResource.getNormalFont());
		lblCookieTitle.setBounds(getScaledInt(15), y, getScaledInt(350) - getScaledInt(30), h);
		panel.add(lblCookieTitle);
		y += h;
		y += getScaledInt(10);
		h = getScaledInt(120);

		txtCookie = new JTextArea();
		txtCookie.setBounds(getScaledInt(15), y, getScaledInt(350) - getScaledInt(50), h);
		txtCookie.setBorder(new LineBorder(ColorResource.getBorderColor()));
		txtCookie.setEditable(false);
		txtCookie.setForeground(ColorResource.getDeepFontColor());
		txtCookie.setOpaque(false);
		panel.add(txtCookie);
		y += h;
		y += getScaledInt(50);

		panel.setPreferredSize(new Dimension(getScaledInt(350), y));
		panel.setBounds(getScaledInt(0), 0, getScaledInt(350), y);

		jsp.setViewportView(panel);
	}
}
