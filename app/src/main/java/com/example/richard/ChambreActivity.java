package com.example.richard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;


public class ChambreActivity extends DrawerActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switch1, switch2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chambre);

        addListenerOnButtonClick();
    }

    public void addListenerOnButtonClick(){
        //Getting the ToggleButton and Button instance from the layout xml file
        switch1= (Switch) findViewById(R.id.toggleButton);
        switch2= (Switch) findViewById(R.id.toggleButton2);
        Button buttonSubmit = (Button) findViewById(R.id.button);

        //Performing action on button click
        buttonSubmit.setOnClickListener(view -> {
            //Displaying the message in toast
            String result = "Lampe : " + switch1.getText() +
                    "\nOrdinateur : " + switch2.getText();
            Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
            //Si c'est ON, on envoie directement la commande pour allumer
            //Dans le cas contraire, on envoie l'autre commande
            if(switch1.getText().equals("ON")){
                //On allume la lampe
                ChambreActivity.super.aBluetoothOp.setDirection("on;chambre;1;");
            }
            else { //on éteint la lampe
                ChambreActivity.super.aBluetoothOp.setDirection("off;chambre;1;");
            }
            ChambreActivity.super.aBluetoothOp.manageConnectedSocket(); // commande d'envoi
            if(switch2.getText().equals("ON")){
                //On allume le PC
                ChambreActivity.super.aBluetoothOp.setDirection("on;chambre;3;");
            }
            else { //on éteint PC
                ChambreActivity.super.aBluetoothOp.setDirection("off;chambre;3;");
            }
            ChambreActivity.super.aBluetoothOp.manageConnectedSocket(); //commande d'envoi
        });

    }
}