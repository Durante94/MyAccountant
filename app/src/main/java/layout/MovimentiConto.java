package layout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.durante.fabrizio.myaccountant.DBHelper;
import com.durante.fabrizio.myaccountant.DettaglioConto;
import com.durante.fabrizio.myaccountant.MappaMovimento;
import com.durante.fabrizio.myaccountant.MovimentiAdapter;
import com.durante.fabrizio.myaccountant.MovimentoSingolo;
import com.durante.fabrizio.myaccountant.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MovimentiConto extends Fragment {

    public MovimentiConto() {
        // Required empty public constructor
    }

    public static MovimentiConto newInstance(int conto) {
        MovimentiConto fragment = new MovimentiConto();
        Bundle args = new Bundle();
        args.putInt("Conto", conto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_movimenti_conto, container, false);

        ListView elenco=(ListView)rootView.findViewById(R.id.lista_movimenti);
        DBHelper db=new DBHelper(getContext());
        MovimentiAdapter adapter;
        final Cursor ris=db.get_Movim(getArguments().getInt("Conto"));
        if(ris.getCount()<=0){
            Toast.makeText(getContext(), "Nessun movimento!", Toast.LENGTH_LONG).show();
            return null;
        }

        MovimentoSingolo temp;
        ArrayList<MovimentoSingolo> list=new ArrayList<MovimentoSingolo>();
        String luogo="";
        Geocoder gc = new Geocoder(getContext());
        List<Address> indirizzi = null;
        Address address;
        while (ris.moveToNext()){
            try {
                int trunc=ris.getString(3).indexOf(" ");
                indirizzi = gc.getFromLocation(Double.parseDouble(ris.getString(3).substring(0, trunc-1)), Double.parseDouble(ris.getString(3).substring(trunc+1)), 1);
                address = indirizzi.get(0);
                luogo = address.getAddressLine(0)+", "+address.getLocality();
            } catch (IOException e) {
                Toast.makeText(getContext(), "Non riesco a trovare il luogo inserito", Toast.LENGTH_SHORT).show();
            }
            temp=new MovimentoSingolo(ris.getString(0), ris.getString(2), ris.getString(4), ris.getFloat(1), luogo);
            list.add(temp);
        }
        adapter=new MovimentiAdapter(getContext(), list);
        elenco.setAdapter(adapter);

        elenco.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(ris.moveToPosition(position)) {
                    int id_Mov=ris.getInt(0);
                    startActivity(new Intent(getContext(), MappaMovimento.class).putExtra("Movim", id_Mov).putExtra("Conto", getArguments().getInt("Conto")));
                }
            }
        });

        return rootView;
    }
}


