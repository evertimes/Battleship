package org.evertimes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class PrimaryController implements Initializable {

    public TableView table;
    public Canvas canvasOne;
    public ComboBox directionBox;
    public ComboBox typeBox;
    public HBox hbox1;
    private BattleField bf = new BattleField();
    Media sound;
    MediaPlayer mediaPlayer;

    ObservableList listDirection = FXCollections.observableArrayList(
            "Вверх",
            "Вниз",
            "Влево",
            "Вправо"
    );
    ObservableList listTypes = FXCollections.observableArrayList(
            "Линкор",
            "Крейсер",
            "Эсминец",
            "Лодка"
    );

    public void addShip(int x, int y) {
        String direction = (String) directionBox.getValue();
        String type = (String) typeBox.getValue();
        int size = 0;
        if (type.equals("Линкор")) {
            size = 4;
        } else if (type.equals("Крейсер")) {
            size = 3;
        } else if (type.equals("Эсминец")) {
            size = 2;
        } else if (type.equals("Лодка")) {
            size = 1;
        }
        if (direction.equals("Вверх")) {
            bf.addShip(x,y,size,0);
        } else if (direction.equals("Вниз")) {
            bf.addShip(x,y,size,2);
        } else if (direction.equals("Влево")) {
            bf.addShip(x,y,size,3);
        } else if (direction.equals("Вправо")) {
            bf.addShip(x,y,size,1);
        }
        drawShips();
        drawGrid();
    }


    public void generateRandom(ActionEvent actionEvent) throws IOException {
        /*Timeline timeline = new Timeline (
                new KeyFrame(
                        Duration.millis(20), //1000 мс * 60 сек = 1 мин
                        ae -> {
                            hbox1.setRotate(hbox1.getRotate()+1);
                        }
                )
        );

        timeline.setCycleCount(360); //Ограничим число повторений
        timeline.play(); //Запускаем
        sound = new Media(new File("src/main/resources/org/evertimes/ff.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();*/
        bf.generateBattleField();
        drawShips();
        drawGrid();
        switchToSecondary();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        directionBox.setItems(listDirection);
        typeBox.setItems(listTypes);
        drawShips();
        drawGrid();
    }


    public void pressCell(MouseEvent mouseEvent) {
        int x = (int) Math.round(mouseEvent.getX());
        int y = (int) Math.round(mouseEvent.getY());
        x = x / 50;
        y = y / 50;
        addShip(x,y);
        drawShips();
        drawGrid();
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setBattleField(bf);
        App.setRoot("Secondary");
    }

    void drawShips() {
        CellState[][] cs = bf.getField();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                canvasOne.getGraphicsContext2D().setFill(cs[i][j].getColor());
                canvasOne.getGraphicsContext2D().fillRect(50 * i, 50 * j, 50, 50);
            }
        }
    }

    void drawGrid() {
        canvasOne.getGraphicsContext2D().setStroke(Color.BLACK);
        for (int i = 0; i < 11; i++) {
            canvasOne.getGraphicsContext2D().strokeLine(50 * i, 0, 50 * i, 500);
            canvasOne.getGraphicsContext2D().strokeLine(0 * i, 50 * i, 500, 50 * i);
        }
    }

    void fillCell(int x, int y) {
        bf.getField()[x][y] = CellState.SHIP;
    }

    private Point2D calculateCellNumbers() {
        return null;
    }
}
