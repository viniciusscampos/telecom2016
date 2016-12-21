package tele.com.election;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SurveyClient extends AsyncTask{

    protected  void onPreExecute(){
        System.out.println("Receiving the message from the server!");
    }

    @Override
    protected String doInBackground(Object[] params) {
        try {
            WifiP2pDevice device = (WifiP2pDevice) params[0];
            WifiP2pInfo wifiinfo = (WifiP2pInfo) params[1];
            int port = (int) params[2];
            InetAddress serverIP = wifiinfo.groupOwnerAddress;
            Socket socket;
            ObjectInputStream mIIS;
            try{
                socket = new Socket(serverIP,port);
                mIIS = new ObjectInputStream(socket.getInputStream());
                System.out.println(mIIS.readObject());

                return "Client is working!";
            }
            catch(Exception e){
                System.out.println("Erro: estou aqui sozinho" + e.getMessage());
                System.out.println(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    return "Client isnt working!";
    }
}
