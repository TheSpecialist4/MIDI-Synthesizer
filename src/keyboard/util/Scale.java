package keyboard.util;

import java.util.ArrayList;
import java.util.List;

import midi.MidiValues;

/**
 * The instances of this class store the information about a scale.
 * That is, it stores the type of the scale, the root note, and a list of all
 * the valid notes in that scale.
 * @author Kaamraan Kamaal
 *
 */
public class Scale {
	
	private Note root;
	private String mode;
	private List<Note> notes;
	//stores the midi values of the first octave of the notes
	private List<Integer> midis;
	
	/**
	 * The default constructor to initialise the Scale.
	 */
	public Scale() {
		mode = new String();
		root = new Note();
		notes = new ArrayList<Note>();
		midis = new ArrayList<>();
	}
	
	/**
	 * Creates a new Scale for the given mode, root and notes
	 * @param mode The mode of the scale
	 * @param root The root note of the scale
	 * @param notes All the valid notes in the scale
	 */
	public Scale(String mode, String root, List<String> notes) {
		this.mode = mode;
		this.root = new Note(root);
	//	this.root.setOctave(octave);
		// A copy of a note in notes is made and added to this.note
		Note tempNote = new Note();
		for (int i = 0; i < notes.size(); i++) {
			tempNote = new Note(notes.get(i));
		//	tempNote.setOctave(octave);
			this.notes.add(tempNote);
		}
	}
	
	/**
	 * Returns the mode of the scale
	 * @return The mode of the scale
	 */
	public String getMode() {
		return this.mode;
	}
	
	/**
	 * Returns the String representation of theh root note of the scale.
	 * @return The root note of the scale
	 */
	public String getRoot() {
		return this.root.getNote();
	}
	
	/**
	 * Returns the Note form of the root note.
	 * @return
	 * 		The root note.
	 */
	public Note getRootNote() {
		return root;
	}
	
	/**
	 * Returns the list of all the notes in the scale
	 * @return A copy of this.notes
	 */
	public List<Note> getNotes() {
		return this.notes;
	}
	
	/**
	 * Returns the MIDI values of the notes in the scale.
	 * @return
	 * 		The MIDI values of the notes in the scale.
	 */
	public List<Integer> getMidiValues() {
		List<Integer> copyOfMidis = new ArrayList<>();
		for (int i = 0; i < midis.size(); i++)
			copyOfMidis.add(midis.get(i));
		return copyOfMidis;
	}
	
	/**
	 * Sets the root of the scale to the given root.
	 * @param root
	 * 			The root of the scale.
	 */
	public void setRoot(String root) {
		this.root = new Note(root);
	}
	
	/**
	 * Sets the notes of the scale to the given list of notes.
	 * @param notes
	 * 			The notes in the scale.
	 */
	public void setNotes(List<Note> notes) {
		this.notes.clear();
		for (int i = 0; i < notes.size(); i++) {
			this.notes.add(notes.get(i));
		}
		setMidiValues();
	}
	
	/**
	 * Sets the notes of the scale to the given list of notes.
	 * @param notes
	 * 			The String representation of the notes.
	 */
	public void setNotesString(List<String> notes) {
		this.notes.clear();
		for (int i = 0; i < notes.size(); i++) {
			this.notes.add(new Note(notes.get(i)));
		}
		setMidiValues();
	}
	
	/**
	 * Sets the MIDI values of the notes in the scale.
	 */
	private void setMidiValues() {
		midis.clear();
		int noOfNotes = notes.size() - 1;
		for (int i = 1; i <= 7; i++) {
			for (int j = 0; j < noOfNotes; j++) {
				String note = notes.get(j).getNote();
				note += i;
				try {
					midis.add(MidiValues.getMidiValue(note));
				}catch (Exception e){}
			}
		}
	}
	/**
	 * Sets the mode of the scale.
	 * @param mode
	 * 			The mode of the scale.
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
}
