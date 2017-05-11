package Battleships;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ShipSegment extends Rectangle {
    private boolean isAlive;
    public ShipSegment(int x, int y, boolean isFriendly){
        this.isAlive = true;
        setWidth(Main.TILE_SIZE);
        setHeight(Main.TILE_SIZE);
        this.setStroke(Color.GRAY);
        this.setStrokeWidth(0.5);
        relocate(x * Main.TILE_SIZE, y * Main.TILE_SIZE);
        setFill(Color.DARKGRAY);
    }
}