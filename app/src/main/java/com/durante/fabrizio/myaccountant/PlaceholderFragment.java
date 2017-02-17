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
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

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
        Date date1=null, date2=null, date3=null;

        try {
            date1 = formatter.parse("10/02/2017");
            date2 = formatter.parse("11/02/2017");
            date3 = formatter.parse("13/02/2017");
        }catch (ParseException e){
            e.printStackTrace();
        }

        BarGraphSeries<DataPoint> series = new BarGraphSeries(new DataPoint[]{
                new DataPoint(date1, 1),
                new DataPoint(date2, 5),
                new DataPoint(date3, 3)
        });
        /*DataPoint [] point=new DataPoint[scanMovim.getCount()];
        Date date, lastData, firstData;
        double imp;
        for(int i=0; i<point.length && scanMovim.moveToNext(); i++){
            try {
                date = formatter.parse(scanMovim.getString(2));
            } catch (ParseException e) {
                Toast.makeText(getContext(), "Impossibile parsare la data: "+scanMovim.getString(2), Toast.LENGTH_SHORT).show();
                return rootView;
            }
            if(i==0)
                firstData=date;
            if(i==scanMovim.getCount()-1)
                lastData=date;
            imp=scanMovim.getDouble(1);
            point[i]=new DataPoint(date, imp);
        }*/

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

        // set manual x bounds to have nice steps
        //graph.getViewport().setMinX(firstData);
        //graph.getViewport().setMaxX(lastData);
        graph.getViewport().setXAxisBoundsManual(true);

// set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0); //da quale saldo iniziare la visualizzazione
        graph.getViewport().setMaxY(8); //da quale saldo finire la visulizzazione, gestire ampiezza in sostanza

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Data");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Saldo");
        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // styling series
        series.setTitle(ris.getString(0));
        series.setColor(Color.BLUE);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);

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