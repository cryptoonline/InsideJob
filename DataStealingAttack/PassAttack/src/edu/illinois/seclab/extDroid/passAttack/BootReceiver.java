package edu.illinois.seclab.extDroid.passAttack;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Listen to the device boot event. Launch the background Sneaky Service once received.
 * @author soteris demetriou
 *
 */
public class BootReceiver extends BroadcastReceiver{

	private static final String TAG = "myBootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction()))
        {
           Log.e(TAG, "Boot completed"); 
           context.startService(new Intent(context, SneakyService.class));
        }
	}

}
