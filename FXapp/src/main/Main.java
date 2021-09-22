package main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.popUp.PopUp;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("main.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());
        MainController controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);
        PopUp.setStage(primaryStage);
        Scene scene = new Scene(root, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
