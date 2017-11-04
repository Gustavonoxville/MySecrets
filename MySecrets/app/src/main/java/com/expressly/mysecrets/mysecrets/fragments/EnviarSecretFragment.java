package com.expressly.mysecrets.mysecrets.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.expressly.mysecrets.mysecrets.R;
import com.expressly.mysecrets.mysecrets.model.Scrap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustavo on 28/07/2017.
 */

public class EnviarSecretFragment extends Fragment {

    //   private EditText inputNick;
    private AutoCompleteTextView actv;

    private EditText msg;
    private Button sendMsg;
    View view;
    private String nick = "";
    private DatabaseReference databaseReference;
    private   List<String> pessoas = new ArrayList<String>();

    public EnviarSecretFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fragment_enviasecret, container, false);

        actv = (AutoCompleteTextView) view.findViewById(R.id.nickdapessoa);

        ArrayAdapter adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, pessoas);

        actv.setAdapter(adapter);

        msg = (EditText) view.findViewById(R.id.msg);
        sendMsg = (Button) view.findViewById(R.id.enviarSecret);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
        nick = sharedPreferences.getString("NICK", "");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        listarUsers();

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(actv.getText()) && !nick.equals(actv.getText().toString())) {

                    String key = databaseReference.push().getKey();

                    if (!databaseReference
                            .child("usuarios")
                            .child(actv.getText().toString().replaceAll(" ", ""))
                            .child("scraps")
                            .child(key)
                            .setValue(new Scrap(msg.getText().toString()))
                            .isSuccessful()) {

                        Toast.makeText(view.getContext(), "Mensagem enviada ^^", Toast.LENGTH_SHORT).show();
                        actv.setText("");
                        msg.setText("");


                    } else {
                        Toast.makeText(view.getContext(), "Erro,Mensagem não enviada :/", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(view.getContext(), "Nick inválido :/", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;

    }

    private void listarUsers() {


        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("LOG","AQUIiiiiiiiiiii" + dataSnapshot);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.i("LOG", postSnapshot.getKey());
                    pessoas.add(postSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("LOG", "Failed to read value.", error.toException());
            }
        });


    }
}