package com.bcdipesh.controller;

import com.bcdipesh.model.BytePatternMatcher;
import com.bcdipesh.view.AppView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Handles user interaction of the view component by hooking its ActionListener to the {@link AppView} using functions provided by the Class.
 *
 * @author Dipesh B.C.
 * @version 2.0
 */
public class AppController {
    // ... The Model and View that the controller will interact with.
    private final BytePatternMatcher model;
    private final AppView view;

    /**
     * Creates a AppController Object that will link to the model, and the view passed to it.
     *
     * @param model Different Byte pattern matching operations will be done using this object.
     * @param view  The graphical user interface to represent the model.
     */
    public AppController(BytePatternMatcher model, AppView view) {
        // ... Bridge connection between model and view.
        this.model = model;
        this.view = view;

        // ... Add ActionListeners to the view.
        this.view.addMenuItemLoadFromFileListener(new LoadFromFileListener());
        this.view.addMenuItemLoadFromDirListener(new LoadFromDirListener());
        this.view.addMenuItemAboutListener(new AboutListener());
        this.view.addMenuItemExitListener(new ExitListener());

        this.view.addLoadDataFromFileBtnListener(new LoadFromFileListener());
        this.view.addLoadDataFromDirBtnListener(new LoadFromDirListener());
        this.view.addLoadPatternBtnListener(new LoadPatternBtnListener());
        this.view.addSearchPatternBtnListener(new SearchPatternBtnListener());
    }

    // ... Inner classes to provide ActionListener to the view.

    /**
     * This class implements ActionListener and provides ActionListener object for the menu item load from a file.
     */
    private class LoadFromFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // ... Get and store the file selected by the user.
            File file = getFile();
            if (file != null) {
                try {
                    // ... Update the file name and flag in the view.
                    model.setFile(file);
                    model.setDirIsSelected(false);
                    view.setLoadDataFromFileLabel(file.getName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * This class implements ActionListener and provides ActionListener object for the menu item load from a directory.
     */
    private class LoadFromDirListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // ... Open file chooser menu in the current folder.
            JFileChooser dirChooser = new JFileChooser(".");
            dirChooser.setDialogTitle("Select a directory");

            // ... Restrict users to select only directory.
            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            dirChooser.setAcceptAllFileFilterUsed(false);

            if (dirChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                // ... Get directory selected by the user.
                File dir = dirChooser.getSelectedFile();
                try {
                    // ... Update the directory name and flag in the view.
                    model.setDir(dir);
                    model.setDirIsSelected(true);
                    view.setLoadDataFromDirLabel(dir.getName());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * This class implements ActionListener and provides ActionListener object for the menu item about.
     */
    private class AboutListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // ... String builder to restrict unwanted String object creation.
            StringBuilder aboutMessage = new StringBuilder();

            // ... The about information.
            aboutMessage.append("<html>").append("<body>").append("<h1>Byte Pattern Matcher</h1>").append(
                    "<p>The sole purpose of this application is to search<br>for byte patterns with files and display<br>")
                    .append("matched patterns if any with their offset.</p><br>")
                    .append("<code>Author: Dipesh B.C. ID: 77202612</code>").append("</body>").append("</html>");

            // ... Setting the about information.
            JOptionPane.showOptionDialog(view, aboutMessage, "About", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
        }
    }

    /**
     * This class implements ActionListener and provides ActionListener object for the menu item exit.
     */
    private static class ExitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // ... By convention a non-negative value means normal termination of the application.
            System.exit(1);
        }
    }

    /**
     * This class implements ActionListener and provides ActionListener object for load pattern button.
     */
    private class LoadPatternBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // ... Get and store the file selected by the user.
            File file = getFile();
            if (file != null) {
                try {
                    // ... Update the respective labels in the view.
                    model.setPattern(file);
                    view.setLoadPatternLabel(file.getName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * This class implements ActionListener and provides ActionListener object for search pattern button.
     */
    private class SearchPatternBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // ... Get and store the results of the search from the model.
            TreeMap<Integer, byte[]> result = model.searchPattern();

            // ... These will be used to format the results to be displayed in the view.
            String pattern;
            StringBuilder resultTxt = new StringBuilder();
            String hexValue;
            ArrayList<String> patternArr = new ArrayList<>();
            // ... Check if the result is not empty and user has scanned a file.
            if (!result.isEmpty() && !model.getDirIsSelected()) {

                // ... Format the results.
                for (Map.Entry<Integer, byte[]> entry : result.entrySet()) {
                    for (byte b : entry.getValue()) {
                        patternArr.add(String.format("%x", b));
                    }
                    pattern = patternArr.toString().replaceAll("[,\\[\\]\\s+]", "");
                    hexValue = String.format("0x%x", entry.getKey());
                    resultTxt.append("Pattern found: ").append(pattern).append(", at offset: ").append(entry.getKey()).append(" (").append(hexValue).append(") within the file.\n");
                    patternArr.clear();
                }
                // ... Check if the result is not empty and user has scanned a directory.
            } else if (!result.isEmpty() && model.getDirIsSelected()) {
                // ... Get the file name and the results of scanning within that file.
                TreeMap<String, TreeMap<Integer, byte[]>> foundPatterns = model.getFoundPatterns();
                for (Map.Entry<String, TreeMap<Integer, byte[]>> entries : foundPatterns.entrySet()) {

                    // ... Format the results.
                    resultTxt.append("Filename: ").append(entries.getKey()).append("\n");
                    for (Map.Entry<Integer, byte[]> entry : entries.getValue().entrySet()) {
                        for (byte b : entry.getValue()) {
                            patternArr.add(String.format("%x", b));
                        }

                        pattern = patternArr.toString().replaceAll("[,\\[\\]\\s+]", "");
                        hexValue = String.format("0x%x", entry.getKey());
                        resultTxt.append("Pattern found: ").append(pattern).append(", at offset: ").append(entry.getKey()).append(" (").append(hexValue).append(") within the file.\n");
                        patternArr.clear();
                    }
                }
            } else {
                resultTxt = new StringBuilder("Pattern not found.");
            }

            // ... Update the text area in view.
            view.setSearchResults(resultTxt.toString());
        }
    }

    // ... Helper Functions.

    /**
     * Gets the file selected by the user.
     * This function displays a file chooser GUI for the user to select a file.
     *
     * @return Returns the file selected by the user.
     */
    private File getFile() {
        // ... Open the file chooser in the current directory.
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setDialogTitle("Select a file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.showOpenDialog(null);
        fileChooser.setAcceptAllFileFilterUsed(false);

        // ... Return the selected file.
        return fileChooser.getSelectedFile();

    }

}
