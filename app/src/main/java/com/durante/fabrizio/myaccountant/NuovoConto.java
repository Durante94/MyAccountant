package com.durante.fabrizio.myaccountant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NuovoConto extends AppCompatActivity {

    DBHelper myDB;
    EditText NomeConto, Bil;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_conto);

        myDB=new DBHelper(this);
        NomeConto=(EditText)findViewById(R.id.etxtNomeConto);
        Bil=(EditText)findViewById(R.id.etxtBilancio);
        btn=(Button)findViewById(R.id.btnNuovoConto);

        addListener();
    }

    private void addListener(){
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tempBil=Bil.getText().toString();
                        String tempNome=NomeConto.getText().toString();

                        if(tempBil.length()<=0){
                            Toast.makeText(getApplicationContext(), "Inserire un importo iniziale, anche 0", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(tempNome.length()<=0){
                            Toast.makeText(getApplicationContext(), "Inserire un nome per il Conto", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(tempNome.length()>20){
                            Toast.makeText(getApplicationContext(), "Inserire un nome più corto", Toast.LENGTH_LONG).show();
                            return;
                        }

                        float floatBil=0;
                        try {
                            floatBil=Float.parseFloat(tempBil);
                        }catch (NumberFormatException e){
                            Toast.makeText(getApplicationContext(), "Problemi nella lettura dell'importo", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(myDB.get_Conto(tempNome)){
                            Toast.makeText(getApplicationContext(), "Esiste già un conto con questo nome, cambiare perfavore", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(myDB.InsertConto(floatBil, tempNome))
                            Toast.makeText(getApplicationContext(), "Conto creato!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Creazione fallita, riprovare", Toast.LENGTH_SHORT);
                    }
                }
        );
    }
}
