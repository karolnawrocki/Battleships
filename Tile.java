package Battleships;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {

    private boolean hasShip;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    private boolean available;

    public boolean getHasShip() {
        return hasShip;
    }

    public void setHasShip(boolean hasShip) {
        this.hasShip = hasShip;
    }

    private boolean alreadyClicked;

    public boolean isAlreadyClicked() {
        return alreadyClicked;
    }

    public void setAlreadyClicked(boolean alreadyClicked) {
        this.alreadyClicked = alreadyClicked;
    }

    public Tile(int x, int y, boolean isFriendly){
        setWidth(Main.TILE_SIZE);
        setHeight(Main.TILE_SIZE);
        this.hasShip = false;
        this.available = true;
        this.alreadyClicked = false;
        if(isFriendly){
            relocate(x * Main.TILE_SIZE, y * Main.TILE_SIZE);
            setFill((x + y) % 2 == 0 ? Color.LIGHTSLATEGRAY : Color.SLATEGRAY);
        }
        else{
            relocate((x+ Main.getWIDTH()) * Main.TILE_SIZE + Main.GAP_BETWEEN_BOARDS , y * Main.TILE_SIZE);
            setFill((x + y) % 2 == 0 ? Color.LIGHTSLATEGRAY.darker() : Color.SLATEGRAY.darker());
        }


    }
}