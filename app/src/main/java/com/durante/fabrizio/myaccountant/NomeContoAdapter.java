package com.durante.fabrizio.myaccountant;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Fabrizio on 30/12/2016.
 */
public class NomeContoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> list;

    public NomeContoAdapter(Context context, ArrayList<String> list) {
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
        View v=View.inflate(context, R.layout.elemento_nome_conti, null);
        TextView nomeC=(TextView)v.findViewById(R.id.Label_Nome);

        nomeC.setText(list.get(position));
        return null;
    }
}
