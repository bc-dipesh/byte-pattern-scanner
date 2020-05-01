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

package main.java.com.bcdipesh.utilities;

import static java.util.stream.Collectors.toCollection;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

import javax.swing.JFileChooser;

/**
 * This class consists exclusively of static methods that operate on files, directories, or other
 * types of files and Arrays.
 *
 * @author Dipesh B.C.
 * @version 1.1
 */
public interface PatternMatcherUtility {

  /*
   *  The max size of a hexadecimal string
   */
  int HEX_STRING_SIZE = 2;

  /**
   * Opens a {@link JFileChooser} for user to select a file.
   *
   * @return the file selected by the user.
   */
  static File getFile() {
    // ... Open the file chooser in the current directory.
    JFileChooser fileChooser = new JFileChooser(".");
    fileChooser.setDialogTitle("Select a file");
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.showOpenDialog(null);
    fileChooser.setAcceptAllFileFilterUsed(false);

    // ... Return the selected file.
    return fileChooser.getSelectedFile();
  }

  /**
   * Opens a {@link JFileChooser} for user to select a directory.
   *
   * @return the directory selected by the user.
   */
  static File getDirectory() {
    // ... Open file chooser menu in the current folder.
    JFileChooser dirChooser = new JFileChooser(".");
    dirChooser.setDialogTitle("Select a directory");

    // ... Restrict users to select only directory.
    dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    dirChooser.setAcceptAllFileFilterUsed(false);
    dirChooser.showOpenDialog(null);

    // ... Return the selected directory.
    return dirChooser.getSelectedFile();
  }

  /**
   * Reads a directory. This function reads a directory containing multiple files as bytes.
   *
   * @param dir The directory containing multiple files.
   * @return A TreeMap of file name and the bytes representing the contents of the file.
   * @throws IOException Throws an {@link IOException} if the file passed to it is invalid.
   */
  static TreeMap<String, ArrayList<Byte>> readDirectory(File dir) throws IOException {
    TreeMap<String, ArrayList<Byte>> dirBytes = new TreeMap<>();
    ArrayList<Path> listOfFilePaths = listFilesWithinDirectory(dir);

    for (Path filePath : listOfFilePaths) {
      dirBytes.put(filePath.getFileName().toString(), readFile(filePath.toFile()));
    }

    return dirBytes;
  }

  /**
   * Reads the content of file as bytes given the target {@link File}.
   *
   * @param file The file to read.
   * @return Returns an ArrayList of Byte containing the contents of the file.
   * @throws IOException if an I/O error occurs reading from the path.
   */
  static ArrayList<Byte> readFile(File file) throws IOException {
    ArrayList<Byte> fileBytes = new ArrayList<>();
    byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));

    for (byte b : bytes) {
      fileBytes.add(b);
    }

    return fileBytes;
  }

  /**
   * Reads a pattern file. This function reads a pattern file containing pattern/patterns to be
   * searched.
   *
   * @param patternFile The file to read.
   * @return Returns an ArrayList of byte[] containing list of pattern/patterns to be searched.
   * @throws IOException Throws an {@link IOException} if the file passed to it is invalid.
   */
  static ArrayList<byte[]> readPatternFile(File patternFile) throws IOException {
    // ... Using try-with-resources so the file handle for patternFile gets closed
    // properly.
    try (Stream<String> line = Files.lines(Paths.get(patternFile.getPath()))) {
      // ... convert to ArrayList<byte[]> and return.
      return line.map(PatternMatcherUtility::splitStringOnSpaces)
          .map(PatternMatcherUtility::getHexString)
          .filter(PatternMatcherUtility::validateHexString)
          .map(PatternMatcherUtility::convertToByteArray)
          .collect(toCollection(ArrayList::new));
    }
  }

  /**
   * Lists only file {@link Path} contained within a directory.
   *
   * @param dir The directory containing multiple files
   * @return Returns a {@link ArrayList} of {@link Path} representing the {@link Path} to the file.
   * @throws IOException Throws and {@link IOException} if the file passed to it is invalid.
   */
  static ArrayList<Path> listFilesWithinDirectory(File dir) throws IOException {
    // ... Using try-with-resources so the file handle for directory gets closed
    // properly.
    try (Stream<Path> filePathStream = Files.walk(Paths.get(dir.getPath()))) {
      // ... Filter files to get only the path of all the files within that directory
      // and
      // return.
      return filePathStream.filter(Files::isRegularFile).collect(toCollection(ArrayList::new));
    }
  }

  /**
   * Gets the total file count present inside a directory. Note: This function only counts the total
   * files present in a directory of depth 1, i.e., it won't count files inside sub-directories.
   *
   * @param dir The Directory whose contents are counted.
   * @return Returns the total file count inside a directory.
   */
  static int countTotalFilesInDir(File dir) {
    return Objects.requireNonNull(dir.listFiles()).length;
  }

  /**
   * Takes a String and splits it into String array using single space.
   *
   * @param stringToSplit The String to split.
   * @return Returns a String array containing the result of the split.
   */
  static String[] splitStringOnSpaces(String stringToSplit) {
    String trimmedString = stringToSplit.trim();

    return trimmedString.split("\\s+");
  }

  /**
   * Gets a byte array given a hexadecimal string. This function converts a hexadecimal string to
   * return a byte array.
   *
   * @param hexString A string representing hexadecimal number.
   * @return Returns a byte[] that represents the hexadecimal number.
   */
  static byte[] getByteArray(String hexString) {
    byte[] byteArray = new BigInteger(hexString, 16).toByteArray();

    if (byteArray[0] == 0) {
      byte[] output = new byte[byteArray.length - 1];
      System.arraycopy(byteArray, 1, output, 0, output.length);
      return output;
    }

    return byteArray;
  }

  /**
   * Converts a string[] to byte array. This function converts a string[] representing hexadecimal
   * numbers to a byte array.
   *
   * @param hexString A String representing hexadecimal values.
   * @return Returns a byte[] of hexadecimal values.
   */
  static byte[] convertToByteArray(String hexString) {

    return getByteArray(hexString);
  }

  /**
   * Gets a hexadecimal string representation provided a hexadecimal String array.
   *
   * @param hexArray A String array representing hexadecimal values.
   * @return A String representing hexadecimal value.
   */
  static String getHexString(String[] hexArray) {
    String[] validHexArray = getValidHexArray(hexArray);

    return Arrays.toString(validHexArray).replaceAll("[,\\s\\[\\]]", "");
  }

  /**
   * Checks if the string representation of hexadecimal value is correct or not.
   *
   * @param hexString The string to be tested.
   * @return true if the string correctly represents hexadecimal value and false otherwise.
   */
  static boolean validateHexString(String hexString) {
    try {
      new BigInteger(hexString, 16).toByteArray();
      return true;
    } catch (NumberFormatException ex) {
      // ... print out the invalid pattern.
      System.out.println(String.join(" ", "Pattern", hexString, "is", "invalid."));
      return false;
    }
  }

  /**
   * Checks and returns a valid hexadecimal string array given an array of string.
   *
   * @param hexArray A String array representing hexadecimal values.
   * @return A String array representing only correct hexadecimal values.
   */
  static String[] getValidHexArray(String[] hexArray) {
    if (checkArrayContents(hexArray)) {
      return hexArray;
    } else {
      return new String[] {};
    }
  }

  /**
   * Checks if the array has valid hexadecimal digits
   *
   * @param hexArray The array to be tested.
   * @return true if the array holds valid hexadecimal digits and false otherwise.
   */
  static boolean checkArrayContents(String[] hexArray) {
    int loopCount = 0;

    // ... check if the contents of the array are valid hexadecimal digits
    for (String hexDigit : hexArray) {
      if (hexDigit.length() == HEX_STRING_SIZE) {
        ++loopCount;
      }
    }
    return loopCount == hexArray.length;
  }
}
