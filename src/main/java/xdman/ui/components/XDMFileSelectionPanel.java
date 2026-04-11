package xdman.ui.components;


import java.awt.BorderLayout;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import xdman.Config;
import xdman.ui.res.ImageResource;
import xdman.util.Logger;
import xdman.util.StringUtils;

import javax.swing.UIManager;

public class XDMFileSelectionPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 2333430406492555559L;
	private JTextField txtFile;
	private JButton btnBrowse;
	private JButton btnDropdown;
	private JPopupMenu pop;

	public XDMFileSelectionPanel() {
		super(new BorderLayout());
		initUI();
	}

	String folder;

	public String getFileName() {
		return txtFile.getText();
	}

	public void setFileName(String file) {
		txtFile.setText(file);
		txtFile.setCaretPosition(0);
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String f) {
		this.folder = f;
	}

	private void initUI() {
		txtFile = new JTextField();
		txtFile.setBorder(null);
		txtFile.setCaretColor(UIManager.getColor("ProgressBar.foreground"));
		add(txtFile, BorderLayout.CENTER);


		btnBrowse = new CustomButton();
		btnBrowse.setIcon(ImageResource.getIcon("folder.png", 16, 16));
		btnBrowse.setMargin(new Insets(2, 2, 2, 2));
		btnBrowse.setBorderPainted(false);
		btnBrowse.setFocusPainted(false);
		btnBrowse.addActionListener(this);

		btnDropdown = new CustomButton();
		btnDropdown.setIcon(ImageResource.getIcon("down.png", 12, 12));
		btnDropdown.setMargin(new Insets(2, 2, 2, 2));
		btnDropdown.setBorderPainted(false);
		btnDropdown.setFocusPainted(false);
		btnDropdown.addActionListener(this);

		JPanel buttonPanel = new JPanel(new java.awt.GridLayout(1, 2));
		buttonPanel.add(btnBrowse);
		buttonPanel.add(btnDropdown);

		add(buttonPanel, BorderLayout.EAST);
		pop = new JPopupMenu();
		if (!StringUtils.isNullOrEmptyOrBlank(Config.getInstance().getLastFolder())) {
			pop.add(createMenuItem(Config.getInstance().getLastFolder()));
		}
		pop.add(createMenuItem(Config.getInstance().getDownloadFolder()));
		if (!Config.getInstance().isForceSingleFolder()) {
			pop.add(createMenuItem(Config.getInstance().getCategoryDocuments()));
			pop.add(createMenuItem(Config.getInstance().getCategoryMusic()));
			pop.add(createMenuItem(Config.getInstance().getCategoryPrograms()));
			pop.add(createMenuItem(Config.getInstance().getCategoryCompressed()));
			pop.add(createMenuItem(Config.getInstance().getCategoryVideos()));
		}
		pop.setInvoker(btnDropdown);
	}

	private JMenuItem createMenuItem(String folder) {
		JMenuItem item = new JMenuItem(folder);
		item.addActionListener(this);
		return item;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem) {
			this.folder = ((JMenuItem) e.getSource()).getText();
			Logger.log("Selected folder: " + this.folder);
		}
		if (e.getSource() == btnBrowse) {
			choseFolder();
		}
		if (e.getSource() == btnDropdown) {
			pop.show(txtFile, 0, txtFile.getHeight());
		}
	}

	private void choseFolder() {
		String dir = folder;
		if (StringUtils.isNullOrEmptyOrBlank(dir)) {
			dir = Config.getInstance().getLastFolder();
			if (StringUtils.isNullOrEmptyOrBlank(dir)) {
				dir = Config.getInstance().getDownloadFolder();
			}
		}
		JFileChooser jfc = XDMFileChooser.getFileChooser(JFileChooser.DIRECTORIES_ONLY, new File(dir));
		if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			folder = jfc.getSelectedFile().getAbsolutePath();
			Config.getInstance().setLastFolder(folder);
		}
	}

	public void setFocus() {
		txtFile.requestFocus();
	}
}
