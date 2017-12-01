package model;
/**
 * Cette classe représente une cellule pour la poursuite de robots
 * @author Decroix Dorian
 *
 */
public class Cellule {
	/** Coodonnée en x de la cellule */
	private int x;
	
	/** Coodonnée en y de la cellule */
	private int y;
	
	/** Booleen correspondant à la présence d'une caisse sur la cellule */
	private boolean caisse;
	
	/** Booleen correspondant à la présence d'un robot sur la cellule */
	private boolean robot;
	
	/** Booleen indiquant si l'intrus est déjà passé sur la cellule */
	private boolean visitee;
	
	/** Booleen correspondant à la présence d'une sortie sur la cellule */
	private boolean sortie;
	
	/** Booleen correspondant à la présence d'un intrus sur la cellule */
	private boolean intrus;
	
	public Cellule() {
		this.x = 0;
		this.y = 0;
		this.caisse = false;
		this.robot = false;
		this.sortie = false;
		this.intrus = false;
	}
	
	public Cellule(int x, int y) {
		this.x = x;
		this.y = y;
		this.caisse = false;
		this.robot = false;
		this.sortie = false;
		this.intrus = false;
	}
	
	public Cellule(int x, int y, boolean caisse) {
		this.x = x;
		this.y = y;
		this.caisse = caisse;
		this.robot = false;
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
	}
	
	public boolean isIntrus() {
		return intrus;
	}
	
	public void setIntrus(boolean intrus) {
		this.intrus = intrus;
	}
	
	public boolean isSortie() {
		return this.sortie;
	}
	
	public void setSortie(boolean sortie) {
		this.sortie = sortie;
	}

	public void prendreCaisse() {
		this.caisse = false;
	}
	
	public void setVisitee(boolean visite) {
		this.visitee = visite;
	}
	
	public boolean isVisitee() {
		return this.visitee;
	}
}
