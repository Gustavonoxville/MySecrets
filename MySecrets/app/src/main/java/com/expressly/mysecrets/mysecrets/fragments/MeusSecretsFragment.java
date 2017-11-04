package com.expressly.mysecrets.mysecrets.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expressly.mysecrets.mysecrets.R;
import com.expressly.mysecrets.mysecrets.activity.MainActivity;
import com.expressly.mysecrets.mysecrets.adapters.SecretAdapter;
import com.expressly.mysecrets.mysecrets.model.Scrap;
import com.expressly.mysecrets.mysecrets.model.Secret;
import com.expressly.mysecrets.mysecrets.model.Util;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by gustavo on 28/07/2017.
 */

public class MeusSecretsFragment extends Fragment {

    private List<Secret> listS = new ArrayList<>();
    private RecyclerView recyclerView;
    private View view;
    private SecretAdapter mAdapter;
    private String nick = "";
    private TextView textSemScraps;
    private ImageView imageSemScraps;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    public MeusSecretsFragment() {
    }

    public static MeusSecretsFragment newInstance() {
        MeusSecretsFragment fragment = new MeusSecretsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.layout_fragment_meussecrets, container, false);

        textSemScraps = (TextView) view.findViewById(R.id.textoSem);
        imageSemScraps = (ImageView) view.findViewById(R.id.imageSemScraps);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new SecretAdapter(listS, view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
        nick = sharedPreferences.getString("NICK", "");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("usuarios").child(nick).child("scraps").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mAdapter.limpalista();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Scrap scrap = postSnapshot.getValue( Scrap.class);
                    Log.e("Get Data", scrap.getRecado());

                    Secret feed = new Secret(scrap.getRecado(), postSnapshot.getKey());
                    listS.add(feed);
                    textSemScraps.setVisibility(View.INVISIBLE);
                    imageSemScraps.setVisibility(View.INVISIBLE);

                    if(!Util.isMyApplicationTaskOnTop(view.getContext())){

                        Intent intent = new Intent(view.getContext(), MainActivity.class);
                        PendingIntent pIntent = PendingIntent.getActivity(view.getContext(), (int) System.currentTimeMillis(), intent, 0);

                        // Build notification
                        // Actions are just fake
                        Notification noti = new Notification.Builder(view.getContext())
                                .setContentTitle("Você possui novos Secrets( ͡° ͜ʖ ͡°)")
                                .setContentText("Clique para visualizar").setSmallIcon(R.drawable.devil)
                                .setContentIntent(pIntent).build();
                        NotificationManager notificationManager = (NotificationManager) view.getContext().getSystemService(NOTIFICATION_SERVICE);
                        // hide the notification after its selected
                        noti.flags |= Notification.FLAG_AUTO_CANCEL;

                        notificationManager.notify(0, noti);
                    }
                }
                if(listS.isEmpty()){
                    progressBar.setVisibility(View.VISIBLE);
                    textSemScraps.setVisibility(View.VISIBLE);
                    imageSemScraps.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.INVISIBLE);
           //     mAdapter.notifyAll();
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("LOG", "Failed to read value.", error.toException());
            }
        });

        return view;
    }

}