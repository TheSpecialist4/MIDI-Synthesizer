package keyboard.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import launcher.SynthUI;
import midi.MidiFileOut;
import midi.MidiValues;

import org.jfugue.pattern.Pattern;
import org.jfugue.rhythm.Rhythm;

import processingblocks.Arpegiator;
import processingblocks.Chordify;
import processingblocks.PitchShift;
import processingblocks.ProcessingBlockManager;
import serialio.SerialOut;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import keyboard.util.KeyboardKeyList;

/**
 * The controller and view class for the virtual keyboard input choice.
 * @author Kaamraan Kamaal
 *
 */
public class KeyboardUI {

	// the list of keys of the virtual piano
	private static List<Button> keys;
	// the list of all the modes
	private ChoiceBox<String> modes;
	// the list of all the roots
	private ChoiceBox<String> roots;
	// Okay button, accepts the input the user chooses
	private Button btnOkay;
	// Button to load the scales file
	private Button btnLoadFile;
	// Button to increase the octave of all the keys
	private Button btnPlusOctave;
	// Button to decrease the octave of all the keys
	private Button btnMinusOctave;
	// Button to shift keys left
	private Button btnShiftLeft;
	// Button to shift keys right
	private Button btnShiftRight;
	// Keyboard Scene
	private Scene currentScene;
	// back button
	private Button btnBack;
	// button to activate the metronome
	private Button btnMetronome;
	// button to start recording to the file
	private static Button btnRecord;
	// visual effects for the metronome
	private List<Rectangle> metroDisplay;
	// boolean to check if the metronome is on
	private boolean isMetroOn = false;
	
	// MenuButton for the processing blocks
	private MenuButton menuButton;
	
	// the mane pane of the scene
	private BorderPane primaryPane;
	 
	// the model of the virtual keyboard
	private KeyboardModel keyboard;
	
	// the HBox containing all the keys of the piano
	private HBox keyBox;
	
	// the list of the valid hardware keyboard keys
	private KeyboardKeyList keyList;
	
	// instance of the ProcessingBlockManager
	private static ProcessingBlockManager blockManager = 
							ProcessingBlockManager.getInstance();
	
	// HBox containing the currently active processing block
	private static HBox processingBlocksBox;
	
	// Flag to check if a key is currently pressed
	private int[] keyPressCounter;
	// The On Note corresponding to the key pressed
	private int[] currentOnNotes;

	// Stores the value of the currently on note
	private int currentNoteOn = 0;
	
	// List of all the notes to arpegiate between 
	private List<Integer> arpNotes;
	
	/*
	 * Integer value to check the output choice selected.
	 * <value> = 1 -> outputChoice = MIDI File
	 * <value> = 2 -> outputChoice = Serial Out
	 * <value> = 3 -> outputChoice = Serial Out + MIDI File
	 */
	private static int outputChoice = SynthUI.getOutputChoice();
	
	/**
	 * Initializes all the fields.
	 * @param k
	 * 		The 'model' of the virtual keyboard.
	 */
	public KeyboardUI(KeyboardModel k) {
		keys = new ArrayList<Button>();
		primaryPane = new BorderPane();
		modes = new ChoiceBox<String>();
		roots = new ChoiceBox<String>();
		keyboard = k;
		keyList = new KeyboardKeyList();
		keyPressCounter  = new int[15];
		currentOnNotes = new int[15];
		arpNotes = Collections.synchronizedList(new ArrayList<>());
		processingBlocksBox = new HBox();
		processingBlocksBox.setAlignment(Pos.CENTER);
		buildScene();
	}
	
	/**
	 * Returns the current Scene. That is, returns the scene of the
	 * virtual piano.
	 * @return
	 * 		The current Scene.
	 */
	public Scene getScene() {
		FadeTransition ft = new FadeTransition();
		ft.setNode(currentScene.getRoot());
		ft.setDuration(new Duration(800));
		ft.setFromValue(0.4);
		ft.setToValue(0.92);
		ft.play();
		return this.currentScene;
	}
	
	/**
	 * Builds the scene. That is, adds all the required javafx.scene.Node
	 * components to the scene.
	 */
	private void buildScene() {
		makeOkayButton();
		makeLoadFileButton();
		makeRightShiftButtons();
		makeLeftShiftButtons();
		makeBackButton();
		makeMetronome();
		makeMenuButton();
		makeRecordButton();
		setTopLayout();
		primaryPane.setBottom(processingBlocksBox);
		currentScene = new Scene(primaryPane, 1100, 600);
		currentScene.setFill(Color.ALICEBLUE);
	}
	
	/**
	 * Sets the style of the key of the virtual piano.
	 * That is, adds effects and sets the background color of the key.
	 * @param key
	 * 			The key on the virtual piano.
	 */
	private void setKeyStyle(Button key) {
		key.setMinSize(50, 160);
		key.setOpacity(1.0);
		key.setScaleX(1.0);
		key.setScaleY(1.0);
		if (key.getText().contains("#")) {
			key.setStyle("-fx-base: #4a4a4a");
		}
		else {
			key.setStyle("-fx-base: #ffffff");
		}
		if (key.getText().contains(keyboard.getRoot())
				&& !(key.getText().contains("*")))
			key.setText(key.getText() + "*");
		Reflection reflection = new Reflection();
		reflection.setFraction(0.35);
		reflection.setTopOpacity(0.3);
		key.setEffect(reflection);
		
	}
	
	/**
	 * Returns the note on the key.
	 * Thats is, trims the text on the key to a valid string representaion
	 * of the note on that key.
	 * @param key
	 * 			The key on the virtual piano.
	 * @return
	 * 			The string representation of the note corresponding to the key.
	 */
	private String getKeyText(Button key) {
		if (key.getText().contains("#")) {
			return key.getText().substring(0, 3);
		}
		else
			return key.getText().substring(0, 2);
	}
	
	/* ########################################################################
	 * VIEW
	 * ########################################################################
	 */
	
	/**
	 * Makes the record button.
	 */
	private void makeRecordButton() {
		btnRecord = new Button("Start");
		btnRecord.setMinSize(60, 30);
		btnRecord.setOnAction(e -> {
			if (btnRecord.getText().equals("Start")) {
				btnRecord.setText("Stop");
				btnRecord.setStyle("-fx-base: #a0ffa0");
			}
			else {
				btnRecord.setText("Start");
				btnRecord.setStyle("");
			}
		});
		btnRecord.setOnKeyPressed(e -> handleKeyboardKeyPressed(e.getCode()));
		btnRecord.setOnKeyReleased(e -> keyboardKeyReleased(e.getCode()));
	}
	
	/**
	 * Makes the back button.
	 */
	private void makeBackButton() {
		btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			SynthUI.backButtonPressed();
		});
		btnBack.setCancelButton(true);
		btnBack.setOnKeyPressed(e -> handleKeyboardKeyPressed(e.getCode()));
		btnBack.setOnKeyReleased(e -> keyboardKeyReleased(e.getCode()));
		btnBack.setMinSize(50,25);
	}
	
	/**
	 * Makes the metronome display.
	 */
	private void makeMetronome() {
		btnMetronome = new Button("Metronome");
		btnMetronome.setMinHeight(30);
		btnMetronome.setOnAction(e -> metronomePressed());
		btnMetronome.setOnKeyPressed(e -> handleKeyboardKeyPressed(e.getCode()));
		btnMetronome.setOnKeyReleased(e -> keyboardKeyReleased(e.getCode()));
		metroDisplay = new ArrayList<>();
		double rectX = 0.0;
		for (int i = 0; i < 4; i++) {
			Rectangle rect = new Rectangle(20,20);
			rect.setX(rectX);
			rect.setFill(Color.BEIGE);
			metroDisplay.add(rect);
			rectX += rect.getX() + 50;
		}
	}
	
	/**
	 * Makes the processing blocks menu button.
	 */
	private void makeMenuButton() {
		menuButton = new MenuButton("Blocks");
		ArrayList<MenuItem> processingBlocks = new ArrayList<>();
		processingBlocks.add(new MenuItem("Chordify"));
		processingBlocks.add(new MenuItem("Arpegiator"));
		processingBlocks.add(new MenuItem("Pitch Shift"));
		processingBlocks.add(new MenuItem("Monophonic"));
		processingBlocks.add(new MenuItem("Load"));
		for (MenuItem menuItem : processingBlocks) {
			menuItem.setOnAction(e -> blocksMenuPressed(menuItem));
		}
		menuButton.getItems().addAll(processingBlocks);
		menuButton.setPopupSide(Side.RIGHT);
		menuButton.setOnKeyPressed(e -> handleKeyboardKeyPressed(e.getCode()));
		menuButton.setOnKeyReleased(e -> keyboardKeyReleased(e.getCode()));
	}
	
	/**
	 * Sets the top portion of the primary layout of the scene.
	 */
	private void setTopLayout() {
		VBox vb = new VBox(btnBack, menuButton);
		vb.setSpacing(5);
		
		HBox hb = new HBox(vb);
		hb.getChildren().addAll(roots, modes, btnOkay, btnLoadFile);
		hb.setSpacing(20);
		hb.setAlignment(Pos.CENTER);
		
		HBox hbMetro = new HBox(btnMetronome);
		hbMetro.getChildren().addAll(metroDisplay);
		hbMetro.setSpacing(10);
		hbMetro.setAlignment(Pos.CENTER_RIGHT);
		
		HBox hbox = new HBox();
		hbox.getChildren().add(new Text("Record:"));
		hbox.getChildren().add(btnRecord);
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER_RIGHT);
		
		VBox vbox = new VBox(hbMetro, hbox);
		vbox.setAlignment(Pos.CENTER_RIGHT);
		vbox.setSpacing(10);
		
		HBox mainBox = new HBox(vb, hb, vbox);
		mainBox.setSpacing(230);
		primaryPane.setTop(mainBox);
	}
	
	/**
	 * Populates the scale roots and modes options.
	 */
	private void makeScalesList() {
		List<String> modesString = keyboard.getAllScales().getAllModes();
		List<String> rootsString = keyboard.getAllScales().getAllRoots();
		roots.getItems().add("Root");
		modes.getItems().add("Mode");
		for (int i = 0; i < 12; i++) {
			roots.getItems().add(rootsString.get(i));
		}
		for (int i = 0; i < modesString.size(); i += 12) {
			modes.getItems().add(modesString.get(i));
		}
		roots.getSelectionModel().selectFirst();
		modes.getSelectionModel().selectFirst();
	}
	
	/**
	 * Makes the okay button.
	 */
	private void makeOkayButton() {
		btnOkay = new Button("Okay");
		btnOkay.setOnAction(e -> okayButtonPressed());
		btnOkay.setOnKeyPressed(e -> handleKeyboardKeyPressed(e.getCode()));
		btnOkay.setOnKeyReleased(e -> keyboardKeyReleased(e.getCode()));
	}
	
	/**
	 * Makes the load file button.
	 */
	private void makeLoadFileButton() {
		btnLoadFile = new Button("Load Scales");
		btnLoadFile.setOnAction(e -> {
			keyboard.loadScales();
			makeScalesList();
		});
		btnOkay.setOnKeyPressed(e -> handleKeyboardKeyPressed(e.getCode()));
		btnOkay.setOnKeyReleased(e -> keyboardKeyReleased(e.getCode()));
	}
	
	/**
	 * Makes the increase octave buttons. That is the right shift, and plus
	 * octave buttons.
	 */
	private void makeRightShiftButtons() {
		btnShiftRight = new Button(">");
		btnShiftRight.setMinSize(50, 50);
		btnShiftRight.setFont(new Font(22));
		btnShiftRight.setOnAction(e -> shiftRightPressed());
		btnShiftRight.setOnKeyPressed(e -> handleKeyboardKeyPressed(e.getCode()));
		btnShiftRight.setOnKeyReleased(e -> keyboardKeyReleased(e.getCode()));
		
		btnPlusOctave = new Button("+");
		btnPlusOctave.setMinSize(50, 50);
		btnPlusOctave.setFont(new Font(22));
		btnPlusOctave.setOnAction(e -> plusOctavePressed());
		btnPlusOctave.setOnKeyPressed(e -> handleKeyboardKeyPressed(e.getCode()));
		btnPlusOctave.setOnKeyReleased(e -> keyboardKeyReleased(e.getCode()));
		
		VBox vb = new VBox(btnShiftRight, btnPlusOctave);
		vb.setAlignment(Pos.CENTER);
		vb.setSpacing(10);
		primaryPane.setRight(vb);
	}
	
	/**
	 *  Makes the decrease octave buttons. That is, makes the shift left and
	 *  minus octave buttons.
	 */
	private void makeLeftShiftButtons() {
		btnMinusOctave = new Button("-");
		btnMinusOctave.setMinSize(50, 50);
		btnMinusOctave.setFont(new Font(22));
		btnMinusOctave.setOnAction(e -> minusOctavePressed());
		btnMinusOctave.setOnKeyPressed(e -> handleKeyboardKeyPressed(e.getCode()));
		btnMinusOctave.setOnKeyReleased(e -> keyboardKeyReleased(e.getCode()));
		
		btnShiftLeft = new Button("<");
		btnShiftLeft.setMinSize(50, 50);
		btnShiftLeft.setFont(new Font(22));
		//btnShiftLeft.setDisable(true);
		btnShiftLeft.setOnAction(e -> shiftLeftPressed());
		btnShiftLeft.setOnKeyPressed(e -> handleKeyboardKeyPressed(e.getCode()));
		btnShiftLeft.setOnKeyReleased(e -> keyboardKeyReleased(e.getCode()));
		
		VBox vb = new VBox(btnShiftLeft, btnMinusOctave);
		vb.setSpacing(10);
		vb.setAlignment(Pos.CENTER_LEFT);
		primaryPane.setLeft(vb);
		
		setOctaveDisables();
	}

	/**
	 * Makes the keys of the virtual piano.
	 */
	private void makeKeys() {
		keyBox = new HBox();
		keyBox.setAlignment(Pos.CENTER);
		for (int i = 0; i < 15; i++) {
			Button key = new Button();
			key.setText(keyboard.getNextNote());
			setKeyStyle(key);
			addKeyEventHandlers(key);
			keys.add(key);
		}
		setKeyText();
		keyBox.getChildren().addAll(keys);
		sendButtonConfig();
		
		//adding animations
		FadeTransition ft = new FadeTransition();
		ft.setNode(keyBox);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.setDuration(new Duration(800));
		
		ScaleTransition st = new ScaleTransition();
		st.setNode(keyBox);
		st.setFromX(0.95);
		st.setFromY(0.95);
		st.setToX(1.0);
		st.setToY(1.0);
		ft.setDuration(new Duration(800));
		
		ParallelTransition p = new ParallelTransition();
		p.getChildren().addAll(ft, st);
		p.play();
		
		primaryPane.setCenter(keyBox);
		setOctaveDisables();
	}

	/**
	 * Sets the text of the keys of the virtual piano.
	 * That is, adds the text of the corresponding hardware keyboard key
	 * to the virtual piano key.
	 */
	private void setKeyText() {
		String keyText = new String();
		for (int i = 0; i < keys.size(); i++) {
			keyText = keys.get(i).getText();
			if (keyText.contains("\n"))
				keyText = keyText.substring(0, keyText.indexOf('\n'));
			keyText += "\n\n\n(" + keyList.getKeyAtIndex(i) + ")";
			keys.get(i).setText(keyText);
		}
	}
	
	/**
	 * Adds the various event handlers associated with each key on the virtual
	 * piano.
	 * @param key
	 * 			The key on the virtual piano.
	 */
	private void addKeyEventHandlers(Button key) {
		key.setOnMouseEntered(e -> mouseEntered(key));
		key.setOnMouseExited(e -> mouseExited(key));
		
		key.setOnKeyPressed(e -> handleKeyboardKeyPressed(e.getCode()));
		key.setOnKeyReleased(e -> keyboardKeyReleased(e.getCode()));
		
		key.setOnMousePressed(e -> {
			keyPressCounter[keys.indexOf(key)] = 1;
			playOnNotes(key, keys.indexOf(key));
		});
		key.setOnMouseReleased(e -> {
			mouseReleased(key);
			setKeyStyle(key);
		});
	}
	
	/**
	 * Removes the box containing the processing block from the HBox
	 * containing all the processing blocks.
	 * @param blockBox
	 * 			The box containing the processing block.
	 */
	public static void removeBlockBox(VBox blockBox) {
		if (processingBlocksBox.getChildren().contains(blockBox))
			processingBlocksBox.getChildren().remove(blockBox);
	}
	
	/**
	 * Makes the box containing all the processing blocks.
	 */
	public static void drawBlockBox() {
		if (! processingBlocksBox.getChildren().isEmpty()) {
			processingBlocksBox.getChildren().clear();
		}
		processingBlocksBox.getChildren().addAll(blockManager.getBlocks());
	}
	
	/**
	 * Disables the plus and minus octave buttons if, the octave of the last
	 * key is 7, and the octave of the first key is 1 respectively.
	 */
	private void setOctaveDisables() {
		if (!keys.isEmpty()) {
			if (keys.get(0).getText().contains("1"))
				btnMinusOctave.setDisable(true);
			else
				btnMinusOctave.setDisable(false);
			if (keys.get(14).getText().contains("7"))
				btnPlusOctave.setDisable(true);
			else
				btnPlusOctave.setDisable(false);
		}
	}

	
	/* ########################################################################
	 * CONTROLLERS
	 * ########################################################################
	 */
	
	/**
	 * Plays the metronome. When the metronome is played, a new thread
	 * is fired to cycle between the colors of the visual metronome and this
	 * thread runs concurrently with the ticks of the metronome.
	 */
	@SuppressWarnings("deprecation")
	private void metronomePressed() {
		Thread thread = new Thread();
		Pattern pattern = new Pattern();
		isMetroOn = !isMetroOn;
		if (isMetroOn) {
			Rhythm rhythm = new Rhythm();
			rhythm.setLength(15);
			rhythm.addLayer("s.x.x.x.");
			if (outputChoice == 1 || outputChoice == 3)
				pattern.add(rhythm.getPattern());
			Runnable task = () -> {
				btnMetronome.setStyle("-fx-base: #a0ffa0");
				
				//makeshift implementation of cycling of colors of rectangle
				int count = 0;
				boolean cycle = true;
				while (isMetroOn) {
					if (cycle) {
						if (count == 0) {
							metroDisplay.get(count++).setFill(Color.YELLOW);
							try {Thread.sleep(500);} catch(Exception e){}
						}
						metroDisplay.get(count++).setFill(Color.DARKCYAN);
						try {Thread.sleep(500);} catch(Exception e){}
						if (count == 4) {
							count = 0;
							cycle = false;
						}
					}
					else {
						if (count == 0) {
							metroDisplay.get(count++).setFill(Color.GOLDENROD);
							try {Thread.sleep(500);} catch(Exception e){}
						}
						metroDisplay.get(count++).setFill(Color.INDIANRED);
						try {Thread.sleep(500);} catch(Exception e){}
						if (count == 4) {
							count = 0;
							cycle = true;
						}
					}
				}
			};
			thread = new Thread(task);
			thread.start();
		}
		else {
			btnMetronome.setStyle("");
			for (Rectangle r : metroDisplay)
				r.setFill(Color.BEIGE);
			thread.stop();
			MidiFileOut.addPatternToFile(pattern);
		}
	}
	
	/**
	 * Performs the action corresponding to the key pressed on the hardware
	 * keyboard. For example, increases the octave when "Plus" key is pressed,
	 * or shifts left when "LEFT_ARROW" key is pressed etc.
	 * @param keyCode
	 * 			The code of the key pressed.
	 */
	private void handleKeyboardKeyPressed(KeyCode keyCode) {
		switch (keyCode) {
		case PLUS:
			plusOctavePressed();
			break;
		case EQUALS:
			plusOctavePressed();
			break;
		case MINUS:
			minusOctavePressed();
			break;
		case RIGHT:
			shiftRightPressed();
			break;
		case LEFT:
			shiftLeftPressed();
			break;
		default:
			playCorrespondingKeyForPress(keyCode);
		}
	}
	
	/**
	 * When the mouse if hovered over a key on the virtual piano,
	 * a drop shadow effect is added to the key.
	 * @param key
	 * 			The key over which the mouse is hovered.
	 */
	private void mouseEntered(Button key) {
		key.setEffect(new DropShadow());
	}
	
	/**
	 * Removes the additional effects added when the mouse exits the key.
	 * @param key
	 * 			The key on the virtual piano.
	 */
	private void mouseExited(Button key) {
		key.setEffect(null);
		Reflection reflection = new Reflection();
		reflection.setFraction(0.35);
		reflection.setTopOpacity(0.3);
		key.setEffect(reflection);
	}
	
	/**
	 * Sets the scale based on the root and mode chosen, and the okay button
	 * is pressed.
	 */
	private void okayButtonPressed() {
		String root = roots.getValue();
		String mode = modes.getValue();
		if (!root.equals("Root") && !mode.equals("Mode")) {
			keyboard.setScale(root, mode);
			if (!keys.isEmpty()) {
				keys.clear();
			}
			makeKeys();
		}	
	}
	
	/**
	 * Shifts the keys right by one step.
	 */
	private void shiftRightPressed() {
		keyboard.setStartOctave(keys.get(0).getText());
		keys.remove(0);
		Button key = new Button(keyboard.getNextNote());
		setKeyStyle(key);
		addKeyEventHandlers(key);
		keys.add(key);
		setKeyText();
		
		keyBox.getChildren().add(key);
		FadeTransition fadeIn = new FadeTransition();
		fadeIn.setNode(key);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		fadeIn.setDuration(new Duration(1000));
		fadeIn.play();

		sendButtonConfig();
		keyBox.getChildren().remove(0);
		
		setOctaveDisables();
	}

	/**
	 * Increases the octave of all the keys by one.
	 */
	private void plusOctavePressed() {
		keyboard.incOctave();
		String firstKey = keys.get(0).getText();
		if (firstKey.length() > 2) {
			firstKey = firstKey.substring(0, 1);
		}
		else {
			firstKey = firstKey.substring(0, 2);
		}
		keys.clear();
		makeKeys();
		setOctaveDisables();
	}
	
	/**
	 * Decreases the octave of all the keys by one.
	 */
	private void minusOctavePressed() {
		keyboard.decOctave();
		String firstKey = keys.get(0).getText();
		if (firstKey.length() > 2) {
			firstKey = firstKey.substring(0, 1);
		}
		else {
			firstKey = firstKey.substring(0, 2);
		}
		keys.clear();
		makeKeys();
		setOctaveDisables();
	}
	
	/**
	 * Shifts the keys to the left by one.
	 */
	private void shiftLeftPressed() {
		keyboard.setEndOctave(keys.get(keys.size() - 1).getText());
		
		keys.remove(keys.size() - 1);
		//String keyText = getKeyText(keys.get(0));
		String keyText = keys.get(0).getText();
		Button key = new Button(keyboard.getPrevNote(keyText));
		setKeyStyle(key);
		addKeyEventHandlers(key);
		keys.add(0, key);
		setKeyText();
		
		keyBox.getChildren().add(0, key);
		FadeTransition fadeIn = new FadeTransition();
		fadeIn.setNode(key);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		fadeIn.setDuration(new Duration(1000));
		fadeIn.play();

		keyBox.getChildren().remove(keyBox.getChildren().size() - 1);
		
		setOctaveDisables();
		sendButtonConfig();
	}
	
	/**
	 * 'Plays' the note corresponding to the key pressed.
	 * @param key
	 * 			The key on the virtual piano.
	 * @param index
	 * 			The index of the key in the list of all the keys
	 * 			on the virtual piano.
	 */
	private void playOnNotes(Button key, int index) {
		if (keyPressCounter[index] == 1) {
			keyPressCounter[index] = 2;
			key.setOpacity(0.6);
			key.setScaleY(0.95);
			key.setScaleX(0.95);
			if (blockManager.isAnyBlockOn())
				sendNotesAfterBlocks(key, index);
			else {
				int onNote = MidiValues.getMidiValue(getKeyText(key));
				currentOnNotes[index] = onNote;
				sendNoteOnToOutput(onNote);
				}
		}
	}
	
	/**
	 * When there is an active processing block, the note on is processed,
	 * and the corresponding modified note is sent to the output.
	 * @param key
	 * 			The key on the virtual piano.
	 * @param index
	 * 			The index of the key in the list of all the keys on the piano.
	 */
	private void sendNotesAfterBlocks(Button key, int index) {
		switch (blockManager.getBlockOn()) {
		case "Monophonic":
			if (currentNoteOn == 0) {
				currentNoteOn = MidiValues.getMidiValue(getKeyText(key));
				currentOnNotes[index] = currentNoteOn;
				sendNoteOnToOutput(currentNoteOn);
			}
			else {
				sendNoteOffToOutput(currentNoteOn);
				currentNoteOn = MidiValues.getMidiValue(getKeyText(key));
				currentOnNotes[index] = currentNoteOn;
				sendNoteOnToOutput(currentNoteOn);
			}
			break;
		case "Pitch Shift":
			int shiftedNote = PitchShift.getPitchShiftedNote(
							MidiValues.getMidiValue(getKeyText(key)));
			currentOnNotes[index] = shiftedNote;
			sendNoteOnToOutput(shiftedNote);
			break;
		case "Chordify":
			int firstNote = MidiValues.getMidiValue(getKeyText(key));
			currentOnNotes[index] = -1;
			Chordify.makeChord(firstNote);
			sendNoteOnToOutput(Chordify.getFirstNote());
			sendNoteOnToOutput(Chordify.getSecondNote(firstNote));
			sendNoteOnToOutput(Chordify.getThirdNote(firstNote));
			// sends the major chord of the note to the file
			if (outputChoice == 1 || outputChoice == 3) {
				MidiFileOut.addToFile(MidiValues.getStringForMidi
													(firstNote) + "maj");
			}
			break;
		case "Arpegiator":
			int note = MidiValues.getMidiValue(getKeyText(key));
			arpNotes.add(note);
			currentOnNotes[index] = note;
			Arpegiator.playArpNotes(arpNotes);
			break;
		}
	}
	
	/**
	 * Send the note on to the output media selected.
	 * @param onNote
	 * 			The note on.
	 */
	public static void sendNoteOnToOutput(int onNote) {
		if (outputChoice == 2 || outputChoice == 3)
			SerialOut.sendNoteOn(onNote);
		if (outputChoice == 1 || outputChoice == 3) {
			if (btnRecord.getText().equals("Stop")) {
				String note = MidiValues.getStringForMidi(onNote);
				MidiFileOut.addToFile(note);
			}
		}
	}
	
	/**
	 * Sends the note off to the output media selected.
	 * @param offNote
	 * 			The note off.
	 */
	public static void sendNoteOffToOutput(int offNote) {
		if (outputChoice == 2 || outputChoice == 3)
			SerialOut.sendNoteOff(offNote);
	}
	
	/**
	 * 'Plays' the key corresponding to the hardware key pressed.
	 * @param keyCode
	 * 			The KeyCode of the hardware key pressed.
	 */
	private void playCorrespondingKeyForPress(KeyCode keyCode) {
		int index = keyList.getIndexOfKeyCode(keyCode);
		if (index != -1) {
			if (keyPressCounter[index] == 0) {
				keyPressCounter[index] = 1;
			}
			playOnNotes(keys.get(index), index);
		}
	}
	
	/**
	 * When the key is released, the off note of the note corresponding to
	 * it is sent to the output media.
	 * @param keyCode
	 * 			The KeyCode of the key released.
	 */
	private void keyboardKeyReleased(KeyCode keyCode) {
			int index = keyList.getIndexOfKeyCode(keyCode);
			if (index != -1) {
				setKeyStyle(keys.get(index));
				keyPressCounter[index] = 0;
				if (! (currentOnNotes[index] == -1)) {
					arpNotes.remove((Integer) MidiValues.getMidiValue(
												getKeyText(keys.get(index))));
					sendNoteOffToOutput(currentOnNotes[index]);
				}
				else
					sendChordOffNotes(keys.get(index));
			}
	}
	
	/**
	 * Sends the off note of the note corresponding to the key released.
	 * @param key
	 * 			The key released.
	 */
	private void mouseReleased(Button key) {
		int index = keys.indexOf(key);
		keyPressCounter[index] = 0;	
		if (!(currentOnNotes[index] == -1)) {
			arpNotes.remove((Integer) MidiValues.getMidiValue(getKeyText(key)));
			SerialOut.sendNoteOff(currentOnNotes[index]);
		}
		else
			sendChordOffNotes(key);
	}
	
	/**
	 * Sends the off notes for the three notes that were 'on' when a chord
	 * was produced.
	 * @param key
	 * 			The key which was released.
	 */
	private void sendChordOffNotes(Button key) {
		int firstNote = MidiValues.getMidiValue(getKeyText(key));
		Chordify.makeChord(firstNote);
		sendNoteOffToOutput(Chordify.getFirstNote());
		sendNoteOffToOutput(Chordify.getSecondNote(firstNote));
		sendNoteOffToOutput(Chordify.getThirdNote(firstNote));
	}
	
	/**
	 * When the blocks menu is pressed, the block corresponding to the menu
	 * item selected is activated.
	 * @param menuItem
	 * 			The MenuItem for which a processing block is to be activated.
	 */
	private void blocksMenuPressed(MenuItem menuItem) {
		blockManager.addNewBlock(menuItem.getText());
		drawBlockBox();
	}
	
	/**
	 * Sends the button mapping to the external synth connected via the serial
	 * port. That is, sends the note values of the first eight buttons, the
	 * configuration of the buttons to the synthesizer.
	 */
	private void sendButtonConfig() {
		String keyName = new String();
		ArrayList<Integer> midiOfKeys = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			keyName = keys.get(i).getText();
			keyName = keyName.substring(0, keyName.indexOf('\n'));
			if (keyName.contains("*"))
				keyName = keyName.substring(0, keyName.indexOf('*'));
			midiOfKeys.add(MidiValues.getMidiValue(keyName));
		}
		if (outputChoice == 2 || outputChoice == 3)
			SerialOut.sendConfigData(midiOfKeys);
	}
}
