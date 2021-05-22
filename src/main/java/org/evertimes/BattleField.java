package org.evertimes;

import java.util.Arrays;
import java.util.Random;

public class BattleField {
    CellState[][] field = new CellState[10][10];

    private int lincoreCounter = 0;
    private int cruiserCounter = 0;
    private int destroyersCounter = 0;
    private int boatCounter = 0;

    BattleField() {

        for (CellState[] row : field) {
            Arrays.fill(row, CellState.REGULAR);
        }
    }

    public void generateComputerBattleField() {
        var rnd = new Random();
        int x;
        int y;
        int direction;
        do {
            x = rnd.nextInt(9);
            y = rnd.nextInt(9);
            direction = rnd.nextInt(3);
        } while (checkCanAdd(x, y, 4, direction) != true);
        addShip(x, y, 4, direction);
        for (var i = 0; i < 2; i++) {
            do {
                x = rnd.nextInt(9);
                y = rnd.nextInt(9);
                direction = rnd.nextInt(3);
            } while (checkCanAdd(x, y, 3, direction) != true);
            addShip(x, y, 3, direction);
        }
        for (var i = 0; i < 3; i++) {
            do {
                x = rnd.nextInt(9);
                y = rnd.nextInt(9);
                direction = rnd.nextInt(3);
            } while (checkCanAdd(x, y, 2, direction) != true);
            addShip(x, y, 2, direction);
        }
        for (int i = 0; i < 4; i++) {
            do {
                x = rnd.nextInt(9);
                y = rnd.nextInt(9);
                direction = rnd.nextInt(3);
            } while (checkCanAdd(x, y, 1, direction) != true);
            addShip(x, y, 1, direction);
        }
    }

    void addShip(int x, int y, int size, int direction) {
        if (checkCanAdd(x, y, size, direction)) {
            if (size == 4 && lincoreCounter < 1) {
                //size = 4;
                lincoreCounter++;
            } else if (size == 3 && cruiserCounter < 2) {
                //size = 3;
                cruiserCounter++;
            } else if (size == 2 && destroyersCounter < 3) {
                //size = 2;
                destroyersCounter++;
            } else if (size == 1 && boatCounter < 4) {
                //size = 1;
                boatCounter++;
            } else {
                return;
            }
            if (direction == 0) {
                for (int i = 0; i < size; i++) {
                    field[x][y - i] = CellState.SHIP;
                }
            } else if (direction == 2) {
                for (int i = 0; i < size; i++) {
                    field[x][y + i] = CellState.SHIP;
                }
            } else if (direction == 3) {
                for (int i = 0; i < size; i++) {
                    field[x - i][y] = CellState.SHIP;
                }
            } else if (direction == 1) {
                for (int i = 0; i < size; i++) {
                    field[x + i][y] = CellState.SHIP;
                }
            }
        }
    }

    boolean checkCanAdd(int x, int y, int size, int direction) {
        if (size > 0) {
            try {
                if (checkRadius(x, y)) {
                    switch (direction) {
                        case 0:
                            return checkCanAdd(x, y - 1, size - 1, direction);
                        case 1:
                            return checkCanAdd(x + 1, y, size - 1, direction);
                        case 2:
                            return checkCanAdd(x, y + 1, size - 1, direction);
                        case 3:
                            return checkCanAdd(x - 1, y, size - 1, direction);
                    }
                } else {
                    return false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
            }
        }
        return true;
    }

    boolean checkRadius(int x, int y) {
        try {
            return (field[x + 1][y] == CellState.REGULAR &&
                    field[x - 1][y] == CellState.REGULAR &&
                    field[x][y + 1] == CellState.REGULAR &&
                    field[x][y - 1] == CellState.REGULAR &&
                    field[x - 1][y - 1] == CellState.REGULAR &&
                    field[x + 1][y + 1] == CellState.REGULAR &&
                    field[x - 1][y + 1] == CellState.REGULAR &&
                    field[x + 1][y - 1] == CellState.REGULAR);
        } catch (ArrayIndexOutOfBoundsException e) {
            if (x == 0) {
                return (field[x + 1][y] == CellState.REGULAR &&
                        field[x][y + 1] == CellState.REGULAR &&
                        field[x][y - 1] == CellState.REGULAR &&
                        field[x + 1][y + 1] == CellState.REGULAR &&
                        field[x + 1][y - 1] == CellState.REGULAR);
            } else if (y == 0) {
                return (field[x + 1][y] == CellState.REGULAR &&
                        field[x - 1][y] == CellState.REGULAR &&
                        field[x][y + 1] == CellState.REGULAR &&
                        field[x + 1][y + 1] == CellState.REGULAR &&
                        field[x - 1][y + 1] == CellState.REGULAR);
            }else{
                return false;
            }

        }
    }

    public CellState[][] getField() {
        return field;
    }
}
