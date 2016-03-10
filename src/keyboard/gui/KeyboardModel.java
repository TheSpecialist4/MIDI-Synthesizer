package keyboard.gui;

import java.util.List;

import keyboard.util.AllScales;
import keyboard.util.Note;
import keyboard.util.Scale;
import utilities.ScaleReader;

/**
 * The model of the Keyboard input option.
 * Stores all the data required to make the virtual keyboard usable.
 * That is, stores the information about the current scale, and the current
 * octave of the scale.
 * @author Kaamraan Kamaal
 *
 */
public class KeyboardModel {
	
	// the current scale chosen
	private static Scale currentScale;
	// list of all the scales available for the user
	private AllScales scales;
	// the position of the latest sent note
	private int currentPosition = 0;
	// the position of the note sent before the latest
	private int prevPosition = 0;
	
	// the octave of the last note on the screen
	private int endOctave = 1;
	// the octave of the first note on the screen
	private int startOctave = 1;
	
	/**
	 * Default constructor. Initializes all the fields.
	 */
	public KeyboardModel() {
		scales = new AllScales();
	}
	
	/**
	 * Loads the list of available scales from the file chosen by the user.
	 */
	public void loadScales() {
		ScaleReader.getFile();
		ScaleReader.getScales(scales);
	}
	
	/**
	 * Returns the list of all the scales (AllScales) available to the user.
	 * @return
	 * 		The list of scales ('AllScales' datatype) available.
	 */
	public AllScales getAllScales() {
		return scales;
	}
	
	/**
	 * Returns the midi values corresponding to the notes in the scale.
	 * @return
	 * 		The List of midi values of the notes in the scale.
	 */
	public static List<Integer> getMidiOfScale() {
		if (currentScale != null) {
			return currentScale.getMidiValues(); 
		}
		return null;
	}
	
	/**
	 * Sets the scale according to the given root and mode.
	 * @param root
	 * 			The root of the scale to be set.
	 * @param mode
	 * 			The mode of the scale to be set.
	 */
	public void setScale(String root, String mode) {
		if (currentScale != null) {
			endOctave = currentScale.getNotes().get(0).getOctave();
			startOctave = endOctave;
		}
		currentScale = new Scale();
		currentScale.setMode(mode);
		currentScale.setRoot(root);
		currentScale.getRootNote().setOctave(endOctave);
		currentScale.setNotes(scales.getNotes(mode, root));
		for (Note note : currentScale.getNotes()) {
			note.setOctave(endOctave);
		}
		prevPosition = 0;
		currentPosition = 0;
	}
	
	/**
	 * Returns the next note to be displayed on the screen.
	 * @return
	 * 		The next note on to be displayed. That is, the 16th note
	 * 		of the scale.
	 */
	public String getNextNote() {
		String nextNote = new String();
		if (currentPosition == currentScale.getNotes().size() - 2) {
			Note tempNote = currentScale.getNotes().get(currentPosition);
			tempNote.setOctave(endOctave);
			nextNote = tempNote.getNote() + endOctave;
			prevPosition = currentPosition;
			currentPosition = 0;
			if (endOctave < 7)
				endOctave++;
			return nextNote;
		}
		prevPosition = currentPosition;
		nextNote = currentScale.getNotes().get(currentPosition++).getNote();
		nextNote += endOctave;
		return nextNote;
	}
	
	/**
	 * Returns the note before the current first note on the screen.
	 * @param currentNote
	 * 			The first note currently on the screen.
	 * @return
	 * 			The note before the current note.
	 */
	public String getPrevNote(String currentNote) {
		setStartOctave(currentNote);
		String prevNote = new String();
		if (currentNote.contains("#"))
			currentNote = currentNote.substring(0, currentNote.indexOf('#')+1);
		else
			currentNote = currentNote.substring(0, 1);
		for (int i = 0; i < currentScale.getNotes().size(); i++) {
			if (currentScale.getNotes().get(i).getNote().equals(currentNote)) {
				if (i == 0) {
					if (startOctave > 1)
						startOctave--;
					i = currentScale.getNotes().size() - 2;
				}
				prevNote = currentScale.getNotes().get(i - 1).getNote();
				break;
			}
		}
		return prevNote += startOctave;
	}
	
	/**
	 * Increments the octave of the scale by one.
	 */
	public void incOctave() {
		currentPosition = prevPosition;
		endOctave--;
		for (Note note : currentScale.getNotes()) {
			note.incOctave();
		}
		currentScale.getRootNote().incOctave();
	}
	
	/**
	 * Decrements the octave of the scale by one.
	 */
	public void decOctave() {
		currentPosition = prevPosition;
		endOctave -= 3;
		for (Note note : currentScale.getNotes()) {
			note.decOctave();
		}
		currentScale.getRootNote().decOctave();
	}
	
	/**
	 * Sets the startOctave.
	 * @param keyName
	 * 			The text on the first key on the screen.
	 */
	public void setStartOctave(String keyName) {
		if (!(keyName == null)) {
			if (keyName.contains("#"))
				startOctave = Integer.parseInt(keyName.charAt(
									keyName.indexOf('#') + 1) + "");
			else
				startOctave = Integer.parseInt(keyName.charAt(1) + "");
		}
	}
	
	/**
	 * Sets the lastOctave.
	 * @param keyName
	 * 			The text on the last key on the screen.
	 */
	public void setEndOctave(String keyName) {
		if (!(keyName == null)) {
			if(!(keyName.contains("*")))
				endOctave = Integer.parseInt(
					keyName.charAt(keyName.indexOf('\n') - 1) + "");
			else
				endOctave = Integer.parseInt(
						keyName.charAt(keyName.indexOf('*') - 1) + "");
		}
	}
	
	/**
	 * Returns the root of the scale.
	 * @return
	 * 		The root of the scale.
	 */
	public String getRoot() {
		return currentScale.getRoot();
	}
}
