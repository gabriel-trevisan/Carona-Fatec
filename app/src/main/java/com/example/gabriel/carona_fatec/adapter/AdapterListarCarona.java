package com.example.gabriel.carona_fatec.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gabriel.carona_fatec.R;
import com.example.gabriel.carona_fatec.api.model.Rota;
import com.example.gabriel.carona_fatec.api.model.Usuario;

import java.util.List;

/**
 * Created by gabriel on 08/11/2017.
 */

public class AdapterListarCarona extends BaseAdapter {

    private final List<Usuario> usuarios;
    private final Activity act;

    public AdapterListarCarona (List<Usuario> usuarios, Activity act) {
        this.usuarios = usuarios;
        this.act = act;
    }

    @Override
    public int getCount() {
        return usuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return usuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //infalte lista_aprovar_carona tempor[ario
        View view = act.getLayoutInflater()
                .inflate(R.layout.lista_aprovar_carona, parent, false);
        Usuario usuario = usuarios.get(position);

        TextView resultado = (TextView) view.findViewById(R.id.tv_resultado);

        resultado.setText(usuario.toString());

        return view;
    }
}
