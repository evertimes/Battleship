package org.evertimes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.evertimes.ships.Ship;

public class PrimaryController implements Initializable {
    private static final int CELL_SIZE = 50;
    private static final String DIRECTION_UP = "Up";
    private static final String DIRECTION_RIGHT = "Right";
    private static final String DIRECTION_DOWN = "Down";
    private static final String DIRECTION_LEFT = "Left";
    private static final int DIRECTION_UP_CODE = 0;
    private static final int DIRECTION_RIGHT_CODE = 1;
    private static final int DIRECTION_DOWN_CODE = 2;
    private static final int DIRECTION_LEFT_CODE = 3;
    private static final String SHIP_NAME_4 = "Lincore";
    private static final String SHIP_NAME_3 = "Cruiser";
    private static final String SHIP_NAME_2 = "Destroyer";
    private static final String SHIP_NAME_1 = "Boat";

    public Canvas canvasOne;
    public ComboBox<String> directionBox;
    public ComboBox<String> typeBox;
    public HBox hbox1;
    private final BattleField battleField = new BattleField();

    private final ObservableList<String> listDirection = FXCollections.observableArrayList(
            DIRECTION_UP,
            DIRECTION_DOWN,
            DIRECTION_LEFT,
            DIRECTION_RIGHT
    );
    private final ObservableList<String> listTypes = FXCollections.observableArrayList(
            SHIP_NAME_4,
            SHIP_NAME_3,
            SHIP_NAME_2,
            SHIP_NAME_1
    );

    public void addShip(int x, int y) {
        String direction = (String) directionBox.getValue();
        String type = (String) typeBox.getValue();
        int size = 0;
        if (type.equals(SHIP_NAME_4)) {
            size = Ship.LINCORE_SIZE;
        } else if (type.equals(SHIP_NAME_3)) {
            size = Ship.CRUISER_SIZE;
        } else if (type.equals(SHIP_NAME_2)) {
            size = Ship.DESTROYERS_SIZE;
        } else if (type.equals(SHIP_NAME_1)) {
            size = Ship.BOAT_SIZE;
        }
        if (direction.equals(DIRECTION_UP)) {
            if (battleField.addShip(x, y, size, DIRECTION_UP_CODE)) {

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Cant perform action");
                alert.setHeaderText("Cant place another ship of this type\n" +
                        "or cant place ship here");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }
        } else if (direction.equals(DIRECTION_DOWN)) {
            if (battleField.addShip(x, y, size, DIRECTION_DOWN_CODE)) {

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Cant perform action");
                alert.setHeaderText("Cant place another ship of this type\n" +
                        "or cant place ship here");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }
        } else if (direction.equals(DIRECTION_LEFT)) {
            if (battleField.addShip(x, y, size, DIRECTION_LEFT_CODE)) {

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Cant perform action");
                alert.setHeaderText("Cant place another ship of this type\n" +
                        "or cant place ship here");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                    }
                });
            }
        } else if (direction.equals(DIRECTION_RIGHT)) {
            if (battleField.addShip(x, y, size, DIRECTION_RIGHT_CODE)) {

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Cant perform action");
                alert.setHeaderText("Cant place another ship of this type\n" +
                        "or cant place ship here");
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
        battleField.generateBattleField();
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
        x = x / CELL_SIZE;
        y = y / CELL_SIZE;
        addShip(x, y);
        drawShips();
        drawGrid();
    }

    @FXML
    private void switchToSecondary() throws IOException {
        if (battleField.getShipsCount() == 10) {
            App.setBattleField(battleField);
            App.setRoot("secondary");
        } else {
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
        CellState[][] cs = battleField.getField();
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
}
