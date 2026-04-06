package xdman.ui.components;

import static xdman.util.XDMUtils.getScaledInt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.formdev.flatlaf.FlatClientProperties;

import xdman.downloaders.metadata.HttpMetadata;
import xdman.ui.res.ColorResource;
import xdman.ui.res.FontResource;
import xdman.ui.res.ImageResource;
import xdman.ui.res.StringResource;
import xdman.util.StringUtils;
import xdman.util.XDMUtils;

public class ClipboardPasteWnd extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextArea txtLinks;
	private JTextField txtSeparator;

	public ClipboardPasteWnd() {
		initUI();
		String text = XDMUtils.getClipBoardText();
		if (!StringUtils.isNullOrEmptyOrBlank(text)) {
			txtLinks.setText(text);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			String name = ((JButton) e.getSource()).getName();
			if ("CLOSE".equals(name) || "CANCEL".equals(name)) {
				dispose();
			} else if ("PARSE".equals(name)) {
				parseAndOpenBatchWnd();
			}
		}
	}

	private void parseAndOpenBatchWnd() {
		String text = txtLinks.getText();
		if (StringUtils.isNullOrEmptyOrBlank(text)) {
			dispose();
			return;
		}

		String sep = txtSeparator.getText();

		if (StringUtils.isNullOrEmptyOrBlank(sep)) {
			// FALLBACK: original extraction behavior (like BatchDownloadWnd.getUrls())
			List<String> urls = new ArrayList<>();
			String[] arr = text.split("\n");
			for (String url : arr) {
				url = url.trim();
				try {
					new URI(url).toURL();
					urls.add(url);
				} catch (Exception e) {
					// skip invalid
				}
			}
			
			if (urls.size() > 0) {
				List<HttpMetadata> mdList = XDMUtils.toMetadata(urls);
				new BatchDownloadWnd(mdList).setVisible(true);
			} else {
				MessageBox.show(null, StringResource.get("MENU_BATCH_DOWNLOAD"),
						StringResource.get("LBL_BATCH_EMPTY_CLIPBOARD"), MessageBox.OK_OPTION, MessageBox.OK);
			}
		} else {
			// CUSTOM BEHAVIOR: parse pairs by separator
			List<String> urls = new ArrayList<>();
			List<String> fileNames = new ArrayList<>();

			String[] arr = text.split("\n");
			for (String line : arr) {
				line = line.trim();
				if (line.isEmpty())
					continue;

				String urlPart = line;
				String filePart = null;

				if (line.contains(sep)) {
					int idx = line.indexOf(sep);
					urlPart = line.substring(0, idx).trim();
					filePart = line.substring(idx + sep.length()).trim();
				}

				try {
					new URI(urlPart).toURL();
					urls.add(urlPart);
					fileNames.add(filePart);
				} catch (Exception e) {
					// skip invalid
				}
			}

			if (urls.size() > 0) {
				List<HttpMetadata> mdList = XDMUtils.toMetadata(urls);
				new BatchDownloadWnd(mdList, fileNames).setVisible(true);
			} else {
				MessageBox.show(null, StringResource.get("MENU_BATCH_DOWNLOAD"),
						StringResource.get("LBL_BATCH_EMPTY_CLIPBOARD"), MessageBox.OK_OPTION, MessageBox.OK);
			}
		}
		
		dispose();
	}

	private void initUI() {
		setUndecorated(true);
		setTitle(StringResource.get("MENU_BATCH_DOWNLOAD"));
		setIconImage(ImageResource.getImage("icon.png"));
		setSize(getScaledInt(500), getScaledInt(420));
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		getContentPane().setBackground(ColorResource.getDarkestBgColor());

		JPanel titlePanel = new TitlePanel(null, this);
		titlePanel.setOpaque(false);
		titlePanel.setBounds(0, 0, getWidth(), getScaledInt(50));

		JButton closeBtn = new CustomButton();
		closeBtn.setBounds(getWidth() - getScaledInt(35), getScaledInt(5), getScaledInt(30), getScaledInt(30));
		closeBtn.setBackground(ColorResource.getDarkestBgColor());
		closeBtn.setBorderPainted(false);
		closeBtn.setFocusPainted(false);
		closeBtn.setName("CLOSE");
		closeBtn.setIcon(ImageResource.getIcon("title_close.png", 20, 20));
		closeBtn.addActionListener(this);
		titlePanel.add(closeBtn);

		JLabel titleLbl = new JLabel(StringResource.get("MENU_BATCH_DOWNLOAD"));
		titleLbl.setFont(FontResource.getBiggerFont());
		titleLbl.setForeground(ColorResource.getSelectionColor());
		titleLbl.setBounds(getScaledInt(25), getScaledInt(15), getScaledInt(300), getScaledInt(30));
		titlePanel.add(titleLbl);

		JLabel lineLbl = new JLabel();
		lineLbl.setBackground(ColorResource.getBorderColor());
		lineLbl.setBounds(0, getScaledInt(55), getWidth(), 1);
		lineLbl.setOpaque(true);
		add(lineLbl);

		add(titlePanel);

		int y = getScaledInt(65);

		JLabel lblInstr = new JLabel("Paste URLs below. Provide a custom separator to split Save Name.");
		lblInstr.setFont(FontResource.getNormalFont());
		lblInstr.setForeground(ColorResource.getDeepFontColor());
		lblInstr.setBounds(getScaledInt(15), y, getScaledInt(470), getScaledInt(20));
		add(lblInstr);
		y += getScaledInt(25);

		txtLinks = new JTextArea();
		txtLinks.setBackground(ColorResource.getDarkBgColor());
		txtLinks.setForeground(ColorResource.getDeepFontColor());
		txtLinks.setCaretColor(ColorResource.getDeepFontColor());
		txtLinks.setFont(FontResource.getNormalFont());

		JScrollPane jsp = new JScrollPane(txtLinks);
		jsp.setBounds(getScaledInt(15), y, getWidth() - getScaledInt(30), getScaledInt(230));
		jsp.setBorder(new LineBorder(ColorResource.getBorderColor(), 1));
		add(jsp);
		y += getScaledInt(245);

		JLabel lblSep = new JLabel("Separator:");
		lblSep.setFont(FontResource.getNormalFont());
		lblSep.setForeground(ColorResource.getDeepFontColor());
		lblSep.setBounds(getScaledInt(15), y, getScaledInt(70), getScaledInt(25));
		add(lblSep);

		txtSeparator = new JTextField();
		txtSeparator.setBackground(ColorResource.getDarkBgColor());
		txtSeparator.setForeground(ColorResource.getDeepFontColor());
		txtSeparator.setCaretColor(ColorResource.getDeepFontColor());
		txtSeparator.setBorder(new LineBorder(ColorResource.getBorderColor(), 1));
		txtSeparator.setBounds(getScaledInt(90), y, getScaledInt(100), getScaledInt(25));
		txtSeparator.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "e.g. | or >");
		add(txtSeparator);

		y += getScaledInt(45);

		JLabel lineLbl2 = new JLabel();
		lineLbl2.setBackground(ColorResource.getDarkBgColor());
		lineLbl2.setBounds(0, y, getWidth(), 1);
		lineLbl2.setOpaque(true);
		add(lineLbl2);

		y += getScaledInt(10);

		int h = getScaledInt(30);
		JButton btnDwn = createButton("MB_OK");
		btnDwn.setBounds(getWidth() - getScaledInt(15) - getScaledInt(100), y, getScaledInt(100), h);
		btnDwn.setName("PARSE");
		add(btnDwn);

		JButton btnCn = createButton("ND_CANCEL");
		btnCn.setBounds(getWidth() - getScaledInt(15) - getScaledInt(100) - getScaledInt(110), y, getScaledInt(100), h);
		btnCn.setName("CANCEL");
		add(btnCn);
	}

	private JButton createButton(String name) {
		JButton btn = new CustomButton(StringResource.get(name));
		btn.addActionListener(this);
		return btn;
	}

}
