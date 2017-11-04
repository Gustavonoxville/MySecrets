package com.expressly.mysecrets.mysecrets.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.expressly.mysecrets.mysecrets.R;
import com.expressly.mysecrets.mysecrets.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelecioneNickActivity extends AppCompatActivity {

    private EditText inputNick;
    private EditText inputNome;
    private Button enterNick;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private String email = "";
    private boolean jaTemNick = false;
    private FirebaseAuth auth;
    private List<String> pessoas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecione_nick);

        SharedPreferences sharedPreferences = getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("EMAIL", "");
        jaTemNick = sharedPreferences.getBoolean("NICK_JA_SELECIONADO", false);

        if (jaTemNick) {
            Intent i = new Intent(SelecioneNickActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        auth = FirebaseAuth.getInstance();

        inputNick = (EditText) findViewById(R.id.nick);
        inputNome = (EditText) findViewById(R.id.nome);
        enterNick = (Button) findViewById(R.id.enter_nick);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        verificaNick();

//        if (!databaseReference
//                .child("usuarios")
//                .child(inputNick.getText().toString().replaceAll(" ", ""))
//                .setValue(new User(email, inputNome.getText().toString()))
//                .isSuccessful())

        enterNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(inputNick.getText()) && !TextUtils.isEmpty(inputNome.getText())) {

                    if (!pessoas.contains(inputNick.getText().toString())) {

                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference
                           .child("usuarios")
                           .child(inputNick.getText().toString().replaceAll(" ", ""))
                           .setValue(new User(email, inputNome.getText().toString()));

                        SharedPreferences sharedPreferences = getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("NICK", inputNick.getText().toString());
                        editor.putString("NOME", inputNome.getText().toString());
                        editor.putBoolean("NICK_JA_SELECIONADO", true);
                        editor.commit();

                        Intent i = new Intent(SelecioneNickActivity.this, MainActivity.class);
                        startActivity(i);
                        // finish();


                    } else {
                        Toast.makeText(getApplicationContext(), "Nick já existente :/", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Campos em branco :/", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private  void verificaNick(){

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("LOG","AQUIiiiiiiiiiii  222" + dataSnapshot);
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
    @Override
    public void onBackPressed() {
        Log.i("LOG", "back");

        auth.signOut();
        Intent i = new Intent(SelecioneNickActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
        return;
    }
}
