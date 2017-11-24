package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUp extends Application {
	Stage mainStage;
	String message;
	
	public PopUp(Stage mainStage, String message, int nbCaisses) {
		this.mainStage = mainStage;
		this.message = message;
		this.start(mainStage, nbCaisses);
	}
	
	public PopUp(Stage mainStage, String message) {
		this.mainStage = mainStage;
		this.message = message;
		try {
			this.start(mainStage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start(final Stage primaryStage, int nbCaisses) {        
	    final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle(message);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("Bravo ! Vous avez récupéré " + nbCaisses + " caisses"));
        Scene dialogScene = new Scene(dialogVbox, 300, 100);
        dialog.setScene(dialogScene);
        dialog.centerOnScreen();
        dialog.show();
        
        Button btn = new Button();
        btn.setText("OK");
        btn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    dialog.close();
                    primaryStage.close();
                }
             });
        dialogVbox.getChildren().add(btn);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle(message);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text("Dommage ! Vous vous êtes fait attrapé"));
        Scene dialogScene = new Scene(dialogVbox, 400, 100);
        dialog.setScene(dialogScene);
        dialog.centerOnScreen();
        dialog.show();
        
        Button btn = new Button();
        btn.setText("OK");
        btn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    dialog.close();
                    primaryStage.close();
                }
             });
        dialogVbox.getChildren().add(btn);
	}
}
