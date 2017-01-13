package com.durante.fabrizio.myaccountant;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Fabrizio on 10/01/2017.
 */

public class MovimentiAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MovimentoSingolo> list;

    public MovimentiAdapter(Context context, ArrayList<MovimentoSingolo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(context, R.layout.movimento_layout, null);
        TextView Data=(TextView)view.findViewById(R.id.tv_data);
        TextView Tipo=(TextView)view.findViewById(R.id.tv_categ);
        TextView Imp=(TextView)view.findViewById(R.id.tv_imp);
        TextView Dove=(TextView)view.findViewById(R.id.tv_luogo);

        Data.setText(list.get(position).getData());
        Tipo.setText(list.get(position).getCategoria());
        Imp.setText(Float.toString(list.get(position).getImporto())+" €");
        if(list.get(position).getLuogo().length()>0)
            Dove.setText(list.get(position).getLuogo());
        else
            Dove.setText("Posizione sconosciuta");
        return view;
    }
}
