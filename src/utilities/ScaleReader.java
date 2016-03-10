package utilities;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.FileChooser;
import keyboard.util.AllScales;
import keyboard.util.Scale;
import launcher.SynthUI;

import com.opencsv.CSVReader;

/**
 * Provides a method to read in a list of scales from a CSV file.
 * @author Kaamraan Kamaal
 *
 */
public class ScaleReader {
	
	private static File scale;
	
	/**
	 * Parses through the CSV file to add the list of scales.
	 * The scales are parsed, and added to allScales.
	 * @param allScales 
	 * 			The scales read from the scales.csv file
	 * 			are added to this.
	 */
	public static void getScales(AllScales allScales) {
		// CSVReader to parse through the CSV file
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(scale));
			String [] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				Scale scale = new Scale();
				scale.setMode(nextLine[0]);
				scale.setRoot(nextLine[1]);
				// A temporary list to hold the notes in the scale
				List<String> notes = new ArrayList<String>();
				for (int i = 1; i < nextLine.length; i++) {
					if (!nextLine[i].equals(""))
						notes.add(nextLine[i]);
				}
				// The notes are added to the scale
				scale.setNotesString(notes);
				// The scale is added to the list of all the scales
				allScales.addScale(scale);
				
			}
		} catch (Exception e) {
			e.printStackTrace();;
		}
		finally {
			try {
				reader.close();
			}catch (IOException e) {
				e.getMessage();
			}
		}
	}
	
	public static void getFile() {
		FileChooser fileChooser = new FileChooser();
		scale = fileChooser.showOpenDialog(SynthUI.getStage());
	}
}
