/*
 * Copyright (c) 2020, Dipesh B.C.. All rights reserved.
 * Unauthorized copying of this file, via any medium is
 * strictly prohibited.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package main.java.com.bcdipesh.controller;

import static java.util.stream.Collectors.toCollection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import main.java.com.bcdipesh.model.BytePatternMatcher;
import main.java.com.bcdipesh.utilities.PatternMatcherUtility;
import main.java.com.bcdipesh.view.AppView;

/**
 * Handles user interaction of the view component by hooking its ActionListener to the {@link
 * AppView} using functions provided by the Class.
 *
 * @author Dipesh B.C.
 * @version 3.0
 */
public class AppController {
  // ... The Model and View that the controller will interact with.
  private final BytePatternMatcher model;
  private final AppView view;

  private int totalFilesInDir;

  /**
   * Creates a AppController Object that will link to the model, and the view passed to it.
   *
   * @param model Different Byte pattern matching operations will be done using this object.
   * @param view The graphical user interface to represent the model.
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
   * This class implements ActionListener and provides ActionListener object for the menu item load
   * from a file.
   */
  private class LoadFromFileListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      // ... Get and store the file selected by the user.
      final File file = PatternMatcherUtility.getFile();
      if (file != null) {
        try {
          // ... Update the file name and flag in the view.
          model.setFile(file);
          model.setIsDirectorySelected(false);
          model.setIsFileSelected(true);
          view.setLoadDataFromFileLabel(file.getName());
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  /**
   * This class implements ActionListener and provides ActionListener object for the menu item load
   * from a directory.
   */
  private class LoadFromDirListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

      File dir = PatternMatcherUtility.getDirectory();
      if (dir != null) {
        try {
          // ... Update the directory name and flag in the view.
          model.setDir(dir);
          totalFilesInDir = PatternMatcherUtility.countTotalFilesInDir(dir);
          model.setIsDirectorySelected(true);
          model.setIsFileSelected(false);
          view.setLoadDataFromDirLabel(dir.getName());
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    }
  }

  /**
   * This class implements ActionListener and provides ActionListener object for the menu item
   * about.
   */
  private class AboutListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      // ... String builder to restrict unwanted String object creation.
      StringBuilder aboutMessage = new StringBuilder(158);

      // ... The about information.
      aboutMessage
          .append("Byte Pattern Matcher")
          .append(
              "\n\nThe sole purpose of this application is to search"
                  + "\nfor byte patterns within files and directory.")
          .append(
              "\n\nCopyright (c) 2020, Dipesh B.C.. All rights reserved.\nUnauthorized copy and distribution of this application,\nvia any medium is strictly prohibited")
          .append(
              "\n\nAuthor: Dipesh B.C. ID: 77202612\nEmail: D.bc8829@student.leedsbeckett.ac.uk");

      // ... Setting the about information.
      JOptionPane.showOptionDialog(
          view,
          aboutMessage,
          "About",
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.INFORMATION_MESSAGE,
          null,
          new Object[] {},
          null);
    }
  }

  /**
   * This class implements ActionListener and provides ActionListener object for the menu item exit.
   */
  private static class ExitListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      // ... By convention a non-negative value means normal termination of the
      // application.
      System.exit(1);
    }
  }

  /**
   * This class implements ActionListener and provides ActionListener object for load pattern
   * button.
   */
  private class LoadPatternBtnListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      // ... Get and store the file selected by the user.
      File file = PatternMatcherUtility.getFile();
      if (file != null) {
        try {
          // ... Update the respective labels in the view.
          model.setPattern(file);
          model.setIsPatternSelected(true);
          view.setLoadPatternLabel(file.getName());
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  /**
   * This class implements ActionListener and provides ActionListener object for search pattern
   * button.
   */
  private class SearchPatternBtnListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      // ... Get the search results
      TreeMap<Integer, byte[]> result = model.searchPattern();

      if (model.isFileSelected() || model.isDirectorySelected()) {
        if (model.isFileSelected() && model.isPatternSelected()) {
          processFileSearchResults(result);
        } else if (model.isDirectorySelected() && model.isPatternSelected()) {
          displayDirSearchResult(model.getFoundPatterns());
        } else {
          view.setSearchResults("Please select a pattern before searching");
        }
      } else {
        if (model.isPatternSelected()) {
          view.setSearchResults("Please select a file/directory before searching");
        } else {
          view.setSearchResults("Please select a file/directory and a pattern before searching");
        }
      }
    }

    /**
     * Processes necessary operations on results obtained after searching file for patterns.
     *
     * @param result The data to be used for processing.
     */
    private void processFileSearchResults(TreeMap<Integer, byte[]> result) {
      if (result.isEmpty()) {
        view.setSearchResults(
            String.join("\n", view.getLoadDataFromFileLabel().getText(), "No pattern found."));
      } else {
        displayFileSearchResult(result);
      }
    }

    /**
     * Displays search results in the application for the users to see.
     *
     * @param result The data used for displaying information.
     */
    private void displayFileSearchResult(TreeMap<Integer, byte[]> result) {
      StringBuilder searchResult = new StringBuilder();

      // ... Gather formatted string of information to display for each entry in the result.
      String collectFormattedResult =
          result
              .entrySet()
              .stream()
              .map(entry -> formatResult(entry.getKey(), entry.getValue()))
              .collect(Collectors.joining());

      searchResult
          .append(view.getLoadDataFromFileLabel().getText())
          .append("\n")
          .append(collectFormattedResult);

      // ... Display the results.
      view.setSearchResults(searchResult.toString());
    }

    /**
     * Displays search results in the application for the users to see.
     *
     * @param result The data used for displaying information.
     */
    private void displayDirSearchResult(TreeMap<String, TreeMap<Integer, byte[]>> result) {

      String dirName = view.getLoadDataFromDirLabel().getText();
      StringBuilder resultTxt = new StringBuilder();

      // ... Additional-data
      resultTxt
          .append("Directory: ")
          .append(dirName)
          .append("\t(")
          .append(totalFilesInDir)
          .append(" files)\n");

      // ... Gather formatted string of information to display for each entry in the result.
      for (Map.Entry<String, TreeMap<Integer, byte[]>> entries : result.entrySet()) {

        if (entries.getValue().isEmpty()) {
          resultTxt
              .append("\nFilename: ")
              .append(entries.getKey())
              .append("\n")
              .append("No patterns found.\n");
          view.setSearchResults(resultTxt.toString());

        } else {

          // ... Some additional-data about files within that directory.
          resultTxt.append("\nFilename: ").append(entries.getKey()).append("\n");

          // ... For each file search results format the results.
          for (Map.Entry<Integer, byte[]> entry : entries.getValue().entrySet()) {
            resultTxt.append(formatResult(entry.getKey(), entry.getValue()));
          }

          // ... Display the results.
          view.setSearchResults(resultTxt.toString());
        }
      }
    }

    /**
     * Formats the data passed to it.
     *
     * @param offset The position where the pattern was found.
     * @param byteArray The array found at that offset.
     * @return A formatted String of information about the given parameters.
     */
    private String formatResult(Integer offset, byte[] byteArray) {
      ArrayList<String> hexStringList = getHexStringList(byteArray);
      String hexPatternString = getHexString(hexStringList);
      String hexOffset = String.format("0x%x", offset);
      StringBuilder resultText = beautifyResult(offset, hexPatternString, hexOffset);

      return resultText.toString();
    }

    /**
     * This formats the extra information supplied by the {@link formatResult} function.
     *
     * @param offset The position where the pattern was found.
     * @param hexPatternString Hexadecimal String representation of the found pattern.
     * @param hexOffset Hexadecimal representation of the position where the pattern was found.
     * @return A formatted String of information about the given parameters.
     */
    private StringBuilder beautifyResult(
        Integer offset, String hexPatternString, String hexOffset) {
      StringBuilder resultText = new StringBuilder();

      resultText
          .append("Pattern found: ")
          .append(hexPatternString)
          .append(", at offset: ")
          .append(offset)
          .append(" (")
          .append(hexOffset)
          .append(") within the file.")
          .append(System.getProperty("line.separator"));

      return resultText;
    }

    /**
     * Provides the caller with a hexadecimal string representation of the provided List.
     *
     * @param hexStringList The list whose contents are to be converted.
     * @return A hexadecimal string generated from the list contents.
     */
    private String getHexString(ArrayList<String> hexStringList) {
      return hexStringList.toString().replaceAll("[,\\[\\]\\s]", "");
    }

    /**
     * Provides the caller with a ArrayList of hexadecimal string.
     *
     * @param byteArray The array to be converted to the ArrayList.
     * @return An ArrayList of hexadecimal string constructed with data from the provided array.
     */
    private ArrayList<String> getHexStringList(byte[] byteArray) {
      ArrayList<Byte> list = convertToList(byteArray);

      // ... format and return the list
      return list.stream()
          .map(element -> String.format("%x", element))
          .collect(toCollection(ArrayList::new));
    }

    /**
     * Converts a primitive byte array to its ArrayList representation.
     *
     * @param byteArray The array to be converted.
     * @return An ArrayList representation of the provided array.
     */
    private ArrayList<Byte> convertToList(byte[] byteArray) {
      ArrayList<Byte> list = new ArrayList<>();

      // ... convert byte[] to ArrayList of Byte
      for (byte b : byteArray) {
        list.add(b);
      }
      return list;
    }
  } // ... End of SearchPatternBtnListener
} // ... End of AppController
