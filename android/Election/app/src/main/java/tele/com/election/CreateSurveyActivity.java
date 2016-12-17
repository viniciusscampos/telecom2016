package tele.com.election;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateSurveyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey);
    }

    public void createSurvey(View view){

        /////////////// CRIAR SURVEY ///////////////

        ArrayList<Option> options = new ArrayList<Option>();
        // Captura o título da enquete inserido pelo usuário
        EditText titleEditText = (EditText) findViewById(R.id.surveyTitle);
        String title = titleEditText.getText().toString();

        // Captura as opções da enquete inseridas pelo usuário
        // Primeira opção
        EditText firstOptionEditText = (EditText) findViewById(R.id.surveyOptionOne);
        EditText secondOptionEditText = (EditText) findViewById(R.id.surveyOptionTwo);
        String firstOptionText = firstOptionEditText.getText().toString();
        String secondOptionText = secondOptionEditText.getText().toString();

        if( (title.isEmpty() || title == null ) ||
            (firstOptionText.isEmpty() || firstOptionText == null) ||
            (secondOptionText.isEmpty() || secondOptionText == null))
        {
            final Context context = getApplicationContext();
            final int length = Toast.LENGTH_LONG;
            String error = "Preencha o título e as opções antes de tentar criar a enquete!";
            Toast toast = Toast.makeText(context,error,length);
            toast.show();
        }
        else
        {
            Option optionOne = new Option(firstOptionEditText.getText().toString());
            options.add(optionOne);
            Option optionTwo = new Option(secondOptionEditText.getText().toString());
            options.add(optionTwo);
            Survey survey = new Survey(title, options);


            /////////////// ANUNCIAR DISPOSITIVO ///////////////
            makeVisible(view);


            /////////////// PASSA PARA ACTIVITY DE PROGRESSO DA ENQUETE ///////////////
            Intent intent = new Intent(this, SurveyInProgressActivity.class);
            startActivity(intent);

            /////////////// ESPERAR CONEXOES + ENVIAR SURVEY + RECEBER RESPOSTAS ///////////////
            // Ver como faz
        }
    }

    public void makeVisible(View view) {
        Intent isDiscoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        isDiscoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(isDiscoverable);
    }

    public void addOption(View view){
    }

    public void delteOption(View view){
    }
}
