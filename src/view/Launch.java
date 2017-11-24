package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Cellule;
import model.Direction;
import model.EtatRobot;
import model.Intrus;
import model.Robot;
import model.Terrain;

public class Launch extends Application {
	/**terrain liee a cet objet graphique*/
	private Terrain terrain;
	/**nb de fourmis*/
	int nbRobots = 10;
	/** Pourcentage de caisse sur le terrain */
	int pourcentageCaisses = 2;
	/**vitesse de simulation*/
	double tempo = 500;
	/**taille de la terrain*/
	private int n;
	/**taille de la terrain*/
	private int m;
	/**taille d'une cellule en pixel*/
	private int espace = 30;
	private  static Rectangle [][] environnement; 



	@Override
	/**initialisation de l'application graphique*/
	public void start(Stage primaryStage) {
		int n = 25; int m = 20;
		this.terrain = new Terrain(n, m, this.nbRobots, this.pourcentageCaisses);
		this.n = terrain.getN();
		this.m = terrain.getM();
		construireScenePourFourmis(primaryStage);

	}



	/**construction du théatre et de la scène */
	void construireScenePourFourmis(Stage primaryStage) 
	{
		//definir la scene principale
		Group root = new Group();
		Scene scene = new Scene(root, 2*espace + this.n*espace, 2*espace + this.m*espace, Color.BLACK);
		primaryStage.setTitle("Life...");
		primaryStage.setScene(scene);
		//definir les acteurs et les habiller
		Launch.environnement = new Rectangle[this.n][this.m];
		dessinEnvironnement(root);

		//afficher le theatre
		primaryStage.show();

		//-----lancer le timer pour faire vivre les fourmis et l'environnement
		Timeline littleCycle = new Timeline(new KeyFrame(Duration.millis(tempo), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				terrain.animGrille();
//				updateTerrain(primaryStage);
			}
		}));
		
		
		littleCycle.setCycleCount(Timeline.INDEFINITE);
		littleCycle.play();
		
		Intrus intrus = terrain.getIntrus();
		Circle dessinIntrus = intrus.getDessin();
		dessinIntrus.requestFocus();
		dessinIntrus.setOnKeyPressed(e->{
		  //System.err.println(e.getCode());
		  switch(e.getCode()) {
		    case UP: intrus.setDirection(Direction.NORD); intrus.bougerVersDirection(); break;
		    case LEFT: intrus.setDirection(Direction.OUEST); intrus.bougerVersDirection(); break;
		    case DOWN: intrus.setDirection(Direction.SUD); intrus.bougerVersDirection(); break;
		    case RIGHT: intrus.setDirection(Direction.EST); intrus.bougerVersDirection(); break;
		  }
		  updateTerrain(primaryStage);
		});
	}



	/** 
	 *creation des cellules et de leurs habits
	 */
	void dessinEnvironnement(Group root)
	{
		Cellule[][] grille = terrain.getGrille();

		for(int i=0; i<this.n; i++) 
		{
			for(int j=0; j<this.m; j++)
			{
				Launch.environnement[i][j] = new Rectangle((i+1)*(espace), (j+1)*(espace), espace, espace);

				if (grille[i][j].isCaisse()) // affichage des caisses
				{
					Launch.environnement[i][j].setFill(Color.BROWN);
				}
				else if (grille[i][j].isSortie()) // affichage des sorties
				{
					Launch.environnement[i][j].setFill(Color.BLUE);
				}
				else {
					Launch.environnement[i][j].setFill(Color.DARKGREEN);

				}
				Launch.environnement[i][j].setVisible(false);
				root.getChildren().add(Launch.environnement[i][j]);
			}
		}
		
//		for(int i=0; i<this.n; i++) {
//			for(int j=0; j<this.m; j++)
//			{
//				Cellule cell = grille[i][j];
//				if (cell.isCaisse()) // affichage des caisses
//				{
////					Color colCaisse = Color.BROWN;
//					Launch.environnement[i][j] = new Rectangle((i+1)*(espace), (j+1)*(espace), espace, espace);
//					Launch.environnement[i][j].setFill(Color.BROWN);
//					root.getChildren().add(Launch.environnement[i][j]);
//				}
//				else
//					if (cell.isSortie()) // affichage des sorties
//					{
//						Launch.environnement[i][j] = new Rectangle((i+1)*(espace), (j+1)*(espace), espace, espace);
//						Launch.environnement[i][j].setFill(Color.BLUE);
//						root.getChildren().remove(Launch.environnement[i][j] );
//						root.getChildren().add(Launch.environnement[i][j]);
//					}
//				Launch.environnement[i][j].setVisible(false);
//			}
//			
//		}
		//création des robots, rouges tomate
		for(Robot  r : terrain.getLesRobots())
		{
			r.setDessin(new Circle(((this.n)*espace)/2 , ((this.m)*espace)/2, espace/2, Color.TOMATO));
			r.setPas(espace);
			root.getChildren().add(r.getDessin());
		}
		
		//Affichage de l'intrus, en jaune
		Intrus i = terrain.getIntrus();
		i.setPas(espace);
		i.setDessin(new Circle((i.getX() * espace), (i.getY() * espace), espace / 2, Color.YELLOW));
		root.getChildren().add(i.getDessin());
		//petit effet de flou général
		root.setEffect(new BoxBlur(2, 2, 5));
		
		System.out.println("Intrus : "+terrain.getIntrus().getPosition().x+","+terrain.getIntrus().getPosition().y);
		for(int i1=0; i1<this.n; i1++) {
			for(int j=0; j<this.m; j++)
			{
				if (terrain.getIntrus().isInRange(i1, j) ) {
					System.out.println("oui");
					Launch.environnement[i1][j].setVisible(true);
				}
//				else {
//					Launch.environnement[i1][j].setVisible(false);
//				}
			}
		}
	}



	/**modification de la couleur des cellules en fonction de la dose de nourriture et de phéromones*/
	private void updateTerrain(Stage primaryStage)
	{
		Intrus intrus = this.terrain.getIntrus();
		for (Robot r : this.terrain.getLesRobots()) {
			if (r.verifierIntrus()) {
//				new PopUp(primaryStage, "DEFAITE");
			}
		}
		if (intrus.verifierSortie()) new PopUp(primaryStage, "VICTOIRE", intrus.getNbCaisses());
		
		
		Cellule[][] grille = terrain.getGrille();
		for(int i=0; i<this.n; i++) {
			for(int j=0; j<this.m; j++)
			{
				Cellule cell = grille[i][j];
				if ( cell.isCaisse()) {
					Color colCaisse = Color.BROWN;
					Launch.environnement[i][j].setFill(colCaisse);
				} else if (cell.isSortie()) {
					Color colSortie = Color.BLUE;
					Launch.environnement[i][j].setFill(colSortie);
				} else {
					Launch.environnement[i][j].setFill(Color.DARKGREEN);
				}
//				else {
//					
//				}
				
				if (terrain.getIntrus().isInRange(i, j) ) {
					System.out.println("oui");
					Launch.environnement[i][j].setVisible(true);
				}
				else {
					Launch.environnement[i][j].setVisible(false);
				}
			}	
		}
		
	}
	
	private void changerCouleurRobots() {
		for (Robot r : this.terrain.getLesRobots()) {
			r.setDessin(new Circle(((this.n)*espace)/2 , ((this.m)*espace)/2, espace/2, Color.WHITE));
		}
	}


	/**lancement de l'application*/
	public static void main(String[] args) {
		launch(args);
	}
}