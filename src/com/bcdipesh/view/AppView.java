package com.bcdipesh.view;

import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.MaterialLiteTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Represents the View Component of the application.
 *
 * @author Dipesh B.C.
 * @version 2.0
 */
public class AppView extends JFrame {

    // ... Components for the view.
    private final JMenuItem menuItemLoadFromFile;
    private final JMenuItem menuItemLoadFromDir;
    private final JMenuItem menuItemAbout;
    private final JMenuItem menuItemExit;

    private final JLabel loadDataFromFileLabel;
    private final JLabel loadDataFromDirLabel;
    private final JLabel loadPatternLabel;

    private final JTextArea searchResults;

    private final JButton loadDataFromFileBtn;
    private final JButton loadDataFromDirBtn;
    private final JButton searchPatternBtn;
    private final JButton loadPatternBtn;

    /**
     * Creates a view for the application, using the components.
     */
    public AppView() {

        // ... Set custom look and feel.
        try {
            JDialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel(new MaterialLookAndFeel(new MaterialLiteTheme()));
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // ... Initialize components.

        // ... JPanel
        JPanel westPanel = new JPanel();
        JPanel westNorthPanel = new JPanel();
        JPanel westSouthPanel = new JPanel();
        JPanel centerPanel = new JPanel();

        // ... ImageIcon
        Icon fileIcon = new ImageIcon(".//icons//file.png");
        Icon dirIcon = new ImageIcon(".//icons//folder.png");
        Icon aboutIcon = new ImageIcon(".//icons//about.png");
        Icon exitIcon = new ImageIcon(".//icons//close.png");
        Icon patternIcon = new ImageIcon(".//icons//pattern.png");
        Icon searchIcon = new ImageIcon(".//icons//search.png");

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
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(menuItemLoadFromFile);
        fileMenu.add(menuItemLoadFromDir);
        fileMenu.add(menuItemAbout);
        fileMenu.add(menuItemExit);

        // ... JMenuBar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);

        // ... JLabel
        loadDataFromFileLabel = new JLabel("Select a file...");
        loadDataFromDirLabel = new JLabel("Select a dir...");
        loadPatternLabel = new JLabel("Select a pattern...");

        // ... JTextArea
        searchResults = new JTextArea(10, 10);

        // ... JScrollPane
        JScrollPane scrollPane = new JScrollPane(searchResults);
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

        // ... Layout the components.
        westPanel.setLayout(new BorderLayout());

        westNorthPanel.add(loadDataFromFileLabel);
        westNorthPanel.add(loadDataFromFileBtn);
        westNorthPanel.add(loadDataFromDirLabel);
        westNorthPanel.add(loadDataFromDirBtn);

        westSouthPanel.add(loadPatternLabel);
        westSouthPanel.add(loadPatternBtn);
        westSouthPanel.add(searchPatternBtn);

        centerPanel.add(scrollPane);

        westPanel.add(BorderLayout.NORTH, westNorthPanel);
        westPanel.add(BorderLayout.SOUTH, westSouthPanel);

        this.setJMenuBar(menuBar);

        this.add(BorderLayout.WEST, westPanel);
        this.add(BorderLayout.CENTER, scrollPane);

        // ... Finalize the layout.
        this.setSize(700, 300);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ... Methods for registering controller's ActionListener.

    /**
     * Provides mechanism to add ActionListener to menu item load from a file.
     * Use this function to add the controller's ActionListener for listening to events when user selects menu item load from a file from the file menu.
     *
     * @param menuItemLoadFromFileListener The Controller's ActionListener to listen to events from the menu item load from a file.
     */
    public void addMenuItemLoadFromFileListener(ActionListener menuItemLoadFromFileListener) {
        menuItemLoadFromFile.addActionListener(menuItemLoadFromFileListener);
    }

    /**
     * Provides mechanism to add ActionListener to menu item load from a directory.
     * Use this function to add the controller's ActionListener for listening to events when user selects menu item load from a directory from the file menu.
     *
     * @param menuItemLoadFromDirListener The Controller's ActionListener object to listen to events from the menu item load from a directory.
     */
    public void addMenuItemLoadFromDirListener(ActionListener menuItemLoadFromDirListener) {
        menuItemLoadFromDir.addActionListener(menuItemLoadFromDirListener);
    }

    /**
     * Provides mechanism to add ActionListener to menu item about.
     * Use this function to add the controller's ActionListener for listening to events when user selects menu item about from the file menu.
     *
     * @param menuItemAboutListener The Controller's ActionListener object to listen to events from the menu item about.
     */
    public void addMenuItemAboutListener(ActionListener menuItemAboutListener) {
        menuItemAbout.addActionListener(menuItemAboutListener);
    }

    /**
     * Provides mechanism to add ActionListener to menu item exit.
     * Use this function to add the controller's ActionListener for listening to events when user selects menu item exit from the file menu.
     *
     * @param menuItemExitListener The Controller's ActionListener object to listen to events from the menu item exit.
     */
    public void addMenuItemExitListener(ActionListener menuItemExitListener) {
        menuItemExit.addActionListener(menuItemExitListener);
    }

    /**
     * Provides mechanism to add ActionListener to load data from file button.
     * Use this function to add the controller's ActionListener for listening to events when user presses load data from file button.
     *
     * @param loadDataFromFileBtnListener The Controller's ActionListener object to listen to events from load data from file button.
     */
    public void addLoadDataFromFileBtnListener(ActionListener loadDataFromFileBtnListener) {
        loadDataFromFileBtn.addActionListener(loadDataFromFileBtnListener);
    }

    /**
     * Provides mechanism to add ActionListener to load data from directory button.
     * Use this function to add the controller's ActionListener for listening to events when user presses load data from directory button.
     *
     * @param loadDataFromDirBtnListener The Controller's ActionListener object to listen to events from load data from directory button.
     */
    public void addLoadDataFromDirBtnListener(ActionListener loadDataFromDirBtnListener) {
        loadDataFromDirBtn.addActionListener(loadDataFromDirBtnListener);
    }

    /**
     * Provides mechanism to add ActionListener to load pattern button.
     * Use this function to add the controller's ActionListener for listening to events when user presses load pattern button.
     *
     * @param loadPatternBtnListener The Controller's ActionListener object to listen to events from load pattern button.
     */
    public void addLoadPatternBtnListener(ActionListener loadPatternBtnListener) {
        loadPatternBtn.addActionListener(loadPatternBtnListener);
    }

    /**
     * Provides mechanism to add ActionListener to search pattern button.
     * Use this function to add the controller's ActionListener for listening to events when user presses search pattern button.
     *
     * @param searchPatternBtnListener The Controller's ActionListener object to listen to events from search pattern button.
     */
    public void addSearchPatternBtnListener(ActionListener searchPatternBtnListener) {
        searchPatternBtn.addActionListener(searchPatternBtnListener);
    }

    // ... Setters to update the components.

    /**
     * Sets/updates load data from file label.
     * This function sets/updates the name of the file to be scanned in load data from file label with the string passed to it.
     *
     * @param fileName The file name to set/update in load data from file label.
     */
    public void setLoadDataFromFileLabel(String fileName) {
        loadDataFromFileLabel.setText(fileName);
    }

    /**
     * Sets/updates load data from directory label.
     * This function sets/updates the name of the directory to be scanned in load data from directory label with the string passed to it.
     *
     * @param dirName The directory name to set/update in load data from directory label.
     */
    public void setLoadDataFromDirLabel(String dirName) {
        loadDataFromDirLabel.setText(dirName);
    }

    /**
     * Sets/updates the load pattern label.
     * This function sets/updates the name of the pattern file containing the pattern/patterns to be searched in load pattern label with the string passed to it.
     *
     * @param patternFileName The file name to set/update in load pattern label.
     */
    public void setLoadPatternLabel(String patternFileName) {
        loadPatternLabel.setText(patternFileName);
    }

    /**
     * Sets/updates the search results text area.
     * This function sets/updates the text area with the results passed to it.
     *
     * @param result The string containing results generated after searching for pattern/patterns.
     */
    public void setSearchResults(String result) {
        searchResults.setText(result);
    }
    
}
