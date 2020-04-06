package model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides byte pattern matching operations.
 * 
 * @author Dipesh B.C.
 * @version 1.0
 *
 */
public class BytePatternMatcher {
	// ... Instance variables
	private ArrayList<Byte> fileBytesArray;
	private ArrayList<byte[]> patternListArray;
	private TreeMap<String, ArrayList<Byte>> dirBytes;
	private TreeMap<String, TreeMap<Integer, byte[]>> foundPatterns;
	private boolean dirIsSelected;

	// .. Constructor
	public BytePatternMatcher() {
		fileBytesArray = new ArrayList<>();
		patternListArray = new ArrayList<>();
		dirBytes = new TreeMap<>();
		foundPatterns = new TreeMap<>();
		dirIsSelected = false;
	}

	// ... user functions
	public TreeMap<Integer, byte[]> searchPattern() {
		return dirIsSelected ? indexOfPattern(dirBytes, patternListArray)
				: indexOfPattern(fileBytesArray, patternListArray);
	}

	// ... Setters
	public void setFile(File file) throws FileNotFoundException, IOException {
		fileBytesArray = readFile(file);
	}

	public void setDir(File dir) throws FileNotFoundException, IOException {
		dirBytes = readDirectory(dir);
	}

	public void setPattern(File file) throws IOException {
		patternListArray = readPatternFile(file);
	}

	public void setDirIsSelected(boolean isSelected) {
		dirIsSelected = isSelected;
	}

	// ... Getters
	public TreeMap<String, TreeMap<Integer, byte[]>> getFoundPatterns() {
		return foundPatterns;
	}

	public boolean getDirIsSelected() {
		return dirIsSelected;
	}

	// ... helper functions
	private TreeMap<Integer, byte[]> indexOfPattern(TreeMap<String, ArrayList<Byte>> source,
			ArrayList<byte[]> patternList) {
		TreeMap<Integer, byte[]> answer = new TreeMap<>();
		TreeMap<Integer, byte[]> answerHolder = new TreeMap<>();

		for (Map.Entry<String, ArrayList<Byte>> entry : source.entrySet()) {
			answerHolder = indexOfPattern(entry.getValue(), patternList);
			answer.putAll(answerHolder);
			foundPatterns.put(new File(entry.getKey()).getName(), answerHolder);
		}

		return answer;
	}

	private TreeMap<Integer, byte[]> indexOfPattern(ArrayList<Byte> source, ArrayList<byte[]> patternList) {
		TreeMap<Integer, byte[]> answer = new TreeMap<>();
		TreeMap<Integer, byte[]> answerHolder = new TreeMap<>();

		for (byte[] pattern : patternList) {
			answerHolder = indexOfPattern(source, pattern);
			answer.putAll(answerHolder);
		}

		return answer;
	}

	private TreeMap<Integer, byte[]> indexOfPattern(ArrayList<Byte> source, byte[] pattern) {
		int sourceLength = source.size();
		int patternLength = pattern.length;
		int offset = 0;
		TreeMap<Integer, byte[]> answer = new TreeMap<>();

		int patternLoopIndex = 0;
		if (patternLength != 0) {
			for (int index = 0; index < sourceLength; index++) {
				if (source.get(index) == pattern[patternLoopIndex]) {
					++patternLoopIndex;
				} else {
					if (source.get(index) == pattern[0]) {
						patternLoopIndex = 1;
					} else {
						patternLoopIndex = 0;
					}
				}
				if (patternLoopIndex == patternLength) {
					offset = index - patternLength + 1;
					answer.put(offset, pattern);
					patternLoopIndex = 0;
				}
			}
		}
		return answer;
	}

	private ArrayList<Byte> readFile(File file) throws FileNotFoundException, IOException {
		ArrayList<Byte> fileBytes = new ArrayList<>();
		try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
			int nextByte;
			byte currentByte;
			// ... loop and store the byte in fileBytes
			while ((nextByte = is.read()) != -1) {
				currentByte = (byte) nextByte;
				fileBytes.add(currentByte);
			}
		}

		return fileBytes;
	}

	private TreeMap<String, ArrayList<Byte>> readDirectory(File dir) throws IOException {
		TreeMap<String, ArrayList<Byte>> dirBytes = new TreeMap<>();

		try (Stream<Path> walk = Files.walk(Paths.get(dir.getPath()))) {
			List<String> result = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
			for (String path : result) {
				dirBytes.put(path, readFile(new File(path)));
			}

		}
		return dirBytes;
	}

	private ArrayList<byte[]> readPatternFile(File file) throws IOException {
		try (Stream<String> line = Files.lines(Paths.get(file.getAbsolutePath()))) {
			ArrayList<byte[]> patternList = line.map(row -> row.split("\\s")).map(row -> convertToByteArray(row))
					.collect(Collectors.toCollection(ArrayList::new));
			return patternList;
		}
	}

	private int toDigit(char hexChar) {
		int digit = Character.digit(hexChar, 16);
		if (digit == -1) {
			throw new IllegalArgumentException("Invalid Hexadecimal Character: " + hexChar);
		}
		return digit;
	}

	private byte hexToByte(String hexString) {
		int firstDigit = toDigit(hexString.charAt(0));
		int secondDigit = toDigit(hexString.charAt(1));

		return (byte) ((firstDigit << 4) + secondDigit);
	}

	private byte[] decodeHexString(String hexString) {
		if (hexString.length() % 2 == 1) {
			throw new IllegalArgumentException("Invalid hexadecimal String supplied.");
		}
		byte[] bytes = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i += 2) {
			bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
		}
		return bytes;
	}

	private byte[] convertToByteArray(String[] line) {
		String lineString = Arrays.toString(line).replaceAll("[,\\s\\[\\]]", "");

		return decodeHexString(lineString);
	}

}