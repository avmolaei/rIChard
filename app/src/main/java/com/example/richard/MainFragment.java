package com.example.richard3;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

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
    private View                aView;
    private MainActivity        aMainActivity;
    private BluetoothThreadApp  aBluetoothOp;


    BluetoothSPP                aBluetooth;
    Button                      connect;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";
    public static final String SWITCH2 = "switch2";
    public static final String SWITCH3 = "switch3";
    public static final String SWITCH4 = "switch4";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.aView = inflater.inflate(R.layout.fragment_main, container, false);

        aBluetooth = new BluetoothSPP(aMainActivity);
        connect    = aView.findViewById(R.id.connect);

        this.aLampeChambre = (Switch) aView.findViewById(R.id.toggleButton);
        aLampeChambre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aLampeChambre.setText("Lampe 1: ON");
                }
                else{
                    aLampeChambre.setText("Lampe 1: OFF");

                }
            }
        });

        this.aPC = (Switch) aView.findViewById(R.id.toggleButton2);
        aPC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aPC.setText("PC: ON");
                }
                else {
                    aPC.setText("PC: OFF");
                }

            }
        });
        this.aLampeSalon = aView.findViewById(R.id.toggleButton3);
        aLampeSalon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aLampeSalon.setText("Lampe 2: ON");
                }
                else{
                    aLampeSalon.setText("Lampe 2: OFF");
                }
            }
        });
        this.aTV = aView.findViewById(R.id.toggleButton4);
        aTV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    aTV.setText("TV: ON");
                }
                else{
                    aTV.setText("TV: OFF");
                }
            }
        });

        if (!aBluetooth.isBluetoothAvailable()) {
            Toast.makeText(getContext(), "Le Bluetooth n'est pas disponible.", Toast.LENGTH_SHORT).show();
            aMainActivity.finish();
        }
        aBluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                connect.setText("Connecté à  " + name);
            }

            public void onDeviceDisconnected() {
                connect.setText("Connexion perdue");
            }

            public void onDeviceConnectionFailed() {
                connect.setText("Connexion échouée");
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aBluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    if(aBluetooth!=null){
                        aBluetooth.disconnect();
                    }
                } else {
                    Intent intent = new Intent(getContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

        this.aApply = aView.findViewById(R.id.button);
        this.aApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();


               if(aLampeChambre.getText().equals("Lampe 1: ON")){
                   aBluetooth.send("chamb1ON", true);
               }
               else{
                   aBluetooth.send("chamb1OF\n6", true);
               }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 100);
               if(aPC.getText().equals("PC: ON")){
                   aBluetooth.send("chamb2ON", true);
               }
               else{
                   aBluetooth.send("chamb2OF", true);
               }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 100);
                if(aLampeSalon.getText().equals("Lampe 2: ON")){
                    aBluetooth.send("salon1ON", true);
                }
                else{
                    aBluetooth.send("salon1OF", true);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 100);
                if(aTV.getText().equals("TV: ON")){
                    aBluetooth.send("salon2ON", true);
                }
                else{
                    aBluetooth.send("salon2OF", true);
                }



               saveData();
            }
        });


        loadData();
        updateViews();

        return aView;
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCH1, aLampeChambre.isChecked());
        editor.putBoolean(SWITCH2, aPC.isChecked());
        editor.putBoolean(SWITCH3, aLampeSalon.isChecked());
        editor.putBoolean(SWITCH4, aTV.isChecked());

        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        aSwitch1State = sharedPreferences.getBoolean(SWITCH1, false);
        aSwitch2State = sharedPreferences.getBoolean(SWITCH2, false);
        aSwitch3State = sharedPreferences.getBoolean(SWITCH3, false);
        aSwitch4State = sharedPreferences.getBoolean(SWITCH4, false);
    }

    public void updateViews() {
            aLampeChambre.setChecked(aSwitch1State);
            aPC.setChecked(aSwitch2State);
            aLampeSalon.setChecked(aSwitch3State);
            aTV.setChecked(aSwitch4State);
        }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                aBluetooth.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                aBluetooth.setupService();
            } else {
                Toast.makeText(aMainActivity.getApplicationContext()
                        , "Le Bluetooth n'est pas activé."
                        , Toast.LENGTH_SHORT).show();
                aMainActivity.finish();
            }
        }
    }


    public void onStart() {
        super.onStart();
        if (!aBluetooth.isBluetoothEnabled()) {
            aBluetooth.enable();
        } else {
            if (!aBluetooth.isServiceAvailable()) {
                aBluetooth.setupService();
                aBluetooth.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        aBluetooth.stopService();
    }

}
