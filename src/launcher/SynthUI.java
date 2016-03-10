package launcher;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import midi.MIDIFileIn;
import midi.MidiFileOut;
import serialio.SerialOut;
import javafx.animation.FadeTransition;
import javafx.application.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import keyboard.gui.KeyboardModel;
import keyboard.gui.KeyboardUI;

/**
 * The main window (Stage) of the application. This class contains the landing
 * page, when the application is launched.
 * @author Kaamraan Kamaal
 *
 */
public class SynthUI extends Application {
	
	// The primary stage of the Application
	private static Stage primaryStage;
	/*
	 * The options given to the user to choose from
	 */
	private RadioButton rdoKeyboard; // keyboard input
	private RadioButton rdoMidiFile; // Midi file input
	// The main scene of the stage (the first scene)
	private static Scene mainScene;
	
	private static String outputChoice = new String();
	
	private static int outChoice;
	
	/**
	 * Creates the UI of the application.
	 */
	@Override
	public void start(Stage arg0) throws Exception {
		primaryStage = arg0;
		
		// The heading of the scene.
		Text inputChoice = new Text("Select the input method");
		inputChoice.setFont(new Font(34));
		inputChoice.setFill(Color.BLACK);
		
		VBox vb = new VBox(inputChoice);
		
		// Placing the radio buttons on the scene.
		rdoKeyboard = new RadioButton("Keyboard/Mouse");
		rdoMidiFile = new RadioButton("MIDI File");
		
		rdoKeyboard.setFont(new Font(18));
		rdoMidiFile.setFont(new Font(18));
		
		ToggleGroup inputs = new ToggleGroup();
		rdoKeyboard.setToggleGroup(inputs);
		rdoMidiFile.setToggleGroup(inputs);
		
		// Okay button to accept the input the user chooses
		Button btnOkay = new Button("Okay");
		btnOkay.setMinSize(70, 35);
		btnOkay.setOnAction(e -> okayButtonPressed());
		
		// Putting the input choices in an HBox
		HBox paneChoice = new HBox(rdoKeyboard, rdoMidiFile);
		paneChoice.setSpacing(20);
		paneChoice.setAlignment(Pos.CENTER);
		
		vb.getChildren().addAll(paneChoice, btnOkay);
		vb.setSpacing(25);
		vb.setAlignment(Pos.CENTER);
		vb.setOpacity(0.0);
		
		mainScene = new Scene(vb, 1100, 600);
		
		FadeTransition ft = new FadeTransition();
		ft.setNode(vb);
		ft.setFromValue(0);
		ft.setToValue(1.0);
		ft.setDelay(new Duration(1000));
		ft.setDuration(new Duration(1500));
		ft.play();
		
		Text welcomeText = new Text("Hello  :)");
		welcomeText.setFont(new Font(42));
		welcomeText.setOpacity(0);
		welcomeText.setFill(new Color(0.3,0.35,0.35,1.0));
		HBox hb = new HBox(welcomeText);
		hb.setAlignment(Pos.CENTER);
		FadeTransition fade = new FadeTransition();
		fade.setNode(welcomeText);
		fade.setFromValue(0.0);
		fade.setToValue(1.0);
		fade.setDelay(new Duration(800));
		fade.setDuration(new Duration(2000));
		fade.play();
		fade.setOnFinished(e -> {
			FadeTransition fadeOut = new FadeTransition();
			fadeOut.setNode(welcomeText);
			fadeOut.setFromValue(1.0);
			fadeOut.setToValue(0.0);
			fadeOut.setDelay(new Duration(1100));
			fadeOut.setDuration(new Duration(2200));
			fadeOut.play();
			fadeOut.setOnFinished(event -> {
				primaryStage.setScene(mainScene);
				try{Thread.sleep(600);} catch(Exception ex){}
			});
		});
		
		Scene welcomeScene = new Scene(hb, 1100, 600);
		primaryStage.setScene(welcomeScene);
		primaryStage.setTitle("MIDIBlocks");
		primaryStage.setOnCloseRequest(e -> {
			e.consume();
			onStageCloseRequest();
		});
		primaryStage.show();
	}
	
	/**
	 * When the Okay button is pressed, based on the input chosen,
	 * a new scene is created.
	 */
	private void okayButtonPressed() {
		makeOutputDialog();
		if (rdoKeyboard.isSelected()) {
			KeyboardModel keyModel = new KeyboardModel();
			KeyboardUI keyUI = new KeyboardUI(keyModel);
			primaryStage.setScene(keyUI.getScene());
		}
		if (rdoMidiFile.isSelected()) {
			MIDIFileIn midiFile = new MIDIFileIn();
			Scene scene = midiFile.getScene();
			primaryStage.setScene(scene);
		}
	}
	
	/**
	 * When the back button, from any of the other scenes is pressed,
	 * the scene is switched back to the Main Scene.
	 */
	public static void backButtonPressed() {
		MidiFileOut.saveFile();
		primaryStage.setScene(mainScene);
	}
	
	/**
	 * Makes the dialog for output selection.
	 */
	private void makeOutputDialog() {
		String [] outputs = {"MIDI File", "Serial Port", 
							"MIDI File and Serial Port"};
		List<String> outputChoices = Arrays.asList(outputs);
		ChoiceDialog<String> dialog = new ChoiceDialog<String>(
					outputChoices.get(0), outputChoices);
		dialog.setTitle("Output choice");
		dialog.setHeaderText("Select the output medium");
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			outputChoice = result.get();
			System.out.println(outputChoice);
			if (outputChoice.equals("Serial Port")) {
				outChoice = 2;
				makeSerialPortDialog();
			}
			else if (outputChoice.equals("MIDI File")) {
				MidiFileOut.getFile();
				outChoice = 1;
			}
			else {
				makeSerialPortDialog();
				MidiFileOut.getFile();
				outChoice = 3;
			}
		}
	}
	
	/**
	 * Makes a dialog for the list of available serial ports.
	 */
	private void makeSerialPortDialog() {
		List<String> serialPorts = SerialOut.getSystemPorts();
		System.out.println(serialPorts);
		if (serialPorts.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error!");
			alert.setContentText("Couldn't access the Serial Ports.\n"
					+ "Please try again");
			Optional<ButtonType> result = alert.showAndWait();
			if (result != null)
				makeOutputDialog();	
		}
		else {
			ChoiceDialog<String> dialog = new ChoiceDialog<String>(
					serialPorts.get(0), serialPorts);
			dialog.setTitle("Serial port");
			dialog.setHeaderText("Select the serial port");
			
			Optional<String> result = dialog.showAndWait();
			String port = new String();
			if (result.isPresent()) {
				port = result.get();
				SerialOut.setSerialPort(port);
			}
		}
	}
	
	/**
	 * Returns the primary stage.
	 * @return
	 * 		The primary stage.
	 */
	public static Stage getStage() {
		return primaryStage;
	}
	
	/**
	 * Returns integer representation of the outputChoice.
	 * @return
	 * 		The output choice.
	 */
	public static int getOutputChoice() {
		return outChoice;
	}
	
	/**
	 * Launches the application.
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * When the stage generates a close request,
	 * the files are first saved and the stage is cleanly closed.
	 */
	private void onStageCloseRequest() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm Exit");
		alert.setHeaderText("Exit MIDIBlocks");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			MidiFileOut.saveFile();
			SerialOut.closePort();
			primaryStage.close();
		}
	}
}
