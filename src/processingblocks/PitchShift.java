package processingblocks;

import java.util.List;

import javafx.scene.control.TextField;
import keyboard.gui.KeyboardModel;

/**
 * Handles the Pitch Shift processing block.
 * @author Kaamraan Kamaal
 *
 */
public class PitchShift extends ProcessingBlock{

	// the pitch shift parameter
	private static TextField shiftParameter;
	
	/**
	 * Creates the processing block with the given name.
	 * @param name
	 * 			The name of the block. Pitch Shift in this case.
	 */
	public PitchShift(String name) {
		super(name);
		shiftParameter = new TextField();
		shiftParameter.setPromptText("Pitch Shift Parameter");
		shiftParameter.setMinWidth(30);
		blkParamBox.getChildren().add(shiftParameter);
	}
	
	/**
	 * Returns the pitch shift parameter.
	 * @return
	 * 		The pitch shift parameter.
	 */
	private static int getShiftParameter() {
		try {
			return (Integer.parseInt(shiftParameter.getText()));
		} catch (NumberFormatException e) {
			shiftParameter.clear();
		}
		return 0;
	}
	
	/**
	 * Returns the value of the shift parameter.
	 * @return
	 * 		The shift parameter.
	 */
	public int getParameter() {
		try {
			return (Integer.parseInt(shiftParameter.getText()));
		} catch (NumberFormatException e) {
			shiftParameter.clear();
			return 0;
		}
	}
	
	/**
	 * Returns the shifted note corresponding to the given note.
	 * @param note
	 * 			The original note.
	 * @return
	 * 			The shifted note.
	 */
	public static int getPitchShiftedNote(int note) {
		int shiftedNote = note + getShiftParameter();
		if (! KeyboardModel.getMidiOfScale().contains(shiftedNote)) {
			shiftedNote = getClosestValue(shiftedNote, 
							KeyboardModel.getMidiOfScale());
		}
		return shiftedNote;
	}
	
	/**
	 * Returns the shifted note closest to the valid notes in the scale.
	 * @param note
	 * 			The shifted note.
	 * @param notes
	 * 			The list of all the MIDI notes in the scale.
	 * @return
	 * 			The shifted note rounded to the closest valid note.
	 */
	private static int getClosestValue(int note, List<Integer> notes) {
		int distance = Math.abs(note - notes.get(0));
		int shiftedNote = notes.get(0);
		for (int i = 1; i < notes.size(); i++) {
			int tempDistance = Math.abs(note - notes.get(i));
			if (tempDistance < distance)
				shiftedNote = notes.get(i);
			distance = tempDistance;
		}
		return shiftedNote;
	}
}
