package processingblocks;

import keyboard.gui.KeyboardModel;

/**
 * Handles the chordify processing block.
 * @author Kaamraan Kamaal
 *
 */
public class Chordify extends ProcessingBlock{
	
	// the chord corresponding to the current on note
	private static Chord chord;
	
	/**
	 * Initialises the block.
	 * @param name
	 * 			The name of the block. Chordify in this case.
	 */
	public Chordify(String name) {
		super(name);
	}

	/**
	 * Makes the chord for the note on.
	 * @param firstNote
	 * 			The note on.
	 */
	public static void makeChord(int firstNote) {
		chord = new Chord(firstNote);
	}
	
	/**
	 * Returns the first note of the chord.
	 * @return
	 * 		The first note of the chord.
	 */
	public static int getFirstNote() {
		return chord.getFirstNote();
	}
	
	/**
	 * Returns the second note in the chord corresponding to the first note.
	 * Musically, returns the third note in the scale after the first note.
	 * @param firstNote
	 * 			The root note of the chord.
	 * @return
	 * 			The second note in the chord. (The third note in the scale)
	 */
	public static int getSecondNote(int firstNote) {
		return chord.getSecondNote(firstNote);
	}
	
	/**
	 * Returns the third note in the chord corresponding to the first note.
	 * Musically, this is the fifth note after the first note in the scale.
	 * @param firstNote
	 * 			The root note of the chord.
	 * @return
	 * 		The third note in the chord.
	 */
	public static int getThirdNote(int firstNote) {
		return chord.getThirdNote(firstNote);
	}
	
	/**
	 * Creates a chord for the given root note.
	 * @author Kaamraan Kamaal
	 *
	 */
	private static class Chord {
		
		// the first note in the chord
		private int firstNote;
		// the second note in the chord
		private int secondNote;
		// the third note in the chord
		private int thirdNote;
		
		/**
		 * Creates a chord based on the root note
		 * @param firstNote
		 * 			The root note of the chord.
		 */
		public Chord(int firstNote) {
			this.firstNote = firstNote;
			makeChord();
		}
		
		/**
		 * Returns the root note of the chord.
		 * @return
		 * 		The first note in the chord.
		 */
		public  int getFirstNote() {
			return firstNote;
		}
		
		/**
		 * Makes the chord.
		 */
		private void makeChord() {
			int rootNoteIndex = KeyboardModel.getMidiOfScale().
												indexOf(firstNote);
			try {
				secondNote = KeyboardModel.getMidiOfScale().
												get(rootNoteIndex + 2);
			} catch (IndexOutOfBoundsException e) {
				secondNote = firstNote;
			}
			try {
				thirdNote = KeyboardModel.getMidiOfScale().
													get(rootNoteIndex + 4);
			} catch (IndexOutOfBoundsException e) {
				thirdNote = firstNote;
			}
		}
		
		/**
		 * Returns the second note of the chord
		 * @param rootNote
		 * 			The root note of the chord.
		 * @return
		 * 			The second note in the chord.
		 */
		public int getSecondNote(int rootNote) {
			if (rootNote == this.firstNote)
				return secondNote;
			else
				return -1;
		}
		
		/**
		 * Returns the third note in the chord.
		 * @param rootNote
		 * 			The root note of the chord.
		 * @return
		 * 			The third note in the chord.
		 */
		public int getThirdNote(int rootNote) {
			if (rootNote == firstNote)
				return thirdNote;
			else
				return -1;
		}
	}
}
