package com.expressly.mysecrets.mysecrets.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;


import com.expressly.mysecrets.mysecrets.R;
import com.expressly.mysecrets.mysecrets.fragments.EnviarSecretFragment;
import com.expressly.mysecrets.mysecrets.fragments.MeusSecretsFragment;
import com.expressly.mysecrets.mysecrets.fragments.PerfilFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigation;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
        String nick = sharedPreferences.getString("NICK", "");

        Log.i("LOGGGG", nick);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);

        fragmentManager = getSupportFragmentManager();

        final Fragment fragment1 = new EnviarSecretFragment();
        final Fragment fragment2 = new MeusSecretsFragment();
        final Fragment fragment3 = new PerfilFragment();


        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_enviar_secret:

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, fragment1).commit();
                        return true;
                    case R.id.action_meus_secrets:

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, fragment2).commit();
                        return true;
                    case R.id.action_perfil:

                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, fragment3).commit();
                        return true;
                }

                return true;
            }

        });

        bottomNavigation.getMenu().getItem(1).setChecked(true);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, MeusSecretsFragment.newInstance());
        transaction.commit();

    }
}