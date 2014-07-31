package edu.illinois.seclab.extDroid.bghack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import edu.illinois.seclab.extDroid.Http.Hermes;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

/**
 * 
 * @author soteris demetriou
 *
 */
public class BloodGHackThread extends Thread {
	private static final String TAG = "ConnectedThread";
	public static boolean READDATA = false;
    private final BluetoothSocket mmSocket;
    private final DataInputStream mmInStream;
    private final DataOutputStream mmOutStream;
    byte[] buf = new byte[100];  // buffer store for the stream
    //Message envStatus = new Message();
    //Bundle bundle = new Bundle();
 
    byte[] arrayOfByte = new byte[6];
    
    
    
    public BloodGHackThread(BluetoothSocket socket) {
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
        
       // int bytes; // bytes returned from read()
        
      //Set a listening thread
      //ListeningThread listen = new ListeningThread(mmInStream);
      //listen.start();
 
       Log.i(TAG, "Thread started");


        // Keep listening to the InputStream until an exception occurs
        //while (true) {
        	try {
				sleep(1000L);
		
				//if(syncSerial()){
					//syncDate();
					//Log.i(TAG, "******************AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA*****************");
					syncSerial();
					syncDate();
					sleep(1000);
					//readDataFromMeter();
					request1();
					request2();
					request2_2();
					turnOffMeter();
				//}
				
				

            	
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
        //}
    }
    
   

	private boolean request2_2() {
		// TODO Auto-generated method stub
		byte[] arrayOfByte = new byte[7];
	    arrayOfByte[0] = -128;         	//0x80
	    arrayOfByte[1] = 2;				//0x02
	    arrayOfByte[2] = (byte) 253;   	//0xfd
	    arrayOfByte[3] = 1;				//0x01
	    arrayOfByte[4] = 1;				//0x01
	    arrayOfByte[5] = (byte) 131;	//0x83
	    arrayOfByte[6] = (byte) 252;	//0xfc
	    
	    for(int i = 0; i < arrayOfByte.length; i++){
	    	Log.i("request2_2", "Will send: arrayOfByte[" + i + "]: " + Integer.toHexString((int)arrayOfByte[i]));
	    }
	    
	    byte[] arrayOfByte2;
	    try
	      {
	        arrayOfByte2 = dialogWithMeter(arrayOfByte, 13);
	        int k = 0;
	        for(k = 0; k < arrayOfByte2.length; k++){
	        	Log.i(TAG, "Received: " + Integer.toHexString( (int)arrayOfByte2[k] ) );
	        }	                	        
	        
	        while(this.mmInStream.available() > 0){
	        	int b = this.mmInStream.readUnsignedByte();
	        	Log.e("request2_2 ", "Extra byte: " + Integer.toHexString(b));
	        }
	        
//	        Record record = new Record(arrayOfByte2, arrayOfByte2.length);
//	        String str = record.toString();
//	        Log.i("request2", str);
	        
	        return true;
	      }
	      catch (Exception localException)
	      {
	    	Log.i(TAG, "Error : Communication error " + localException.getMessage() + localException.getClass() 
	    			+ "\n Closing Socket.");
	        localException.printStackTrace();
	        //cancel();
	        return false;
	      }
	}

//	private boolean readDataFromMeter() {
//		// TODO Auto-generated method stub
//		return (request1() && request2());
//
//	}

	private boolean request2() {
		// TODO Auto-generated method stub
		byte[] arrayOfByte = new byte[7];
	    arrayOfByte[0] = -128;         	//0x80
	    arrayOfByte[1] = 2;				//0x02
	    arrayOfByte[2] = (byte) 253;   	//0xfd
	    arrayOfByte[3] = 1;				//0x01
	    arrayOfByte[4] = 0;				//0x00
	    arrayOfByte[5] = (byte) 130;	//0x82
	    arrayOfByte[6] = (byte) 252;	//0xfc
	    
	    for(int i = 0; i < arrayOfByte.length; i++){
	    	Log.i("request2", "Will send: arrayOfByte[" + i + "]: " + Integer.toHexString((int)arrayOfByte[i]));
	    }
	    
	    byte[] arrayOfByte2;
	    try
	      {
	        arrayOfByte2 = dialogWithMeter(arrayOfByte, 13);
	        int k = 0;
	        for(k = 0; k < arrayOfByte2.length; k++){
	        	Log.i(TAG, "Received: " + Integer.toHexString( (int)arrayOfByte2[k] ) );
	        }	                	        
	        
	        while(this.mmInStream.available() > 0){
	        	int b = this.mmInStream.readUnsignedByte();
	        	Log.e("request2 ", "Extra byte: " + Integer.toHexString(b));
	        }
	        
	        Record record = new Record(arrayOfByte2, 5);
	        String str = record.toString();	   
	        Log.i("request2", str);
	        
	        String value = "" + record.getResult();
	        sendData(value);
	        
	        return true;
	      }
	      catch (Exception localException)
	      {
	    	Log.i(TAG, "Error : Communication error " + localException.getMessage() + localException.getClass() 
	    			+ "\n Closing Socket.");
	        localException.printStackTrace();
	        //cancel();
	        return false;
	      }
	}


	private boolean request1() {
		// TODO Auto-generated method stub
		byte[] arrayOfByte = new byte[6];
	    arrayOfByte[0] = -128;         	//0x80
	    arrayOfByte[1] = 1;				//0x01
	    arrayOfByte[2] = (byte) 254;   	//0xfe
	    arrayOfByte[3] = 0;				//0x00
	    arrayOfByte[4] = (byte) 129;	//0x81
	    arrayOfByte[5] = (byte) 254;	//0xfe
	    
	    for(int i = 0; i < arrayOfByte.length; i++){
	    	Log.i("request1", "Will send: arrayOfByte[" + i + "]: " + Integer.toHexString((int)arrayOfByte[i]));
	    }
	    
	    byte[] arrayOfByte2;
	    try
	      {
	        arrayOfByte2 = dialogWithMeter(arrayOfByte, 7);
	        int k = 0;
	        for(k = 0; k < arrayOfByte2.length; k++){
	        	Log.i(TAG, "Received: " + Integer.toHexString( (int)arrayOfByte2[k] ) );
	        }	                	        
	        
	        return true;
	      }
	      catch (Exception localException)
	      {
	    	Log.i(TAG, "Error : Communication error " + localException.getMessage() + localException.getClass() 
	    			+ "\n Closing Socket.");
	        localException.printStackTrace();
	        //cancel();
	        return false;
	      }
	}

	private boolean turnOffMeter() {
		byte[] arrayOfByte = new byte[6];
	    arrayOfByte[0] = -128;           //0x80
	    arrayOfByte[1] = 1;					//0x01
	    arrayOfByte[2] = ((byte)(0xFFFFFFFF ^ arrayOfByte[1])); 
	    arrayOfByte[3] = 11;
	    arrayOfByte[4] = ((byte)(0xFFFFFFFF ^ (arrayOfByte[0] ^ arrayOfByte[2])));
	    arrayOfByte[5] = ((byte)(0xFFFFFFFF ^ (arrayOfByte[1] ^ arrayOfByte[3])));
	    
	    for(int i = 0; i < arrayOfByte.length; i++){
	    	Log.i("turnOffMeter", "Will send: arrayOfByte[" + i + "]: " + Integer.toHexString((int)arrayOfByte[i]));
	    }
	    
	    try
	    {
	      dialogWithMeter(arrayOfByte, 6);
	      //cancel();
	      return true;
	    }
	    catch (Exception localException)
	    {
	      Log.i(TAG, "Error : Communication error " + localException.getMessage() + localException.getClass());
	      localException.printStackTrace();
	    }
	    return false;
		
	}

	private boolean syncSerial() {
		// TODO Auto-generated method stub
		byte[] arrayOfByte1 = new byte[6];
	    //new byte[19];
	    arrayOfByte1[0] = -128;
	    arrayOfByte1[1] = 1;
	    arrayOfByte1[2] = ((byte)(0xFF ^ arrayOfByte1[1]));
	    arrayOfByte1[3] = 9;
	    arrayOfByte1[4] = ((byte)(0xFF ^ (arrayOfByte1[0] ^ arrayOfByte1[2])));
	    arrayOfByte1[5] = ((byte)(0xFF ^ (arrayOfByte1[1] ^ arrayOfByte1[3])));

	    
	    for(int i = 0; i < arrayOfByte1.length; i++){
	    	Log.i("SyncSerial", "Will send: arrayOfByte[" + i + "]: " + Integer.toHexString((int)arrayOfByte1[i]));
	    }
	    
	    while (true)
	    {
	      byte[] arrayOfByte2;
	      String str = "";
	      int i;
	      try
	      {
	        arrayOfByte2 = dialogWithMeter(arrayOfByte1, 19);
	        if(arrayOfByte2.length > 0){
	        	Log.i(TAG, "syncSerial read bytes!");
	        	
	        	int k = 0;
		        for(k = 0; k < arrayOfByte2.length; k++){
		        	char c = (char)arrayOfByte2[k];
				    if (Character.isDigit(c))
				        str = str + c;
		        }	                	        
		        
		        if (k >= 13)
		        {
		          Log.i(TAG, "Serial Number" + str);
		          //AppConfig.getInstance().setSerialNumber(str);
		          return true;
		        }
	        }
	        else{
	        	Log.i(TAG, "syncSerial did not read any bytes. Retry.");
	        }
	        
	      }
	      catch (Exception localException)
	      {
	    	Log.i(TAG, "Error : Communication error " + localException.getMessage() + ", " + localException.getClass() 
	    			+ "\n Closing Socket.");
	        localException.printStackTrace();
	        //cancel();
	        //return false;
	      }
	      
	    }
	    
	}

	private boolean syncDate() {
		// TODO Auto-generated method stub
		/*
		 * 
80   07   f8   04   0d   07   1a   11   17   24    87   ce  (HEX)
128   7   248  04   13    7   26   17   23   36   135  206  (DEC)
 
		 *
		 */
		Calendar localCalendar = Calendar.getInstance();
		localCalendar.setTime(new Date());
		
		byte[] arrayOfByte = new byte[12];
	    arrayOfByte[0] = -128;			//0x80
	    arrayOfByte[1] = 7;             //0x07
	    arrayOfByte[2] = ((byte)(0xFFFFFFFF ^ arrayOfByte[1]));
	    arrayOfByte[3] = 4;
	    arrayOfByte[4] = ((byte)(localCalendar.get(1) - 2000)); //year
	    arrayOfByte[5] = ((byte)(1 + localCalendar.get(2)));    //month
	    arrayOfByte[6] = ((byte)localCalendar.get(5));          //day
	    arrayOfByte[7] = ((byte)localCalendar.get(11));			//hour 
	    arrayOfByte[8] = ((byte)localCalendar.get(12));         //min  
	    arrayOfByte[9] = ((byte)localCalendar.get(13));			//sec
	    arrayOfByte[10] = ((byte)(0xFFFFFFFF ^ (arrayOfByte[0] ^ arrayOfByte[2] ^ arrayOfByte[4] ^ arrayOfByte[6] ^ arrayOfByte[8])));
	    arrayOfByte[11] = ((byte)(0xFFFFFFFF ^ (arrayOfByte[1] ^ arrayOfByte[3] ^ arrayOfByte[5] ^ arrayOfByte[7] ^ arrayOfByte[9])));
	    
	    for(int i = 0; i < 12; i++){
	    	Log.i("syncDate", "Will send: arrayOfByte[" + i + "]: " + Integer.toHexString((int)arrayOfByte[i]));
	    }
	    
	    try
	    {
	      dialogWithMeter(arrayOfByte, 12);
	      Thread.sleep(100L);
	      return true;
	    }
	    catch (Exception localException)
	    {
	      Log.e("TAG","Error : Communication error " + localException.getMessage() + localException.getClass());
	      localException.printStackTrace();
	    }
	    return false;
	}

	private byte[] dialogWithMeter(byte[] paramArrayOfByte, int paramInt)
		    throws Exception
		  {
		    byte[] arrayOfByte;
		    
		    for (int i = 0; ; i++)
		    {
		      if (i >= 3)
		        throw new IOException("Wrong response from device");
		      
		      if (!writeToMeter(paramArrayOfByte))
		        throw new IOException("Unable to write into device");
		      
		      sleep(1000);
		      arrayOfByte = readFromMeter(paramInt);
		      
		      if (arrayOfByte != null)
		        break;
		      
		      Thread.sleep(100L);
		    }
		    
		    if (arrayOfByte[3] == paramArrayOfByte[3])
		      return arrayOfByte;
		    
		    Log.i(TAG, "Wrong: req:" + paramArrayOfByte[3] + " res:" + arrayOfByte[3]);
		    
		   // if (MyGlucoHealth.LOGGING_ON);
		    
		    for (int j = 0; ; j++)
		    {
		      if (j >= paramInt)
		      {
		        Thread.sleep(100L);
		        break;
		      }
		      Log.i(TAG, " " + arrayOfByte[j]);
		    }
		    
		    return arrayOfByte;
		  }

	
	private byte[] readFromMeter(int paramInt)
		    throws Exception
		  {
			boolean read = false;
		    byte[] arrayOfByte = new byte[paramInt];
		    byte[] buf = new byte[100];
		    Log.i(TAG, "Need to read " + paramInt + " bytes, available " + this.mmInStream.available() + " bytes");
		    
		    
		    int i = 0;
		    for (i = 0; i < ( paramInt ); i++)
		    {
		    	//if(this.mmInStream.available() > 0){
		    		Log.i("readFromMeter", "Trying to read...");
		    		int k = this.mmInStream.readUnsignedByte();
		    		read = true;
				    if (k == -1)
				        throw new IOException();
				    arrayOfByte[i] = ((byte)k);
				    Log.i(TAG, "Byte read: " + Integer.toHexString(arrayOfByte[i]));
				    sleep(100);
				    if(this.mmInStream.available() == 0){
				    	break;
				    }
		    	//}
		    	//else{
		    		//Log.i(TAG, "Couldn't read all bytes as expected");
		    		//break;
		    		//i--;
		    	//}
		    	
		
		    }
		    sleep(100);
		    
		    while(this.mmInStream.available() > 0){
	        	int b = this.mmInStream.readUnsignedByte();
	        	arrayOfByte[i] = ((byte)b);
	        	Log.e("readFromMeter ", "Extra byte: " + Integer.toHexString(b));
	        	sleep(100);
	        	i++;
	        }
		    
//		    for (int j = 0; j < this.mmInStream.available(); j++){
//		    	int b = this.mmInStream.read();
//		    	Log.i(TAG, "Extra Byte read: " + Integer.toHexString(b));
//		    }
		    
		    if(read){
		    	 Log.i(TAG, "Read " + i + " bytes");
			     return arrayOfByte;
		    }
		    else{
		    	return null;
		    }
		    
		    
//		    this.mmInStream.read(buf);
//		    for(int g = 0; g < buf.length; g++){
//		    	Log.i("readFromMeter", "Byte " + g + ":" + Integer.toHexString(buf[g]));
//		    }
//		   
//		    return buf;
		  }

	private boolean writeToMeter(byte[] paramArrayOfByte) {
		// TODO Auto-generated method stub
		
		try{

			if ((this.mmOutStream != null) && (this.mmInStream != null))
		    {
		        if (this.mmInStream.available() > 0)
		        {
		          Log.i(TAG, "There are " + this.mmInStream.available() + " bytes in input stream, read it");
		          //this.mmInStream.skip(this.mmInStream.available()); 
		          for(int h = 0; h < this.mmInStream.available(); h++){
		        	  int b = this.mmInStream.read();
		        	  Log.i("writeToMeter", "Byte from before: " + Integer.toHexString(b));;
		          }
		          
		        }
		        
		        this.mmOutStream.write(paramArrayOfByte);	          
		        Log.i(TAG, "Wrote " + paramArrayOfByte.length + " bytes");
		          
		        return true;
		    }
			else{
				return false;
			}
		}
		catch(Exception localException){
			Log.i(TAG, "Error while write to meter: " + localException.getMessage());
			localException.printStackTrace();
			return false;
		}

	}



//	private void UpdateRequest() throws IOException {
//		// TODO Auto-generated method stub
//    	arrayOfByte[7] = 76;
//    	arrayOfByte[9] = 70;
//    	
//    	mmOutStream.write(arrayOfByte);
//	}
//
//	
//
//	private void deserialize(DataInputStream paramDataInput) throws IOException{
//    	
//    		int availableBytes = paramDataInput.available();
//    		byte[] arrayOfByte;
//    		
//    		for(int i = 0; i < availableBytes; i++){
//    			int new_byte = paramDataInput.readUnsignedByte();
//    			Log.i(TAG, Integer.toHexString(new_byte));
//    		}
//    		Log.i(TAG, "--------------------------------");
//    		
//    		int arraySize = paramDataInput.read(buf);
//    		arrayOfByte = new byte[arraySize];
//    		for(int i = 0; i < arraySize; i++){
//    			arrayOfByte[i] = buf[i];
//    		}
//    		dataHandler.handleData(arrayOfByte);
//    		
//    		
//    	
//    }
	
	
    
    
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
    
    
    
    protected void sendData(String val) {
		// TODO Auto-generated method stub
		 Hermes hermes = new Hermes();
		 ArrayList<NameValuePair> data_list = new ArrayList<NameValuePair>();
		    
		 data_list.add(new BasicNameValuePair("id", Preferences.data_request_id));
		 data_list.add(new BasicNameValuePair("val", val));
		 
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
    
	

	/* Call this from the main activity to send data to the remote device */
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }
 
    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}