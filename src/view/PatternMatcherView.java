package view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialLiteTheme;

/**
 * View Component for Byte Pattern Matcher.
 * 
 * @author Dipesh B.C.
 * @version 1.0
 *
 */
public class PatternMatcherView extends JFrame {

	// ... Components
	private JPanel westPanel;
	private JPanel westNorthPanel;
	private JPanel westSouthPanel;
	private JPanel centerPanel;

	private Icon fileIcon;
	private Icon dirIcon;
	private Icon aboutIcon;
	private Icon exitIcon;
	private Icon patternIcon;
	private Icon searchIcon;

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem menuItemLoadFromFile;
	private JMenuItem menuItemLoadFromDir;
	private JMenuItem menuItemAbout;
	private JMenuItem menuItemExit;

	private JLabel loadDataFromFileLabel;
	private JLabel loadDataFromDirLabel;
	private JLabel loadPatternLabel;

	private JTextArea searchResults;

	private JScrollPane scrollPane;

	private JButton loadDataFromFileBtn;
	private JButton loadDataFromDirBtn;
	private JButton searchPatternBtn;
	private JButton loadPatternBtn;

	// ... Constructor
	public PatternMatcherView() {

		// ... set look and feel
		try {
			JDialog.setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel(new MaterialLookAndFeel(new MaterialLiteTheme()));
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// ... Initialize components

		// ... JPanel
		westPanel = new JPanel();
		westNorthPanel = new JPanel();
		westSouthPanel = new JPanel();
		centerPanel = new JPanel();

		// ... ImageIcon
		fileIcon = new ImageIcon(".//icons//file.png");
		dirIcon = new ImageIcon(".//icons//folder.png");
		aboutIcon = new ImageIcon(".//icons//about.png");
		exitIcon = new ImageIcon(".//icons//close.png");
		patternIcon = new ImageIcon(".//icons//pattern.png");
		searchIcon = new ImageIcon(".//icons//search.png");

		// ... JMenuItem
		menuItemLoadFromFile = new JMenuItem("Load from file");
		menuItemLoadFromDir = new JMenuItem("Load from dir");
		menuItemAbout = new JMenuItem("About");
		menuItemExit = new JMenuItem("Exit");

		menuItemLoadFromFile.setIcon(fileIcon);
		menuItemLoadFromDir.setIcon(dirIcon);
		menuItemAbout.setIcon(aboutIcon);
		menuItemExit.setIcon(exitIcon);

		// ... JMenu
		fileMenu = new JMenu("File");
		fileMenu.add(menuItemLoadFromFile);
		fileMenu.add(menuItemLoadFromDir);
		fileMenu.add(menuItemAbout);
		fileMenu.add(menuItemExit);

		// ... JMenuBar
		menuBar = new JMenuBar();
		menuBar.add(fileMenu);

		// ... JLabel
		loadDataFromFileLabel = new JLabel("Select a file...");
		loadDataFromDirLabel = new JLabel("Select a dir...");
		loadPatternLabel = new JLabel("Select a pattern...");

		// ... JTextArea
		searchResults = new JTextArea(10, 10);

		// ... JScrollPane
		scrollPane = new JScrollPane(searchResults);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// ... JButton
		loadDataFromFileBtn = new JButton(fileIcon);
		loadDataFromDirBtn = new JButton(dirIcon);
		loadPatternBtn = new JButton(patternIcon);
		searchPatternBtn = new JButton(searchIcon);

		loadDataFromFileBtn.setFocusPainted(false);
		loadDataFromDirBtn.setFocusPainted(false);
		loadPatternBtn.setFocusPainted(false);
		searchPatternBtn.setFocusPainted(false);

		// ... Layout the components
		westPanel.setLayout(new BorderLayout());

		westNorthPanel.add(loadDataFromFileLabel);
		westNorthPanel.add(loadDataFromFileBtn);
		westNorthPanel.add(loadDataFromDirLabel);
		westNorthPanel.add(loadDataFromDirBtn);

		westSouthPanel.add(loadPatternLabel);
		westSouthPanel.add(loadPatternBtn);
		westSouthPanel.add(searchPatternBtn);

		centerPanel.add(scrollPane);

		this.setJMenuBar(menuBar);

		westPanel.add(BorderLayout.NORTH, westNorthPanel);
		westPanel.add(BorderLayout.SOUTH, westSouthPanel);

		this.add(BorderLayout.WEST, westPanel);
		this.add(BorderLayout.CENTER, scrollPane);

		// ... finalize layout
		this.setSize(700, 300);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// ... methods for registering controller's listener
	public void addMenuItemLoadFromFileListener(ActionListener menuItemLoadFromFileListener) {
		menuItemLoadFromFile.addActionListener(menuItemLoadFromFileListener);
	}

	public void addMenuItemLoadFromDirListener(ActionListener menuItemLoadFromDirListener) {
		menuItemLoadFromDir.addActionListener(menuItemLoadFromDirListener);
	}

	public void addMenuItemAboutListener(ActionListener menuItemAboutListener) {
		menuItemAbout.addActionListener(menuItemAboutListener);
	}

	public void addMenuItemExitListener(ActionListener menuItemExitListener) {
		menuItemExit.addActionListener(menuItemExitListener);
	}

	public void addLoadDataFromFileBtnListener(ActionListener loadDataFromFileBtnListener) {
		loadDataFromFileBtn.addActionListener(loadDataFromFileBtnListener);
	}

	public void addLoadDataFromDirBtnListener(ActionListener loadDataFromDirBtnListener) {
		loadDataFromDirBtn.addActionListener(loadDataFromDirBtnListener);
	}

	public void addLoadPatternBtnListener(ActionListener loadPatternBtnListener) {
		loadPatternBtn.addActionListener(loadPatternBtnListener);
	}

	public void addSearchPatternBtnListener(ActionListener searchPatternBtnListener) {
		searchPatternBtn.addActionListener(searchPatternBtnListener);
	}

	// ... Setters to update the components.
	public void setLoadDataFromFileLabel(String fileName) {
		loadDataFromFileLabel.setText(fileName);
	}

	public void setLoadDataFromDirLabel(String dirName) {
		loadDataFromDirLabel.setText(dirName);
	}

	public void setLoadPatternLabel(String infoTxt) {
		loadPatternLabel.setText(infoTxt);
	}

	public void setSearchResults(String result) {
		searchResults.setText(result);
	}

	// ... Getters for accessing components

}
