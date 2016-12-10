package tele.com.election;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
        Option optionOne = new Option(firstOptionEditText.getText().toString());
        options.add(optionOne);

        // Segunda opção
        EditText secondOptionEditText = (EditText) findViewById(R.id.surveyOptionTwo);
        Option optionTwo = new Option(secondOptionEditText.getText().toString());
        options.add(optionTwo);

        Survey survey = new Survey(title, options);


        /////////////// ANUNCIAR DISPOSITIVO ///////////////
        makeVisible(view);

        /////////////// ESPERAR CONEXOES + ENVIAR SURVEY + RECEBER RESPOSTAS ///////////////
        // Ver como faz
    }

    public void makeVisible(View view) {
        Intent isDiscoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        isDiscoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(isDiscoverable);
    }
}
