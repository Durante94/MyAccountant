package layout;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.durante.fabrizio.myaccountant.DBHelper;
import com.durante.fabrizio.myaccountant.MovimentiAdapter;
import com.durante.fabrizio.myaccountant.MovimentoSingolo;
import com.durante.fabrizio.myaccountant.R;

import java.util.ArrayList;


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
        Cursor ris=db.get_Movim(getArguments().getInt("Conto"));
        if(ris.getCount()<=0){
            Toast.makeText(getContext(), "Nessun movimento!", Toast.LENGTH_LONG).show();
            return null;
        }

        MovimentoSingolo temp;
        ArrayList<MovimentoSingolo> list=new ArrayList<MovimentoSingolo>();
        while (ris.moveToNext()){
            temp=new MovimentoSingolo(ris.getString(0), ris.getString(1), ris.getFloat(2), ris.getString(3));
            list.add(temp);
        }
        adapter=new MovimentiAdapter(getContext(), list);
        elenco.setAdapter(adapter);

        return rootView;
    }
}


