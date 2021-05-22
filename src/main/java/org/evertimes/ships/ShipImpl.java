package org.evertimes.ships;

import org.evertimes.Point;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ShipImpl {
    private int size;
    private Set<Point> points;
    private int currentSize;

    public ShipImpl(Set<Point> points, int size) {
        this.points = points;
        this.size = size;
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
                filter(i -> i.getX() >= 0 && i.getY() >= 0 && i.getX() < 10 && i.getY() < 10)
                .collect(Collectors.toSet());
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

    public int getSize() {
        return size;
    }

    public int getCurrentSize() {
        return currentSize;
    }
}
