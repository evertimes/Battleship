package org.evertimes.ships;

import org.evertimes.Point;

import java.util.Set;

public class Boat implements Ship{
    @Override
    public Set<Point> getRadiusCords() {
        return null;
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }
}
