package com.bcdipesh;

import com.bcdipesh.controller.AppController;
import com.bcdipesh.model.BytePatternMatcher;
import com.bcdipesh.view.AppView;

/**
 * This class represents the starting point for the application from where code
 * execution starts.
 *
 * @author Dipesh B.C.
 * @version 1.0
 */
public class App {
	public static void main(String[] args) {
		// ... Create objects of model and view for the app to use.
		BytePatternMatcher model = new BytePatternMatcher();
		AppView view = new AppView();

		// ... Passing the objects to the controller and linking them.
		new AppController(model, view);

		// ... Display the app.
		view.setVisible(true);
	}
}
