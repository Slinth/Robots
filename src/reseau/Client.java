package reseau;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import view.Launch;

/**
 *	Classe Client = Joueur de l'intrus
 *  Initialise la pour la communication qui se connecte sur le port et l'adresse du serveur 
 *  Initialise les buffers pour les messages
 */
class Client extends CommunicationThread {
    
    public Client(Launch jeu) {
        super();
        this.jeu = jeu;
        try {
            InetAddress addr = InetAddress.getByName(jeu.getServerName());
            System.out.println("adresse : " + addr);
            // Attendre une connection
            socket = new Socket(addr, jeu.getPort());
            try {
                System.out.println("socket = " + socket);
                InputStreamReader socket_in = new InputStreamReader( socket.getInputStream());
                in = new BufferedReader( socket_in );
                OutputStreamWriter socket_out =  new OutputStreamWriter( socket.getOutputStream());
                out = new PrintWriter( new BufferedWriter(socket_out),true);
            }
            catch (IOException e){}
        }
        catch (IOException e){}
    }
}