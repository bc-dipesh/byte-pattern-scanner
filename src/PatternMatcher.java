import java.util.List;

/**
 * Provides pattern matching operations.
 * 
 * @author Dipesh B.C.
 * @version 1.0
 *
 */
public interface PatternMatcher {
	/**
	 * Returns true if the srouceData contains the pattern is it.
	 * 
	 * @param source  The array to be searched in.
	 * @param pattern The pattern to be searched inside the source.
	 * @return true if the source contains the pattern.
	 */
	boolean contains(List source, List pattern);

	/**
	 * Returns the index of the matched pattern present inside dataToScan.
	 * 
	 * @param source  The array to be scanned.
	 * @param pattern The pattern to be searched inside the source.
	 * @return Returns a positive integer which is the value of the index where the
	 *         pattern was found. Otherwise -1 if no pattern is matched.
	 */
	int indexOfPattern(List source, List pattern);
}
