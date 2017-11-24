package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * Classe représentant la grille de cellule pour la poursuite des robots
 * @author Decroix Dorian
 *
 */
public class Terrain {
	/** grille a l'instant t*/
	private Cellule [][] grille;
	
	/** taille de la grille*/
	private int n;
	
	/** taille de la grille*/
	private int m;
	
	/** nombre de fourmis */
	private int nbRobots;
	
	/** Nombre de caisses */
	private int nbCaisses;
	
	/**liste des robots*/
	private ArrayList<Robot> lesRobots;
	
	/**tableau des intrus*/
	private Intrus intrus;
	
	public Terrain()
	{
		this.grille = new  Cellule[20][20];
		this.n = 20;
		this.m = 20;
	}

	/** constructeur par defaut, initialise la taille, le nombre de cellules initiales,  
    ainsi que les grilles a l'instnat t et t-1*/
	public Terrain(int n, int m, int nbRobots, int pourcentageCaisses) {
		this.n = n;
		this.m = m;
		this.grille = new Cellule[n][m];
		this.nbRobots = nbRobots;
		this.nbCaisses = ((n * m) * pourcentageCaisses) / 100;
		initialiserGrille();
		initialiserSorties();
		initialiserCaisses();
		initialiserRobots(nbRobots);
		initialiserIntrus();
	}

	public Cellule[][] getGrille() {
		return grille;
	}
	
	public int getN() {
		return this.n;
	}
	
	public int getM() {
		return this.m;
	}
	
	public ArrayList<Robot> getLesRobots() {
		return this.lesRobots;
	}
	
	public Intrus getIntrus( ) {
		return this.intrus;
	}
	
	public int nbAlea(int borneSup) {
		Random r = new Random();
		return r.nextInt(borneSup);
	}

	/** 
	 * initialise les grilles a l'instant t et t-1 : ajout de cellules mortes et appel de initHasard
	 */
	private void initialiserGrille() {
		for(int i = 0; i< this.n; i++)
			for(int j = 0; j < this.m; j++)
				grille[i][j] = new Cellule(i, j);
	}
	
	/**
	 * Initialise les sorties
	 */
	private void initialiserSorties() {
		for (int i = 0; i < 4; i++) {
			grille[0][m / 2].setSortie(true);
			grille[n-1][m / 2].setSortie(true);
			grille[n / 2][0].setSortie(true);
			grille[n / 2][m-1].setSortie(true);
		}
	}
	
	/**
	 * Initialise les caisses dans certaines cellules
	 */
	private void initialiserCaisses() {
		for (int c = 0; c < this.nbCaisses; c++) {
			int i = nbAlea(this.n);
			int j = nbAlea(this.m);
			this.grille[i][j].setCaisse(true);
		}
	}
		
	/**
	 * Crée les robots
	 * @param nbRobots
	 */
	private void initialiserRobots(int nbRobots) {
		lesRobots = new ArrayList<Robot>();
		for(int r = 0; r < nbRobots; r++) {
			int i = nbAlea(this.n);
			int j = nbAlea(this.m);
			lesRobots.add(new Robot(i, j, this));
		}
	}
	
	/**
	 * Crée les intrus
	 * @param nbRobots
	 */
	private void initialiserIntrus() {
		int i = nbAlea(this.n);
		int j = nbAlea(this.m);
		intrus = new Intrus(i, j, this);
	}
	
	/**  
	 * demande a toutes les cellules de la grille a l'instant t d'evoluer, 
	 * c'est à dire de diffuser de la phéromone et d'en évaporer une partie
	 */
	public void animGrille() {
		for(Robot r : lesRobots) {
			r.evoluer(); 
		}
	}

}
