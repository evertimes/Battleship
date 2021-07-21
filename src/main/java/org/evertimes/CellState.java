package org.evertimes;

import javafx.scene.paint.Color;

public enum CellState {
    REGULAR(Color.web("0x86BBD8")),
    SHIP(Color.web("0x000000")),
    DESTROYED(Color.web("0xE3170A")),
    CHECKED(Color.web("0x33658A"));
    private final Color color;

    CellState(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
