package com.example.richard3;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import android.widget.Toast;
import java.util.UUID;

public class BluetoothThreadApp extends Thread
{
    //Attributs de la classe BluetoothThreadApp
    private MainFragment    aMainActivity;
    private BluetoothDevice aBluetoothDevice;
    private BluetoothSocket aBluetoothSocket;
    private InputStream     aInputStream;
    private OutputStream    aOutputStream;
    private String          aDirection;

    //Constructeur naturel de la classe BluetoothThreadApp
    public BluetoothThreadApp(final MainFragment pMainActivity, final BluetoothDevice pBluetoothDevice)
    {
        this.aBluetoothDevice = pBluetoothDevice;
        this.aMainActivity = pMainActivity;
    }

    //Modificateur de l’attribut aDirection
    public void setDirection(final String pString)
    {
        this.aDirection = pString;
    }

    //Méthode run
    public void run()
    {
        try
        {
            this.aBluetoothSocket = this.aBluetoothDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));//On cree une BluetoothSocket a partir d’un String que l’on cast en UUID
        }
        catch(IOException pIOException)
        {
            pIOException.printStackTrace();//On affiche l’exception
        }
        try
        {
            if(this.aBluetoothSocket != null)//On vérifie si l’attribut n’est pas nul pour pouvoir appeler une méthode dessus
            {
                this.aBluetoothSocket.connect();//On appelle la méthode connect qui se connecte à un appareil
            }
            manageConnectedSocket();//On appelle la méthode manageConnectedSocket qui va donner toutes les instructions pendant que la connection est faite (écrire et lire sur les streams pour envoyer un message)
        }
        catch(IOException pIOException)
        {
            pIOException.printStackTrace();//On affiche l’exception
            this.aMainActivity.getView().findViewById(R.id.spinner).post(() -> {
                Toast.makeText(BluetoothThreadApp.this.aMainActivity.getContext(), "Connection failed !", Toast.LENGTH_SHORT).show();//On affiche un Toast pour prévenir l’utilisateur que la connection n’a pas pu se faire
            });
        }
    }

    //Méthode manageConnectedSocket
    private void manageConnectedSocket()
    {
        while(!Thread.currentThread().isInterrupted())//Tant que le thread n’est pas interrompu, on reste dans la boucle
        {
            this.writeMessage(this.aDirection);//On appelle la méthode writeMessage avec la direction en paramètre
            this.readMessage();//On appelle la méthode readMessage
            try
            {
                Thread.sleep(100);//On met en pause le Thread pendant 100 millisecondes par prévention et pour éviter d’écrire plus que nécéssaire
            }
            catch(InterruptedException pInterruptedException)
            {
                pInterruptedException.printStackTrace();//On affiche l’exception
            }
        }
        this.deconnect();//Quand le Thread est interrompu, on sort de la boucle du dessus et on appelle la méthode deconnect
    }

    //Méthode writeMessage
    void writeMessage(final String pString)
    {
        try
        {
            if(this.aBluetoothSocket != null)//On vérifie si aBluetoothSocket n’est pas nul pour pouvoir appeler une méthode dessus
            {
                this.aInputStream = this.aBluetoothSocket.getInputStream();//On initialise aInputStream avec la méthode getInputStream pour pouvoir écrire dessus
                this.aOutputStream = this.aBluetoothSocket.getOutputStream();//On initialise aInputStream avec la méthode getOutputStream pour plus tard pouvoir lire ce qu'on envoie
            }
            else
            {

            }
        }
        catch(IOException pIoException)
        {
            pIoException.printStackTrace();//On affiche l’exception
        }
        try
        {
            if(this.aOutputStream != null)//On vérifie si aOutputStream n’est pas nul pour pouvoir appeler une méthode dessus
            {
                this.aOutputStream.write(pString.getBytes());//On écrit ce qui est passé en paramètre sur aOutputStream à l’aide de la méthode write
            }
            else
            {

            }
        }
        catch(IOException pIOException)
        {
            pIOException.printStackTrace();//On affiche l’exception
        }
    }

    private void readMessage()
    {
        if(this.aInputStream != null)//On vérifie si aInputStream n’est pas nul pour pouvoir appeler une méthode dessus
        {
            BufferedReader vBufferedReader = new BufferedReader(new InputStreamReader(this.aInputStream));//On cree une variable locale de type BufferedReader sur aInputStream
            try
            {
                if(vBufferedReader.ready())//Si vBufferedReader.ready() renvoie true, on lit ce qu’il y a sur aInputStream
                {
                    String vMessage = "" + vBufferedReader.read();
                }
            }
            catch(IOException pIOException)
            {
                pIOException.printStackTrace();//On affiche l’exception
            }
        }
        else
        {

        }
    }

    //Méthode deconnect
    public void deconnect()
    {
        try
        {
            if (this.aBluetoothSocket != null)//On vérifie si aBluetoothSocket n’est pas nul pour pouvoir appeler une méthode dessus
            {
                this.aBluetoothSocket.close();//On appelle la méthode close qui ferme aBluetoothSocket et ses streams
            }
        }
        catch(IOException pIOException)
        {
            pIOException.printStackTrace();//On affiche l’exception
        }
    }
}