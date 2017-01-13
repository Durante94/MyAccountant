package com.durante.fabrizio.myaccountant;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Fabrizio on 30/12/2016.
 */
public class MovimentoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ElementoListaMovimenti> list;

    public MovimentoAdapter(Context context, ArrayList<ElementoListaMovimenti> list) {
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
        View v=View.inflate(context, R.layout.elemento_lista_movimenti, null);
        TextView nomeC=(TextView)v.findViewById(R.id.NomeC);
        TextView categ=(TextView)v.findViewById(R.id.Categoria);
        TextView imp=(TextView)v.findViewById(R.id.imp);

        nomeC.setText(list.get(position).getNomeC());
        categ.setText(list.get(position).getCategoria());
        imp.setText(Float.toString(list.get(position).getImporto())+" €");

        return v;
    }
}
