package tele.com.election;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class SurveyClient extends AsyncTask{

    private int port;
    private WifiP2pDevice serverDevice;
    private WifiP2pInfo wifiinfo;

    public SurveyClient(int port, WifiP2pDevice device,WifiP2pInfo wifiinfo){
        this.port = port;
        this.wifiinfo = wifiinfo;
        this.serverDevice = device;
    }

    protected  void onPreExecute(){
        System.out.println("Receiving the message from the server!");
    }

    @Override
    protected String doInBackground(Object[] params) {
        try {
            InetAddress serverIP = InetAddress.getByName("192.168.49.1");
            Socket socket;
            ObjectInputStream mIIS;
            try{
                System.out.println("O Ip do servidor e a porta são: " + serverIP+ ":"+port);
                socket = new Socket(serverIP,port);
                System.out.println("Cá estou");
                mIIS = new ObjectInputStream(socket.getInputStream());
                System.out.println(mIIS.readObject());

                return "Client is working!";
            }
            catch(Exception e){
                System.out.println("Erro: estou aqui sozinho" + e.getMessage());
                System.out.println(e);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    return "Client isnt working!";
    }
}
