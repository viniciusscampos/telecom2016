package tele.com.election;

import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SurveyClientAsyncTask extends AsyncTask{

    private AnswerSurveyActivity activity;
    private Survey survey;
    private Option chosenOption;
    public int port;
    public WifiP2pInfo wifiinfo;

    protected  void onPreExecute(){
        System.out.println("Receiving the message from the server!");
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            this.wifiinfo = (WifiP2pInfo) params[0];
            this.port = (int) params[1];
            this.activity = (AnswerSurveyActivity) params[2];
            InetAddress serverIP = wifiinfo.groupOwnerAddress;
            Socket socket = new Socket(serverIP,port);
            ObjectInputStream mIIS;
            try{
                mIIS = new ObjectInputStream(socket.getInputStream());
                this.survey = (Survey) mIIS.readObject();
                return this.survey;
            }
            catch(Exception e){
                System.out.println("Erro: no client" + e.getMessage());
                System.out.println(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    return null;
    }

    protected  void sendAnswer(Option option) throws IOException {
        InetAddress serverIP = wifiinfo.groupOwnerAddress;
        Socket socket = new Socket(serverIP,port);
        try{
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            output.writeObject(option);
            output.close();
        } finally {
            socket.close();
        }
    }

    protected void onPostExecute(Object result){
        System.out.println("onPostExecute");
        LinearLayout linearLayout = (LinearLayout) this.activity.findViewById(R.id.activity_answer_survey);
        TextView titleLabel = new TextView(this.activity);
        titleLabel.setText("Título:");
        TextView titleContent = new TextView(this.activity);
        titleContent.setText(this.survey.getTitle());
        TextView optionsLabel = new TextView(this.activity);
        optionsLabel.setText("Alternativas:");

        linearLayout.addView(titleLabel);
        linearLayout.addView(titleContent);
        linearLayout.addView(optionsLabel);

        for(final Option option : this.survey.getResult()){
            Button alternative = new Button(this.activity);
            alternative.setText(option.text);
            linearLayout.addView(alternative);

            alternative.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    activity.dataFromPostExecute(option);
                }
            });
        }
    }

}
