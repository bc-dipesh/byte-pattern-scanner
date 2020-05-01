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

package main.java.com.bcdipesh;

import main.java.com.bcdipesh.controller.AppController;
import main.java.com.bcdipesh.model.BytePatternMatcher;
import main.java.com.bcdipesh.view.AppView;

/**
 * This class is the entry point for the application to run.
 *
 * @author Dipesh B.C.
 * @version 1.1
 */
public final class BytePatternMatcherApp {

  /** Don't let anyone instantiate this class. */
  private BytePatternMatcherApp() {}

  /**
   * This function will be called automatically to run the application.
   *
   * @param args Command-line arguments
   */
  public static void main(final String[] args) {
    // ... Create objects of model and view for the application to use.
    final BytePatternMatcher model = new BytePatternMatcher();
    final AppView view = new AppView();

    // ... Passing the objects to the controller and linking them.
    new AppController(model, view);

    // ... Display the application.
    view.setVisible(true);
  }
}
