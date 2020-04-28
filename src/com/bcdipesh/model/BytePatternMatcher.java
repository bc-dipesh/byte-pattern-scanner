package com.bcdipesh.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.bcdipesh.patternMatcherUtils.PatternMatcherUtility;

/**
 * Provides byte pattern matching operations.
 *
 * @author Dipesh B.C.
 * @version 1.1
 */
public class BytePatternMatcher {
	// ... All the bytes read from the file will be stored here.
	private ArrayList<Byte> fileBytesArray;
	private ArrayList<byte[]> patternListArray;
	private TreeMap<String, ArrayList<Byte>> dirBytes;

	// ... The patterns that are found in the file will be stored here.
	private final TreeMap<String, TreeMap<Integer, byte[]>> foundPatterns;

	// ... Flag to determine whether user selected a file or a directory.
	private boolean isDirSelected;
	private boolean isFileSelected;

	/**
	 * Creates an object of this class. Creating object using this constructor will
	 * initialize all the class variables to their default values.
	 */
	public BytePatternMatcher() {
		fileBytesArray = new ArrayList<>();
		patternListArray = new ArrayList<>();
		dirBytes = new TreeMap<>();
		foundPatterns = new TreeMap<>();
		isDirSelected = false;
		isFileSelected = false;
	}

	// ... User functions.

	/**
	 * Searches for pattern/patterns after user has selected a file or directory
	 * along with the pattern file that contains patterns to be searched.
	 *
	 * @return Returns a TreeMap with Integer that represents offset of the matched
	 *         pattern as a key and a byte[] containing the matched pattern as the
	 *         value.
	 */
	public TreeMap<Integer, byte[]> searchPattern() {
		return isDirSelected ? indexOfPattern(dirBytes, patternListArray)
				: indexOfPattern(fileBytesArray, patternListArray);
	}

	// ... Setters

	/**
	 * Sets/Updates the source file. This function will set/update the currently
	 * selected file to search for pattern/patterns with the file passed to it.
	 *
	 * @param file The file selected by the user.
	 * @throws IOException Throws an {@link IOException} if the file provided is
	 *                     invalid.
	 */
	public void setFile(File file) throws IOException {
		fileBytesArray = PatternMatcherUtility.readFile(file);
	}

	/**
	 * Sets/Updates the source directory. This function will set/update the
	 * currently selected directory to search for pattern/patterns with the file
	 * passed to it.
	 *
	 * @param dir A file representing the directory selected by the user.
	 * @throws IOException Throws an {@link IOException} if the file provided is
	 *                     invalid.
	 */
	public void setDir(File dir) throws IOException {
		dirBytes = PatternMatcherUtility.readDirectory(dir);
	}

	/**
	 * Sets/Updates the pattern file. This function will set/update the currently
	 * selected pattern file containing the patterns list with the file passed to
	 * it.
	 *
	 * @param file The file selected by the user.
	 * @throws IOException Throws an {@link IOException} if the file is invalid.
	 */
	public void setPattern(File file) throws IOException {
		patternListArray = PatternMatcherUtility.readPatternFile(file);
	}

	/**
	 * Sets/Updates the directory selection flag. This function will set/update the
	 * flag for the application to determine if the user a directory.
	 *
	 * @param isSelected A boolean value that represents if user selected a
	 *                   directory.
	 */
	public void setIsDirectorySelected(boolean isSelected) {
		isDirSelected = isSelected;
	}

	/**
	 * Sets/Updates the file selection flag. This function will set/update the flag
	 * for the application to determine if the user selected a file.
	 *
	 * @param isSelected A boolean value that represents if user selected a file.
	 */
	public void setIsFileSelected(boolean isSelected) {
		isFileSelected = isSelected;
	}

	// ... Getters

	/**
	 * Gets a TreeMap of patterns that are matched.
	 *
	 * @return Returns a TreeMap with String that represents the file name of the
	 *         source as key, and a value of TreeMap of matched patterns with
	 *         Integer that represents offset of the matched pattern as a key and a
	 *         byte[] containing the matched pattern as the value.
	 */
	public TreeMap<String, TreeMap<Integer, byte[]>> getFoundPatterns() {
		return foundPatterns;
	}

	/**
	 * Gets the flag for the selected state of directory.
	 *
	 * @return Returns a boolean value that represents if a directory is selected.
	 */
	public boolean getIsDirectorySelected() {
		return isDirSelected;
	}

	/**
	 * Gets the flag for the selected state of file.
	 *
	 * @return Returns a boolean value that represents if a file is selected.
	 */
	public boolean getIsFileSelected() {
		return isFileSelected;
	}

	// ... Helper functions.

	/**
	 * Provides the index of pattern/patterns matched given a source and pattern
	 * list.
	 *
	 * @param source      A TreeMap of String as the file name, and an ArrayList of
	 *                    Byte that represents the bytes of the contents inside
	 *                    file.
	 * @param patternList An ArrayList of byte[] containing the pattern/patterns to
	 *                    be within the source.
	 * @return Returns a TreeMap with Integer representing the offset of the matched
	 *         pattern as key and, a byte[] of pattern matched at that offset as
	 *         value.
	 */
	private TreeMap<Integer, byte[]> indexOfPattern(TreeMap<String, ArrayList<Byte>> source,
			ArrayList<byte[]> patternList) {
		TreeMap<Integer, byte[]> answer = new TreeMap<>();
		TreeMap<Integer, byte[]> resultMap;

		for (Map.Entry<String, ArrayList<Byte>> entry : source.entrySet()) {
			resultMap = indexOfPattern(entry.getValue(), patternList);
			answer.putAll(resultMap);
			foundPatterns.put(entry.getKey(), resultMap);
		}

		return answer;
	}

	/**
	 * Provides the index of pattern/patterns matched given a source and pattern
	 * list.
	 *
	 * @param source      An ArrayList of Byte that represents the bytes of the
	 *                    contents inside file.
	 * @param patternList An ArrayList of byte[] containing the pattern/patterns to
	 *                    be matched within the source.
	 * @return Returns a TreeMap with Integer representing the offset of the matched
	 *         pattern as key and, a byte[] of pattern matched at that offset as
	 *         value.
	 */
	private TreeMap<Integer, byte[]> indexOfPattern(ArrayList<Byte> source, ArrayList<byte[]> patternList) {
		TreeMap<Integer, byte[]> answer = new TreeMap<>();
		TreeMap<Integer, byte[]> answerHolder;

		for (byte[] pattern : patternList) {
			answerHolder = indexOfPattern(source, pattern);
			answer.putAll(answerHolder);
		}

		return answer;
	}

	/**
	 * Provides the index of pattern/patterns matched given a source and pattern
	 * list. This is the core function that encapsulates the byte pattern searching
	 * algorithm.
	 *
	 * @param source  An ArrayList of Byte that represents the bytes of the contents
	 *                inside file.
	 * @param pattern A byte[] containing the pattern/patterns to be matched within
	 *                the source.
	 * @return Returns a TreeMap with Integer representing the offset of the matched
	 *         pattern as key and, a byte[] of pattern matched at that offset as
	 *         value.
	 */
	private TreeMap<Integer, byte[]> indexOfPattern(ArrayList<Byte> source, byte[] pattern) {
		int sourceLength = source.size();
		int patternLength = pattern.length;
		int offset;
		TreeMap<Integer, byte[]> answer = new TreeMap<>();

		int patternLoopIndex = 0;
		if (patternLength != 0) {
			// ... Loop until the end of the source.
			for (int index = 0; index < sourceLength; index++) {
				// ... Advance the pattern index the first character of the pattern matches.
				if (source.get(index) == pattern[patternLoopIndex]) {
					++patternLoopIndex;
				} else {
					// ... Re-iterate from the start of the pattern if in any point during the loop
					// the sequence of the source bytes doesn't match the pattern's byte sequence.
					if (source.get(index) == pattern[0]) {
						patternLoopIndex = 1;
					} else {
						patternLoopIndex = 0;
					}
				}
				// ... Store the results once the byte sequence from the source fully matches
				// the pattern's byte sequence.
				if (patternLoopIndex == patternLength) {
					offset = index - patternLength + 1;
					answer.put(offset, pattern);
					patternLoopIndex = 0;
				}
			}
		}
		return answer;
	}
}