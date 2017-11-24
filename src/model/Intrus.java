package model;

import java.awt.Point;
import javafx.scene.shape.Circle;

/** Cette classe représente l'intrus dont le but est de voler les caisses dans l'entrepot */
public class Intrus {
	
	/** Point contenant les coordonnées de l'intrus sur le plateau */
	private Point position;
	
	/** Terrain sur lequel se trouve l'intrus*/
	private Terrain terrain;
	
	/** Direction de l'intrus pour savoir si le déplacement vers la prochaine cellule est valide */
	private Direction direction;
	
	/** Objet graphique représentant l'intrus */
	private Circle dessin;
	
	/** Taille du pas de l'intrus pour se déplacer graphiquement */
	private int pas;
	
	/** Nombre de caisses que l'intrus a récupéré */
	private int nbCaisses;
	
	/** Champ de vision de l'intrus */
	private int fov;
		
	/** Constructeur par défaut */
	public Intrus(){}
	
	
	/** Constructeur d'un intrus 
	 * @param x : coordonee x initiale du robot
	 * @param y : coordonee y initiale du robot
	 * @param terrain : terrain ou se trouve le robot
	 */
	public Intrus(int _x, int _y, Terrain _terrain, int _fov) {
		position = new Point(_x, _y);
		terrain= _terrain;
		nbCaisses = 0;
		fov = _fov;
	}
	

	/** Donne la prochaine case dans la direction donnée
	 * @param dir la direction
	 * @return la cellule voisine dans la direction donnée, null si aucune cellule*/
	private Cellule getNextCellule(Direction dir)
	{
		Cellule cell = null;
		Point newPoint = Direction.getNextPoint(position, dir);
		if ((newPoint.x>=0 && newPoint.x < this.terrain.getN()) && (newPoint.y>=0 && newPoint.y<this.terrain.getM()))
		{
			Cellule[][] grille = terrain.getGrille();
			cell = grille[newPoint.x][newPoint.y];
		}
		return cell;
	}	
	
	
	/** Fait avancer l'intrus dans sa direction si la case devant existe et n'est ni un intrus ni un robot */
	public void bougerVersDirection()
	{
		Cellule cell = getNextCellule(direction);
		if(cell!=null && !cell.isIntrus() && !cell.isRobot()) {
			Cellule[][] grille = terrain.getGrille();
			grille[position.x][position.y].setIntrus(false);
			position.x = cell.getX();
			position.y = cell.getY();
			dessin.setCenterX((position.x+1) * pas + (pas / 2));
			dessin.setCenterY((position.y+2) * pas - (pas / 2));
			cell.setIntrus(true);
			
			if (cell.isCaisse()) {
				cell.prendreCaisse();
				this.nbCaisses++;
			}
		}
	}
	
	
	/** Verifie si l'intrus est une sortie 
	 * @return renvoie un boolean si l'intrus est sur une sortie*/
	public boolean verifierSortie() {
		return terrain.getGrille()[position.x][position.y].isSortie();
	}
	
	
	/** Verifie si les coordonnées d'une cellule passée en argument sont dans le champ de vision
	 * @param _xCellule abscisse des coordonnées d'une cellule
	 * @param _yCellule ordonnée des coordonnées d'une cellule
	 * @return boolean correspond à la présence d'une cellule dans le champ de vision de l'intrus */
	public boolean isInRange(int _xCellule,int _yCellule) {
		int xIntrus = position.x;
		int yIntrus = position.y;
//		System.out.println("xCellule : "+xCellule+",yCellule : "+yCellule);
		
		if ( (_xCellule >= xIntrus + fov*-1 && _xCellule <= xIntrus + fov ) && (_yCellule <= yIntrus + fov && _yCellule >= yIntrus +fov*-1) )  {
//			System.out.print(" => estAPortee");
			return true;
		}
//		System.out.print(" => pas � port�e");
		return false;
	}

	
	public Point getPosition() {
		return position;
	}
	
	public void setDessin(Circle dessin) {
		this.dessin = dessin;
	}
	
	public Circle getDessin() {
		return dessin;
	}
	
	public void setPas(int pas) {
		this.pas = pas;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public int getX() {
		return this.position.x;
	}
	
	public int getY() {
		return this.position.y;
	}
	
	public int getNbCaisses() {
		return this.nbCaisses;
	}
}
