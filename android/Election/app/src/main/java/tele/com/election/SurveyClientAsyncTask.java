package tele.com.election;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class SurveyClientAsyncTask extends AsyncTask{

    private AnswerSurveyActivity activity = new AnswerSurveyActivity();
    private Survey survey;

    protected  void onPreExecute(){
        System.out.println("Receiving the message from the server!");
    }

    @Override
    protected Object doInBackground(Object[] params) {
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
                this.survey = (Survey) mIIS.readObject();
                //Survey survey = (Survey) mIIS.readObject();
                //System.out.println(mIIS.readObject());

                return this.survey;
            }
            catch(Exception e){
                System.out.println("Erro: estou aqui sozinho" + e.getMessage());
                System.out.println(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    return null;
    }

    protected void onPostExecute(Object result){
        this.activity.dataFromPostExecute(this.survey);
    }

}
