package com.example.richard;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.richard.databinding.ActivityDrawerBinding;

import java.util.ArrayList;
import java.util.Locale;

public class DrawerActivity extends AppCompatActivity {


    //LISTE DE ATTRIBUTS
    private AppBarConfiguration mAppBarConfiguration;
    public ActivityDrawerBinding binding;
    public String sttResult = "null";
    public com.example.richard.BluetoothThreadApp aBluetoothOp;
    private Spinner aSpinner;
    private Button aBoutonChambre;
    private Button aBoutonSalon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.aBoutonChambre = this.findViewById(R.id.boutonChambre);
        this.aBoutonChambre.setOnClickListener(new ButtonListener("Chambre"));
        this.aBoutonSalon = this.findViewById(R.id.boutonSalon);
        this.aBoutonSalon.setOnClickListener(new ButtonListener("Salon"));
        this.aSpinner = this.findViewById(R.id.spinner);

        binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDrawer.toolbar);
        binding.appBarDrawer.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSpeechToText();
                // deprecated
                // jlenleve psk anthony il rage y peut pas piger mon génie
                // Snackbar.make(view, "Commande précédente: " + sttResult, Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    /** START BLUETOOTH */

        this.startDiscovery();//On appelle la méthode startDiscovery pour établir la liste des appareils
        BluetoothAdapter vBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!vBluetoothAdapter.isEnabled())//Si le bluetooth n’est pas activé, on demande à l’utilisateur de le faire
        {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //Methode startDiscovery
    public void startDiscovery()
    {
        this.aSpinner.setEnabled(true);//On active ou réactive le spinner
        BluetoothDevice[] vBluetoothDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices().toArray(new BluetoothDevice[0]);//On créé une liste des appareils
        String[] vDevicesNames = new String[vBluetoothDevices.length+1];//On crée la liste mais que de leurs noms (qu'on affichera)
        vDevicesNames[0] = "No device connected";//Le premier élément signifie qu’il n’y a pas d’appareil
        for(int i=0; i<vBluetoothDevices.length; i++)//Pour chaque élément
        {
            vDevicesNames[i+1] = vBluetoothDevices[i].getName();//On ajoute son nom à la liste
        }
        ArrayAdapter vArrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, vDevicesNames);
        vArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//On choisitt l’affichage de la liste du spinner
        this.aSpinner.setAdapter(vArrayAdapter);
        this.aSpinner.setOnItemSelectedListener(this);
    }

    //Méthode à decortiquer
    public void onItemSelected(final AdapterView<?> pAdapterView, final View pView, final int pInt, final long pLong)
    {
        if(pInt == 0)//On verifie si c'est le premier élément du spinner qui est sélectionné (donc déconnecté)
        {
            if(this.aBluetoothOp != null)
            {
                this.aBluetoothOp.interrupt();//On interrompt le thread donc on va sortir de la boucle de la classe BluetoothThreadApp
            }
        }
        else
        {
            this.findViewById(R.id.spinner).setEnabled(false);//On désactive le bouton car on a deja choisit un appareil
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();//On arrête donc la recherche de BluetoothDevice
            BluetoothDevice vBluetoothDevice = BluetoothAdapter.getDefaultAdapter().getBondedDevices().toArray(new BluetoothDevice[0])[pInt-1];//On crée un BluetoothDevice en fonction de ce qu'on a sélectionné
            this.aBluetoothOp = new BluetoothThreadApp(this, vBluetoothDevice);//On crée un nouvel objet de type BluetoothThreadApp
            this.aBluetoothOp.start();//On appelle la méthode start pour démarrer le thread
            this.aBluetoothOp.run();//On appelle la méthode run de la classe BluetoothThreadApp

        }
    }

    //Méthode onNothingSelected
    public  void onNothingSelected(final AdapterView<?> pAdapterView)
    {
        //Même si inutilisée, on déclare la méthode onNothingSelected car on a implémenté OnItemSelectedListener
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
        try{
            DrawerActivity.this.aBluetoothOp.setDirection(sttParse); //replace with sttParse
            DrawerActivity.this.aBluetoothOp.manageConnectedSocket();
        }
        catch (Exception e){
            Toast.makeText("No Device connected!");
        }


    }
}
