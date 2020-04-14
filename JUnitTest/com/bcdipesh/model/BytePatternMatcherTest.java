/**
 * 
 */
package com.bcdipesh.model;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * This class is purely used for the purpose of Unit Testing.
 * 
 * @author Dipesh B.C.
 *
 */
public class BytePatternMatcherTest {

	/**
	 * Test method for
	 * {@link com.bcdipesh.model.BytePatternMatcher#searchPattern()}. This will test
	 * the main functionality of the application i.e, to search for byte patterns
	 * within a file or directory.
	 * 
	 * @throws IOException Throws IOException if the provided file is not present.
	 */
	@Test
	public void testSearchPattern() throws IOException {
		// ... Get the object of the class to be tested.
		BytePatternMatcher tester = new BytePatternMatcher();

		// ... Setting the object state.

		// ... Test - 1 for individual file.
		tester.setFile(new File("./files/test1.txt"));
		tester.setPattern(new File("./patterns/patterns.txt"));
		tester.setIsFileSelected(true);

		// ... Test the output
		assertEquals("Size of output must be 2", 2, tester.searchPattern().size());

		// ... Test - 2 for directory.
		tester.setDir(new File("./files"));
		tester.setPattern(new File("./patterns/patterns_ex1.txt"));
		tester.setIsDirectorySelected(true);

		assertEquals("Size of output must be 9", 9, tester.searchPattern().size());
	}

}
