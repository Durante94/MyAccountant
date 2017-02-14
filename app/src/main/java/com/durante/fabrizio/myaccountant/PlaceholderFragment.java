package com.durante.fabrizio.myaccountant;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Fabrizio on 09/01/2017.
 */

public class PlaceholderFragment extends Fragment {

    private TextView NomeConto;
    private TextView Saldo;
    private GraphView graph;

    public PlaceholderFragment() {
    }

    public static PlaceholderFragment newInstance(int conto) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt("Conto", conto);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dettaglio_conto, container, false);

        NomeConto=(TextView)rootView.findViewById(R.id.tv_NomeC);
        Saldo=(TextView)rootView.findViewById(R.id.tv_Saldo);
        graph = (GraphView)rootView.findViewById(R.id.graph);
        DBHelper db=new DBHelper(getContext());

        Calendar calendar = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yy");

        Cursor ris=db.get_Conto(getArguments().getInt("Conto"));
        if(!ris.moveToFirst()){
            Toast.makeText(getContext(), "Conto inesistente!", Toast.LENGTH_LONG).show();
            return null;
        }
        NomeConto.setText(ris.getString(0));
        Saldo.setText(ris.getString(1));

        Cursor scanMovim=db.get_Movim(getArguments().getInt("Conto"));
        if(scanMovim.getCount()<=0){
            return rootView;
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries();
        DataPoint [] point=new DataPoint[scanMovim.getCount()];
        for(int i=0; i<=point.length && scanMovim.moveToNext(); i++){
            //carica date sulle x e importi sulle y, il primo importo deve essere il bilancio iniziale
            //codice utile:
                //Date date = formatter.parse("01/29/02");
                //Calendar calendar = Calendar.getInstance();
                //calendar.setTime(date);
                //
        }

        //series.appendData();

        graph.addSeries(series);

        // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);

// activate horizontal scrolling
        graph.getViewport().setScrollable(true);

// activate horizontal and vertical zooming and scrolling
        graph.getViewport().setScalableY(true);

// activate vertical scrolling
        graph.getViewport().setScrollableY(true);

        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0); //gestire range di date da cui visulaizzare
        graph.getViewport().setMaxX(3.5);

// set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0); //da quale saldo iniziare la visualizzazione
        graph.getViewport().setMaxY(8); //da quale saldo finire la visulizzazione, gestire ampiezza in sostanza

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Data");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Saldo");

        // styling series
        series.setTitle(ris.getString(0));
        series.setColor(Color.BLUE);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(12);
        series.setThickness(10);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getContext(), "Saldo al "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    public void update_Nome(String nome){
        NomeConto.setText(nome);
    }
}