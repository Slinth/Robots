package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**classe representant un robot evoluant a la recherche de l'intrus*/
public class Robot {	/**position du robot*/
	Point p;
	/**direction  du robot*/
	private Direction direction;
	/**etat du robot*/
	private EtatRobot etat;	

	/**lien vers le terrain dans lequel se trouve le robot*/
	private Terrain terrain;
	
	private int pas;
	
	private Point positionIntrus;

	/**objet necessaire pour le tirage aleatoire de la prochaine direction*/
	private Random hasard;

	/**objet graphique associe a la fourmi*/
	private Circle dessin;

	public Robot(){}

	/**construit un robot
	 * @param x : coordonee x initiale du robot
	 * @param y : coordonee y initiale du robot
	 * @param terrain : terrain ou se trouve le robot
	 */
	public Robot(int x, int y, Terrain terrain) {
		this.p = new Point(x, y);
		this.terrain= terrain;
		this.direction = Direction.randomDirection();
		this.etat = EtatRobot.PATROUILLER;
		this.hasard = new Random();
		this.positionIntrus = new Point(-1, -1);
	}
	
	public void setPas(int pas) {
		this.pas = pas;
	}
	
	public void setCoordonneesIntrus(int x, int y) {
		this.positionIntrus.x = x;
		this.positionIntrus.y = y;
	}
	
	public void setEtat(EtatRobot etat) {
		this.etat = etat;
	}
	
	public EtatRobot getEtat() {
		return this.etat;
	}

	/**active les actions du robot selon son etat*/
	public void evoluer() {
		switch(etat) {
			case PATROUILLER: //recherche de l'intrus
				this.dessin.setFill(Color.GREEN);
				
				//S'oriente vers une case libre au hasard puis se déplace sur celle-ci
				direction = getNextRandomDirection(); 
				this.bougerVersDirection();	
				
				/*
				 * Regarde si l'intrus est présent dans le champ de vision du robot,
				 * Si oui il donne sa position aux autres robots qui commencent la poursuite
				 */
				this.chercherIntrusEtAlerter();
	
				break;
			case POURSUIVRE:
				this.dessin.setFill(Color.ORANGE);
				
				//S'oriente vers la case où l'intrus a été repéré puis s'y déplace
				direction = getBestDirection(); 
				this.bougerVersDirection();
				
				/*
				 * Si la case sur lequel est le robot contient également l'intrus, il l'attrape (passe dans l'état ATTRAPER)
				 * Sinon il regarde si l'intrus est présent dans son champ de vision :
				 * 		 si oui il continue la poursite en donnant sa nouvelle position aux autre robots
				 * 		sinon il recommence à patrouiller
				 */
				if (this.verifierIntrus()) {
					this.setEtat(EtatRobot.ATTRAPER);
				} else {
					this.chercherIntrusEtAlerter();
				}
				break;
			case ATTRAPER:
				this.dessin.setFill(Color.RED);
				
				//Notifie tous les autres robots qu'il a attrapé l'intrus (i.e. les passe dans l'etat d'arret)
				this.stopPatrouille();
				break;
			case STOP:
				//Etat symbolisant la fin de la partie 
				this.dessin.setFill(Color.WHITE);
				break;
		}
	}
	
	/**
	 * Donne la direction vers une cellule libre aléatoire
	 * @return Direction : une direction vers une cellule libre aléatoire
	 */
	private Direction getNextRandomDirection() {
		Direction d;
		Direction []dirAlentours = Direction.get3Dir(this.direction);
		ArrayList<Direction> listeDir = possibleNextDirections(dirAlentours);
		if(!listeDir.isEmpty()) {
			int i = this.hasard.nextInt(listeDir.size());
			d = listeDir.get(i); 
		} else {
			d = Direction.getInverse(this.direction);
		}
		return d;
	}
	
	/**retourne une liste de directions possibles vers des cases videsdans les directions donnees
	 * @param directions tableaux des drections dans lesquelles il faut tester si les cellules sont vides de fourmis
	 * @return une liste de directions possibles vers des cases vides de fourmis*/
	private ArrayList<Direction> possibleNextDirections(Direction []directions)
	{
		ArrayList<Direction> liste = new ArrayList<Direction>();
		for(Direction dir:directions)
		{
			Cellule cell = getNextCellule(dir);
			if(cell != null && !cell.isRobot() && !cell.isCaisse() && !cell.isSortie())
				liste.add(dir);
		}
		return liste;		
	}
	
	
	/**
	 * Calcule la direction vers laquelle se diriger pour rejoindre la position de l'intrus
	 * @return Direction : la direction pour aller vers l'intrus
	 */
	private Direction getBestDirection() {
		int x = p.x;
		int y = p.y;
		int xIntrus = this.positionIntrus.x;
		int yIntrus = this.positionIntrus.y;
		if (xIntrus < x) {
			if (yIntrus < y) {
				return Direction.NORD_OUEST;
			} else if (yIntrus == y) {
				return Direction.OUEST;
			} else {
				return Direction.SUD_OUEST;
			}
		} else if (xIntrus == x){
			if (yIntrus > y) {
				return Direction.SUD;
			} else {
				return Direction.NORD;
			}
		} else {
			if (yIntrus < y) {
				return Direction.NORD_EST;
			} else if (yIntrus == y) {
				return Direction.EST;
			} else {
				return Direction.SUD_EST;
			}
		}
	}

	/**fait avancer la fourmi dans sa direction si la case devant existe et est non occupee*/
	private void bougerVersDirection()
	{
		Cellule cell = getNextCellule(direction);
		if(cell!=null && !cell.isRobot()) {
			Cellule[][] grille = terrain.getGrille();
			grille[p.x][p.y].setRobot(false);
			p.x = cell.getX();
			p.y = cell.getY();
			dessin.setCenterX((p.x+1) * pas + (pas / 2));
			dessin.setCenterY((p.y+2) * pas - (pas / 2));
			cell.setRobot(true);
//			if (this.isInRange()) {
//				this.getDessin().setVisible(true);
//			} else {
//				this.getDessin().setVisible(false);
//			}
		}
	}
	
	/**donne la prochaine case dans la direction donnée
	 * @param dir la direction
	 * @return la cellule voisine dans la direction donnée, null si aucune cellule*/
	private Cellule getNextCellule(Direction dir)
	{
		Cellule cell = null;
		Point newPoint = Direction.getNextPoint(p, dir);
		if ((newPoint.x>=0 && newPoint.x < this.terrain.getN()) && (newPoint.y>=0 && newPoint.y<this.terrain.getM()))
		{
			Cellule[][] grille = terrain.getGrille();
			cell = grille[newPoint.x][newPoint.y];
		}
		return cell;
	}	

	/**
	 * @return the dessin
	 */
	public Circle getDessin() {
		return dessin;
	}

	/**
	 * @param dessin the dessin to set
	 */
	public void setDessin(Circle dessin) {
		this.dessin = dessin;
	}
	
	/**
	 * Permet de savoir si l'intrus est sur la cellule actuelle du robot
	 * @return vrai si la cellule sur laquelle se trouve le robot est également la cellule sur laquelle se trouve l'intrus, faux sinon
	 */
	public boolean verifierIntrus() {
		Cellule[][] grille = terrain.getGrille();
		Cellule cell = grille[p.x][p.y];
		if (cell.isIntrus()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Permet de trouver la position de l'intrus s'il se trouve dans le champ de vision du robot
	 * @return le point de coordonnées la position de l'intrus, le point de coordonnée (-1,-1) si intrus pas détecté
	 */
	public Point chercherPositionIntrus() {
		Cellule[][] grille = terrain.getGrille();
		int x = p.x;
		int y = p.y;
		int xIntrus = -1;
		int yIntrus = -1;
		for (int i = 1; i <= 3; i++) {
			for (int j = 1; j <= 3; j++) {
				if (x + i < grille.length && y + j < grille[i].length && x - i >= 0 && y - j >= 0) {
					if (grille[x + i][y].isIntrus()) {
						xIntrus = x + i;
						yIntrus = y;
					} else if (grille[x + i][y + j].isIntrus()) {
						xIntrus = x + i;
						yIntrus = y + j;
					} else if (grille[x + i][y - j].isIntrus()) {
						xIntrus = x + i;
						yIntrus = y - j;
					} else if (grille[x - i][y].isIntrus()) {
						xIntrus = x - i;
						yIntrus = y;
					} else if (grille[x - i][y + j].isIntrus()) {
						xIntrus = x - i;
						yIntrus = y + j;
					} else if (grille[x - i][y - j].isIntrus()) {
						xIntrus = x - i;
						yIntrus = y - j;
					} else if (grille[x][y + j].isIntrus()) {
						xIntrus = x;
						yIntrus = y + j;
					} else if (grille[x][y - j].isIntrus()) {
						xIntrus = x;
						yIntrus = y - j;
					} 
				} 
			}
		}
		return new Point(xIntrus, yIntrus);
	}
	
	
	/**
	 * Passe tous les robots dans l'état d'arrêt
	 */
	public void stopPatrouille() {
		ArrayList<Robot> tousLesRobots = terrain.getLesRobots();
		for (Robot r : tousLesRobots) {
			r.setEtat(EtatRobot.STOP);
		}
	}
	
	/**
	 * Transmet à tous les robots la position de l'intrus et les passe dans l'état de poursuite
	 * @param Point donnant la position de l'intrus
	 */
	public void alerterRobots(Point p) {
		ArrayList<Robot> tousLesRobots = terrain.getLesRobots();
		for (Robot r : tousLesRobots) {
			r.setCoordonneesIntrus(p.x, p.y);
			r.setEtat(EtatRobot.POURSUIVRE);
		}
	}
	
	/**
	 * L'intrus n'est pas visible si les coordonnées du point passé en argument sont (-1,-1)
	 * @param Point pour lequel il faut tester la visibilité de l'intrus
	 * @return vrai si l'intrus est dans le champ de vision du robot, faux sinon
	 */
	public boolean intrusVisible(Point p) {
		if (p.x != -1 && p.y != -1) {
			return true;
		} else {
			return false;
		}
	} 
	
	/**
	 * Cherche si l'intrus se situe dans le champ de vision du robot
	 * Si l'intrus est détecté, il alerte tous les autres robots de sa position
	 * Sinon il recommence à patrouiller
	 */
	public void chercherIntrusEtAlerter( ) {
		Point p = this.chercherPositionIntrus();
		if (this.intrusVisible(p)) {
			this.alerterRobots(p);
		} else {
			this.setEtat(EtatRobot.PATROUILLER);
		}
	}
	
	/** Verifie si le robot est dans le champ de vision de l'intrus, si oui il devient visible
	 * @return boolean egal a vrai si le robot est dans le champ de vision d'un intrus et faux sinon */
	public boolean isInRange() {
		int x = this.p.x;
		int y = this.p.y;
		int xIntrus = this.positionIntrus.x;
		int yIntrus = this.positionIntrus.y;
		
		if ( (x >= xIntrus + 3 && x <= xIntrus + 4 ) && (y <= yIntrus + 4 && y >= yIntrus + 3) )  {
			return true;
		}
		return false;
	}
}
