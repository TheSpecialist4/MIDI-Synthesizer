package keyboard.util;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.input.KeyCode;

/**
 * Contains the list of all the keys on the hardware keyboard, which correspond
 * to a key on the virtual piano.
 * @author Kaamraan Kamaal
 *
 */
public class KeyboardKeyList {
	
	// list of the KeyCodes for the 'valid' keys
	private List<KeyCode> keys;
	private KeyCode [] keyCodes = {
			KeyCode.Z, KeyCode.X, KeyCode.C, KeyCode.V, KeyCode.B, KeyCode.N,
			KeyCode.M, KeyCode.Q, KeyCode.W, KeyCode.E, KeyCode.R, KeyCode.T,
			KeyCode.Y, KeyCode.U, KeyCode.I
	};
	
	/**
	 * Initializes the fields with the list of all the valid keys.
	 */
	public KeyboardKeyList() {
		keys = new ArrayList<KeyCode>();
		for (int i = 0; i < keyCodes.length; i++)
			keys.add(keyCodes[i]);
	}
	
	/**
	 * Returns the index of the KeyCode in the list of KeyCodes.
	 * @param keyCode
	 * 			The KeyCode whose index is to be obtained.
	 * @return
	 * 			The index of the KeyCode.
	 */
	public int getIndexOfKeyCode(KeyCode keyCode) {
		return keys.indexOf(keyCode);
	}
	
	/**
	 * Gets the key at the given index.
	 * @param index
	 * 			The index position for which the key is to be obtained.
	 * @return
	 * 			The key present at the given index.
	 */
	public String getKeyAtIndex(int index) {
		try {
			return keys.get(index).getName();
		}catch (IndexOutOfBoundsException e) {
			System.out.println("index out of bounds");
		}
		return "";
	}
}
