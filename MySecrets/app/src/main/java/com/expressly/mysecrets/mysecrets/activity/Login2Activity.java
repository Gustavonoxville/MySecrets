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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.expressly.mysecrets.mysecrets.R;
import com.expressly.mysecrets.mysecrets.model.User2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login2Activity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private Button btnReset,  btnSignIn;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnSignIn = (Button) findViewById(R.id.sign_in_button);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login2Activity.this, SelecioneNickActivity.class));
            finish();
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login2Activity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  startActivity(new Intent(Login2Activity.this, Login2Activity.class));

                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Coloque um email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Coloque uma senha", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login2Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                final String email = inputEmail.getText().toString().trim();
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(Login2Activity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                    SharedPreferences sharedPreferences = getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("EMAIL", email);
                                    editor.apply();

                                    databaseReference = FirebaseDatabase.getInstance().getReference();
                                    databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            Log.i("LOG", "AQUIiiiiiiiiiii" + dataSnapshot);

                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                Log.i("LOG", postSnapshot.getKey());

                                                User2 user2 = postSnapshot.getValue(User2.class);

                                                if (email.equals(user2.getEmail())) {
                                                    Log.i("LOG", ">>>>>>>>>>>>>>>>>>>>>>>>>");
                                                    SharedPreferences sharedPreferences = getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("NICK", postSnapshot.getKey());
                                                    editor.putString("NOME", user2.getNome());
                                                    editor.putBoolean("NICK_JA_SELECIONADO", true);
                                                    editor.apply();
                                                    Intent intent = new Intent(Login2Activity.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }

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
                        });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
