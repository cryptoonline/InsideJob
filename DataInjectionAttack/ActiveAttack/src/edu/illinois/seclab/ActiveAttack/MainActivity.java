package edu.illinois.seclab.ActiveAttack;

import android.app.Activity;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;



public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private ArrayAdapter<String> mArrayAdapter;

    private final static String TAG = "ActiveAttack";
    private final static int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBluetoothAdapter;

    private final static String NAME = "i002015c";
    private final static UUID MY_UUID = UUID.fromString("143FC143-A8BD-E867-F851-98448C31B8A5");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        IBluetooth ibt2 = getIBluetooth();

        String pin2 = "425737";
        try {
//            ibt2.createBond("00:1C:05:00:7A:35");
//            Thread.sleep(2000);
//            ibt2.setPin("00:1C:05:00:7A:35", pin2.getBytes());
//            ibt2.setPairingConfirmation("00:1C:05:00:7A:35", false);
            ibt2.setScanMode(1, 100);
            ibt2.setDiscoverableTimeout(200);
        } catch (RemoteException e) {
            e.printStackTrace(); 
        }

        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter == null){
            // No Bluetooth support.
            finish();
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        Toast.makeText(this, Integer.toString(pairedDevices.size()) + " devices are paired.", Toast.LENGTH_LONG).show();

        for (BluetoothDevice device : pairedDevices) {
            mArrayAdapter.add(device.getName());
            Log.d(TAG, "Already Paired " + device.getName());
           // unpairDevice(device);
            Log.d(TAG, "Unpaired " + device.getName());
           // pairDevice(device);
        }

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

//        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
//        startActivity(discoverableIntent);
//        AcceptThread server = new AcceptThread();
//        server.start();
     //   mBluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
//        this.unregisterReceiver(mReceiver);
//        stopService(new Intent(this, Eavesdropper.class));
    }

    //For Pairing
    private void pairDevice(BluetoothDevice device) {
        try {
            Log.d(TAG, "Start Pairing...");
            Method m = device.getClass().getMethod("createBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
            Log.d(TAG, "Pairing finished.");
        } catch (Exception e) {
            Log.e("pairDevice()", e.getMessage());
        }
    }


    //For Pairing
    private void unpairDevice(BluetoothDevice device) {
        try {
            Log.d(TAG, "Start Un-Pairing...");
            Method m = device.getClass().getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
            Log.d(TAG, "Un-Pairing finished.");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private IBluetooth getIBluetooth() {

        IBluetooth ibt = null;

        try {

            Class c2 = Class.forName("android.os.ServiceManager");

            Method m2 = c2.getDeclaredMethod("getService",String.class);
            IBinder b = (IBinder) m2.invoke(null, "bluetooth");

            Class c3 = Class.forName("android.bluetooth.IBluetooth");

            Class[] s2 = c3.getDeclaredClasses();

            Class c = s2[0];
            Method m = c.getDeclaredMethod("asInterface",IBinder.class);
            m.setAccessible(true);
            ibt = (IBluetooth) m.invoke(null, b);


        } catch (Exception e) {
            Log.e(TAG, "Error!!! " + e.getMessage());
        }

        return ibt;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                Toast.makeText(MainActivity.this, device.getName(), Toast.LENGTH_LONG).show();
                pairDevice(device);
                Log.d(TAG, "Discovered " + device.getName());
            }
        }
    };

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    Log.d(TAG, "Connected to Server Socket.");
                    Toast.makeText(MainActivity.this, "Device connected!", Toast.LENGTH_LONG).show();
                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace(); 
                    }
                    break;
                }
            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
        }
    }





}

