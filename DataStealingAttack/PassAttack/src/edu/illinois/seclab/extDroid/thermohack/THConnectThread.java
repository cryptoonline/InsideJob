package edu.illinois.seclab.extDroid.thermohack;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import edu.illinois.seclab.extDroid.passAttack.DeviceStatus;
import edu.illinois.seclab.extDroid.passAttack.ExtDevice;
import edu.illinois.seclab.extDroid.passAttack.SneakyService;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author soteris demetriou
 *
 */
public class THConnectThread extends Thread {
	private static final String TAG = "ConnectThread";
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mBluetoothAdapter;
	private ExtDevice extDevice;
	private int connect_mode;
    public static Handler handler;
    
    /**
     * 
     * @param device
     * @param extDevice
     * @param myUuid
     * @param bluetoothAdapter
     * @param mode
     * @author soteris demetriou
     */
    public THConnectThread(BluetoothDevice device, ExtDevice extDevice, UUID myUuid, BluetoothAdapter bluetoothAdapter, int mode) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
    	Log.i(TAG, "THConnectThread created.");
    	
        BluetoothSocket tmp = null;
        mmDevice = device;
        mBluetoothAdapter = bluetoothAdapter;
        this.extDevice = extDevice;
        this.connect_mode = mode;
        
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
//        	if (device.getBondState() != device.BOND_BONDED){
//        		tmp = device.createRfcommSocketToServiceRecord(myUuid);
//        		Log.i(TAG, "Secure socket");
	        	BluetoothDevice localBluetoothDevice = mBluetoothAdapter.getRemoteDevice(device.getAddress());
	            Class localClass = localBluetoothDevice.getClass();
	            Class[] arrayOfClass = new Class[1];
	            arrayOfClass[0] = Integer.TYPE;
        		Method localMethod = localClass.getMethod("createRfcommSocket", arrayOfClass);
        	    Object[] arrayOfObject = new Object[1];
        	    arrayOfObject[0] = Integer.valueOf(1);
        	    tmp = ((BluetoothSocket)localMethod.invoke(localBluetoothDevice, arrayOfObject));
        		Log.i(TAG, "Secure socket");
//        	}
//        	else{
//        		tmp = device.createInsecureRfcommSocketToServiceRecord(myUuid);
//        		Log.i(TAG, "Insecure socket");
//        	}
            
            
        } catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); }
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        SneakyService.socket = tmp;
       // SneakyService.socket = mmSocket;
    }
 
    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
        	extDevice.setConStatus(DeviceStatus.CONNECTED_BY_US);
        	SneakyService.socket.connect();
            //SneakyService.connected = true;
            Log.i(TAG, "Connection Established");
            
            
            
            Message msg = new Message();
            msg.what = SneakyService.CONN_SUCCESS;
            Bundle b = new Bundle();
            b.putInt(SneakyService.CON_ONCE_LBL, connect_mode);            
            SneakyService.sshandler.sendMessage(msg);
            
         // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(SneakyService.socket);
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
        	extDevice.setConStatus(DeviceStatus.UNKNOWN);
        	Log.i(TAG, "Connection Failed");
 
        	connectException.printStackTrace();
        	
        	Message msg = new Message();
            msg.what = SneakyService.CONN_FAIL;
            Bundle b = new Bundle();
            b.putInt(SneakyService.CON_ONCE_LBL, connect_mode);            
            SneakyService.sshandler.sendMessage(msg);
        	
            //MainActivity.connected = false;
                
            //Message msg = new Message();
            //Bundle b = new Bundle();
            //msg.what = EnvironmentCheck.CONFAILED;
            //SneakyService.handler.sendMessage(msg);
            return;
        }
 
    }
 
    /**
     * Initiate ConnectedThread
     * @param mmSocket2
     */
    private void manageConnectedSocket(BluetoothSocket mmSocket2) {
		// TODO Auto-generated method stub
		
//    	if(SneakyService.thermoThread != null){
//    		SneakyService.thermoThread.cancel();
//    	}
    	
		SneakyService.thermoThread  = new ThermoHackThread(SneakyService.socket);
		SneakyService.thermoThread.start();
	}

	/** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
        	SneakyService.socket.close();
        } catch (IOException e) { }
    }
}

