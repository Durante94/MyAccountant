package com.durante.fabrizio.myaccountant;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DBHelper MyDB;
    private ListView ElencoConti, UltimiMov;
    private TextView SaldoTot;
    int[] idMov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //mio codice
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NuovoConto.class));
            }
        });

        MyDB=new DBHelper(this);

        ElencoConti=(ListView)findViewById(R.id.elenco_conti);
        SaldoTot=(TextView)findViewById(R.id.BilTW);
        UltimiMov=(ListView)findViewById(R.id.ultimi);
        Cursor righe=MyDB.get_Conti();
        ContoAdapter adpt;
        MovimentoAdapter adpt2;
        idMov=new int[10];

        if(righe.getCount()<=0){
            ArrayAdapter<TextView> adapter=new ArrayAdapter<TextView>(this, R.layout.noconti_layout);
            ElencoConti.setAdapter(adapter);
        }else{
            ElementoListaConti temp;
            ArrayList<ElementoListaConti> lista=new ArrayList<ElementoListaConti>();
            while (righe.moveToNext()){
                temp=new ElementoListaConti(righe.getString(0), righe.getFloat(1));
                lista.add(temp);
            }
            adpt=new ContoAdapter(getApplicationContext(), lista);
            ElencoConti.setAdapter(adpt);
        }

        SaldoTot.setText(MyDB.get_SaldoTot()+" €");

        righe=MyDB.get_Pay();

        if(righe.getCount()<=0){
            ArrayAdapter<TextView> adapter=new ArrayAdapter<TextView>(this, R.layout.nomovimenti_layout);
            UltimiMov.setAdapter(adapter);
        }else{
            ElementoListaMovimenti temp;
            ArrayList<ElementoListaMovimenti> list=new ArrayList<ElementoListaMovimenti>();
            for (int i=0; righe.moveToNext(); i++){
                temp=new ElementoListaMovimenti(righe.getString(0), righe.getString(1), righe.getFloat(2));
                list.add(temp);
                idMov[i]=righe.getInt(3);
            }
            adpt2=new MovimentoAdapter(this, list);
            UltimiMov.setAdapter(adpt2);
        }
        setListener();
    }

    private void setListener(){
        ElencoConti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putString("Parent", "MainActivity");
                startActivity(new Intent(MainActivity.this, DettaglioConto.class).putExtra("Conto", position+1));
            }
        });

        UltimiMov.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, MappaMovimento.class).putExtra("Movim", idMov[position]));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.aggiunta:{
                startActivity(new Intent(MainActivity.this, NuovoConto.class));
                break;
            }
            case R.id.dash:{
                //qui nulla
                break;
            }
            case R.id.opzioni:{
                //nuova activity
                break;
            }
            case R.id.deb_cred:{
                startActivity(new Intent(MainActivity.this, DebCredActivity.class));
                break;
            }
            case R.id.operazione:{
                startActivity(new Intent(MainActivity.this, NuovaOperazione.class));
                break;
            }
        }
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
