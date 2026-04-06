package xdman.ui.components;

import static xdman.util.XDMUtils.getScaledInt;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import xdman.Config;
import xdman.DownloadQueue;
import xdman.XDMApp;
import xdman.downloaders.metadata.HttpMetadata;
import xdman.ui.res.ColorResource;
import xdman.ui.res.FontResource;
import xdman.ui.res.ImageResource;
import xdman.ui.res.StringResource;
import xdman.util.Logger;
import xdman.util.StringUtils;
import xdman.util.XDMUtils;

public class NewDownloadWindow extends JDialog implements ActionListener, DocumentListener {

	private static final long serialVersionUID = 416356191545932172L;
	private JTextField txtURL;
	private XDMFileSelectionPanel filePane;
	private JPopupMenu pop;
	private CustomButton btnMore, btnDN, btnCN;
	private HttpMetadata metadata;
	// private String folder;
	private String queueId;

	public NewDownloadWindow(HttpMetadata metadata, String fileName, String folderPath) {
		initUI();
		// this.folder = Config.getInstance().getDownloadFolder();
		this.metadata = metadata;
		if (this.metadata == null) {
			this.metadata = new HttpMetadata();
		}
		if (this.metadata.getUrl() != null) {
			txtURL.setText(this.metadata.getUrl());
			txtURL.setCaretPosition(0);
		} else {
			String clip = XDMUtils.getClipBoardText();
			if (clip != null && clip.trim().length() > 0) {
				if (XDMUtils.validateURL(clip)) {
					txtURL.setText(clip);
					txtURL.setCaretPosition(0);
				}
			}
		}
		if (!StringUtils.isNullOrEmptyOrBlank(fileName)) {
			filePane.setFileName(fileName);
		}
		if (!StringUtils.isNullOrEmptyOrBlank(folderPath)) {
			filePane.setFolder(folderPath);
		}

		getRootPane().setDefaultButton(btnDN);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				txtURL.requestFocus();
			}
		});

		queueId = "";
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JComponent) {
			String name = ((JComponent) e.getSource()).getName();
			if (name.startsWith("QUEUE")) {
				String[] arr = name.split(":");
				if (arr.length < 2) {
					queueId = "";
				} else {
					queueId = arr[1].trim();
				}
				createDownload(false);
			} else if (name.equals("CLOSE")) {
				dispose();
			} else if (name.equals("DOWNLOAD_NOW")) {
				queueId = "";
				createDownload(true);
			} else if (name.equals("BTN_MORE")) {
				if (pop == null) {
					createPopup();
				}
				pop.show(btnMore, 0, btnMore.getHeight());
			}
			//
			// else if (name.equals("BROWSE_FOLDER")) {
			// choseFolder();
			// }
			//
			else if (name.equals("IGNORE_URL")) {
				String urlStr = txtURL.getText();
				if (urlStr.length() < 1) {
					return;
				}

				if (XDMUtils.validateURL(urlStr)) {
					try {
						URL url = new URI(urlStr).toURL();
						String host = url.getHost().trim();
						if (StringUtils.isNullOrEmptyOrBlank(host)) {
							return;
						}
						Config.getInstance().addBlockedHosts(host);
						Config.getInstance().save();
						dispose();
					} catch (Exception e2) {
						Logger.log(e2);
					}
				}
			}

		}
	}

	private void createDownload(boolean now) {
		String urlStr = txtURL.getText();
		if (urlStr.length() < 1) {
			JOptionPane.showMessageDialog(this, StringResource.get("MSG_NO_URL"));
			return;
		}
		if (!XDMUtils.validateURL(urlStr)) {
			urlStr = "http://" + urlStr;
			if (!XDMUtils.validateURL(urlStr)) {
				JOptionPane.showMessageDialog(this, StringResource.get("MSG_INVALID_URL"));
				return;
			} else {
				txtURL.setText(urlStr);
			}
		}
		if (!urlStr.equals(metadata.getUrl())) {
			metadata.setUrl(urlStr);
		}
		dispose();
		Logger.log("file: " + filePane.getFileName());
		if (filePane.getFileName().length() < 1) {
			JOptionPane.showMessageDialog(this, StringResource.get("MSG_NO_FILE"));
			return;
		}

		String file = XDMUtils.createSafeFileName(filePane.getFileName());

		XDMApp.getInstance().createDownload(file, filePane.getFolder(), metadata, now, queueId, 0, 0);
	}

	// private void choseFolder() {
	// String dir = folder;
	// if (StringUtils.isNullOrEmptyOrBlank(dir)) {
	// dir = Config.getInstance().getLastFolder();
	// if (StringUtils.isNullOrEmptyOrBlank(dir)) {
	// dir = Config.getInstance().getDownloadFolder();
	// }
	// }
	// JFileChooser jfc =
	// XDMFileChooser.getFileChooser(JFileChooser.DIRECTORIES_ONLY, new File(dir));
	// if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	// folder = jfc.getSelectedFile().getAbsolutePath();
	// Config.getInstance().setLastFolder(folder);
	// // Config.getInstance().setDownloadFolder(folder);
	// }
	// }

	@Override
	public void changedUpdate(DocumentEvent e) {
		update(e);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		update(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		update(e);
	}

	void update(DocumentEvent e) {
		try {
			Document doc = e.getDocument();
			int len = doc.getLength();
			String text = doc.getText(0, len);
			filePane.setFileName(XDMUtils.getFileName(text));
		} catch (Exception err) {
			Logger.log(err);
		}
	}

	private void initUI() {
		setUndecorated(true);

		setIconImage(ImageResource.getImage("icon.png"));
		setSize(getScaledInt(400), getScaledInt(210));
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		getContentPane().setLayout(new java.awt.BorderLayout());

		JPanel titlePanel = new TitlePanel(null, this);
		titlePanel.setOpaque(false);
		titlePanel.setPreferredSize(new java.awt.Dimension(getScaledInt(400), getScaledInt(50)));
		titlePanel.setLayout(null);

		JButton closeBtn = new CustomButton();
		closeBtn.setBounds(getScaledInt(365), getScaledInt(10), getScaledInt(25), getScaledInt(25));
		closeBtn.setBorderPainted(false);
		closeBtn.setFocusPainted(false);
		closeBtn.setName("CLOSE");
		closeBtn.setIcon(ImageResource.getIcon("title_close.png", 16, 16));
		closeBtn.addActionListener(this);
		titlePanel.add(closeBtn);

		JLabel titleLbl = new JLabel(StringResource.get("ND_TITLE"));
		titleLbl.setFont(FontResource.getBiggerFont());
		titleLbl.setForeground(ColorResource.getDeepFontColor());
		titleLbl.setBounds(getScaledInt(20), getScaledInt(10), getScaledInt(200), getScaledInt(30));
		titlePanel.add(titleLbl);

		add(titlePanel, java.awt.BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new java.awt.GridBagLayout());
		centerPanel.setBackground(ColorResource.getDarkestBgColor());
		centerPanel.setBorder(new EmptyBorder(getScaledInt(10), getScaledInt(20), getScaledInt(10), getScaledInt(20)));
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5);

		JLabel lblURL = new JLabel(StringResource.get("ND_ADDRESS"), JLabel.RIGHT);
		lblURL.setFont(FontResource.getNormalFont());
		lblURL.setForeground(ColorResource.getDeepFontColor());
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		centerPanel.add(lblURL, gbc);

		txtURL = new JTextField();
		txtURL.getDocument().addDocumentListener(this);
		txtURL.setCaretColor(ColorResource.getDeepFontColor());
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		centerPanel.add(txtURL, gbc);

		JLabel lblFile = new JLabel(StringResource.get("ND_FILE"), JLabel.RIGHT);
		lblFile.setFont(FontResource.getNormalFont());
		lblFile.setForeground(ColorResource.getDeepFontColor());
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		centerPanel.add(lblFile, gbc);

		filePane = new XDMFileSelectionPanel();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		centerPanel.add(filePane, gbc);

		add(centerPanel, java.awt.BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new java.awt.GridLayout(1, 3));
		bottomPanel.setBackground(ColorResource.getDarkerBgColor());
		bottomPanel.setPreferredSize(new java.awt.Dimension(getScaledInt(400), getScaledInt(50)));

		btnMore = new CustomButton(StringResource.get("ND_MORE"));
		btnDN = new CustomButton(StringResource.get("ND_DOWNLOAD_NOW"));
		btnCN = new CustomButton(StringResource.get("ND_CANCEL"));

		btnMore.setName("BTN_MORE");
		styleButton(btnMore);
		bottomPanel.add(btnMore);

		btnDN.setName("DOWNLOAD_NOW");
		styleButton(btnDN);
		bottomPanel.add(btnDN);

		btnCN.setName("CLOSE");
		styleButton(btnCN);
		bottomPanel.add(btnCN);

		add(bottomPanel, java.awt.BorderLayout.SOUTH);
	}

	private void createPopup() {
		pop = new JPopupMenu();
		JMenu dl = new JMenu(StringResource.get("ND_DOWNLOAD_LATER"));
		dl.setBorder(new EmptyBorder(getScaledInt(5), getScaledInt(5), getScaledInt(5), getScaledInt(5)));
		dl.addActionListener(this);
		pop.add(dl);

		createQueueItems(dl);

		JMenuItem ig = new JMenuItem(StringResource.get("ND_IGNORE_URL"));
		ig.setName("IGNORE_URL");
		ig.addActionListener(this);
		pop.add(ig);
		pop.setInvoker(btnMore);
	}

	private void styleButton(CustomButton btn) {
		btn.setFont(FontResource.getBigFont());
		btn.setBorderPainted(false);
		btn.setMargin(new Insets(0, 0, 0, 0));
		btn.setFocusPainted(false);
		btn.setBackground(ColorResource.getDarkestBgColor());
		btn.setForeground(ColorResource.getDeepFontColor());
		btn.setPressedBackground(ColorResource.getDarkerBgColor());
		btn.addActionListener(this);
	}

	private void createQueueItems(JMenuItem queueMenuItem) {
		ArrayList<DownloadQueue> queues = XDMApp.getInstance().getQueueList();
		for (int i = 0; i < queues.size(); i++) {
			DownloadQueue q = queues.get(i);
			JMenuItem mItem = new JMenuItem(q.getName().length() < 1 ? "Default queue" : q.getName());
			mItem.setName("QUEUE:" + q.getQueueId());
			mItem.addActionListener(this);
			queueMenuItem.add(mItem);
		}
	}

}
