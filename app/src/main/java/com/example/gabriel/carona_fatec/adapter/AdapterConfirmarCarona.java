package com.example.gabriel.carona_fatec.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabriel.carona_fatec.ConfirmarCaronaActivity;
import com.example.gabriel.carona_fatec.R;
import com.example.gabriel.carona_fatec.api.model.Reserva;
import com.example.gabriel.carona_fatec.api.model.Rota;

import java.util.List;

/**
 * Created by gabriel on 21/10/2017.
 */

public class AdapterConfirmarCarona extends RecyclerView.Adapter<AdapterConfirmarCarona.ReservasViewHolder> {

    private List<Reserva> reservas;
    Context context;

    public AdapterConfirmarCarona(List<Reserva> reservas){
        this.reservas = reservas;
    }


    public class ReservasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView tv_resultado;
        ImageButton confirmar;
        ImageButton recusar;
        View mCardView;

        //TextView personAge;
        //ImageView personPhoto;

        ReservasViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            tv_resultado = (TextView) itemView.findViewById(R.id.tv_resultado);
            confirmar = (ImageButton) itemView.findViewById(R.id.img_btn_confirmar);
            recusar = (ImageButton) itemView.findViewById(R.id.img_btn_recusar);
            mCardView = (CardView) itemView.findViewById(R.id.cv);
            recusar.setOnClickListener(this);
            confirmar.setOnClickListener(this);
            //personAge = (TextView)itemView.findViewById(R.id.person_age);
            //personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }

        @Override
        public void onClick(View v) {

            if(v.getTag().toString().equals("aceitar")) {

                int position = getAdapterPosition();

                final Reserva reserva = reservas.get(position);

                context = v.getContext();

                new AlertDialog.Builder(context)
                        .setTitle("Aprovar usuário")
                        .setMessage("Tem certeza que deseja aprovar este usuário para esta carona?")
                        .setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ConfirmarCaronaActivity conf = new ConfirmarCaronaActivity();
                                        conf.requisicaoPut(reserva, context);
                                    }
                                })
                        .setNegativeButton("Cancelar", null)
                        .show();

            }

            else{

                int position = getAdapterPosition();

                final Reserva reserva = reservas.get(position);

                context = v.getContext();

                new AlertDialog.Builder(context)
                        .setTitle("Recusar usuário")
                        .setMessage("Tem certeza que deseja recusar este usuário para esta carona?")
                        .setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ConfirmarCaronaActivity conf = new ConfirmarCaronaActivity();
                                        conf.requisicaoDelete(reserva, context);
                                    }
                                })
                        .setNegativeButton("Cancelar", null)
                        .show();

            }

        }
    }


    @Override
    public ReservasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmar_carona, parent, false);
        ReservasViewHolder vh = new ReservasViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ReservasViewHolder holder, int position) {
        holder.tv_resultado.setText(reservas.get(position).toString());
        holder.mCardView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return reservas.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
