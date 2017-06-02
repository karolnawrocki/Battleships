package Battleships;

import java.io.Serializable;

class Coordinates implements Serializable {
    private int x;
    private int y;

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
