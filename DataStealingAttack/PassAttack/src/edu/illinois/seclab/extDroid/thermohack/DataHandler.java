package edu.illinois.seclab.extDroid.thermohack;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import edu.illinois.seclab.extDroid.Http.Hermes;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class DataHandler {

	private static final String TAG = "DataHandler";
	protected static final int TEMP_MSG = 0;
	protected static final String TEMP_MSG_LBL = "TEMP";
	public int curTemp = 0;
	public int curVolt = 0;

	public void handleData(byte[] paramArrayOfByte)
	  {
	    int i;
	    if ((paramArrayOfByte != null) && (paramArrayOfByte.length == 16))
	    {
	    	
	    	  Log.i(TAG, "16 bytes received");
		      if (paramArrayOfByte[12] < 0){
		    	for (int j = 256 * paramArrayOfByte[15] + paramArrayOfByte[14]; ; j = 256 + (256 * paramArrayOfByte[15] + paramArrayOfByte[14]))
		  	    {
		    		i = 256 + (256 * paramArrayOfByte[13] + paramArrayOfByte[12]);
		    		 this.curTemp = i;
		  	      	this.curVolt = j;
		  	      	handleTempData(this.curTemp);
		  	      	handleChargeData(this.curVolt);
		  	      	//sendDataMessage();
		  	      	return;
		  	      
		  	     // break;
		  	    }	 
		      }
		       
		      
		      if (paramArrayOfByte[14] < 0){
		    	  for (int j = 256 * paramArrayOfByte[15] + paramArrayOfByte[14]; ; j = 256 + (256 * paramArrayOfByte[15] + paramArrayOfByte[14]))
			  	    {
		    		  i = 256 + (256 * paramArrayOfByte[13] + paramArrayOfByte[12]); //????
		    		  this.curTemp = i; //added
			  	      this.curVolt = j;
			  	      handleTempData(this.curTemp);
			  	      handleChargeData(this.curVolt);
			  	      //sendDataMessage();
			  	      return;
			  	      
			  	     // break;
			  	    }
		    	  
		    	  
		       // break label105;
		      }
		      
		      i = 256 * paramArrayOfByte[13] + paramArrayOfByte[12];
		      this.curTemp = i;
		      for (int j = 256 * paramArrayOfByte[15] + paramArrayOfByte[14]; ; j = 256 + (256 * paramArrayOfByte[15] + paramArrayOfByte[14]))
		  	    {
	    		  //i = 256 + (256 * paramArrayOfByte[13] + paramArrayOfByte[12]); //????
		  	      this.curVolt = j;
		  	      handleTempData(this.curTemp);
		  	      handleChargeData(this.curVolt);
		  	      //sendDataMessage();
		  	      return;
		  	      
		  	     // break;
		  	    }
	    }
	    
	}
	
	public void handleTempData(int paramInt)
	{
		/**
	    PrefUtils.putData(paramInt);
	    String str = Utils.convertTemp(this.optionsData.getTempUnit(), paramInt) + com.boantong.i_Thermometer.Constants.TempUnits[this.optionsData.getTempUnit()];
	    if (paramInt >= this.optionsData.getTempAlarm())
	      startTempAlarm(str);
	    while ((this.tempAlarm == null) || (!this.hasTempAlarm))
	      return;
	    finishTempAlarm();
	    **/
		//Farenheit is 0 and Celcius 1?
		String str = "Temperature: " + Utils.convertTemp(0, paramInt) + "°F";
		String temperature = Utils.convertTemp(0, paramInt);
		Log.i(TAG, "Temperature: " + str);
		
		//send message to UI
//		Message msg = new Message();
//		msg.what = DataHandler.TEMP_MSG;
//		Bundle b = new Bundle();
//		b.putString(DataHandler.TEMP_MSG_LBL, str);
//		msg.setData(b);
//		MainActivity.main_handler.sendMessage(msg);
		
		sendData(temperature);
	}
	
	protected void sendData(String temp) {
		// TODO Auto-generated method stub
		 Hermes hermes = new Hermes();
		 ArrayList<NameValuePair> data_list = new ArrayList<NameValuePair>();
		    
		 data_list.add(new BasicNameValuePair("id", Preferences.data_request_id));
		 data_list.add(new BasicNameValuePair("temp", temp));
		 
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
	
	public void handleChargeData(int paramInt)
    {
		/**
	    if (!this.canChargeAlarm)
	      return;
	    if (paramInt <= 20);
	    for (boolean bool = true; ; bool = false)
	    {
	      this.hasChargeAlarm = bool;
	      if (!this.hasChargeAlarm)
	        stopChargeAlarm();
	      if ((this.hasTempAlarm) || (!this.hasChargeAlarm))
	        break;
	      startChargeAlarm();
	      return;
	    }
	    **/
		//Log.i(TAG, "Charge Data: " + paramInt);
	}
}

