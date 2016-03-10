package midi;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import launcher.SynthUI;

/**
 * Handles the MIDI file input.
 * @author Kaamraan Kamaal
 *
 */
public class MIDIFileIn {
	
	// back button
	private Button btnBack;
	// start button
	private Button btnStart;
	// stop button
	private Button btnStop;
	// button to load the file
	private Button btnLoadFile;
	
	// main pane for the scene
	private BorderPane bPane;
	
	// checks if the correct file is loaded
	private boolean isFileLoaded;
	
	// the main scene
	private Scene scene;
	
	// the MIDI file
	private File midiFile;
	
	/**
	 * Creates a new MIDI file scene.
	 * @param primaryStage
	 * 				The main stage of the application.
	 */
	public MIDIFileIn() {
		isFileLoaded = false;
		btnBack = new Button();
		btnStart = new Button();
		btnStop = new Button();
		btnLoadFile = new Button();
		
		bPane = new BorderPane();
		drawScene();
	}
	
	/**
	 * Draws the skeleton of the scene.
	 */
	private void drawScene() {
		btnLoadFile.setText("Load File");
		btnLoadFile.setMinSize(50, 30);
		
		btnBack.setText("Back");
		btnBack.setMinSize(50,30);
		
		btnStart.setText("START");
		btnStart.setDisable(!isFileLoaded);
		btnStop.setText("STOP");
		btnStop.setDisable(!isFileLoaded);
		btnStart.setMinSize(70, 50);
		btnStop.setMinSize(70, 50);
		
		VBox pane = new VBox(btnStart, btnStop, btnLoadFile);
		pane.setSpacing(30);
		pane.setAlignment(Pos.CENTER);
		bPane.setLeft(btnBack);
		bPane.setCenter(pane);
		
		scene = new Scene(bPane,1100,600);
		
		btnStart.setOnAction(e -> startPressed());
		
		btnStop.setOnAction(e -> stopPressed());
		
		btnBack.setOnAction(e -> backButtonPressed());
		btnBack.setCancelButton(true);
		
		btnLoadFile.setOnAction(e -> loadButtonPressed());
	}
	
	/*
	 * ##################################################################
	 * CONTROLLERS
	 * ##################################################################
	 */

	/**
	 * When the load button is pressed, a FileChooser is opened to allow the
	 * user to select the file to be played.
	 */
	private void loadButtonPressed() {
		FileChooser fileChooser = new FileChooser();
		midiFile = fileChooser.showOpenDialog(SynthUI.getStage());
		if (midiFile != null) {
			isFileLoaded = true;
			btnStart.setDisable(!isFileLoaded);
			btnStop.setDisable(!isFileLoaded);
		}
	}
	
	/**
	 * Starts playing the file when the play button is pressed.
	 */
	private void startPressed() {
		btnStart.setDisable(true);
		btnStop.setDisable(false);
		try {
			Pattern pattern = MidiFileManager.loadPatternFromMidi(midiFile);
			MidiFileOut.addPatternToFile(pattern);
		} catch (IOException | InvalidMidiDataException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setHeaderText("Error! Wrong file format");
			alert.setContentText("The chosen file was not of correct format.\n"
					+ "Please load in (.mid) files and try again");
			alert.showAndWait();
		}
	}
	
	/**
	 * Saves the file played to the external file when the stop button
	 * is pressed.
	 */
	private void stopPressed() {
		btnStart.setDisable(false);
		btnStop.setDisable(true);
		MidiFileOut.saveFile();
	}
	
	/**
	 * Goes back to the homescreen when the back button is pressed.
	 */
	private void backButtonPressed() {
		SynthUI.backButtonPressed();
	}
	
	/**
	 * Returns the current scene.
	 * @return
	 * 		Returns the current scene.
	 */
	public Scene getScene() {
		return scene;
	}
}
