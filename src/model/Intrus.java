package model;

import java.awt.Point;
import java.util.Random;

import javax.swing.JOptionPane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Intrus {
	Point position;
	
	/**lien vers le terrain dans lequel se trouve la  fourmi*/
	private Terrain terrain;
	
	private int pas;
	
	private Direction direction;
	
	/**objet graphique associe a la fourmi*/
	private Circle dessin;
	
	private int nbCaisses;
	
	public Intrus(){}

	/**construit une fourmi
	 * @param x : coordonee x initiale du robot
	 * @param y : coordonee y initiale du robot
	 * @param terrain : terrain ou se trouve le robot
	 */
	public Intrus(int x, int y, Terrain terrain) {
		this.position = new Point(x, y);
		this.terrain= terrain;
		this.nbCaisses = 0;
	}
	
	/**
	 * @return the dessin
	 */
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
	
	/**
	 * @param dessin the dessin to set
	 */
	public void setDessin(Circle dessin) {
		this.dessin = dessin;
	}
	
	public int getNbCaisses() {
		return this.nbCaisses;
	}
	
	/**donne la prochaine case dans la direction donnée
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
	
	/**fait avancer la fourmi dans sa direction si la case devant existe et est non occupee*/
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
			verifierCaisse();
		}
	}
	
	public void verifierCaisse() {
		Cellule[][] grille = terrain.getGrille();
		Cellule cell = grille[position.x][position.y];
		if (cell.isCaisse()) {
			cell.prendreCaisse();
			this.nbCaisses++;
		}
	}
	
	public boolean verifierSortie() {
		Cellule[][] grille = terrain.getGrille();
		Cellule cell = grille[position.x][position.y];
		if (cell.isSortie()) {
			return true;
		}
		return false;
	}
	
//	public boolean celluleAPortee(Cellule c) {
//		int x = c.getX();
//		int y = c.getY();
//		if ((this.p.x + 4 <= x && this.p.y + 4 <= y) || (this.p.x - 4 >= x && this.p.y + 4 <= y) || (this.p.x + 4 <= x && this.p.y - 4 >= y) || (this.p.x - 4 >= x && this.p.y - 4 >= y)) {
//			return true;
//		} else return false;
//	}
	
	public boolean estAPortee(int xCellule,int yCellule) {
		int xIntrus = position.x;
		int yIntrus = position.y;
		System.out.println("xCellule : "+xCellule+",yCellule : "+yCellule);
		
		if ( (xCellule >= xIntrus -4 && xCellule <= xIntrus + 4 ) && (yCellule <= yIntrus + 4 && yCellule >= yIntrus -4) )  {
			System.out.print(" => estAPortee");
			return true;
		}
		System.out.print(" => pas � port�e");
		return false;
	}

	public Point getPosition() {
		return position;
	}
	
	
	
}
