package test.java;
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

import static main.java.com.bcdipesh.utilities.PatternMatcherUtility.readDirectory;
import static main.java.com.bcdipesh.utilities.PatternMatcherUtility.readFile;
import static main.java.com.bcdipesh.utilities.PatternMatcherUtility.readPatternFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.com.bcdipesh.model.BytePatternMatcher;

/**
 * This class is purely used for the purpose of Unit Testing.
 *
 * @author Dipesh B.C.
 */
public class BytePatternMatcherTest {
  final URL fileURL = BytePatternMatcherTest.class.getResource("/test/resources/testFile.txt");

  final URL patternURL = BytePatternMatcherTest.class.getResource("/test/resources/patterns.txt");

  // ... default files and values for testing purpose
  private final File TEST_FILE = new File(fileURL.getFile());
  private final File PATTERN_FILE = new File(patternURL.getFile());

  private final ArrayList<Byte> EXPECTED_BYTE_LIST = new ArrayList<>();
  private final byte[] EXPECTED_BYTE = {
    116, 101, 115, 116, 105, 110, 103, 32, 102, 105, 108, 101, 32, 114, 101, 97, 100, 105, 110, 103
  };

  private final byte[] PATTERN_ONE = {65, 66, 67};
  private final byte[] PATTERN_TWO = {88, 89, 90};

  private BytePatternMatcher fileSearch;
  private BytePatternMatcher dirSearch;

  /**
   * Fills up the {@link EXPECTED_BYTE_LIST} with {@link EXPECTED_BYTE} that represents expected
   * byte array results.
   *
   * @throws IOException if an I/O error occurs reading from the file
   */
  @Before
  public void setUp() throws IOException {

    // ... setup object and variables for test
    convertToArrayList(EXPECTED_BYTE);
    fileSearch = getFileSearchObj();
    dirSearch = getDirSearchObj();
  }

  /**
   * Test method for {@link main.java.com.bcdipesh.utilities.PatternMatcherUtility#readFile(File)},
   * This will test that the function is working as intended.
   *
   * @throws IOException if an I/O error occurs reading from the file.
   */
  @Test
  public void testReadFile() throws IOException {
    ArrayList<Byte> result = readFile(TEST_FILE);

    compare(result, EXPECTED_BYTE);
  }

  /**
   * Test method for {@link
   * main.java.com.bcdipesh.utilities.PatternMatcherUtility#readDirectory(File)}, This will test
   * that the function is working as intended.
   *
   * @throws IOException if an I/O error occurs reading from the file.
   */
  @Test
  public void testReadDirectory() throws IOException {
    TreeMap<String, ArrayList<Byte>> expectedOutput = new TreeMap<>();
    expectedOutput.put("testFile.txt", EXPECTED_BYTE_LIST);

    TreeMap<String, ArrayList<Byte>> result = readDirectory(TEST_FILE);

    assertEquals("Map should contain 1 key and value", expectedOutput, result);
  }

  /**
   * Test method for {@link
   * main.java.com.bcdipesh.utilities.PatternMatcherUtility#readPatternFile(File)}, This will test
   * that the function is working as intended.
   *
   * @throws IOException if an I/O error occurs reading from the file.
   */
  @Test
  public void testReadPatternFile() throws IOException {
    ArrayList<byte[]> expectedOutput = new ArrayList<>();
    expectedOutput.add(PATTERN_ONE);
    expectedOutput.add(PATTERN_TWO);
    ArrayList<byte[]> result = readPatternFile(PATTERN_FILE);

    compare(expectedOutput, result);
  }

  /**
   * Test method for {@link main.java.com.bcdipesh.model.BytePatternMatcher#searchPattern()}. This
   * will test the main functionality of the application i.e, to search for byte patterns within a
   * file or directory.
   *
   * @throws IOException Throws IOException if the provided file is not present.
   */
  @Test
  public void testSearchPattern() throws IOException {

    TreeMap<Integer, byte[]> expectedOutput = new TreeMap<>();
    expectedOutput.put(57, PATTERN_ONE);
    expectedOutput.put(65, PATTERN_TWO);
    TreeMap<Integer, byte[]> result = fileSearch.searchPattern();

    // ... Test the output
    compare(expectedOutput, result);
    assertEquals("Size of output must be 1", 1, dirSearch.searchPattern().size());
  }

  // ... Helper functions

  // ... convert a normal array to array list.
  private void convertToArrayList(byte[] arrayToConvert) {
    for (byte b : arrayToConvert) {
      EXPECTED_BYTE_LIST.add(b);
    }
  }

  /**
   * Creates a test object to search within a file.
   *
   * @return A {@link BytePatternMatcher} object to search for patterns within a file.
   * @throws IOException if an I/O error occurs reading from the file.
   */
  private BytePatternMatcher getFileSearchObj() throws IOException {
    BytePatternMatcher testObj = new BytePatternMatcher();
    URL fileURL = BytePatternMatcher.class.getResource("/test/resources/test1.txt");

    // ... Setting the object state.

    // ... Test - 1 for searching within a file.

    testObj.setFile(new File(fileURL.getFile()));
    testObj.setPattern(PATTERN_FILE);
    testObj.setIsFileSelected(true);

    return testObj;
  }

  /**
   * Creates a test object to search within a directory.
   *
   * @return A {@link BytePatternMatcher} object to search for patterns within a directory.
   * @throws IOException if an I/O error occurs reading from the file.
   */
  private BytePatternMatcher getDirSearchObj() throws IOException {
    BytePatternMatcher testObj = new BytePatternMatcher();
    URL dirURL = BytePatternMatcher.class.getResource("/test/resources");
    URL patternURL = BytePatternMatcher.class.getResource("/test/resources/patterns_ex1.txt");

    // ... Setting the object state.

    // ... Test - 2 for searching within files inside a directory.
    testObj.setDir(new File(dirURL.getFile()));
    testObj.setPattern(new File(patternURL.getFile()));
    testObj.setIsDirectorySelected(true);

    return testObj;
  }

  /**
   * Compares if a ArrayList and byte[] are equal, by checking the size and contents.
   *
   * @param actualOutput The ArrayList to be checked.
   * @param expectedOutput The array used to check the provided ArrrayList.
   */
  private void compare(ArrayList<Byte> actualOutput, byte[] expectedOutput) {
    // ... check length
    assertTrue("Arrays not the same length", expectedOutput.length == actualOutput.size());

    // ... check data within the array list.
    for (int index = 0; index < expectedOutput.length; index++) {
      assertEquals(
          "Byte representing the data must be same",
          (Byte) expectedOutput[index],
          actualOutput.get(index));
    }
  }

  /**
   * Compares if two ArrayLists are equal, by checking the size and contents.
   *
   * @param actualOutput The ArrayList to be checked.
   * @param expectedOutput The ArrayList used to check the provided ArrrayList.
   */
  private void compare(ArrayList<byte[]> actualOutput, ArrayList<byte[]> expectedOutput) {
    // ... check size
    assertTrue("ArrayList must have two patterns", expectedOutput.size() == actualOutput.size());

    // ... check data within the array list.
    for (int i = 0; i < expectedOutput.size(); i++) {
      for (int j = 0; j < expectedOutput.get(i).length; j++) {
        assertEquals("Pattern byte must be same", expectedOutput.get(i)[j], actualOutput.get(i)[j]);
      }
    }
  }

  /**
   * Compares if two TreeMap are equal, by checking the size and contents.
   *
   * @param expectedOutput The TreeMap to be checked.
   * @param actualOutput The TreeMap used to check the provided TreeMap.
   */
  private void compare(
      TreeMap<Integer, byte[]> expectedOutput, TreeMap<Integer, byte[]> actualOutput) {
    // ... check size
    assertTrue("TreeMap must have two values", expectedOutput.size() == actualOutput.size());

    // ... check key set
    assertTrue("Keys must be equal", expectedOutput.keySet().equals(actualOutput.keySet()));

    ArrayList<byte[]> expectedArray = new ArrayList<>(expectedOutput.values());
    ArrayList<byte[]> actualArray = new ArrayList<>(actualOutput.values());

    // ... check values
    compare(expectedArray, actualArray);
  }

  /** Empty the {@link EXPECTED_BYTE_LIST} after the test */
  @After
  public void cleanUp() {
    EXPECTED_BYTE_LIST.clear();
  }
}
