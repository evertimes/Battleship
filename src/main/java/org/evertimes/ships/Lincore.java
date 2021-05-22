package org.evertimes.ships;

import org.evertimes.Point;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Lincore implements Ship {
    int size = 4;
    List<Point> points;

    Lincore(List<Point> points,int size) {
        this.points = points;
    }

    @Override
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
                filter(i -> i.getX() > 0 && i.getY() >0 && i.getX()<10 && i.getY()<10)
                .collect(Collectors.toSet());
        return radiusCords;
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }
}
