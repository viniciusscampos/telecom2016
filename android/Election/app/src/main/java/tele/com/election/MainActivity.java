package tele.com.election;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private BluetoothAdapter bluetoothAdapter;

    private ArrayList<BluetoothDevice> foundDevices = new ArrayList<BluetoothDevice>();

    private BluetoothSocket btSocket = null;
    private BluetoothDevice btDevice = null;

    private InputStream btInStream = null;
    private OutputStream btOutStream = null;

    private final UUID myuuid = UUID.randomUUID();

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
        final Context context = getApplicationContext();
        final int length = Toast.LENGTH_LONG;
        try{
            synchronized (this){
                wait(20000);
            }

            //////////////////////
            //   COLOCAR SPIN   //
            //////////////////////

            ListView lv = (ListView)findViewById(R.id.listview1);
            lv.setAdapter(new ArrayAdapter<BluetoothDevice>(this, android.R.layout.simple_list_item_1,foundDevices));

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    BluetoothDevice device = (BluetoothDevice) parent.getAdapter().getItem(position);

                    // Cria o socket utilizando o device escolhido e o mac address do device que realizou a busca
                    // Conecta o socket
                    try {
                        btSocket = device.createRfcommSocketToServiceRecord(myuuid);
                        btSocket.connect();
                        btInStream = btSocket.getInputStream();
                        btOutStream = btSocket.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Toast toast = Toast.makeText(context,device,length);
                    Toast toast = Toast.makeText(context,bluetoothAdapter.getBondedDevices().toString(),length);

                    toast.show();
                }
            });

            System.out.println(foundDevices.toString());
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
                //foundDevices.add(btdevice.getName() + "\n" + btdevice.getAddress());
                foundDevices.add(btdevice);
            }
        }
    };

    public void makeVisible(View view) {
        Intent isDiscoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        isDiscoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(isDiscoverable);
    }
}
