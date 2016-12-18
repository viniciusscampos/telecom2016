package tele.com.election;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

public class AnswerSurveyActivity extends AppCompatActivity{

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private List peers = new ArrayList();
    private WifiP2pInfo mWifiinfo;
    private WifiP2pDevice mServerDevice;
    private boolean isConnected;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_in_progress);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new ClientBroadcastReceiver(mManager, mChannel, this);
        isConnected = false;

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

    }

    protected void onResume(){
        super .onResume();
        registerReceiver(mReceiver,mIntentFilter);

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener(){
            final Context context = getApplicationContext();

            @Override
            public void onSuccess(){

                Toast.makeText(context, "Busca iniciada com sucesso!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode){
                Toast.makeText(context, "A busca falhou!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected  void onPause(){
        super .onPause();
        unregisterReceiver(mReceiver);
    }


    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peersList) {
            peers.clear();
            final WifiP2pConfig config = new WifiP2pConfig();
            System.out.println("Listener.");
            for(WifiP2pDevice device : peersList.getDeviceList()){
                if(device.isGroupOwner()){
                    peers.add(device);
                    break;
                }
            }
            if(!peers.isEmpty() && !isConnected){
                System.out.println("Chamando toda hora.");
                connect();
            }
        }
    };

    public void connect(){
        final WifiP2pDevice device = (WifiP2pDevice) peers.get(0);

        final WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(AnswerSurveyActivity.this, config.toString(),
                        Toast.LENGTH_LONG).show();
                isConnected = true;

                receiveSurvey(device);
            }

            @Override
            public void onFailure(int reason) {
                isConnected = false;
            }
        });
    }

    /*
    private void callSurveyClientTask(String host, int port){
        SurveyClient surveyClientTask = new SurveyClient(host,port);
        surveyClientTask.dataTransfer();
    }
    */

    public void receiveSurvey(WifiP2pDevice device){
        System.out.println("Receive Survey.");
        //this.mWifiinfo = info;
        //this.mServerDevice = device;
        //SurveyClient surveyClientTask = new SurveyClient(8888,mWifiinfo,mServerDevice);
        SurveyClient surveyClientTask = new SurveyClient(8888,device);
        surveyClientTask.dataTransfer();
    }

}
