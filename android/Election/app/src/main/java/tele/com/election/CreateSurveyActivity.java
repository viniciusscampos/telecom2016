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

        ArrayList<String> options = new ArrayList<String>();
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
            String optionOne = firstOptionEditText.getText().toString();
            options.add(optionOne);
            String optionTwo = secondOptionEditText.getText().toString();
            options.add(optionTwo);

            /////////////// PASSA PARA ACTIVITY DE PROGRESSO DA ENQUETE ///////////////
            Intent intent = new Intent(this, SurveyInProgressActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("options",options);
            startActivity(intent);

        }
    }

    public void addOption(View view){
    }

    public void delteOption(View view){
    }
}
