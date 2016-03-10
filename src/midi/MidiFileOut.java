package midi;

import java.io.File;
import java.io.IOException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import launcher.SynthUI;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;

/**
 * Handles the output MIDI file. That is, saves the notes being played
 * to a standard MIDI file.
 * @author Kaamraan Kamaal
 *
 */
public class MidiFileOut {
	
	// the file to save the notes to
	private static File midiOutFile;
	// the pattern stores all the notes being played.
	private static Pattern pattern = new Pattern();
	
	/**
	 * Adds the given string to the pattern (which is finally saved to
	 * the MIDI file)
	 * @param note
	 * 			The note on.
	 */
	public static void addToFile(String note) {
		pattern.add(note);
	}
	
	/**
	 * Adds the pattern to the pattern. Which is finally saved
	 * to the MIDI file.
	 * @param patternToAdd
	 * 			The pattern to add to the current pattern.
	 * (This function is used to store the metronome)
	 */
	public static void addPatternToFile(Pattern patternToAdd) {
		pattern.add(patternToAdd);
	}
	
	/**
	 * Brings up a FileChooser for the user to create a new file where
	 * they would like to store their MIDI data.
	 */
	public static void getFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(
						new ExtensionFilter("Midi Files (.mid)", "*.mid"));
		midiOutFile = fileChooser.showSaveDialog(SynthUI.getStage());
	}
	
	/**
	 * Saves all the notes to the MIDI file.
	 */
	public static void saveFile() {
		if (midiOutFile != null) {
			try {
				MidiFileManager.savePatternToMidi(
									pattern.getPattern(), midiOutFile);
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Sorry! Couldn't save file");
				alert.setContentText("Oops! There was an error in "
											+ "saving to MIDI file.\n"
											+ "Please go back and try again");
				alert.showAndWait();
			}
		}
	}
}
