package com.durante.fabrizio.myaccountant;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MappaMovimento extends AppCompatActivity implements OnMapReadyCallback {

    private TextView Data, Conto, Importo, Tipo;
    private GoogleMap mappa;
    private DBHelper db;
    private Bundle args;
    private Cursor ris;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(googleServicesAvilable()){
            setContentView(R.layout.activity_mappa_movimento);
            initMap();
        }else {
            Toast.makeText(this, "Impossibile creare la mappa", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.layout_no_mappa);
        }

        Data = (TextView) findViewById(R.id.tv_data_movimento);
        Conto = (TextView) findViewById(R.id.tv_conto_movimento);
        Importo = (TextView) findViewById(R.id.tv_importo_movimento);
        Tipo = (TextView) findViewById(R.id.tv_tipo_movimento);
        db = new DBHelper(this);
        args = getIntent().getExtras();


        ris=db.get_singol_Movim(args.getInt("Movim"));
        if(ris.getCount()<=0){
            Toast.makeText(this, "Movimento inesistente!", Toast.LENGTH_LONG);
            return;
        }

        ris.moveToFirst();
        Data.setText(ris.getString(0));
        Conto.setText(ris.getString(4));
        Importo.setText(ris.getString(2));
        Tipo.setText(ris.getString(1));
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

    public boolean googleServicesAvilable(){
        GoogleApiAvailability api=GoogleApiAvailability.getInstance();
        int isAvilable=api.isGooglePlayServicesAvailable(this);
        if(isAvilable== ConnectionResult.SUCCESS)
            return true;
        else if (api.isUserResolvableError(isAvilable)){
            Dialog d=api.getErrorDialog(this, isAvilable, 0);
            d.show();
        }else
            Toast.makeText(this, "Impossibile connettersi a Google Play Servicies", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void initMap(){
        MapFragment map= (MapFragment)getFragmentManager().findFragmentById(R.id.Mappa);
        map.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mappa=googleMap;
        goToLocation();
    }

    private void goToLocation() {
        String stringMap=ris.getString(3);
        if(stringMap.isEmpty())
            return;
        int trunc=stringMap.indexOf(" ");
        LatLng pos=new LatLng(Double.parseDouble(stringMap.substring(0, trunc-1)), Double.parseDouble(stringMap.substring(trunc+1)));
        CameraUpdate update= CameraUpdateFactory.newLatLngZoom(pos, 17);
        mappa.moveCamera(update);
    }
}
