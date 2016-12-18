package tele.com.election;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SurveyClient{

    private int port;
    private WifiP2pDevice serverDevice;
    private WifiP2pInfo wifiinfo;

    public SurveyClient(int port, WifiP2pDevice device){
        this.port = port;
        //this.wifiinfo = wifiinfo;
        this.serverDevice = device;
    }

    public void dataTransfer(){
        //InetAddress serverIP = wifiinfo.groupOwnerAddress;
        String serverIP =  serverDevice.deviceAddress;

        Socket clientSocket = null;
        InputStream input = null;

        try{
            System.out.println("O Ip do servidor é: " + serverIP);
            clientSocket = new Socket(serverIP, port);
            System.out.println("Input ...");
            input = clientSocket.getInputStream();
            System.out.println("O Input é: ");
            System.out.println(input.toString());
            input.close();
        }
        catch(Exception e){
            System.out.println("Erro: " + e.getMessage());
        }

    }

}
