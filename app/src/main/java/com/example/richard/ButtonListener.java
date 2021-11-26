 //On créer la classe ButtonListener mais toujours dans ce fichier
    public class ButtonListener implements View.OnClickListener
    {
        private String aDirection;

        //Constructeur naturel de la classe ButtonListener
        private ButtonListener(final String pString)
        {
            this.aDirection = pString;
        }

        //Méthode onClick qui détecte un clic et agit en conséquence
        public void onClick(final View pView)
        {
            if(MainActivity.this.aBluetoothOp != null)
            {
                //MainActivity.this.aBluetoothOp.setDirection("" + this.aDirection);//On change la commande à transmettre en récupérant celle associée au bouton sur lequel on vient de cliquer

                Toast.makeText(getApplicationContext(), "" + this.aDirection, Toast.LENGTH_SHORT).show();//On affiche la commande sélectionnée à l’aide d’un toast
                if(this.aDirection == "Chambre") //S'il s’agit du bouton de la chambre
                {
                    Intent(this, ChambreActivity.class); //le .class est à créér/compiler
                    // MainActivity.this.aBluetoothOp.deconnect();//On se déconnecte
                    // MainActivity.this.startDiscovery();//On rappelle startDiscovery pour l’avoir de nouveau
                }
                else if(this.aDirection == "Salon") Intent(this, SalonActivity.class); //le .class est à créér/compiler
            }
            else
            {
                //Toast.makeText(getApplicationContext(), "" + this.aDirection, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "No device connected !", Toast.LENGTH_SHORT).show();//
            }
        }
    }