package edu.illinois.seclab.extDroid.bm;

import java.io.IOException;
import java.util.UUID;

import org.apache.http.conn.ConnectTimeoutException;

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

public class BMConnectThread extends Thread {
	private static final String TAG = "BMConnectThread";
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mBluetoothAdapter;
    private ExtDevice extDevice;
    public static Handler handler;
    private int connect_mode;
    
    /**
     * 
     * @param device
     * @param extDevice
     * @param myUuid
     * @param bluetoothAdapter
     * @param connect_mode
     * @author soteris demetriou
     */
    public BMConnectThread(BluetoothDevice device, ExtDevice extDevice, UUID myUuid, 
    		BluetoothAdapter bluetoothAdapter, int connect_mode) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;
        mBluetoothAdapter = bluetoothAdapter;
        this.extDevice = extDevice;
        this.connect_mode = connect_mode;
        Log.i(TAG, "mode=" + this.connect_mode);
        
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
        	if (device.getBondState() != 12){
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
        SneakyService.socket = tmp;
        //SneakyService.socket = mmSocket;
    }
 
    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();
 
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
        	SneakyService.socket.connect();
            //MainActivity.connected = true;
        	extDevice.setConStatus(DeviceStatus.CONNECTED_BY_US);
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
        	Log.i(TAG, "Connection Failed");
        	extDevice.setConStatus(DeviceStatus.UNKNOWN);
        	
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
		
    	if(SneakyService.bmThread!= null){
    		SneakyService.bmThread.cancel();
    	}
    	
    	SneakyService.bmThread = new BodyHackThread(mmSocket2);
    	SneakyService.bmThread.start();
	}

	/** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
        	SneakyService.socket.close();
        } catch (IOException e) { }
    }
}

