package tele.com.election;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;

import java.net.UnknownHostException;


public class ClientBroadcastReceiver extends BroadcastReceiver{

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private AnswerSurveyActivity mActivity;

    public ClientBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                   AnswerSurveyActivity activity){
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if(mManager!=null){
                mManager.requestPeers(mChannel, mActivity.peerListListener);
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            NetworkInfo networkState = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            WifiP2pInfo wifiinfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
            WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);

            if(networkState.isConnected()){
                System.out.println("NetworkState isConected");
                System.out.println(wifiinfo);
                try {
                    mActivity.receiveSurvey(device,wifiinfo,8888);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }


}
