package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.BytePatternMatcher;
import view.PatternMatcherView;

/**
 * Handles user interaction with listeners.
 * 
 * @author Dipesh B.C.
 * @version 1.0
 */
public class PatternMatcherController {
	// ... The Controller needs to interact with both the Model and View.
	private BytePatternMatcher model;
	private PatternMatcherView view;

	// ... Constructor.
	public PatternMatcherController(BytePatternMatcher model, PatternMatcherView view) {
		// ... bridge connection between model and view
		this.model = model;
		this.view = view;

		// ... Add listeners to the view.
		this.view.addMenuItemLoadFromFileListener(new LoadFromFileListener());
		this.view.addMenuItemLoadFromDirListener(new LoadFromDirListener());
		this.view.addMenuItemAboutListener(new AboutListener());
		this.view.addMenuItemExitListener(new ExitListener());

		this.view.addLoadDataFromFileBtnListener(new LoadFromFileListener());
		this.view.addLoadDataFromDirBtnListener(new LoadFromDirListener());
		this.view.addLoadPatternBtnListener(new LoadPatternBtnListener());
		this.view.addSearchPatternBtnListener(new SearchPatternBtnListener());
	}

	// ... inner class for handling action listeners
	// ... When user requests to load a file.
	private class LoadFromFileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			File file = getFile();
			if (file != null) {
				try {
					model.setFile(file);
					model.setDirIsSelected(false);
					view.setLoadDataFromFileLabel(file.getName());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	// ... When user requests to load a dir.
	private class LoadFromDirListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser dirChooser = new JFileChooser(".");
			dirChooser.setDialogTitle("Select a directory");
			dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			dirChooser.setAcceptAllFileFilterUsed(false);

			if (dirChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				File dir = dirChooser.getSelectedFile();
				try {
					model.setDir(dir);
					model.setDirIsSelected(true);
					view.setLoadDataFromDirLabel(dir.getName());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	// ... When user requests for about information.
	private class AboutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			StringBuilder aboutMessage = new StringBuilder();
			aboutMessage.append("<html>").append("<body>").append("<h1>Byte Pattern Matcher</h1>").append(
					"<p>The sole purpose of this application is to search<br>for byte patterns with files and display<br>")
					.append("matched patterns if any with their offset.</p><br>")
					.append("<code>Author: Dipesh B.C. ID: 77202612</code>").append("</body>").append("</html>");
			JOptionPane.showOptionDialog(view, aboutMessage, "About", JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, new Object[] {}, null);
		}
	}

	// ... When user requests to exit the application.
	private class ExitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(1);
		}
	}

	// ... When user requests to load pattern from file.
	private class LoadPatternBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			File file = getFile();
			if (file != null) {
				try {
					model.setPattern(file);
					view.setLoadPatternLabel(file.getName());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	// ... When user requests to search for pattern.
	private class SearchPatternBtnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			TreeMap<Integer, byte[]> result = model.searchPattern();
			String pattern = "";
			String resultTxt = "";
			String hexValue = "";
			ArrayList<String> patternArr = new ArrayList<>();
			if (!result.isEmpty() && !model.getDirIsSelected()) {

				for (Map.Entry<Integer, byte[]> entry : result.entrySet()) {
					for (byte b : entry.getValue()) {
						patternArr.add(String.format("%x", b));
					}
					pattern = patternArr.toString().replaceAll("[,\\[\\]\\s+]", "");
					hexValue = String.format("0x%x", entry.getKey());
					resultTxt += "Pattern found: " + pattern + ", at offset: " + entry.getKey() + " (" + hexValue
							+ ") within the file.\n";
					patternArr.clear();
				}

			} else if (!result.isEmpty() && model.getDirIsSelected()) {
				TreeMap<String, TreeMap<Integer, byte[]>> foundPatterns = model.getFoundPatterns();
				for (Map.Entry<String, TreeMap<Integer, byte[]>> entries : foundPatterns.entrySet()) {
					System.out.println(entries.getKey());
					resultTxt += "Filename: " + entries.getKey() + "\n";
					for (Map.Entry<Integer, byte[]> entry : entries.getValue().entrySet()) {
						for (byte b : entry.getValue()) {
							patternArr.add(String.format("%x", b));
						}

						pattern = patternArr.toString().replaceAll("[,\\[\\]\\s+]", "");
						hexValue = String.format("0x%x", entry.getKey());
						resultTxt += "Pattern found: " + pattern + ", at offset: " + entry.getKey() + " (" + hexValue
								+ ") within the file.\n";
						patternArr.clear();
					}
				}
			}

			else {
				resultTxt = "Pattern not found.";
			}

			// ... update the view.
			view.setSearchResults(resultTxt);
		}
	}

	// ... helper functions
	private File getFile() {
		// ... Point the current directory.
		JFileChooser fileChooser = new JFileChooser(".");
		fileChooser.setDialogTitle("Select a file");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.showOpenDialog(null);
		fileChooser.setAcceptAllFileFilterUsed(false);
		// ... Get the selected file
		File file = fileChooser.getSelectedFile();

		return file;
	}

}
