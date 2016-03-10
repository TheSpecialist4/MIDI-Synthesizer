package keyboard.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the list of all the scales available to the user to choose from.
 * @author Kaamraan Kamaal
 *
 */
public class AllScales {
	// List of all the scales available
	private List<Scale> allScales;
	
	/**
	 * Creates a new empty list of scales.
	 */
	public AllScales() {
		allScales = new ArrayList<Scale>();
	}
	
	/**
	 * Adds a given scale to the list.
	 * @param scale
	 * 			The scale to be added to the list of scales.
	 */
	public void addScale(Scale scale) {
		Scale copyOfScale = new Scale();
		copyOfScale.setMode(scale.getMode()); 
		copyOfScale.setRoot(scale.getRoot());
		copyOfScale.setNotes(scale.getNotes());
		allScales.add(copyOfScale);
	}
	
	/**
	 * Returns the list of all the modes available.
	 * @return
	 * 		The list of the modes available.
	 */
	public List<String> getAllModes() {
		List<String> modes = new ArrayList<String>();
		for (int i = 0; i < allScales.size(); i++) {
			modes.add(allScales.get(i).getMode());
		}
		return modes;
	}
	
	/**
	 * Returns the list of all the roots available.
	 * @return
	 * 		The list of all the roots available.
	 */
	public List<String> getAllRoots() {
		List<String> roots = new ArrayList<String>();
		for (int i = 0; i < allScales.size(); i++) {
			roots.add(allScales.get(i).getRoot());
		}
		return roots;
	}
	
	/**
	 * Returns the notes corresponding to the scale given by the
	 * mode and the root.
	 * @param mode
	 * 			The mode of the scale to be chosen.
	 * @param root
	 * 			The root of the scale to be chosen.
	 * @return
	 * 			The list of the notes in the selected scale.
	 */
	public List<Note> getNotes(String mode, String root) {
		List<Note> notes = new ArrayList<Note>();
		for (int i = 0; i < allScales.size(); i++) {
			if ((mode.equals(allScales.get(i).getMode())) &&
					root.equals(allScales.get(i).getRoot())) {
				notes = allScales.get(i).getNotes();
				break;
			}
		}
		return notes;
	}
}
