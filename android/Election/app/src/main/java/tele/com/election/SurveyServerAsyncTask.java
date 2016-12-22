package tele.com.election;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.AsyncTask;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class SurveyServerAsyncTask extends AsyncTask {

    private String title;
    private ArrayList<String> options;
    private ArrayList<String> deviceAddress = new ArrayList<String>();

    protected  void onPreExecute(){System.out.println("Sending the message to the client!");
    }

    @Override
    protected String doInBackground(Object[] params) {
        ObjectInputStream mIIS;
        int port = (int) params[0];
        Survey survey = (Survey) params[1];
        try{
            ServerSocket listener = new ServerSocket(port);
            Socket socket = listener.accept();
            String address = socket.getRemoteSocketAddress().toString();
            if(deviceAddress.contains(address)){
                System.out.println("Computando a resposta!");
                return "Resposta computada";
            }
            else{
                try{
                    ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                    output.flush();
                    output.writeObject(survey);
                    output.close();
                    deviceAddress.add(socket.getRemoteSocketAddress().toString());
                    return "Funcionou!";
                } finally {
                    socket.close();
                }
            }

        }
        catch (Exception e){
            System.out.println("Erro: no server " + e.getMessage());
            return null;
        }
    }


}
