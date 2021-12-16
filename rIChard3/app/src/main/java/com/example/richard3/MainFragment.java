package com.example.richard3;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private Button              aApply;
    private Boolean             aSwitch1State;
    private Boolean             aSwitch2State;
    private Boolean             aSwitch3State;
    private Boolean             aSwitch4State;
    private Spinner             aSpinner;
    private View                aView;
    private MainActivity        aMainActivity;
    private BluetoothThreadApp  aBluetoothOp;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";
    public static final String SWITCH2 = "switch2";
    public static final String SWITCH3 = "switch3";
    public static final String SWITCH4 = "switch4";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.aView = inflater.inflate(R.layout.fragment_main, container, false);
        Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
        this.aLampeChambre = aView.findViewById(R.id.toggleButton);
        this.aPC = aView.findViewById(R.id.toggleButton2);
        this.aLampeSalon = aView.findViewById(R.id.toggleButton3);
        this.aTV = aView.findViewById(R.id.toggleButton4);
        this.aApply = aView.findViewById(R.id.button);
        this.aSpinner = aView.findViewById(R.id.spinner);
        this.startDiscovery();//On appelle la méthode startDiscovery pour établir la liste des appareils
        BluetoothAdapter vBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!vBluetoothAdapter.isEnabled())//Si le bluetooth n’est pas activé, on demande à l’utilisateur de le faire
        {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 0);
        }

        this.aLampeChambre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if(aBluetoothOp != null)
                        aBluetoothOp.writeMessage("test");
                        Toast.makeText(getContext(), "test2", Toast.LENGTH_SHORT).show();
                }
                else
                {

                }
            }
        });
        this.aPC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                }
                else
                {

                }
            }
        });
        this.aLampeSalon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                }
                else
                {

                }
            }
        });
        this.aTV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                }
                else
                {

                }
            }
        });
        this.aApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });


        loadData();
        updateViews();

        return aView;
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
        this.aSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)//On verifie si c'est le premier élément du spinner qui est sélectionné (donc déconnecté)
                {
                    if(aBluetoothOp != null)
                    {
                        aBluetoothOp.interrupt();//On interrompt le thread donc on va sortir de la boucle de la classe BluetoothThreadApp
                    }
                }
                else
                {
                    aSpinner.setEnabled(false);//On désactive le bouton car on a deja choisit un appareil
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();//On arrête donc la recherche de BluetoothDevice
                    BluetoothDevice vBluetoothDevice = BluetoothAdapter.getDefaultAdapter().getBondedDevices().toArray(new BluetoothDevice[0])[i-1];//On crée un BluetoothDevice en fonction de ce qu'on a sélectionné
                    aBluetoothOp = new BluetoothThreadApp(MainFragment.this, vBluetoothDevice);//On crée un nouvel objet de type BluetoothThreadApp
                    aBluetoothOp.start();//On appelle la méthode start pour démarrer le thread
                    aBluetoothOp.run();//On appelle la méthode run de la classe BluetoothThreadApp
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void saveData()
    {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCH1, aLampeChambre.isChecked());
        editor.putBoolean(SWITCH2, aPC.isChecked());
        editor.putBoolean(SWITCH3, aLampeSalon.isChecked());
        editor.putBoolean(SWITCH4, aTV.isChecked());

        editor.apply();
    }

    public void loadData()
    {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        aSwitch1State = sharedPreferences.getBoolean(SWITCH1, false);
        aSwitch2State = sharedPreferences.getBoolean(SWITCH2, false);
        aSwitch3State = sharedPreferences.getBoolean(SWITCH3, false);
        aSwitch4State = sharedPreferences.getBoolean(SWITCH4, false);
    }

    public void updateViews()
    {
        aLampeChambre.setChecked(aSwitch1State);
        aPC.setChecked(aSwitch2State);
        aLampeSalon.setChecked(aSwitch3State);
        aTV.setChecked(aSwitch4State);
    }
}
