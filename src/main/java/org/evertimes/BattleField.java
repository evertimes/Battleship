package org.evertimes;

import org.evertimes.ships.Ship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BattleField {
    CellState[][] field = new CellState[10][10];
    ArrayList<Ship> ships = new ArrayList();
    private int lincoreCounter = 0;
    private int cruiserCounter = 0;
    private int destroyersCounter = 0;
    private int boatCounter = 0;

    BattleField() {
        for (CellState[] row : field) {
            Arrays.fill(row, CellState.REGULAR);
        }
    }
    public int getShipsCount(){
        return lincoreCounter+cruiserCounter+destroyersCounter+boatCounter;
    }

    public ArrayList<Ship> getShips() {
        return ships;
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

    boolean addShip(int x, int y, int size, int direction) {
        if (checkCanAdd(x, y, size, direction)) {
            if (size == 4 && lincoreCounter < 1) {
                lincoreCounter++;
            } else if (size == 3 && cruiserCounter < 2) {
                cruiserCounter++;
            } else if (size == 2 && destroyersCounter < 3) {
                destroyersCounter++;
            } else if (size == 1 && boatCounter < 4) {
                boatCounter++;
            } else {
                return false;
            }
            Set<Point> pointSet = new HashSet<>();
            if (direction == 0) {
                for (int i = 0; i < size; i++) {
                    pointSet.add(new Point(x, y - i));
                    field[x][y - i] = CellState.SHIP;
                }
            } else if (direction == 2) {
                for (int i = 0; i < size; i++) {
                    pointSet.add(new Point(x, y + i));
                    field[x][y + i] = CellState.SHIP;
                }
            } else if (direction == 3) {
                for (int i = 0; i < size; i++) {
                    pointSet.add(new Point(x - i, y));
                    field[x - i][y] = CellState.SHIP;
                }
            } else if (direction == 1) {
                for (int i = 0; i < size; i++) {
                    pointSet.add(new Point(x + i, y));
                    field[x + i][y] = CellState.SHIP;
                }
            }
            ships.add(new Ship(pointSet, size));
            return true;
        }else{
            return false;
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
            if (x == 0 && y == 0) {
                return (field[x + 1][y] == CellState.REGULAR &&
                        field[x][y + 1] == CellState.REGULAR &&
                        field[x + 1][y + 1] == CellState.REGULAR);
            } else if (x == 9 && y == 9) {
                return (field[x - 1][y] == CellState.REGULAR &&
                        field[x][y - 1] == CellState.REGULAR &&
                        field[x - 1][y - 1] == CellState.REGULAR);
            } else if (x == 0 && y == 9) {
                return (field[x + 1][y] == CellState.REGULAR &&
                        field[x][y - 1] == CellState.REGULAR &&
                        field[x + 1][y - 1] == CellState.REGULAR);
            }  else if (x == 9 && y == 0) {
                return (field[x - 1][y] == CellState.REGULAR &&
                        field[x][y + 1] == CellState.REGULAR &&
                        field[x - 1][y + 1] == CellState.REGULAR);
            }  else if (x == 0) {
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
            } else if (x == 9) {
                return (field[x - 1][y] == CellState.REGULAR &&
                        field[x][y + 1] == CellState.REGULAR &&
                        field[x][y - 1] == CellState.REGULAR &&
                        field[x - 1][y - 1] == CellState.REGULAR &&
                        field[x - 1][y + 1] == CellState.REGULAR);
            } else if (y == 9) {
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
