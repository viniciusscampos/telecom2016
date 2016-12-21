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
        //String title = (String) params[1];
        //ArrayList<String> options = (ArrayList<String>) params[2];
        Survey survey = (Survey) params[1];
        try{
            ServerSocket listener = new ServerSocket(port);
            Socket socket = listener.accept();
            try{
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.flush();
                output.writeObject(survey);
                //output.writeObject(title);
                //output.writeObject(options);
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
