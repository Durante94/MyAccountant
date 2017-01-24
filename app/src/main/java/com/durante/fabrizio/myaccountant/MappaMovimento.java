package com.durante.fabrizio.myaccountant;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MappaMovimento extends AppCompatActivity {

    private TextView Data, Conto, Importo, Tipo;
    private String mappa;
    private DBHelper db;
    public Bundle args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappa_movimento);

        Data=(TextView)findViewById(R.id.tv_data_movimento);
        Conto=(TextView)findViewById(R.id.tv_conto_movimento);
        Importo=(TextView)findViewById(R.id.tv_importo_movimento);
        Tipo=(TextView)findViewById(R.id.tv_tipo_movimento);
        db=new DBHelper(this);
        args=getIntent().getExtras();

        Cursor ris=db.get_singol_Movim(args.getInt("Movim"));
        if(ris.getCount()<=0){
            Toast.makeText(this, "Movimento inesistente!", Toast.LENGTH_LONG);
            return;
        }

        ris.moveToFirst();
        Data.setText(ris.getString(0));
        Conto.setText(ris.getString(4));
        Importo.setText(ris.getString(2));
        Tipo.setText(ris.getString(1));
        mappa=ris.getString(3);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_singolo_movimento, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.cancel:{
                if(db.delete_Mov(args.getInt("Movim"))){
                    Toast.makeText(getApplicationContext(), "Operazione cancellata", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MappaMovimento.this, DettaglioConto.class).putExtra("Conto", args.getInt("Conto")));
                }else
                    Toast.makeText(getApplicationContext(), "Cancellazione falita", Toast.LENGTH_SHORT).show();
                db.Aggiorna(args.getInt("Conto"));
                break;
            }
            case R.id.modifica:{
                startActivity(new Intent(MappaMovimento.this, ModificaMovimento.class)
                        .putExtra("Importo", Importo.getText().toString())
                        .putExtra("Data", Data.getText().toString())
                        .putExtra("Conto", args.getInt("Conto"))
                        .putExtra("Tipo", Tipo.getText().toString())
                        .putExtra("ID", args.getInt("Movim")));
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
