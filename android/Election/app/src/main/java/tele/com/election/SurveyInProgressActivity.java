package tele.com.election;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

public class SurveyInProgressActivity extends AppCompatActivity{

    private  WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private List peers = new ArrayList();
    private String title;
    private ArrayList<String> options;
    private Survey survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_in_progress);

        Bundle bundle = getIntent().getExtras();
        this.title = bundle.getString("title");
        this.options = bundle.getStringArrayList("options");
        this.survey = constructSurvey(this.title,this.options);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new ServerBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

    }

    protected void onResume(){
        super .onResume();
        registerReceiver(mReceiver,mIntentFilter);
        callSurveyAsyncTask(this.survey);
        //callSurveyAsyncTask(this.title,this.options);

        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            final Context context = getApplicationContext();

            @Override
            public void onSuccess(){
                Toast.makeText(context, "O Grupo foi criado com sucesso!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reasonCode){
                //Toast.makeText(context, "O Grupo não foi criado! Código de erro: " + reasonCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected  void onPause(){
        super .onPause();
        unregisterReceiver(mReceiver);
    }

    /*
    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peersList) {
            peers.clear();
            peers.addAll(peersList.getDeviceList());
            //connect();
        }
    };

    public void connect(){
        WifiP2pDevice device = (WifiP2pDevice) peers.get(0);

        final WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(SurveyInProgressActivity.this, config.toString(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(SurveyInProgressActivity.this, config.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    */

    private Survey constructSurvey(String title, ArrayList<String> opts){
        ArrayList<Option> options = new ArrayList<Option>();
        for(String option : opts){
            options.add(new Option(option));
        }
        return new Survey(title,options);
    }


    private void callSurveyAsyncTask(Survey survey){
        SurveyServerAsyncTask surveyServerAsyncTask = new SurveyServerAsyncTask();
        surveyServerAsyncTask.execute(8888,survey);
    }


    /*
    private void callSurveyAsyncTask(String title, ArrayList<String> options){
        SurveyServerAsyncTask surveyServerAsyncTask = new SurveyServerAsyncTask();
        surveyServerAsyncTask.execute(8888,title,options);
    }
    */

}
