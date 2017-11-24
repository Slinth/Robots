package model;
/**
 * Cette classe représente une cellule pour la poursuite de robots
 * @author Decroix Dorian
 *
 */
public class Cellule {
	private int x, y;
	private boolean caisse;
	private boolean robot;
	private boolean hasJustChanged;
	private boolean sortie;
	private boolean intrus;
	
	public Cellule() {
		this.x = 0;
		this.y = 0;
		this.caisse = false;
		this.robot = false;
		this.hasJustChanged = false;
		this.sortie = false;
		this.intrus = false;
	}
	
	public Cellule(int x, int y) {
		this.x = x;
		this.y = y;
		this.caisse = false;
		this.robot = false;
		this.hasJustChanged = false;
		this.sortie = false;
		this.intrus = false;
	}
	
	public Cellule(int x, int y, boolean caisse) {
		this.x = x;
		this.y = y;
		this.caisse = caisse;
		this.robot = false;
		this.hasJustChanged = false;
		this.sortie = false;
		this.intrus = false;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isCaisse() {
		return caisse;
	}

	public void setCaisse(boolean caisse) {
		this.caisse = caisse;
	}

	public boolean isRobot() {
		return robot;
	}
	
	public void setRobot(boolean robot) {
		this.robot = robot;
		this.hasJustChanged = true;
	}
	
	public boolean isIntrus() {
		return intrus;
	}
	
	public void setIntrus(boolean intrus) {
		this.intrus = intrus;
		this.hasJustChanged = true;
	}
	
	public boolean isHasJustChanged() {
		return hasJustChanged;
	}
	
	public boolean isSortie() {
		return this.sortie;
	}
	
	public void setSortie(boolean sortie) {
		this.sortie = sortie;
	}

	public void prendreCaisse() {
		this.caisse = false;
		this.hasJustChanged = true;
	}
	 
	
	
}
