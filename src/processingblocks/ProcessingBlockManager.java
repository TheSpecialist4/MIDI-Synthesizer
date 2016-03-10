package processingblocks;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

/**
 * Manager to manage all the processing blocks.
 * This class follows the singleton design.
 * @author Kaamraan Kamaal
 *
 */
public class ProcessingBlockManager {
	
	// the instance of the manager available outside
	private static final ProcessingBlockManager INSTANCE = 
									new ProcessingBlockManager();
	// the list of all the blocks on
	private List<ProcessingBlock> processingBlocks;
	// the box containing all the procesisng blocks
	private List<VBox> blockBoxes = new ArrayList<>();
	// checks if there is a block active
	private boolean isAnyBlockOn = false;
	
	/**
	 * Initialises the fields.
	 */
	private ProcessingBlockManager() {
		processingBlocks = new ArrayList<>();
	}
	
	/**
	 * Returns the only instance of the manager available outside.
	 * @return
	 * 		The instance of the manager.
	 */
	public static ProcessingBlockManager getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Adds a new block with the given name.
	 * @param blockName
	 * 			The name of the new block to be added.
	 */
	public void addNewBlock(String blockName) {
		if (!processingBlocks.isEmpty()) {
			processingBlocks.clear();
			blockBoxes.clear();
		}
		switch (blockName) {
		case "Chordify":
			Chordify chordify = new Chordify("Chordify");
			processingBlocks.add(chordify);
			blockBoxes.add(chordify.getProcessingBlock());
			break;
		case "Arpegiator":
			Arpegiator arpegiator = new Arpegiator("Arpegiator");
			processingBlocks.add(arpegiator);
			blockBoxes.add(arpegiator.getProcessingBlock());
			break;
		case "Pitch Shift":
			PitchShift pitchShift = new PitchShift("Pitch Shift");
			processingBlocks.add(pitchShift);
			blockBoxes.add(pitchShift.getProcessingBlock());
			break;
		case "PitchShift":
			PitchShift pitchShiftBlock = new PitchShift("Pitch Shift");
			processingBlocks.add(pitchShiftBlock);
			blockBoxes.add(pitchShiftBlock.getProcessingBlock());
			break;
		case "Monophonic":
			Monophonic monophonic = new Monophonic("Monophonic");
			processingBlocks.add(monophonic);
			blockBoxes.add(monophonic.getProcessingBlock());
			break;
		case "Load":
			ArrayList<String> blockConfig = readFromFile();
			addNewBlock(blockConfig.get(0));
			break;
		}	
		isAnyBlockOn = true;
	}
	
	/**
	 * Removes the given processing block from the list of blocks.
	 * @param processingBlock
	 * 				The processing block to be removed.
	 */
	@SuppressWarnings("deprecation")
	public void removeBlock(ProcessingBlock processingBlock) {
		if (processingBlock.getName().equals("Arpegiator")) {
			Arpegiator arp = (Arpegiator) processingBlock;
			if (arp.getPlayThread().isAlive())
				arp.getPlayThread().stop();
		}
		ArrayList<String> blockConfigs = new ArrayList<>();
		blockConfigs.add(processingBlock.getName());
		if (processingBlock.getName().equals("Arpegiator"))
			blockConfigs.add(((Arpegiator) processingBlock).getParameter());
		if (processingBlock.getName().equals("PitchShift"))
			blockConfigs.add(Integer.toString(
							((PitchShift) processingBlock).getParameter()));
		writeConfigToFile(blockConfigs);
		blockBoxes.remove(processingBlock.getProcessingBlock());
		processingBlocks.remove(processingBlock);
		if (processingBlocks.isEmpty())
			isAnyBlockOn = false;
	}
	
	/**
	 * Returns the box containing all the blocks.
	 * @return
	 * 		The vbox containing all the blocks.
	 */
	public List<VBox> getBlocks() {
		return blockBoxes;
	}
	
	/**
	 * Checks if there is a block active.
	 * @return
	 * 		True if there is a currently active block.
	 */
	public boolean isAnyBlockOn() {
		return this.isAnyBlockOn;
	}
	
	/**
	 * Returns the name of the block active.
	 * @return
	 * 		The name of the block active.
	 */
	public String getBlockOn() {
		return processingBlocks.get(0).getName();
	}
	
	/**
	 * Writes the configurations of the block to the file.
	 * @param blockConfig
	 * 			The configurations of the block to be saved.
	 */
	private void writeConfigToFile(ArrayList<String> blockConfig) {
		try {
			PrintWriter pw = new PrintWriter("blocks-config.txt");
			for (String s : blockConfig)
				pw.print(s + ' ');
			pw.println();
			pw.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads the configurations of the last loaded processing block.
	 * @return
	 * 		List containing the description of the block last loaded.
	 */
	public ArrayList<String> readFromFile() {
		ArrayList<String> blockConfig = new ArrayList<>();
		try {
			FileReader fr = new FileReader("blocks-config.txt");
			Scanner scanner = new Scanner(fr);
			blockConfig.add(scanner.next());
			if (scanner.hasNext())
				blockConfig.add(scanner.next());
			scanner.close();
			return blockConfig;
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText("No previous configuration found");
			alert.setContentText("There was no previous processing"
					+ " block configuration found. Try loading a new block");
			alert.showAndWait();
		}
		return blockConfig;
	}
}
