package main.popUp;

import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PopUp {
    static Stage primaryStage;
    public PopUp(String msg){
        final Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(msg));
        Scene dialogScene = new Scene(dialogVbox, 500, 50);
        dialogScene.getStylesheets().add("/main/popUp/popUpStyle.css");
        dialog.setScene(dialogScene);
        dialog.show();

    }

    public PopUp(String msg,int width,int height){
        final Stage dialog = new Stage();
        dialog.initOwner(primaryStage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(msg));
        Scene dialogScene = new Scene(dialogVbox, width, height);
        dialog.setScene(dialogScene);
        dialog.show();

    }

    static public void setStage(Stage newStage){
        primaryStage=newStage;
    }


}
