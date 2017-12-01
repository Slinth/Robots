package reseau;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

import view.Launch;

abstract  class CommunicationThread extends Thread {
    /** Port de communication*/
    public static int PORT = 7777;
    
    /** Socket du serveur */
    public ServerSocket s;
    
    /** Socket de communication pour le(s) client(s) */
    public Socket socket;
    
    /** Buffer pour la lecture (reception) */
    public BufferedReader in;
    
    /** Buffer pour l'ecriture (envoi) */
    public PrintWriter out;
    
    /** Indicateur de fin de partie*/
    boolean fin = false;
    
    /** Reference sur le plateu de jeu */
    public Launch jeu;
    
    /**  Transmet les coordonnees des robots / intrus sous la forme d'une chaine de caractères à l'aide des buffers */
    void transmettreCoordonnees(int x,int y) {
            String str = ""+x+","+y;
            out.println(str);
            System.out.println("transmettreCoordonnees(" +x+","+y+")");
    }

    /**
     * Boucle principale qui tourne tout le long de la partie
     * Attend un message 
     */
    public void run() {
        while (!fin) {
        	//INSERER CODE TRANSMETTANT LES COORDONNEES DE TOUS LES COMPOSANTS
        	//REDESSINER LE PLATEAU AVEC LES NOUVELLES POSITIONS / COULEURS
        }
    }
    
    public void fin()
    {
        fin = true;
    }
    
}