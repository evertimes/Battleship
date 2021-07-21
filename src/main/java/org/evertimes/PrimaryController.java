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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
            if(bf.addShip(x,y,size,0)){

            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Действие невозможно");
                alert.setHeaderText("Нельзя установить еше один корабль этого типа\n" +
                        "или невозможно установить корабль сюда ");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }
        } else if (direction.equals("Вниз")) {
            if(bf.addShip(x,y,size,2)){

            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Действие невозможно");
                alert.setHeaderText("Нельзя установить еше один корабль этого типа\n" +
                        "или невозможно установить корабль сюда ");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }
        } else if (direction.equals("Влево")) {
            if(bf.addShip(x,y,size,3)){

            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Действие невозможно");
                alert.setHeaderText("Нельзя установить еше один корабль этого типа\n" +
                        "или невозможно установить корабль сюда ");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }
        } else if (direction.equals("Вправо")) {
            if(bf.addShip(x,y,size,1)){

            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Действие невозможно");
                alert.setHeaderText("Нельзя установить еше один корабль этого типа\n" +
                        "или невозможно установить корабль сюда ");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }
        }
        drawShips();
        drawGrid();
    }


    public void generateRandom(ActionEvent actionEvent) throws IOException {
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
        if(bf.getShipsCount()==10) {
            App.setBattleField(bf);
            App.setRoot("secondary");
        } else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Слишком рано");
            alert.setHeaderText("Необходимо установить все 9 кораблей или же сгенерировать их случайно");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
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
