package com.example.gabriel.carona_fatec.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.gabriel.carona_fatec.R;
import com.example.gabriel.carona_fatec.api.model.Rota;

import java.util.List;

/**
 * Created by gabriel on 21/10/2017.
 */

public class AdapterAprovarCarona extends BaseAdapter {

    private final List<Rota> rotas;
    private final Activity act;

    public AdapterAprovarCarona(List<Rota> rotas, Activity act) {
        this.rotas = rotas;
        this.act = act;
    }

    @Override
    public int getCount() {
        return rotas.size();
    }

    @Override
    public Object getItem(int position) {
        return rotas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater()
                .inflate(R.layout.lista_aprovar_carona, parent, false);
        Rota rota = rotas.get(position);

        TextView resultado = (TextView) view.findViewById(R.id.tv_resultado);

        resultado.setText(rota.toString());

        return view;
    }
}
