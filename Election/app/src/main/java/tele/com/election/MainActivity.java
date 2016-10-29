package tele.com.election;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayList<String> foundDevices = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(this.bluetoothAdapter!=null){
            if(!this.bluetoothAdapter.isEnabled()){
                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
            }
            bluetoothAdapter.startDiscovery();
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(bdReceiver,filter);
        }
    }

    @Override
    protected void onDestroy(){
        unregisterReceiver(bdReceiver);
        super.onDestroy();
    }

    public void listDevices(View view) {
        Context context = getApplicationContext();
        int length = Toast.LENGTH_LONG;
        try{
            synchronized (this){
                wait(10000);
            }
            Toast toast = Toast.makeText(context,foundDevices.toString(),length);
            toast.show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    final BroadcastReceiver bdReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice btdevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                foundDevices.add(btdevice.getName() + "\n" + btdevice.getAddress());
            }
        }
    };

    public void makeVisible(View view) {
        Intent isDiscoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        isDiscoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(isDiscoverable);
    }
}
