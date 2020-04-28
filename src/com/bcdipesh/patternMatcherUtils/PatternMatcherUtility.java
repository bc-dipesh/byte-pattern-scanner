package com.bcdipesh.patternMatcherUtils;

import static java.util.stream.Collectors.toCollection;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * This class consists exclusively of static methods that operate on files,
 * directories, or other types of files.
 * 
 * @author Dipesh B.C.
 * @version 1.0
 *
 */
public interface PatternMatcherUtility {

	/**
	 * Reads a directory. This function reads a directory containing multiple files
	 * as bytes.
	 *
	 * @param dir The directory containing multiple files.
	 * @return Returns a TreeMap with String that represents the file name inside
	 *         directory as key, and an ArrayList of Byte that represents the bytes
	 *         of content within the file as value.
	 * @throws IOException Throws an {@link IOException} if the file passed to it is
	 *                     invalid.
	 */
	public static TreeMap<String, ArrayList<Byte>> readDirectory(File dir) throws IOException {
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
	public static ArrayList<Byte> readFile(File file) throws IOException {
		ArrayList<Byte> fileBytes = new ArrayList<>();
		byte[] bytes = Files.readAllBytes(Paths.get(file.getPath()));

		for (byte b : bytes) {
			fileBytes.add(b);
		}

		return fileBytes;
	}

	/**
	 * Reads a pattern file. This function reads a pattern file containing
	 * pattern/patterns to be searched.
	 *
	 * @param patternFile The file to read.
	 * @return Returns an ArrayList of byte[] containing list of pattern/patterns to
	 *         be searched.
	 * @throws IOException Throws an {@link IOException} if the file passed to it is
	 *                     invalid.
	 */
	public static ArrayList<byte[]> readPatternFile(File patternFile) throws IOException {
		// ... Using try-with-resources so the file handle for patternFile gets closed
		// properly.
		try (Stream<String> line = Files.lines(Paths.get(patternFile.getPath()))) {
			// ... convert to ArrayList<byte[]> and return.
			return line.map(PatternMatcherUtility::splitStringUsingSpace).map(PatternMatcherUtility::convertToByteArray)
					.collect(toCollection(ArrayList::new));
		}
	}

	/**
	 * Lists only file {@link Path} contained within a directory.
	 * 
	 * @param dir The directory containing multiple files
	 * @return Returns a {@link ArrayList} of {@link Path} representing the
	 *         {@link Path} to the file.
	 * @throws IOException Throws and {@link IOException} if the file passed to it
	 *                     is invalid.
	 */
	public static ArrayList<Path> listFilesWithinDirectory(File dir) throws IOException {

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
	 * Gets the total file count present inside a directory. Note: This function
	 * only counts the total files present in a directory of depth 1, i.e., it won't
	 * count files inside sub-directories.
	 * 
	 * @return Returns the total file count inside a directory.
	 */
	public static int countTotalFilesInDir(File dir) {
		return dir.listFiles().length;
	}

	/**
	 * Takes a String and splits it into String array using single space.
	 * 
	 * @param stringToSplit The String to split.
	 * @return Returns a String array containing the result of the split.
	 */
	public static String[] splitStringUsingSpace(String stringToSplit) {
		return stringToSplit.split("\\s");
	}

	/**
	 * Gets a byte array given a hexadecimal string. This function converts a
	 * hexadecimal string to return a byte array.
	 *
	 * @param hexString A string representing hexadecimal number.
	 * @return Returns a byte[] that represents the hexadecimal number.
	 */
	public static byte[] getByteArray(String hexString) {
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
	public static byte[] convertToByteArray(String[] line) {
		String lineString = Arrays.toString(line).replaceAll("[,\\s\\[\\]]", "");

		return getByteArray(lineString);
	}
}
