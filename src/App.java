import java.io.IOException;

import controller.PatternMatcherController;
import model.BytePatternMatcher;
import view.PatternMatcherView;

/**
 * The main class to initialize and bind {@link BytePatternMatcher},
 * {@link PatternMatcherController} together.
 * 
 * @author Dipesh B.C.
 * @version 1.0
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		BytePatternMatcher model = new BytePatternMatcher();
		PatternMatcherView view = new PatternMatcherView();
		PatternMatcherController controller = new PatternMatcherController(model, view);
		view.setVisible(true);
	}
}
