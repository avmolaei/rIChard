package com.example.richard3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout aDrawerLayout;
    private String sttResult = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.aDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.aDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.aDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_main);
        }
    }

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Dites une commande...");
        try {
            startActivityForResult(intent, 666);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Désolé, la reconnaissance vocale Google n'est pas disponible sur cet appareil : ( uWu (cheh en vrai).",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (this.aDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.aDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
                break;
            case R.id.nav_bluetooth:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BluetoothFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.nav_legal_notice:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LegalNoticeFragment()).commit();
                break;
        }
        this.aDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    protected String parseCommand(String sstResult){
        String commandWord = "";
        String room = "";
        String device = "";


        if (sttResult.contains("éteindre") || sttResult.contains("éteins")) {
            commandWord += "off";
            commandWord += ";";
        }
        else if (sttResult.contains("allumer") || sttResult.contains("allume")||sttResult.contains("allumes")||sttResult.contains("allumé")){
            commandWord += "on";
            commandWord += ";";
        }

        if (sttResult.contains("salon")) {
            room += "salon";
            room += ";";
        }
        else if (sttResult.contains("chambre")) {
            room += "chambre";
            room += ";";
        }

        if (sttResult.contains("lampe") || sttResult.contains("lumière")) {
            device += "1";
            device += ";";
        }
        else if (sttResult.contains("télé") || sttResult.contains("TV")||sttResult.contains("télévision")){
            device += "2";
            device += ";";
        }
        else if (sttResult.contains("ordinateur") || sttResult.contains("PC") || sttResult.contains("ordi")) {
            device += "3";
            device += ";";
        }

        //formattage de sortie: commmande;piece;numéro;
        String output = commandWord+room+device;

        Toast.makeText(getApplicationContext(),
                output,
                Toast.LENGTH_SHORT).show();
        return output;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 666: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    sttResult = text;
                }
                break;
            }
        }

        Toast.makeText(getApplicationContext(), sttResult, Toast.LENGTH_SHORT).show();
        //INSERT PARSE FUNCTION
        String sttParse = parseCommand(sttResult);
        Toast.makeText(getApplicationContext(), sttParse, Toast.LENGTH_SHORT).show();
    }
}