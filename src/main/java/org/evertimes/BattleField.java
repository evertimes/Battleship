package org.evertimes;

import org.evertimes.ships.Ship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class BattleField {
    public static final int FIELD_SIZE = 10;

    private static final int DIRECTION_UP_CODE = 0;
    private static final int DIRECTION_RIGHT_CODE = 1;
    private static final int DIRECTION_DOWN_CODE = 2;
    private static final int DIRECTION_LEFT_CODE = 3;

    public CellState[][] field = new CellState[FIELD_SIZE][FIELD_SIZE];
    private final List<Ship> ships = new ArrayList<>();
    private int lincoreCounter;
    private int cruiserCounter;
    private int destroyersCounter;
    private int boatCounter;

    BattleField() {
        for (CellState[] row : field) {
            Arrays.fill(row, CellState.REGULAR);
        }
    }

    public int getShipsCount() {
        return lincoreCounter + cruiserCounter + destroyersCounter + boatCounter;
    }

    public Ship getShip(Point point) {
        for (Ship sh : ships) {
            if (sh.isContains(point)) {
                return sh;
            }
        }
        return null;
    }

    public void generateBattleField() {
        var rnd = new Random();
        int x;
        int y;
        int direction;
        do {
            x = rnd.nextInt(FIELD_SIZE - 1);
            y = rnd.nextInt(FIELD_SIZE - 1);
            direction = rnd.nextInt(3);
        } while (!checkCanAdd(x, y, Ship.LINCORE_SIZE, direction));
        addShip(x, y, Ship.LINCORE_SIZE, direction);
        for (var i = 0; i < 2; i++) {
            do {
                x = rnd.nextInt(FIELD_SIZE - 1);
                y = rnd.nextInt(FIELD_SIZE - 1);
                direction = rnd.nextInt(3);
            } while (!checkCanAdd(x, y, Ship.CRUISER_SIZE, direction));
            addShip(x, y, Ship.CRUISER_SIZE, direction);
        }
        for (var i = 0; i < 3; i++) {
            do {
                x = rnd.nextInt(FIELD_SIZE - 1);
                y = rnd.nextInt(FIELD_SIZE - 1);
                direction = rnd.nextInt(3);
            } while (!checkCanAdd(x, y, Ship.DESTROYERS_SIZE, direction));
            addShip(x, y, Ship.DESTROYERS_SIZE, direction);
        }
        for (int i = 0; i < 4; i++) {
            do {
                x = rnd.nextInt(FIELD_SIZE - 1);
                y = rnd.nextInt(FIELD_SIZE - 1);
                direction = rnd.nextInt(3);
            } while (!checkCanAdd(x, y, Ship.BOAT_SIZE, direction));
            addShip(x, y, Ship.BOAT_SIZE, direction);
        }
    }

    boolean addShip(int x, int y, int size, int direction) {
        if (checkCanAdd(x, y, size, direction)) {
            if (size == Ship.LINCORE_SIZE && lincoreCounter < 1) {
                lincoreCounter++;
            } else if (size == Ship.CRUISER_SIZE && cruiserCounter < 2) {
                cruiserCounter++;
            } else if (size == Ship.DESTROYERS_SIZE && destroyersCounter < 3) {
                destroyersCounter++;
            } else if (size == Ship.BOAT_SIZE && boatCounter < 4) {
                boatCounter++;
            } else {
                return false;
            }
            Set<Point> pointSet = new HashSet<>();
            if (direction == DIRECTION_UP_CODE) {
                for (int i = 0; i < size; i++) {
                    pointSet.add(new Point(x, y - i));
                    field[x][y - i] = CellState.SHIP;
                }
            } else if (direction == DIRECTION_DOWN_CODE) {
                for (int i = 0; i < size; i++) {
                    pointSet.add(new Point(x, y + i));
                    field[x][y + i] = CellState.SHIP;
                }
            } else if (direction == DIRECTION_LEFT_CODE) {
                for (int i = 0; i < size; i++) {
                    pointSet.add(new Point(x - i, y));
                    field[x - i][y] = CellState.SHIP;
                }
            } else if (direction == DIRECTION_RIGHT_CODE) {
                for (int i = 0; i < size; i++) {
                    pointSet.add(new Point(x + i, y));
                    field[x + i][y] = CellState.SHIP;
                }
            }
            ships.add(new Ship(pointSet, size));
            return true;
        } else {
            return false;
        }
    }

    boolean checkCanAdd(int x, int y, int size, int direction) {
        if (size > 0) {
            try {
                if (checkRadius(x, y)) {
                    switch (direction) {
                        case DIRECTION_UP_CODE:
                            return checkCanAdd(x, y - 1, size - 1, direction);
                        case DIRECTION_RIGHT_CODE:
                            return checkCanAdd(x + 1, y, size - 1, direction);
                        case DIRECTION_DOWN_CODE:
                            return checkCanAdd(x, y + 1, size - 1, direction);
                        case DIRECTION_LEFT_CODE:
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
            if (x == 0 && y == 0) {
                return (field[x + 1][y] == CellState.REGULAR &&
                        field[x][y + 1] == CellState.REGULAR &&
                        field[x + 1][y + 1] == CellState.REGULAR);
            } else if (x == FIELD_SIZE - 1 && y == FIELD_SIZE - 1) {
                return (field[x - 1][y] == CellState.REGULAR &&
                        field[x][y - 1] == CellState.REGULAR &&
                        field[x - 1][y - 1] == CellState.REGULAR);
            } else if (x == 0 && y == FIELD_SIZE - 1) {
                return (field[x + 1][y] == CellState.REGULAR &&
                        field[x][y - 1] == CellState.REGULAR &&
                        field[x + 1][y - 1] == CellState.REGULAR);
            } else if (x == FIELD_SIZE - 1 && y == 0) {
                return (field[x - 1][y] == CellState.REGULAR &&
                        field[x][y + 1] == CellState.REGULAR &&
                        field[x - 1][y + 1] == CellState.REGULAR);
            } else if (x == 0) {
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
            } else if (x == FIELD_SIZE - 1) {
                return (field[x - 1][y] == CellState.REGULAR &&
                        field[x][y + 1] == CellState.REGULAR &&
                        field[x][y - 1] == CellState.REGULAR &&
                        field[x - 1][y - 1] == CellState.REGULAR &&
                        field[x - 1][y + 1] == CellState.REGULAR);
            } else if (y == FIELD_SIZE - 1) {
                return (field[x + 1][y] == CellState.REGULAR &&
                        field[x - 1][y] == CellState.REGULAR &&
                        field[x][y - 1] == CellState.REGULAR &&
                        field[x - 1][y - 1] == CellState.REGULAR &&
                        field[x + 1][y - 1] == CellState.REGULAR);
            } else {
                return false;
            }

        }
    }

    public CellState[][] getField() {
        return field;
    }
}
