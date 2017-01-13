package com.durante.fabrizio.myaccountant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class NuovaOperazione extends AppCompatActivity {

    private Spinner Conti;
    private EditText Importo, Luogo;
    private TextView Data;
    private Button btnData, btnMappa, btnSalva;
    private Switch Tipo;
    private DBHelper MyDB;
    private int anno, mese, giorno;
    private static final int DIALOG_ID=0;
    private DatePickerDialog.OnDateSetListener DatePikerListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            anno=year;
            mese=monthOfYear+1;
            giorno=dayOfMonth;
            Data.setText(Integer.toString(giorno)+"/"+Integer.toString(mese)+"/"+Integer.toString(anno));
        }
    };

    @Override
    protected Dialog onCreateDialog(int id){
        if(id==DIALOG_ID)
            return new DatePickerDialog(this, DatePikerListener, anno, mese, giorno);
        return null;
    }

    private void setListener(){
        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });

        Tipo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Tipo.setText("Entrata");
                else
                    Tipo.setText("Spesa");
            }
        });

        btnSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringImp=Importo.getText().toString();
                float imp;
                int conto=Conti.getSelectedItemPosition();
                String tipo;
                String data=Data.getText().toString();
                String luogo=Luogo.getText().toString();

                if(stringImp.length()<=0){
                    Toast.makeText(getApplicationContext(), "Inserire un importo", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    try {
                        imp=Float.parseFloat(stringImp);
                    }catch (NumberFormatException e){
                        Toast.makeText(getApplicationContext(), "Formato importo errato, reinserire", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if(conto<=0){
                    Toast.makeText(getApplicationContext(), "Scegliere un conto su cui legare l'operazione", Toast.LENGTH_LONG).show();
                    return;
                }
                if(data.length()<=0){
                    Toast.makeText(getApplicationContext(), "Inserire una data", Toast.LENGTH_LONG).show();
                    return;
                }
                if(luogo.length()<=0){
                    Toast.makeText(getApplicationContext(), "Attenzione! Non hai inserito la posizione del pagamento", Toast.LENGTH_LONG).show();
                }
                if(Tipo.isChecked())
                    tipo="Entrata";
                else
                    tipo="Uscita";

                if(MyDB.InsertPay(imp, data, luogo, tipo, conto))
                    Toast.makeText(NuovaOperazione.this, "Operazione salvata!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(NuovaOperazione.this, "Impossibile memorizzare l'operazione", Toast.LENGTH_LONG).show();

                MyDB.Aggiorna(conto);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_operazione);

        Conti=(Spinner)findViewById(R.id.spinner_Conti);
        Importo=(EditText)findViewById(R.id.et_Imp);
        Luogo=(EditText)findViewById(R.id.et_Luogo);
        Data=(TextView)findViewById(R.id.tv_Data);
        btnData=(Button)findViewById(R.id.btn_Data);
        btnMappa=(Button)findViewById(R.id.btn_Mappa);
        btnSalva=(Button)findViewById(R.id.btn_MemPag);
        Tipo=(Switch)findViewById(R.id.switch_Tipo);
        MyDB=new DBHelper(this);

        final Calendar cal=Calendar.getInstance();
        anno=cal.get(Calendar.YEAR);
        mese=cal.get(Calendar.MONTH);
        giorno=cal.get(Calendar.DAY_OF_MONTH);

        Cursor ris=MyDB.get_Conti();
        Bundle extra=getIntent().getExtras();

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
        }else{
            ArrayAdapter<TextView> ad=new ArrayAdapter<TextView>(this, R.layout.noconti_layout);
            Conti.setAdapter(ad);
            Toast.makeText(this, "Nessun Conto su cui legare", Toast.LENGTH_LONG).show();
        }

        if(extra!=null){

            int conto=extra.getInt("Conto");
            Conti.setSelection(conto);
            if(extra.getInt("from")==1) {
                float imp = extra.getFloat("Importo");
                int tipo = extra.getInt("Tipo");
                String day = extra.getString("Data");

                Importo.setText(Float.toString(imp)+" €");
                if (tipo == 1) {
                    Tipo.setChecked(true);
                    Tipo.setText("Entrata");
                } else if (tipo == 2)
                    Tipo.setChecked(false);
                else
                    Tipo.setChecked(false);
                Data.setText(day);
            }
            extra.clear();
        }

        setListener();
    }
}
