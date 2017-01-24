package com.durante.fabrizio.myaccountant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ModificaMovimento extends AppCompatActivity {

    private EditText Importo;
    private TextView Data;
    private Spinner Conti;
    private Switch Tipo;
    private DBHelper DB;
    private Button Modifica, SelezData;
    private Bundle args;
    private static final int DIALOG_ID = 0;

    private int anno, mese, giorno;
    private DatePickerDialog.OnDateSetListener DatePikerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            anno = year;
            mese = monthOfYear + 1;
            giorno = dayOfMonth;
            Data.setText(Integer.toString(giorno) + "/" + Integer.toString(mese) + "/" + Integer.toString(anno));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_movimento);

        Importo=(EditText)findViewById(R.id.et_Importo);
        Data=(TextView)findViewById(R.id.tv_dataMov);
        Conti=(Spinner)findViewById(R.id.spinner_conti);
        Tipo=(Switch)findViewById(R.id.switch_Tipo);
        Modifica=(Button)findViewById(R.id.btn_Conferma);
        SelezData=(Button)findViewById(R.id.btn_selez_Data);
        DB=new DBHelper(this);
        Cursor ris=DB.get_Conti();
        final Calendar cal=Calendar.getInstance();
        anno=cal.get(Calendar.YEAR);
        mese=cal.get(Calendar.MONTH);
        giorno=cal.get(Calendar.DAY_OF_MONTH);

        args=getIntent().getExtras();
        if (args.isEmpty()){
            Toast.makeText(this, "Movimento inesistente!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ModificaMovimento.this, MainActivity.class));
            return;
        }

        if(!(ris.getCount()<=0 || ris==null)){
            String temp;
            ArrayList<String> list=new ArrayList<String>();
            list.add("Seleziona un conto");
            while (ris.moveToNext()){
                temp=ris.getString(0);
                list.add(temp);
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.elemento_nome_conti, list);
            Conti.setAdapter(adapter);
        }

        Importo.setText(args.getString("Importo")+" €");
        Data.setText(args.getString("Data"));
        int conto=args.getInt("Conto");
        Conti.setSelection(conto);
        switch (args.getString("Tipo")){
            case "Entrata":{
                Tipo.setChecked(true);
                Tipo.setText("Entrata");
                break;
            }
            case "Spesa":{
                Tipo.setChecked(false);
                Tipo.setText("Spesa");
                break;
            }
            default:{
                Tipo.setChecked(false);
                Tipo.setText("Spesa");
                break;
            }
        }

        setListener();
    }

    private void setListener() {
        Modifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringImp = Importo.getText().toString();
                float imp;
                int conto = Conti.getSelectedItemPosition();
                String tipo;
                String data = Data.getText().toString();
                //String luogo = Luogo.getText().toString();

                if (stringImp.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Inserire un importo", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if(stringImp.endsWith(" €"))
                        stringImp=stringImp.substring(0, stringImp.length()-2);
                    try {
                        imp = Float.parseFloat(stringImp);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Formato importo errato, reinserire", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (conto <= 0) {
                    Toast.makeText(getApplicationContext(), "Scegliere un conto su cui legare l'operazione", Toast.LENGTH_LONG).show();
                    return;
                }
                if (data.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Inserire una data", Toast.LENGTH_LONG).show();
                    return;
                }
                /*if (luogo.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Attenzione! Non hai inserito la posizione del pagamento", Toast.LENGTH_LONG).show();
                }*/
                if (Tipo.isChecked())
                    tipo = "Entrata";
                else
                    tipo = "Uscita";

                if (DB.update_Mov(args.getInt("ID"), imp, data, tipo, "")) {
                    Toast.makeText(ModificaMovimento.this, "Modifica salvata!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ModificaMovimento.this, MappaMovimento.class)
                            .putExtra("Movim", args.getInt("ID")).putExtra("Conto", args.getInt("Conto")));
                }else
                    Toast.makeText(ModificaMovimento.this, "Impossibile modificare i dati", Toast.LENGTH_LONG).show();

                DB.Aggiorna(conto);
            }
        });

        SelezData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID)
            return new DatePickerDialog(this, DatePikerListener, anno, mese, giorno);
        return null;
    }
}
