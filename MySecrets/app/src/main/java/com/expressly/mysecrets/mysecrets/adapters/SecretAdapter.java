package com.expressly.mysecrets.mysecrets.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.expressly.mysecrets.mysecrets.R;
import com.expressly.mysecrets.mysecrets.model.Secret;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by gustavo on 28/07/2017.
 */

public class SecretAdapter extends RecyclerView.Adapter<SecretAdapter.MyViewHolder> {

    private List<Secret> lista;
    private View view;
    private DatabaseReference databaseReference;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView conteudotv,datatv;
        public ImageButton btdelete;
        public MyViewHolder(View view) {
            super(view);

            conteudotv = (TextView) view.findViewById(R.id.conteudoSecret);
            datatv = (TextView) view.findViewById(R.id.dataSecret);
            btdelete = (ImageButton) view.findViewById(R.id.btdelete);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        }
    }

    public SecretAdapter(List<Secret> lista,View view) {
        this.lista = lista;
        this.view = view;
    }



    @Override
    public SecretAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meus_secrets, parent, false);
        return new SecretAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
        final String nick = sharedPreferences.getString("NICK", "");

        Secret s = lista.get(position);
        final String key = s.getData();

        holder.conteudotv.setText(s.getConteudo());
        holder.datatv.setText(s.getData());
        holder.btdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference = FirebaseDatabase.getInstance().getReference();

                databaseReference
                        .child("usuarios")
                        .child(nick)
                        .child("scraps")
                        .child(key).removeValue();

                Toast.makeText(view.getContext(), "Excluído ^^", Toast.LENGTH_SHORT).show();

            }
        });

    }


    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void limpalista(){
        lista.clear();
    }


}