package com.bcdipesh.model;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		fileBytesArray = readFile(file);
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
		dirBytes = readDirectory(dir);
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
		patternListArray = readPatternFile(file);
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
		TreeMap<Integer, byte[]> answerHolder;

		for (Map.Entry<String, ArrayList<Byte>> entry : source.entrySet()) {
			answerHolder = indexOfPattern(entry.getValue(), patternList);
			answer.putAll(answerHolder);
			foundPatterns.put(new File(entry.getKey()).getName(), answerHolder);
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

	/**
	 * Reads a file. This function reads source file as bytes and stores them into
	 * fileBytes.
	 *
	 * @param file The source file to be read.
	 * @return Returns an ArrayList of Byte generated after reading the file passed
	 *         to it.
	 * @throws IOException Throws an {@link IOException} if the file passed to it is
	 *                     invalid.
	 */
	private ArrayList<Byte> readFile(File file) throws IOException {
		ArrayList<Byte> fileBytes = new ArrayList<>();
		try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
			int nextByte;
			byte currentByte;
			// ... loop until the end of the file and store the bytes.
			while ((nextByte = is.read()) != -1) {
				currentByte = (byte) nextByte;
				fileBytes.add(currentByte);
			}
		}

		return fileBytes;
	}

	/**
	 * Reads a directory. This function reads a directory containing multiple source
	 * files.
	 *
	 * @param dir The file containing multiple source files.
	 * @return Returns a TreeMap with String that represents the file name inside
	 *         directory as key, and an ArrayList of Byte that represents the bytes
	 *         of content within the file as value.
	 * @throws IOException Throws an {@link IOException} if the file passed to it is
	 *                     invalid.
	 */
	private TreeMap<String, ArrayList<Byte>> readDirectory(File dir) throws IOException {
		TreeMap<String, ArrayList<Byte>> dirBytes = new TreeMap<>();

		// ... Scan the directory.
		try (Stream<Path> walk = Files.walk(Paths.get(dir.getPath()))) {
			// ... Filter files and get the path of all the files within that directory.
			List<String> result = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());

			// ... Read the file specified by the path and store it.
			for (String path : result) {
				dirBytes.put(path, readFile(new File(path)));
			}

		}
		return dirBytes;
	}

	/**
	 * Reads a pattern file. This function reads a pattern file containing
	 * pattern/patterns to be searched.
	 *
	 * @param file The file to be read.
	 * @return Returns an ArrayList of byte[] containing list of pattern/patterns to
	 *         be searched.
	 * @throws IOException Throws an {@link IOException} if the file passed to it is
	 *                     invalid.
	 */
	private ArrayList<byte[]> readPatternFile(File file) throws IOException {
		try (Stream<String> line = Files.lines(Paths.get(file.getAbsolutePath()))) {
			return line.map(row -> row.split("\\s")).map(this::convertToByteArray)
					.collect(Collectors.toCollection(ArrayList::new));
		}
	}

	/**
	 * Gets a byte array given a hexadecimal string. This function converts a
	 * hexadecimal string to return a byte array.
	 *
	 * @param hexString A string representing hexadecimal number.
	 * @return Returns a byte[] that represents the hexadecimal number.
	 */
	private byte[] getByteArray(String hexString) {
		byte[] byteArray = new BigInteger(hexString, 16).toByteArray();
		if (byteArray[0] == 0) {
			byte[] output = new byte[byteArray.length - 1];
			System.arraycopy(byteArray, 1, output, 0, output.length);
			return output;
		}
		return byteArray;
	}

	/**
	 * Converts a string[] to byte array. This function converts a string[]
	 * representing hexadecimal numbers to a byte array.
	 *
	 * @param line A String array representing hexadecimal values.
	 * @return Returns a byte[] of hexadecimal values.
	 */
	private byte[] convertToByteArray(String[] line) {
		String lineString = Arrays.toString(line).replaceAll("[,\\s\\[\\]]", "");

		return getByteArray(lineString);
	}

}