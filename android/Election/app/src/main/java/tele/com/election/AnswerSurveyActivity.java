package tele.com.election;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.ArrayList;

public class AnswerSurveyActivity extends AppCompatActivity{

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private List peers = new ArrayList();
    private boolean isConnected;
    private Survey survey;
    private TextView titleView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_survey);

        titleView = (TextView)findViewById(R.id.survey_title_view);
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

        if (this.survey != null) {
            this.titleView.setText(this.survey.getTitle());
        }
        //setContentView(R.layout.activity_answer_survey);
        //TextView tv1 = (TextView)findViewById(R.id.survey_title_view);
        //if(this.survey!=null){
         //   tv1.setText(this.survey.getTitle());
        //}

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
            System.out.println("Listener.");
            for(WifiP2pDevice device : peersList.getDeviceList()){
                if(device.isGroupOwner()){
                    peers.add(device);
                    break;
                }
            }
            if(!peers.isEmpty() && !isConnected){
                connect();
            }
        }
    };

    public void connect(){
        final WifiP2pDevice device = (WifiP2pDevice) peers.get(0);

        final WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        connectionActive(device);

        if(!isConnected){
            mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AnswerSurveyActivity.this, config.toString(),
                            Toast.LENGTH_LONG).show();
                    isConnected = true;

                }

                @Override
                public void onFailure(int reason) {
                    isConnected = false;
                    Toast.makeText(AnswerSurveyActivity.this, "Não foi possível se conectar ao dispositivo!",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            disconnect();
        }
    }

    public void disconnect(){
        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(AnswerSurveyActivity.this, "O grupo ao qual estava conectado foi removido com sucesso!",
                        Toast.LENGTH_LONG).show();
                isConnected = false;
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(AnswerSurveyActivity.this, "Não foi possível removê-lo do grupo",
                        Toast.LENGTH_LONG).show();
                isConnected = true;
            }
        });
    }

    public void connectionActive(WifiP2pDevice device){
        final WifiP2pDevice serverDevice = device;
        mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                if(group!= null){
                    if(group.getOwner().equals(serverDevice)){
                        isConnected = true;
                    }
                    else{
                        isConnected = false;
                    }
                }
                else{
                    isConnected = false;
                }
            }
        });
    }

    public void receiveSurvey(WifiP2pInfo wifiinfo, int port) {
        System.out.println("Receiving Survey.");
        SurveyClientAsyncTask surveyClientAsyncTask = new SurveyClientAsyncTask();
        surveyClientAsyncTask.execute(wifiinfo,port,this);
    }

    public void dataFromPostExecute(Option option){
        //this.survey = survey;
        System.out.println("A opção escolhida foi: "+ option.text);
    }

}
