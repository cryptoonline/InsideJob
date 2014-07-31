package edu.illinois.seclab.extDroid.passAttack;

/**
 * 
 * @author soteris demetriou
 *
 */
public class DeviceStatus {

	/**
	 * The device's state is currently unknown
	 */
	public static final int UNKNOWN = 0;
	/**
	 * The device is connected with another app
	 */
	public static final int CONNECTED = 1;
	/**
	 * The device has disconnected from another app
	 */
	public static final int DISCONNECTED = 2;
	/**
	 * The device is connected with us
	 */
	public static final int CONNECTED_BY_US = 3;
	/**
	 * We have disconnected with the device
	 */
	public static final int DISCONNECTED_BY_US = 4;

}
