package org.evertimes;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.evertimes.ships.Ship;

public class SecondaryController implements Initializable {
    public Canvas userField;
    public Canvas computerField;
    BattleField userBattleField;
    BattleField computerBattleField;
    boolean checkNear = false;
    boolean foundDirection = false;
    Point lastPoint;
    Point firstPoint;
    int direction = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userBattleField = App.getBattleField();
        computerBattleField = new BattleField();
        computerBattleField.generateBattleField();
        drawShips(userBattleField, userField);
        drawGrid(userField);
        drawComputerShips(computerBattleField, computerField);
        drawGrid(computerField);
    }

    void drawShips(BattleField battleField, Canvas canvas) {
        CellState[][] cs = battleField.getField();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                canvas.getGraphicsContext2D().setFill(cs[i][j].getColor());
                canvas.getGraphicsContext2D().fillRect(50 * i, 50 * j, 50, 50);
            }
        }
    }

    void drawComputerShips(BattleField battleField, Canvas canvas) {
        CellState[][] cs = battleField.getField();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (cs[i][j] == CellState.CHECKED || cs[i][j] == CellState.DESTROYED) {
                    canvas.getGraphicsContext2D().setFill(cs[i][j].getColor());
                    canvas.getGraphicsContext2D().fillRect(50 * i, 50 * j, 50, 50);
                }
            }
        }
    }

    void drawGrid(Canvas canvas) {
        canvas.getGraphicsContext2D().setStroke(Color.BLACK);
        for (int i = 0; i < 11; i++) {
            canvas.getGraphicsContext2D().strokeLine(50 * i, 0, 50 * i, 500);
            canvas.getGraphicsContext2D().strokeLine(0 * i, 50 * i, 500, 50 * i);
        }
    }


    public void pressComputerCell(MouseEvent mouseEvent) {
        int x = (int) Math.round(mouseEvent.getX());
        int y = (int) Math.round(mouseEvent.getY());
        x = x / 50;
        y = y / 50;
        int state = fireCell(x, y);
        if(state == -1){
            return;
        }
        else if (state == 1) {
            Ship ship = computerBattleField.getShip(new Point(x, y));
            Set<Point> cords = ship.getRadiusCords();
            for (Point cord : cords) {
                computerBattleField.field[cord.getX()][cord.getY()] = CellState.CHECKED;
            }
        }
        drawComputerShips(computerBattleField, computerField);
        drawGrid(computerField);
        if (checkEndGame(computerBattleField)) {
            endGame(true);
            return;
        }
        if (!checkNear) {
            computerTurn();
        } else {
            computerTurn(lastPoint);
        }
        drawShips(userBattleField, userField);
        drawGrid(userField);
        if (checkEndGame(userBattleField)) {
            endGame(false);
        }
    }

    private void endGame(boolean isWin) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Игра завершена");
        if(isWin){
            alert.setHeaderText("Вы выиграли :)");
        }else {
            alert.setHeaderText("Вы проиграли :(");
        }
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                try {
                    switchToPrimary();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public int fireCell(int x, int y) {
        if (computerBattleField.field[x][y] == CellState.CHECKED ||
                computerBattleField.field[x][y] == CellState.DESTROYED){
            return -1;
        }
        if (computerBattleField.field[x][y] == CellState.SHIP) {
            computerBattleField.field[x][y] = CellState.DESTROYED;
            Ship ship = computerBattleField.getShip(new Point(x, y));
            ship.removePoint();
            if (ship.isDestroyed()) {
                return 1;
            } else {
                return 0;
            }
        } else if (computerBattleField.field[x][y] == CellState.REGULAR) {
            computerBattleField.field[x][y] = CellState.CHECKED;
        }
        return 2;
    }

    void computerTurn(Point pt) {
        int i = 0;
        int j = 0;
        switch (direction) {
            case 0:
                j = -1;
                break;
            case 1:
                i = 1;
                break;
            case 2:
                j = 1;
                break;
            case 3:
                i = -1;
                break;
        }
        if (pt.getY() == 0 && direction == 0 ||
                pt.getX() == 9 && direction == 1 ||
                pt.getY() == 9 && direction == 2 ||
                pt.getX() == 0 && direction == 3) {
            if (foundDirection) {
                direction = (direction + 2) % 4;
                lastPoint = firstPoint;
            } else {
                direction++;
                direction = direction % 4;
            }
            computerTurn(pt);
            return;
        }
        if (userBattleField.field[pt.getX() + i][pt.getY() + j] == CellState.SHIP) {
            shipHandler(pt, i, j);
        } else if (userBattleField.field[pt.getX() + i][pt.getY() + j] == CellState.REGULAR) {
            regularHandler(pt, i, j);
        } else if (userBattleField.field[pt.getX() + i][pt.getY() + j] == CellState.CHECKED) {
            checkedHandler();
            computerTurn(pt);
        }
    }

    void shipHandler(Point pt, int i, int j) {
        foundDirection = true;
        userBattleField.field[pt.getX() + i][pt.getY() + j] = CellState.DESTROYED;
        Ship sh = userBattleField.getShip(pt);
        sh.removePoint();
        lastPoint = new Point(pt.getX() + i, pt.getY() + j);
        if (sh.isDestroyed()) {
            Set<Point> cords = sh.getRadiusCords();
            checkNear = false;
            for (Point cord : cords) {
                userBattleField.field[cord.getX()][cord.getY()] = CellState.CHECKED;
            }
            direction = 0;
            foundDirection = false;
        }
    }

    void regularHandler(Point pt, int i, int j) {
        userBattleField.field[pt.getX() + i][pt.getY() + j] = CellState.CHECKED;
        if (foundDirection) {
            direction = (direction + 2) % 4;
            lastPoint = firstPoint;
        } else {
            direction++;
            direction = direction % 4;
        }
    }

    void checkedHandler() {
        if (foundDirection) {
            direction = (direction + 2) % 4;
            lastPoint = firstPoint;
        } else {
            direction++;
            direction = direction % 4;
        }
    }

    void computerTurn() {
        Random rnd = new Random();
        int x;
        int y;
        do {
            x = rnd.nextInt(10);
            y = rnd.nextInt(10);
        } while (userBattleField.field[x][y] == CellState.CHECKED ||
                userBattleField.field[x][y] == CellState.DESTROYED);
        if (userBattleField.field[x][y] == CellState.SHIP) {
            userBattleField.field[x][y] = CellState.DESTROYED;
            Ship ship = userBattleField.getShip(new Point(x, y));
            ship.removePoint();
            if (ship.isDestroyed()) {
                Set<Point> cords = ship.getRadiusCords();
                checkNear = false;
                for (Point cord : cords) {
                    userBattleField.field[cord.getX()][cord.getY()] = CellState.CHECKED;
                }
            } else {
                lastPoint = new Point(x, y);
                firstPoint = new Point(x, y);
                checkNear = true;
            }
        } else if (userBattleField.field[x][y] == CellState.REGULAR) {
            userBattleField.field[x][y] = CellState.CHECKED;
        }
    }

    boolean checkEndGame(BattleField battleField) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (battleField.field[i][j] == CellState.SHIP) {
                    return false;
                }
            }
        }
        return true;
    }

    public void pressUserCell(MouseEvent mouseEvent) {
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}