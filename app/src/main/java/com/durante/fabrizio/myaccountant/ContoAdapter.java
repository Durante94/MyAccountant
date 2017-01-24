package com.durante.fabrizio.myaccountant;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Fabrizio on 29/12/2016.
 */
public class ContoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ElementoListaConti> list;

    public ContoAdapter(Context context, ArrayList<ElementoListaConti> list) {
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
        int pos;
        String temp;
        View v=View.inflate(context, R.layout.elemento_lista_conti, null);
        TextView nomeC=(TextView)v.findViewById(R.id.twNomeConto);
        TextView bilC=(TextView)v.findViewById(R.id.twBilancio);
        try{
            temp=Float.toString(list.get(position).getBilancio());
        }catch (NumberFormatException e){
            Toast.makeText(context, "Non riesco a convertire l'importo", Toast.LENGTH_SHORT).show();
            return null;
        }
        //musse per tagliare la stringa importo...
        pos=temp.indexOf(".");
        temp=temp.substring(0, pos+2);

        nomeC.setText(list.get(position).getNome());
        bilC.setText(temp+" €");

        return v;
    }
}
