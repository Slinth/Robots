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

	/**construit une fourmi
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
	public void evoluer()
	{
		Cellule[][] grille = terrain.getGrille();
		int x = p.x;
		int y = p.y;
		switch(etat)
		{
		case PATROUILLER: //recherche de l'intrus
			this.dessin.setFill(Color.GREEN);
			Point p = chercherPositionIntrus();
			
			direction = getNextRandomDirection(); //s'orienter vers une case libre au hasard
			this.bougerVersDirection();
			
			
			if (intrusVisible(p)) {
//				System.out.println("DANS LE IF");
				alerterRobots(p);
			} 
			break;
		case POURSUIVRE:
			this.dessin.setFill(Color.ORANGE);
			direction = getBestDirection(); //s'orienter vers la case où l'intrus a été repéré
			this.bougerVersDirection();
			
			if (this.verifierIntrus()) {
				this.setEtat(EtatRobot.ATTRAPER);
			} else {
				this.setEtat(EtatRobot.PATROUILLER);
			}
			
			break;
		case ATTRAPER:
			this.dessin.setFill(Color.RED);
			this.stopPatrouille();
			break;
		case STOP:
			this.dessin.setFill(Color.WHITE);
			break;
		}
	}
	
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
	
	public boolean verifierIntrus() {
		Cellule[][] grille = terrain.getGrille();
		Cellule cell = grille[p.x][p.y];
		if (cell.isIntrus()) {
			return true;
		}
		return false;
	}
	
	public Point chercherPositionIntrus() {
		Cellule[][] grille = terrain.getGrille();
		int x = p.x;
		int y = p.y;
		int xIntrus = 0;
		int yIntrus = 0;
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
					else {
						xIntrus = -1;
						yIntrus = -1;
					}
				} else {
					xIntrus = -1;
					yIntrus = -1;
				}
			}
		}
		System.out.println("X : " + xIntrus + "Y : " + yIntrus);
		return new Point(xIntrus, yIntrus);
	}
	
	public void reinitialiserPositionIntrus() {
		this.setCoordonneesIntrus(-1, -1);
	}
	
	public void stopPatrouille() {
		ArrayList<Robot> tousLesRobots = terrain.getLesRobots();
		for (Robot r : tousLesRobots) {
			r.setEtat(EtatRobot.STOP);
		}
	}
	
	public void alerterRobots(Point p) {
		ArrayList<Robot> tousLesRobots = terrain.getLesRobots();
		for (Robot r : tousLesRobots) {
			r.setCoordonneesIntrus(p.x, p.y);
			r.setEtat(EtatRobot.POURSUIVRE);
		}
	}
	
	public boolean intrusVisible(Point p) {
		if (p.x != -1 && p.y != -1) {
			return true;
		} else {
			return false;
		}
//		return (positionIntrus.x != -1 && positionIntrus.y != -1);
	} 
}
