package org.evertimes.ships;

import org.evertimes.BattleField;
import org.evertimes.Point;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Ship {
    public static final int LINCORE_SIZE = 4;
    public static final int CRUISER_SIZE = 3;
    public static final int DESTROYERS_SIZE = 2;
    public static final int BOAT_SIZE = 1;

    private final Set<Point> points;
    private int currentSize;

    public Ship(Set<Point> points, int size) {
        this.points = points;
        this.currentSize = size;
    }

    public Set<Point> getRadiusCords() {
        Set<Point> radiusCords = new HashSet<>();
        for (Point point : points) {
            radiusCords.add(new Point(point.getX(), point.getY() + 1));
            radiusCords.add(new Point(point.getX(), point.getY() - 1));
            radiusCords.add(new Point(point.getX() + 1, point.getY()));
            radiusCords.add(new Point(point.getX() - 1, point.getY()));
            radiusCords.add(new Point(point.getX() + 1, point.getY() + 1));
            radiusCords.add(new Point(point.getX() - 1, point.getY() - 1));
            radiusCords.add(new Point(point.getX() + 1, point.getY() - 1));
            radiusCords.add(new Point(point.getX() - 1, point.getY() + 1));
        }
        points.forEach(radiusCords::remove);
        radiusCords = radiusCords.stream().
                filter(i -> i.getX() >= 0 &&
                        i.getY() >= 0 &&
                        i.getX() < BattleField.FIELD_SIZE &&
                        i.getY() < BattleField.FIELD_SIZE
                ).collect(Collectors.toSet());
        return radiusCords;
    }

    public void removePoint() {
        currentSize--;
    }

    public boolean isDestroyed() {
        return currentSize == 0;
    }

    public boolean isContains(Point point) {
        return points.contains(point);
    }

}
