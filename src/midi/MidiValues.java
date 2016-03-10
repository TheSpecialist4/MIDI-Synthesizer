package midi;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores two maps (indirect form of bidi maps) to map the midi values
 * and the String representation of the notes.
 * This class uses a singleton design.
 * @author Kaamraan Kamaal
 *
 */
public class MidiValues {
	
	// the only instance of this class available to use.
	private static final MidiValues INSTANCE = new MidiValues();
	// map of string to the MIDI value
	private static Map<String, Integer> midiMap;
	// map of MIDI value to the string
	private static Map<Integer, String> midiToStringMap;
	
	/**
	 * Creates an instance of the map.
	 */
	private MidiValues() {
		midiMap = new HashMap<>();
		midiToStringMap = new HashMap<>();
		populateMap();
	}
	
	/**
	 * Returns the available instance.
	 * @return
	 * 		The instance that can be accessed by the outside classes.
	 */
	public static MidiValues getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Populates the Hash map with the note name (String),
	 * corresponding to its MIDI note value. Ex: C7 -> 84
	 */
	/*
	 * The algorithm used is inspired from the original found on:
	 * 	http://stackoverflow.com/questions/712679/convert-midi-note-
	 * 										numbers-to-name-and-octave
	 */
	private void populateMap() {
		String notes = "C C#D D#E F F#G G#A A#B ";
        int octave;
        String note;
        for (int noteNum = 12; noteNum <= 95; noteNum++) {
            octave = noteNum / 12;
            note = notes.substring(
                (noteNum % 12) * 2,
                (noteNum % 12) * 2 + 2);
            note += octave;
            midiMap.put(note.replace(" ",""), noteNum);
            midiToStringMap.put(noteNum, note.replace(" ", ""));
        }
	}
	
	/**
	 * Returns the MIDI value corresponding to the note.
	 * @param noteString
	 * 			The string representation of the note
	 * @return
	 * 			The MIDI value of the note.
	 */
	public static int getMidiValue(String noteString) {
		if (noteString.length() > 3) {
			noteString = noteString.substring(0, 3);
		}
		noteString.replace('+', ' ');
		noteString.trim();
		return midiMap.get(noteString);
	}
	
	/**
	 * Returns the string representation of the note corresponding
	 * to the MIDI value.
	 * @param midiNote
	 * @return
	 */
	public static String getStringForMidi(int midiNote) {
		return midiToStringMap.get(midiNote);
	}
}
