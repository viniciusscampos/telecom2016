package tele.com.election;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SurveyClientAsyncTask extends AsyncTask{

    private AnswerSurveyActivity activity;
    private Survey survey;
    private Option chosenOption;

    protected  void onPreExecute(){
        System.out.println("Receiving the message from the server!");
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            WifiP2pInfo wifiinfo = (WifiP2pInfo) params[0];
            int port = (int) params[1];
            this.activity = (AnswerSurveyActivity) params[2];
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
