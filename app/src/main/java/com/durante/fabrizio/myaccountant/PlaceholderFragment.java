package com.durante.fabrizio.myaccountant;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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
        LineGraphSeries<DataPoint> series = new LineGraphSeries();

        Cursor ris=db.get_Conto(getArguments().getInt("Conto"));
        if(!ris.moveToFirst()){
            Toast.makeText(getContext(), "Conto inesistente!", Toast.LENGTH_LONG).show();
            return null;
            //DFFHGF
        }
        NomeConto.setText(ris.getString(0));
        Saldo.setText(ris.getString(1));

        return rootView;
    }

    public void update_Nome(String nome){
        NomeConto.setText(nome);
    }
}