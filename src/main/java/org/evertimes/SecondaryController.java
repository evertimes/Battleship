package org.evertimes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.evertimes.ships.Ship;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
    Media sound;
    MediaPlayer mediaPlayer;

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
        if (state == 1) {
            Ship ship = computerBattleField.getShip(new Point(x, y));
            Set<Point> cords = ship.getRadiusCords();
            for (Point cord : cords) {
                computerBattleField.field[cord.getX()][cord.getY()] = CellState.CHECKED;
            }
        }
        drawComputerShips(computerBattleField, computerField);
        drawGrid(computerField);
        if (checkEndGame(computerBattleField)) {
            endGame();
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
            userField.getGraphicsContext2D().clearRect(0, 0, 500, 500);
            userField.getGraphicsContext2D().fillText("You loose", 50, 250);
            computerField.getGraphicsContext2D().clearRect(0, 0, 500, 500);
            computerField.getGraphicsContext2D().fillText("Computer wins", 50, 250);
            return;
        }
    }

    private void endGame() {
        userField.getGraphicsContext2D().clearRect(0, 0, 500, 500);
        userField.getGraphicsContext2D().setStroke(Color.BLACK);
        computerField.getGraphicsContext2D().setStroke(Color.BLACK);
        userField.getGraphicsContext2D().setFont(Font.font(java.awt.Font.SANS_SERIF, 25));
        userField.getGraphicsContext2D().strokeText("You win", 50, 250);
        computerField.getGraphicsContext2D().setFont(Font.font(java.awt.Font.SANS_SERIF, 25));
        computerField.getGraphicsContext2D().clearRect(0, 0, 500, 500);
        computerField.getGraphicsContext2D().strokeText("Computer loose", 50, 250);
    }

    public int fireCell(int x, int y) {
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
            direction++;
            direction = direction % 4;
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
        sound = new Media(new File("src/main/resources/org/evertimes/ff.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}