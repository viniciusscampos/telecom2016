package tele.com.election;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SurveyServerAsyncTask extends AsyncTask {

    private String title;
    private ArrayList<String> options;
    public SurveyServerAsyncTask(){

    }
    @Override
    protected String doInBackground(Object[] params) {
        try{
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket client = serverSocket.accept();

            ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            output.writeObject("Oi");
            output.close();
            client.close();

            return "Funcionou!";

        }
        catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }
}
