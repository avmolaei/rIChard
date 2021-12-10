package com.example.richard;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

//On créer la classe ButtonListener mais toujours dans ce fichier
    public class ButtonListener extends DrawerActivity implements View.OnClickListener
    {
        private final String aDirection;

        //Constructeur naturel de la classe ButtonListener
        ButtonListener(final String pString)
        {
            this.aDirection = pString;
        }

        //Méthode onClick qui détecte un clic et agit en conséquence
        public void onClick(final View pView)
        {
            if(super.aBluetoothOp != null)
            {
                //MainActivity.this.aBluetoothOp.setDirection("" + this.aDirection);//On change la commande à transmettre en récupérant celle associée au bouton sur lequel on vient de cliquer
                Toast.makeText(getApplicationContext(), "" + this.aDirection, Toast.LENGTH_SHORT).show();//On affiche la commande sélectionnée à l’aide d’un toast
                if(this.aDirection.equals("Chambre")) //S'il s’agit du bouton de la chambre
                {
                    new Intent(this, ChambreActivity.class);
                    // MainActivity.this.aBluetoothOp.deconnect();//On se déconnecte
                    // MainActivity.this.startDiscovery();//On rappelle startDiscovery pour l’avoir de nouveau
                }
                else if(this.aDirection.equals("Salon")) {
                    new Intent(this, SalonActivity.class);
                }//le .class est à créér/compiler

            }
            else
            {
                //Toast.makeText(getApplicationContext(), "" + this.aDirection, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "No device connected !", Toast.LENGTH_SHORT).show();//
            }
        }
}