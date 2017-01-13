package com.durante.fabrizio.myaccountant;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Fabrizio on 09/01/2017.
 */

public class PlaceholderFragment extends Fragment {

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

        TextView NomeConto=(TextView)rootView.findViewById(R.id.tv_NomeC);
        TextView Saldo=(TextView)rootView.findViewById(R.id.tv_Saldo);
        DBHelper db=new DBHelper(getContext());

        Cursor ris=db.get_Conto(getArguments().getInt("Conto"));
        if(!ris.moveToFirst()){
            Toast.makeText(getContext(), "Conto inesistente!", Toast.LENGTH_LONG).show();
            return null;
        }
        NomeConto.setText(ris.getString(0));
        Saldo.setText(ris.getString(1));

        return rootView;
    }
}