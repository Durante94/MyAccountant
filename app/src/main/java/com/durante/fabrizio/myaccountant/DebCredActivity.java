package com.durante.fabrizio.myaccountant;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.SynchronousQueue;

public class DebCredActivity extends AppCompatActivity {

    private Spinner Tipi, Conti;
    private EditText Import;
    private TextView VisualData;
    private Button ChooseData;
    private Button Save;
    private DBHelper MyDB;
    private int anno, mese, giorno;
    private static final int DIALOG_ID=0;
    private DatePickerDialog.OnDateSetListener DatePikerListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            anno=year;
            mese=monthOfYear+1;
            giorno=dayOfMonth;
            VisualData.setText(Integer.toString(giorno)+"/"+Integer.toString(mese)+"/"+Integer.toString(anno));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deb_cred);
        final Calendar cal=Calendar.getInstance();
        anno=cal.get(Calendar.YEAR);
        mese=cal.get(Calendar.MONTH);
        giorno=cal.get(Calendar.DAY_OF_MONTH);

        MyDB=new DBHelper(this);
        Tipi=(Spinner)findViewById(R.id.Type_spinner);
        Conti=(Spinner)findViewById(R.id.Conti_spinner);
        Import=(EditText)findViewById(R.id.imp_et);
        VisualData=(TextView)findViewById(R.id.tv_data);
        ChooseData=(Button)findViewById(R.id.btn_Data);
        Save=(Button)findViewById(R.id.Btn_Mem);

        Cursor righe=MyDB.get_Conti();
        if(!(righe.getCount()<=0 || righe==null)){
            String temp;
            ArrayList<String> list=new ArrayList<String>();
            list.add("Seleziona un conto");
            while (righe.moveToNext()){
                temp=righe.getString(0);
                list.add(temp);
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.elemento_nome_conti, list);
            Conti.setAdapter(adapter);
        }else{
            ArrayAdapter<TextView> ad=new ArrayAdapter<TextView>(this, R.layout.nomovimenti_layout);
            Conti.setAdapter(ad);
            Toast.makeText(this, "Nessun Conto su cui legare", Toast.LENGTH_LONG).show();
        }

        setListener();
    }

    private void setListener(){
        Save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int tipo = Tipi.getSelectedItemPosition();
                int conto = Conti.getSelectedItemPosition();
                String imp = Import.getText().toString();
                String day = VisualData.getText().toString();
                float imp2 = 0;

                if (tipo < 1) {
                    Toast.makeText(getApplicationContext(), "Selezionare se debito o credito", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (conto < 1) {
                    Toast.makeText(getApplicationContext(), "Selezionare il conto da associare", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imp.length() <= 0) {
                    Toast.makeText(getApplicationContext(), "Inserire un importo", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    try {
                        imp2 = Float.parseFloat(imp);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Non sono riuscito a convertire l'importo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (imp2 < 0) {
                        Toast.makeText(getApplicationContext(), "L'importo non può essere negativo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (day.length() < 0) {
                    Toast.makeText(getApplicationContext(), "Inserire una data per la notifica", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Calendar cal;
                Date oggi, when;

                try {
                    when = format.parse(day);
                } catch (ParseException e) {
                    Toast.makeText(DebCredActivity.this, "Non riesco a convertire la data", Toast.LENGTH_LONG).show();
                    return;
                }
                oggi = new Date();
                cal = Calendar.getInstance();
                cal.setTime(when);
                long quando = cal.getTimeInMillis();
                cal.setTime(oggi);
                long diff = quando - cal.getTimeInMillis();

                Intent proxOperazione = new Intent(DebCredActivity.this, NuovaOperazione.class)
                        .putExtra("Importo", imp2)
                        .putExtra("Conto", conto)
                        .putExtra("Tipo", tipo)
                        .putExtra("Data", day)
                        .putExtra("from", 1);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, proxOperazione, PendingIntent.FLAG_UPDATE_CURRENT);

                long futureInMillis = SystemClock.elapsedRealtime() + diff;
                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
            }
        });

        ChooseData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id==DIALOG_ID)
            return new DatePickerDialog(this, DatePikerListener, anno, mese, giorno);
        return null;
    }
}
