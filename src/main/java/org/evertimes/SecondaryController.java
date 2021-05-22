package org.evertimes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SecondaryController implements Initializable {
    public Canvas userField;
    public Canvas computerField;
    BattleField userBattleField;
    BattleField computerBattleField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userBattleField = App.getBattleField();
        computerBattleField = new BattleField();
        computerBattleField.generateComputerBattleField();
        CellState[][] cs = computerBattleField.field;
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

    List<String> getCords(int x, int y) {
        ArrayList<String> cords = new ArrayList<>();
        cords.add(x + "" + y);
        for (int i = 1; i < 3; i++) {
            if (computerBattleField.field[x + i][y] == CellState.DESTROYED ||
                    computerBattleField.field[x + i][y] == CellState.SHIP) {
                cords.add(x + "" + y);
            } else {
                break;
            }
        }
        for (int i = 1; i < 3; i++) {
            if (computerBattleField.field[x - i][y] == CellState.DESTROYED
                    ||
                    computerBattleField.field[x - i][y] == CellState.SHIP) {
                cords.add(x + "" + y);
            } else {
                break;
            }
        }
        for (int i = 1; i < 3; i++) {
            if (computerBattleField.field[x][y + 1] == CellState.DESTROYED ||
                    computerBattleField.field[x][y + 1] == CellState.SHIP) {
                cords.add(x + "" + y);
            } else {
                break;
            }
        }
        for (int i = 1; i < 3; i++) {
            if (computerBattleField.field[x][y - 1] == CellState.DESTROYED ||
                    computerBattleField.field[x][y-1] == CellState.SHIP) {
                cords.add(x + "" + y);
            } else {
                break;
            }
        }

        return cords;
    }

    public void pressComputerCell(MouseEvent mouseEvent) {
        int x = (int) Math.round(mouseEvent.getX());
        int y = (int) Math.round(mouseEvent.getY());
        x = x / 50;
        y = y / 50;
        int state = fireCell(x, y);
        if (state == 1) {
            List<String> cords = getCords(x, y);
            for (String cord : cords) {
                int localx = Integer.parseInt(cord.substring(0, 1));
                int localy = Integer.parseInt(cord.substring(1, 2));
                if (computerBattleField.field[localx + 1][localy] != CellState.DESTROYED) {
                    computerBattleField.field[localx + 1][localy] = CellState.CHECKED;
                }
                if (computerBattleField.field[localx - 1][localy] != CellState.DESTROYED) {
                    computerBattleField.field[localx - 1][localy] = CellState.CHECKED;
                }
                if (computerBattleField.field[localx][localy + 1] != CellState.DESTROYED) {
                    computerBattleField.field[localx][localy + 1] = CellState.CHECKED;
                }
                if (computerBattleField.field[localx][localy - 1] != CellState.DESTROYED) {
                    computerBattleField.field[localx][localy - 1] = CellState.CHECKED;
                }
                /*if(computerBattleField.field[localx+1][localy-1] != CellState.DESTROYED){
                    computerBattleField.field[localx+1][localy-1] = CellState.CHECKED;
                }
                if(computerBattleField.field[localx-1][localy-1] != CellState.DESTROYED){
                    computerBattleField.field[localx-1][localy-1] = CellState.CHECKED;
                }
                if(computerBattleField.field[localx][localy-1] != CellState.DESTROYED){
                    computerBattleField.field[localx][localy-1] = CellState.CHECKED;
                }*/
                computerBattleField.field[localx + 1][localy - 1] = CellState.CHECKED;
                computerBattleField.field[localx + 1][localy + 1] = CellState.CHECKED;
                computerBattleField.field[localx - 1][localy + 1] = CellState.CHECKED;
                computerBattleField.field[localx - 1][localy - 1] = CellState.CHECKED;

            }
        }
        drawComputerShips(computerBattleField, computerField);
        drawGrid(computerField);
        if (checkEndGame(computerBattleField)) {
            userField.getGraphicsContext2D().clearRect(0, 0, 500, 500);
            userField.getGraphicsContext2D().setStroke(Color.BLACK);
            computerField.getGraphicsContext2D().setStroke(Color.BLACK);
            userField.getGraphicsContext2D().setFont(Font.font(java.awt.Font.SANS_SERIF, 25));
            userField.getGraphicsContext2D().strokeText("You win", 50, 250);
            computerField.getGraphicsContext2D().setFont(Font.font(java.awt.Font.SANS_SERIF, 25));
            computerField.getGraphicsContext2D().clearRect(0, 0, 500, 500);
            computerField.getGraphicsContext2D().strokeText("Computer loose", 50, 250);
            return;
        }
        computerTurn();
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

    public int fireCell(int x, int y) {
        if (computerBattleField.field[x][y] == CellState.SHIP) {
            computerBattleField.field[x][y] = CellState.DESTROYED;
            if (computerBattleField.field[x + 1][y] == CellState.SHIP ||
                    computerBattleField.field[x - 1][y] == CellState.SHIP ||
                    computerBattleField.field[x][y + 1] == CellState.SHIP ||
                    computerBattleField.field[x][y - 1] == CellState.SHIP) {
                return 0;
            } else {
                return 1;
            }
        } else if (computerBattleField.field[x][y] == CellState.REGULAR) {
            computerBattleField.field[x][y] = CellState.CHECKED;
        }
        return 2;
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