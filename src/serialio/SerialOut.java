package serialio;

import java.util.ArrayList;
import java.util.List;

import com.fazecast.jSerialComm.SerialPort;

/**
 * Handles the communications with the serial port.
 * @author Kaamraan Kamaal
 *
 */
public class SerialOut {
	
	// an array of all the port names available on the machine
	private static SerialPort[] portNames = SerialPort.getCommPorts();
	// the port chosen
	private static SerialPort chosenPort;
	// checks if the chosen port is open
	private static boolean isPortOpen = false;
	
	/**
	 * Returns the list of all the ports available.
	 * @return
	 * 		The list of ports available.
	 */
	public static List<String> getSystemPorts() {
		System.out.println(portNames);
		List<String> systemPorts = new ArrayList<>();
		for (int i = 0; i < portNames.length; i++)
			systemPorts.add(portNames[i].getSystemPortName());
		return systemPorts;
	}
	
	/**
	 * Sets the chosen port to the port corresponding to the portName
	 * @param portName
	 * 			The String representation of the SystemPortName;
	 */
	public static void setSerialPort(String portName) {
		if (! (chosenPort == null))
			chosenPort.closePort();
		for (int i = 0; i < portNames.length; i++) {
			if (portName.equals(portNames[i].getSystemPortName())) {
				chosenPort = portNames[i];
				break;
			}
		}
		isPortOpen = chosenPort.openPort();
		if (isPortOpen)
			System.out.println("Port successfully opened");
		chosenPort.setComPortTimeouts(SerialPort.
									TIMEOUT_WRITE_SEMI_BLOCKING, 0, 0);
	}
	
	/**
	 * Sends the button configuration data to the selected port. 
	 * @param btnConfigs
	 * 			The list of button MIDI values.
	 */
	public static void sendConfigData(ArrayList<Integer> btnConfigs) {
		byte[] configs = new byte [9];
		configs[0] = 0b01111101;
		for (int i = 0; i < btnConfigs.size(); i++) {
			int temp = btnConfigs.get(i);
			configs[i + 1] = (byte) temp;
		}
		if (isPortOpen) {
			for (int i = 0; i < configs.length; i++) {
				byte[] buffer = {configs[i]};
				chosenPort.writeBytes(buffer, 1);
				try { Thread.sleep(5);} catch (InterruptedException e){}
			}
		}
	}
	
	/**
	 * Closes the port if it is open.
	 */
	public static void closePort() {
		if (isPortOpen) {
			chosenPort.closePort();
			isPortOpen = false;
		}
	}
	
	/**
	 * Sends an off note for the given note
	 * @param noteValue
	 * 			The note to off.
	 */
	public static void sendNoteOff(int noteValue) {
		if (isPortOpen) {
			byte noteOff = 0b01111110;
			byte note = (byte) noteValue;
			byte[] buffer = {noteOff};
			chosenPort.writeBytes(buffer, 1);
			try {Thread.sleep(5);} catch(InterruptedException e){}
			buffer = new byte[1];
			buffer[0] = note;
			chosenPort.writeBytes(buffer, 1);
			try {Thread.sleep(5);} catch(InterruptedException e){}
		}
	}
	
	/**
	 * Sends an on note for the given note.
	 * @param noteValue
	 * 			The note to on.
	 */
	public static void sendNoteOn(int noteValue) {
		if (isPortOpen) {
			byte noteOn = 0b01111111;
			byte note = (byte) noteValue;
			byte[] buffer = {noteOn};
			chosenPort.writeBytes(buffer, 1);
			try {Thread.sleep(5);} catch(InterruptedException e){}
			buffer = new byte[1];
			buffer[0] = note;
			chosenPort.writeBytes(buffer, 1);
			try {Thread.sleep(5);} catch(InterruptedException e){}
		}
	}
}
