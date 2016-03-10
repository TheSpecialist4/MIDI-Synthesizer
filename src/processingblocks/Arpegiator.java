package processingblocks;

import java.util.Collections;
import java.util.List;

import javafx.scene.control.ChoiceBox;
import keyboard.gui.KeyboardUI;;

/**
 * This class handles the Arpegiator processing block.
 * An arpegiator can operate in various modes, namely:
 * Ascending, Descending, Ping Pong and Random.
 * @author Kaamraan Kamaal
 *
 */
public class Arpegiator extends ProcessingBlock{

	// the various modes of the Arpegiator
	private static ChoiceBox<String> parameters;
	// thread to play the arpegiate the notes
	private static Thread playThread = new Thread();
	
	/**
	 * Initialies the fields.
	 * @param name
	 * 			Name of the block.
	 */
	public Arpegiator(String name) {
		super(name);
		parameters = new ChoiceBox<>();
		parameters.getItems().addAll("Ascending", "Descending", 
							"Ping Pong", "Random");
		parameters.getSelectionModel().selectFirst();
		blkParamBox.getChildren().add(parameters);
	}

	/**
	 * Arpegiates the notes given in the List of notes
	 * @param notes
	 * 			The notes on which are to be arpegiated.
	 */
	@SuppressWarnings("deprecation")
	public static void playArpNotes(List<Integer> notes) {
		Collections.sort(notes);
		Runnable task = () -> {
			switch (parameters.getValue()) {
			case "Ascending":
				for (int note : notes) {
					KeyboardUI.sendNoteOnToOutput(note);
					try{Thread.sleep(150);} catch (InterruptedException e){}
					KeyboardUI.sendNoteOffToOutput(note);
					try{Thread.sleep(150);} catch (InterruptedException e){}
				}
				break;
			case "Descending":
				for (int i = notes.size() - 1; i >= 0; i--) {
					KeyboardUI.sendNoteOnToOutput(notes.get(i));
					try{Thread.sleep(150);} catch (InterruptedException e){}
					KeyboardUI.sendNoteOffToOutput(notes.get(i));
					try{Thread.sleep(150);} catch (InterruptedException e){}
				}
				break;
			case "Ping Pong":
				for (int note : notes) {
					KeyboardUI.sendNoteOffToOutput(note);
					try{Thread.sleep(150);} catch (InterruptedException e){}
					KeyboardUI.sendNoteOffToOutput(note);
					try{Thread.sleep(150);} catch (InterruptedException e){}
				}
				for (int i = notes.size() - 2; i >= 0; i--) {
					KeyboardUI.sendNoteOnToOutput(notes.get(i));
					try{Thread.sleep(150);} catch (InterruptedException e){}
					KeyboardUI.sendNoteOffToOutput(notes.get(i));
					try{Thread.sleep(150);} catch (InterruptedException e){}
				}
				break;
			case "Random":
				Collections.shuffle(notes);
				for (int note : notes) {
					KeyboardUI.sendNoteOnToOutput(note);
					try{Thread.sleep(500);} catch (InterruptedException e){}
					KeyboardUI.sendNoteOffToOutput(note);
					try{Thread.sleep(500);} catch (InterruptedException e){}
				}
				break;
			}
		};
		if (playThread.isAlive()) {
			playThread.stop();
		}
		playThread = new Thread(task);
		playThread.start();
	}
	
	/**
	 * Returns the thread responsible for playing the arpegiator.
	 * @return
	 * 		The thread playing the arpegiator.
	 */
	public Thread getPlayThread() {
		return playThread;
	}
	
	/**
	 * Returns the mode of the arpegiator.
	 * @return
	 * 		The mode of the arpegiator.
	 */
	public String getParameter() {
		return parameters.getValue();
	}
}
