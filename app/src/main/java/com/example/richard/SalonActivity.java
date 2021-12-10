package com.example.richard;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;


public class ChambreActivity extends DrawerActivity {
    private ToggleButton toggleButton1, toggleButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salon);

        addListenerOnButtonClick();
    }

    public void addListenerOnButtonClick(){
        //Getting the ToggleButton and Button instance from the layout xml file
        toggleButton1=(ToggleButton)findViewById(R.id.toggleButton);
        toggleButton2=(ToggleButton)findViewById(R.id.toggleButton2);
        Button buttonSubmit = (Button) findViewById(R.id.button);

        //Performing action on button click
        buttonSubmit.setOnClickListener(view -> {
            //Displaying the message in toast
            String result = "Lampe : " + toggleButton1.getText() +
                    "\nTélé : " + toggleButton2.getText();
            Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
            //Si c'est ON, on envoie directement la commande pour allumer
            //Dans le cas contraire, on envoie l'autre commande
            if(toggleButton1.getText().equals("ON")){
                //On allume la lampe
                ChambreActivity.super.aBluetoothOp.setDirection("on;chambre;1;");
            }
            else { //on éteint la lampe
                ChambreActivity.super.aBluetoothOp.setDirection("off;chambre;1;");
            }
            ChambreActivity.super.aBluetoothOp.manageConnectedSocket(); // commande d'envoi
            if(toggleButton2.getText().equals("ON")){
                //On allume le PC
                ChambreActivity.super.aBluetoothOp.setDirection("on;chambre;2;");
            }
            else { //on éteint PC
                ChambreActivity.super.aBluetoothOp.setDirection("off;chambre;2;");
            }
            ChambreActivity.super.aBluetoothOp.manageConnectedSocket(); //commande d'envoi
        });

    }
}
