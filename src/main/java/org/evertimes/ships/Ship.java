package org.evertimes.ships;

import org.evertimes.Point;

import java.util.List;
import java.util.Set;

public interface Ship {
    Set<Point> getRadiusCords();
    boolean isDestroyed();
}
