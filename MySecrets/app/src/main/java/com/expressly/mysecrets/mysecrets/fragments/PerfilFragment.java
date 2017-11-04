package com.expressly.mysecrets.mysecrets.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.expressly.mysecrets.mysecrets.R;
import com.expressly.mysecrets.mysecrets.activity.LoginActivity;
import com.expressly.mysecrets.mysecrets.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by gustavo on 28/07/2017.
 */

public class PerfilFragment extends Fragment {

    private View view;
    private Button btnSingOut;
    private FirebaseAuth auth;
    private TextView meunick;
    private TextView meuLink;
    private String nick;

    public PerfilFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.layout_fragment_perfil, container, false);
        auth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
        nick = sharedPreferences.getString("NICK", "");

        meunick = (TextView) view.findViewById(R.id.meunick);
        meuLink = (TextView) view.findViewById(R.id.meuLink);

        meunick.setText("'" + nick + "'");
        meuLink.setPaintFlags(meuLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        meuLink.setText("https://expresslynow.com/" + nick);
        final String meulinktext = meuLink.getText().toString();
        meuLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://expresslynow.com/" + nick)));

                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                share.putExtra(Intent.EXTRA_SUBJECT, "Meu Link");
                share.putExtra(Intent.EXTRA_TEXT, meulinktext);

                startActivity(Intent.createChooser(share, meulinktext));

            }
        });

        btnSingOut = (Button) view.findViewById(R.id.sign_out_button);

        btnSingOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences settings = view.getContext().getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
                settings.edit().clear().apply();

                auth.signOut();

                FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user == null) {
                            // user auth state is changed - user is null
                            // launch login activity
                            startActivity(new Intent(view.getContext(), LoginActivity.class));
                            getActivity().finish();
                        }
                    }
                };

                startActivity(new Intent(view.getContext(), LoginActivity.class));


            }
        });
        return view;
    }
}