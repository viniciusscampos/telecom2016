package tele.com.election;

import android.os.AsyncTask;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class SurveyServerAsyncTask extends AsyncTask {

    private String title;
    private ArrayList<String> options;
    public SurveyServerAsyncTask(){

    }

    protected  void onPreExecute(){
        System.out.println("Sending the message to the client!");
    }

    @Override
    protected String doInBackground(Object[] params) {
        int port = (int) params[0];
        Survey survey = (Survey) params[1];
        try{
            ServerSocket listener = new ServerSocket(port);
            Socket socket = listener.accept();
            try{
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.flush();
                //output.writeObject("Oi");
                output.writeObject(survey);
                output.close();
                return "Funcionou!";
            } finally {
                socket.close();
            }
        }
        catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }


}
