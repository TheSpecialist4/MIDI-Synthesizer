package processingblocks;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import keyboard.gui.KeyboardModel;
import keyboard.gui.KeyboardUI;

/**
 * An abstract class forming the base class for all the Processing Blocks.
 * @author Kaamraan Kamaal.
 *
 */
public abstract class ProcessingBlock {
	
	// button representing the block
	private Button btnBlock;
	// button to delete this block
	private Button btnClose;
	// box to hold the block and its parameters
	private VBox vbox;
	
	// List of all the MIDI notes in the scale
	protected List<Integer> midiOfScale = KeyboardModel.getMidiOfScale();
	// Block containing the parameters of the block
	protected HBox blkParamBox;
	
	// The processing block manager to handle all the processing blocks
	private ProcessingBlockManager blockManager = 
									ProcessingBlockManager.getInstance();
	
	/**
	 * Initialises all the fields.
	 */
	private ProcessingBlock() {
		btnBlock = new Button();
		
		btnClose = new Button("x");
		setButtonSize(btnClose);
		btnClose.setOnAction(e -> {
			blockManager.removeBlock(this);
			KeyboardUI.removeBlockBox(vbox);
		});
		
		blkParamBox = new HBox(btnClose);
		blkParamBox.setSpacing(10);
		blkParamBox.setAlignment(Pos.CENTER);
		vbox = new VBox(btnBlock, blkParamBox);
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);
	}
	
	/**
	 * Makes a new block with given name.
	 * @param name
	 * 			The name of the block.
	 */
	public ProcessingBlock(String name) {
		this();
		btnBlock.setText(name);
		btnBlock.setMinSize(120, 60);
	}
	
	/**
	 * Sets the size of the close button.
	 * @param btnClose
	 * 			The close button
	 */
	private void setButtonSize(Button btnClose) {
		btnClose.setMinSize(40, 30);
		btnClose.setFont(new Font(14));
	}
	
	/**
	 * Returns the VBox containing the processing block and its parameters.
	 * @return
	 * 		The VBox of the block.
	 */
	public VBox getProcessingBlock() {
		return vbox;
	}
	
	/**
	 * Returns the name of the block.
	 * @return
	 * 		The name of the block.
	 */
	public String getName() {
		return btnBlock.getText();
	}
}
