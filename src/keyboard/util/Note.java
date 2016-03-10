package keyboard.util;

/**
 * Stores a musical note.
 * A note is represented by name of the note (A, C#, G etc),
 * and the octave of that note.
 * @author Kaamraan Kamaal
 *
 */
public class Note {
	// The name of the note
	private String note;
	// The octave of the note
	private int octave;
	
	/**
	 * Creates a new empty note, with the default octave as 1.
	 */
	public Note() {
		note = new String();
		octave = 1;
	}
	
	/**
	 * Creates a new note with the specified name of the note.
	 * The default octave of the note created is 1.
	 * @param note
	 * 			The name of the note to be created.
	 */
	public Note(String note) {
		this.note = note;
		this.removeSharpNote();
		this.octave = 1;
	}
	
	/**
	 * Creates a new note with the given note name, and the given octave.
	 * @param note
	 * 			The name of the note.
	 * @param octave
	 * 			The octave of the note.
	 */
	public Note(String note, int octave) {
		this.note = note;
		this.removeSharpNote();
		this.octave = octave;
	}
	
	/**
	 * Increases the octave of the note.
	 */
	public void incOctave() {
		if (this.octave < 7) {
			this.octave += 1;
		}
	}
	
	/**
	 * Decreases the octave of the note.
	 */
	public void decOctave() {
		if (this.octave > 1) {
			this.octave -= 1;
		}
	}
	
	/**
	 * Returns the string representation of the note.
	 * @return
	 * 		The string representation of the note.
	 */
	public String getNote() {
		return this.note;
	}
	
	/**
	 * Sets the octave of the note to the given octave.
	 * @param octave
	 * 			The octave value to be set.
	 */
	public void setOctave(int octave) {
		this.octave = octave;
	}
	
	/**
	 * Returns the octave of the note.
	 * @return
	 * 		The octave of the note.
	 */
	public int getOctave() {
		return this.octave;
	}
	
	/**
	 * Removes the 'flat' representation from the note.
	 * That is, A#/Bb becomes A#.
	 */
	private void removeSharpNote() {
		if (this.note.length() > 2) {
			this.note = this.note.substring(0, 2);
		}
	}
}
