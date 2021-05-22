package org.evertimes;

import javafx.scene.paint.Color;

public enum CellState {
    REGULAR(Color.AQUA),
    SHIP(Color.GRAY),
    DESTROYED(Color.RED),
    CHECKED(Color.DARKBLUE);
    Color color;

    CellState(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
