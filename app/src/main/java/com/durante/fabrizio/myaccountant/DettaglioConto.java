package com.durante.fabrizio.myaccountant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import layout.MovimentiConto;

public class DettaglioConto extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static int conto;
    private static DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettaglio_conto);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                startActivity(new Intent(DettaglioConto.this, NuovaOperazione.class).putExtra("Conto", conto).putExtra("from", 2));
            }
        });

        conto=getIntent().getExtras().getInt("Conto");
        db=new DBHelper(this);
    }

    private Intent getParentActivityIntentImplement(){
        Intent intent=null;

        Bundle extra=getIntent().getExtras();
        String parentActivity=extra.getString("Parent");

        if(parentActivity.equals("MainActivity")){
            intent=new Intent(DettaglioConto.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        return intent;
    }

    public Intent getSupportParentActivityIntent(){
        return getParentActivityIntentImplement();
    }

    public Intent getParentActivityIntent(){
        return getParentActivityIntentImplement();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dettaglio_conto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.delete:{
                AlertDialog ad=new AlertDialog.Builder(DettaglioConto.this).create();
                ad.setTitle("Attenzione!");
                ad.setMessage("Sei sicuro di voler cancellare il conto?");
                ad.setButton(AlertDialog.BUTTON_POSITIVE, "SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(db.delete_Conto(conto)) {
                            startActivity(new Intent(DettaglioConto.this, MainActivity.class));
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getApplicationContext(), "Non c'è nessun conto da cancellare!", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });
                ad.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
                break;
            }
            case R.id.operazione:{
                startActivity(new Intent(DettaglioConto.this, NuovaOperazione.class).putExtra("Conto", conto).putExtra("from", 2));
                break;
            }

            case R.id.modifica:{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DettaglioConto.this);
                alertDialog.setTitle("Modifica");
                alertDialog.setMessage("Inserisci la nuova denominazione:");

                final EditText input = new EditText(DettaglioConto.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                alertDialog.setView(input);

                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nome=input.getText().toString();
                        if(db.get_Conto(nome))
                            Toast.makeText(getApplicationContext(), nome+" è già in uso, sceglierne un altro", Toast.LENGTH_LONG).show();
                        else{
                            if(db.update_Conto(conto, nome)){
                                Toast.makeText(getApplicationContext(), "Modifica effettuata", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }else{
                                Toast.makeText(getApplicationContext(), "Modifica fallita", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }
                    }
                });
                alertDialog.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:{
                    PlaceholderFragment def= PlaceholderFragment.newInstance(conto);
                    return def;
                }
                case 1:{
                    MovimentiConto tab=MovimentiConto.newInstance(conto);
                    return tab;
                }
                default:{
                    PlaceholderFragment def= PlaceholderFragment.newInstance(conto);
                    return def;
                }
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Dati";
                case 1:
                    return "Movimenti";
            }
            return null;
        }
    }
}
