package edu.illinois.seclab.extDroid.pulsehack;

import java.io.IOException;
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

public class POConnectThread extends Thread {
	private static final String TAG = "ConnectThread";
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mBluetoothAdapter;
    public static Handler handler;
    private Message msg;
	private ExtDevice extDevice;
	private int connect_mode;
    
	/**
	 * 
	 * @param device
	 * @param extDevice
	 * @param myUuid
	 * @param bluetoothAdapter
	 * @param mode
	 * @author soteris demetriou
	 */
    public POConnectThread(BluetoothDevice device, ExtDevice extDevice, UUID myUuid, BluetoothAdapter bluetoothAdapter, int mode) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
    	msg = new Message();
        BluetoothSocket tmp = null;
        mmDevice = device;
        this.extDevice = extDevice;
        this.connect_mode = mode;
        mBluetoothAdapter = bluetoothAdapter;
        
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
        	if (device.getBondState() != BluetoothDevice.BOND_BONDED){
        		tmp = device.createRfcommSocketToServiceRecord(myUuid);
        		Log.i(TAG, "Secure socket");
        	}
        	else{
        		tmp = device.createInsecureRfcommSocketToServiceRecord(myUuid);
        		Log.i(TAG, "Insecure socket");
        	}
            
            
        } catch (IOException e) { 
        	e.printStackTrace();
        }
        mmSocket = tmp;
    }
 
    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();
 
        try {
            
            extDevice.setConStatus(DeviceStatus.CONNECTED_BY_US);
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
        	SneakyService.socket.connect();
            //SneakyService.connected = true;
            Log.i(TAG, "Connection Established");
     
            Message msg = new Message();
            msg.what = SneakyService.CONN_SUCCESS;
            Bundle b = new Bundle();
            b.putInt(SneakyService.CON_ONCE_LBL, connect_mode);            
            SneakyService.sshandler.sendMessage(msg);
            
            // Do work to manage the connection (in a separate thread)
            manageConnectedSocket(mmSocket);
            
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
//        	Log.i(TAG, "Connection Failed");
//        	msg.what = MainActivity.NOT_CONNECTED_MSG;
//            MainActivity.mainHandler.handleMessage(msg);
//            try {
//                mmSocket.close();
//            } catch (IOException closeException) { }
//            return;
            
            // Unable to connect; close the socket and get out
        	extDevice.setConStatus(DeviceStatus.UNKNOWN);
        	Log.i(TAG, "Connection Failed");
 
        	connectException.printStackTrace();
        	
        	Message msg = new Message();
            msg.what = SneakyService.CONN_FAIL;
            Bundle b = new Bundle();
            b.putInt(SneakyService.CON_ONCE_LBL, connect_mode);            
            SneakyService.sshandler.sendMessage(msg);
        }
 
    }
 
    /**
     * Initiate ConnectedThread
     * @param mmSocket2
     */
    private void manageConnectedSocket(BluetoothSocket mmSocket2) {
		// TODO Auto-generated method stub
		
    	SneakyService.poThread = new PulseHackThread(SneakyService.socket);
    	SneakyService.poThread.start();
	}

	/** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
