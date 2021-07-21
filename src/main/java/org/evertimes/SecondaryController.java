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
    private static final int CELL_SIZE = 50;
    public static final int DIRECTION_UP_CODE = 0;
    public static final int DIRECTION_RIGHT_CODE = 1;
    public static final int DIRECTION_DOWN_CODE = 2;
    public static final int DIRECTION_LEFT_CODE = 3;
    public Canvas userField;
    public Canvas computerField;
    private BattleField userBattleField;
    private BattleField computerBattleField;
    private boolean checkNear;
    private boolean foundDirection;
    private Point lastPoint;
    private Point firstPoint;
    private int direction;

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
        for (int i = 0; i < BattleField.FIELD_SIZE; i++) {
            for (int j = 0; j < BattleField.FIELD_SIZE; j++) {
                canvas.getGraphicsContext2D().setFill(cs[i][j].getColor());
                canvas.getGraphicsContext2D().fillRect(CELL_SIZE * i, CELL_SIZE * j, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    void drawComputerShips(BattleField battleField, Canvas canvas) {
        CellState[][] cs = battleField.getField();
        for (int i = 0; i < BattleField.FIELD_SIZE; i++) {
            for (int j = 0; j < BattleField.FIELD_SIZE; j++) {
                if (cs[i][j] == CellState.CHECKED || cs[i][j] == CellState.DESTROYED) {
                    canvas.getGraphicsContext2D().setFill(cs[i][j].getColor());
                    canvas.getGraphicsContext2D().fillRect(CELL_SIZE * i, CELL_SIZE * j, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    void drawGrid(Canvas canvas) {
        canvas.getGraphicsContext2D().setStroke(Color.BLACK);
        for (int i = 0; i < BattleField.FIELD_SIZE + 1; i++) {
            canvas.getGraphicsContext2D().strokeLine(CELL_SIZE * i, 0, CELL_SIZE * i, CELL_SIZE * BattleField.FIELD_SIZE);
            canvas.getGraphicsContext2D().strokeLine(0, CELL_SIZE * i, CELL_SIZE * BattleField.FIELD_SIZE, CELL_SIZE * i);
        }
    }


    public void pressComputerCell(MouseEvent mouseEvent) {
        int x = (int) Math.round(mouseEvent.getX());
        int y = (int) Math.round(mouseEvent.getY());
        x = x / CELL_SIZE;
        y = y / CELL_SIZE;
        int state = fireCell(x, y);
        if (state == -1) {
            return;
        } else if (state == 1) {
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
        alert.setTitle("Game over");
        if (isWin) {
            alert.setHeaderText("You win :)");
        } else {
            alert.setHeaderText("You loose :(");
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
                computerBattleField.field[x][y] == CellState.DESTROYED) {
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
            case DIRECTION_UP_CODE:
                j = -1;
                break;
            case DIRECTION_RIGHT_CODE:
                i = 1;
                break;
            case DIRECTION_DOWN_CODE:
                j = 1;
                break;
            case DIRECTION_LEFT_CODE:
                i = -1;
                break;
        }
        if (pt.getY() == 0 && direction == DIRECTION_UP_CODE ||
                pt.getX() == 9 && direction == DIRECTION_LEFT_CODE ||
                pt.getY() == 9 && direction == DIRECTION_DOWN_CODE ||
                pt.getX() == 0 && direction == DIRECTION_LEFT_CODE) {
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
            x = rnd.nextInt(BattleField.FIELD_SIZE);
            y = rnd.nextInt(BattleField.FIELD_SIZE);
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
        for (int i = 0; i < BattleField.FIELD_SIZE; i++) {
            for (int j = 0; j < BattleField.FIELD_SIZE; j++) {
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
