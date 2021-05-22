package org.evertimes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.scene.paint.Color;

public class PrimaryController implements Initializable {

    public TableView table;
    public Canvas canvasOne;
    public ComboBox directionBox;
    public ComboBox typeBox;
    public TextField cordBox;
    private BattleField bf = new BattleField();
    private int lincoreCounter = 0;
    private int cruiserCounter = 0;
    private int destroyersCounter = 0;
    private int boatCounter = 0;

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

    public void addShip(ActionEvent actionEvent) {
        int x = Integer.parseInt(cordBox.getText().charAt(0) + "");
        int y = Integer.parseInt(cordBox.getText().charAt(1) + "");
        String direction = (String) directionBox.getValue();
        String type = (String) typeBox.getValue();
        int size = 0;
        if (type.equals("Линкор")) {
            size = 4;
            //lincoreCounter++;
        } else if (type.equals("Крейсер")) {
            size = 3;
            //cruiserCounter++;
        } else if (type.equals("Эсминец")) {
            size = 2;
            //destroyersCounter++;
        } else if (type.equals("Лодка")) {
            size = 1;
            //boatCounter++;
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

    public void checkButton(ActionEvent actionEvent) {
        System.out.println(bf.checkCanAdd(7,7,4,1));
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
        fillCell(x, y);
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
