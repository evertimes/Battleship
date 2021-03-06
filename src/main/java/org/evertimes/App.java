package org.evertimes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 1100, 680);
        stage.setScene(scene);
        stage.setTitle("Battleship");
        stage.show();
    }

    static BattleField transferBattleField;

    public static void setBattleField(BattleField battleField) {
        transferBattleField = battleField;
    }

    public static BattleField getBattleField() {
        return transferBattleField;
    }

    static void setRoot(String fxml) throws IOException {

        scene.setRoot(loadFXML(fxml));
    }

    static Parent getRoot() {
        return scene.getRoot();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader;
        if (fxml.compareTo("primary") == 0) {
            fxmlLoader = new FXMLLoader(PrimaryController.class.getResource(fxml + ".fxml"));
        } else {
            fxmlLoader = new FXMLLoader(SecondaryController.class.getResource(fxml + ".fxml"));
        }
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
