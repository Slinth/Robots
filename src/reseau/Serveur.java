package reseau;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;

import view.Launch;

/**
 *	Classe Serveur = Jeu
 * 	Initialise la ServerSocket et attend la connection d'un ou plusieurs clients
 * 	Initialise les buffers pour les messages
 */
class Serveur extends CommunicationThread {
    public Serveur(Launch jeu) {
        super();
        this.jeu = jeu;
        try {
            InetAddress addr = InetAddress.getByName(jeu.getServerName());
            System.out.println("adresse : " + addr);
            s = new ServerSocket(jeu.getPort(), 1, addr);
            System.out.println("Socket lancee : " + s);
            // Attendre une connection
            socket = s.accept();
            try {
                System.out.println( "Connection acceptee: "+ socket);
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