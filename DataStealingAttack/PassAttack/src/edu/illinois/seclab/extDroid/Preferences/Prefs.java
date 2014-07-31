package edu.illinois.seclab.extDroid.Preferences;

/**
 * 
 * @author soteris demetirou
 *
 */
public class Prefs {

	/** The package name od the Bodymedia app */
	public static final String BM_PACKAGE = "com.bodymedia.android.display";
	/** The package name od the iThermometer app */
	public static final String TH_PACKAGE = "com.boantong.i_Thermometer";
	/** The package name od the PulseOxymeter app */
	public static final String PO_PACKAGE = "com.ideasynthesis.simpleeye.livepulseoximeter";
	/** The package name od the BloodGlucose app */
	public static final String BG_PACKAGE = "com.entrahealth";
	
	/** The Handler Delay for TC2 */
	public static final long TC2_DELAY = 0;
	/** The Handler Delay for TC3 */
	public static final long INTERVAL_CONNECT = 0;


	/** Use this as victim name if the device is a Bodymedia device */
	public static final int BODYMEDIA_ID = 0;
	/** A Bodymedia MAC address */
	public static final String BODYMEDIA_MAC_START = "94:A7:BC";
	
	/** Use this as victim name if the device is a Thermometer device */
	public static final int THERMOMETER_ID = 1;
	/** A Thermometer start MAC address */
	public static final String THERMOMETER_MAC_START = "00:01:18";
	//public static final String THERMOMETER_MAC = "00:43:A8:23:10:F0";
	
	/** Use this as victim name if the device is a Nonin device */
	public static final int PULSE_OXYMETER_ID = 2;
	/** A Nonin start MAC address. <b>Currently empty</b> */
	public static final String PULSE_OXYMETER_MAC_START = "";
	/** Pulse Oxymeter app works with this device name */
	public static final String PULSE_OXYMETER_DEV_NAME1 = "Nonin_Medical_Inc";
	/** Pulse Oxymeter app works with this device name */
	public static final String PULSE_OXYMETER_DEV_NAME2 = "BCOXM";
	
	/** Use this as victim name if the device is a BloodGlucose Meter device */
	public static final int BLOOG_GLUCOSE_ID = 3;
	/** A Blood Glucose start MAC address */
	public static final String BLOOD_GLUCOSE_MAC_START = "";
	/** Blood Glucose app works with this device name */
	public static final String BLOOD_GLUCOSE_DEV_NAME = "myglucohealth";
	
	/** The window time limit for the psot attack */
	public static final long POST_WINDOW = 5000;
	
	

	

	
	/** The connect technique. Set to 1 if you want to try to connect once, anything else for repeated trials */
	public static int connect_mode;
	/** The victim's name */
	public static int victim_id = 0;

}
