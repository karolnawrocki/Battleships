package Battleships;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

class Tile extends Rectangle implements Serializable{

    private boolean hasShip;

    boolean isAvailable() {
        return available;
    }

    void setAvailable(boolean available) {
        this.available = available;
    }

    private boolean available;

    boolean getHasShip() {
        return hasShip;
    }

    void setHasShip(boolean hasShip) {
        this.hasShip = hasShip;
    }

    private boolean alreadyClicked;

    boolean isAlreadyClicked() {
        return alreadyClicked;
    }

    void setAlreadyClicked(boolean alreadyClicked) {
        this.alreadyClicked = alreadyClicked;
    }

    Tile(int x, int y, boolean isFriendly){
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