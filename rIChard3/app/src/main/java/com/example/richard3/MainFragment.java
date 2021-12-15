package com.example.richard3;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

public class MainFragment extends Fragment {

    private Switch              aLampeChambre;
    private Switch              aPC;
    private Switch              aLampeSalon;
    private Switch              aTV;
    private Spinner             aSpinner;
    private MainActivity        aMainActivity;
    private BluetoothThreadApp  aBluetoothOp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        this.aLampeChambre = view.findViewById(R.id.toggleButton);
        this.aPC = view.findViewById(R.id.toggleButton2);
        this.aLampeSalon = view.findViewById(R.id.toggleButton3);
        this.aTV = view.findViewById(R.id.toggleButton4);
        this.aSpinner = view.findViewById(R.id.spinner);
        this.startDiscovery();//On appelle la méthode startDiscovery pour établir la liste des appareils
        BluetoothAdapter vBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!vBluetoothAdapter.isEnabled())//Si le bluetooth n’est pas activé, on demande à l’utilisateur de le faire
        {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 0);
        }

        this.aLampeChambre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aBluetoothOp.writeMessage("on;chamb;1;");
                }
            }
        });
        this.aPC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aBluetoothOp.writeMessage("on;chamb;3;");
                }
            }
        });
        this.aLampeSalon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aBluetoothOp.writeMessage("on;salon;1;");
                }
            }
        });
        this.aTV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aBluetoothOp.writeMessage("on;chamb;2;");
                }
            }
        });
        //yet another button listener

//        try{
//            aBluetoothOp.writeMessage(aMainActivity.getSttResult());
//        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.Appareils, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.aSpinner.setAdapter(adapter);

        return view;
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
        ArrayAdapter vArrayAdapter = new ArrayAdapter(this.getContext(), R.layout.support_simple_spinner_dropdown_item, vDevicesNames);
        vArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//On choisitt l’affichage de la liste du spinner
        this.aSpinner.setAdapter(vArrayAdapter);
        //this.aSpinner.setOnItemSelectedListener();
    }

    //Méthode onItemSelected
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
            this.aSpinner.setEnabled(false);//On désactive le bouton car on a deja choisit un appareil
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

    //On créer la classe ButtonListener mais toujours dans ce fichier
    public class ButtonListener implements View.OnClickListener
    {
        private char aDirection;

        //Constructeur naturel de la classe ButtonListener
        private ButtonListener(final char pChar)
        {
            this.aDirection = pChar;
        }

        //Méthode onClick qui détecte un clic et agit en conséquence
        public void onClick(final View pView)
        {
            if(MainFragment.this.aBluetoothOp != null)
            {
                MainFragment.this.aBluetoothOp.setDirection("" + this.aDirection);//On change la direction à transmettre en récupérant celle associée au bouton sur lequel on vient de cliquer
                if(this.aDirection == 'p')//Si il s’agit du bouton de déconnexion
                {
                    MainFragment.this.aBluetoothOp.deconnect();//On se déconnecte
                    MainFragment.this.startDiscovery();//On rappelle startDiscovery pour l’avoir de nouveau
                }
            }
            else
            {

            }
        }
    }
}
