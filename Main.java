package Battleships;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;

public class Main extends Application {
    public static final int TILE_SIZE = 40;

    private static int HEIGHT;
    private static int WIDTH;
    public static final int GAP_BETWEEN_BOARDS = 10;

    private boolean playersTurn = true;

    private Scene scene;

    private Tile[][] enemyBoard;

    private Tile[][] board;

    private Group tileGroup = new Group();
    private Group enemyTileGroup = new Group();
    private Group shipGroup = new Group();
    private Group shotResultsGroup = new Group();

    private Stack<ShipSegment[]> shipsToPlace = new Stack<>();

    private int numberOfShipSegments = 0;

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    private Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(TILE_SIZE * WIDTH * 2 + GAP_BETWEEN_BOARDS - 10,TILE_SIZE * HEIGHT - 10);


        root.getChildren().addAll(tileGroup,enemyTileGroup,shipGroup,shotResultsGroup);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile(x,y,true);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);

                Tile enemyTile = new Tile(x ,y,false);
                enemyBoard[x][y] = enemyTile;
                enemyTileGroup.getChildren().add(enemyTile);
            }
        }
        return root;
    }

    @Override
    public void start(Stage stage){
        Alert hostOrClientAlert = new Alert(Alert.AlertType.CONFIRMATION);
        hostOrClientAlert.setTitle("Battleships");
        hostOrClientAlert.setHeaderText("Launch client or server?");
        ButtonType clientButton = new ButtonType("Client");
        ButtonType serverButton = new ButtonType("Server");
        hostOrClientAlert.getButtonTypes().setAll(clientButton, serverButton);
        Optional<ButtonType> hostOrClientResult = hostOrClientAlert.showAndWait();

        if(hostOrClientResult.get() == serverButton){
            Alert chooseSizeAlert = new Alert(Alert.AlertType.CONFIRMATION);
            chooseSizeAlert.setTitle("Battleships");
            chooseSizeAlert.setHeaderText("Choose board size");
            ButtonType button7 = new ButtonType("7x7");
            ButtonType button10 = new ButtonType("10x10");
            ButtonType button12 = new ButtonType("12x12");
            chooseSizeAlert.getButtonTypes().setAll(button7, button10, button12);
            Optional<ButtonType> chooseSizeResult = chooseSizeAlert.showAndWait();

            if(chooseSizeResult.get() == button7){
                WIDTH = HEIGHT =  7;
                int[] shipsSizes = {1,2,3,4};
                setShipsToPlace(shipsSizes);
            }

            else if(chooseSizeResult.get() == button10){
                WIDTH = HEIGHT =  10;
                int[] shipsSizes = {2,2,2,3,3,4,5};
                setShipsToPlace(shipsSizes);
            }

            else if(chooseSizeResult.get() == button12){
                WIDTH = HEIGHT =  12;
                int[] shipsSizes = {1,1,1,2,2,3,3,3,4,4,5};
                setShipsToPlace(shipsSizes);
            }
            //TODO: setup server for client to connect
        }
        else if(hostOrClientResult.get() == clientButton){
            //TODO: connect with server
        }




        board = new Tile[WIDTH][HEIGHT];
        enemyBoard = new Tile[WIDTH][HEIGHT];

        stage.setTitle("Place ship of size: " + Integer.toString(shipsToPlace.peek().length));
        stage.setResizable(false);
        this.scene = new Scene(createContent());

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.R) {
                    Random random = new Random();
                    boolean isHorizontal;
                    int x;
                    int y;

                    while(!shipsToPlace.isEmpty()){
                        do{
                            isHorizontal = random.nextBoolean();
                            x = random.nextInt(WIDTH);
                            y = random.nextInt(HEIGHT);
                        }while(!canShipBePlaced(shipsToPlace.peek(),x,y,isHorizontal));
                        placeShip(shipsToPlace.pop(),x,y,isHorizontal);
                    }
                    stage.setTitle("All ships placed");
                    enemyBoard = board.clone();//TODO: remove after implementing socket connection

                }
            }
        });
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if(!shipsToPlace.empty()){
                    int x = (int)event.getX() / TILE_SIZE;
                    int y = (int)event.getY() / TILE_SIZE;
                    boolean isHorizontal;
                    if(event.getButton() == MouseButton.PRIMARY) isHorizontal = true;
                    else if(event.getButton() == MouseButton.SECONDARY) isHorizontal = false;
                    else return;
                    if(canShipBePlaced(shipsToPlace.peek(),x,y,isHorizontal)){
                        placeShip(shipsToPlace.pop(),x,y,isHorizontal);
                    }

                    if(!shipsToPlace.empty())
                        stage.setTitle("Place ship of size: " + Integer.toString(shipsToPlace.peek().length));
                    else{
                        stage.setTitle("All ships placed");
                        enemyBoard = board.clone();//TODO: remove after implementing socket connection
                    }
                }
                else if(playersTurn && (int)event.getX() > (WIDTH*TILE_SIZE) + GAP_BETWEEN_BOARDS){
                    int x = ((int)event.getX() - (WIDTH*TILE_SIZE) - GAP_BETWEEN_BOARDS) / TILE_SIZE;
                    int y = (int)event.getY()  / TILE_SIZE;
                    //System.out.println(x + ", " + y);
                    shot(x, y);
                }

////////////////////////////////////////////////////////////////
//                for(int i = 0; i < WIDTH; i++){
//                    for(int j = 0; j < HEIGHT; j++){
//                        if(enemyBoard[j][i].isHasShip())
//                            System.out.print(1 + " ");
//                        else
//                            System.out.print(0 + " ");
//                    }
//                    System.out.println();
//                }
//                System.out.println();
////////////////////////////////////////////////////////////////
            }
        });
        stage.setScene(scene);
        stage.show();

    }

    private void setShipsToPlace(int[] shipsSizes){
        for(int i:shipsSizes){
            shipsToPlace.push(new ShipSegment[i]);
            numberOfShipSegments += i;
        }
    }

    private boolean canShipBePlaced(ShipSegment[] ship, int x, int y, boolean isHorizontal){
        for(int i = 0; i < ship.length; i++){
            if(isHorizontal){
                if(x+i >= 0 && y >= 0 && x+i< WIDTH && y < HEIGHT){
                    if(!board[x+i][y].isAvailable()) return false;
                }
                else return false;
            }
            else{
                if(x >= 0 && y+i >= 0 && x < WIDTH && y+i < HEIGHT){
                    if(!board[x][y+i].isAvailable()) return false;
                }
                else return false;
            }
        }
        return true;
    }

    private void setUnavailable(int x, int y){
        if(x >= 0 && y >= 0 && x < WIDTH && y < HEIGHT)
            board[x][y].setAvailable(false);
    }

    private void shot(int x, int y){
        if(!enemyBoard[x][y].isAlreadyClicked()) {
            if (enemyBoard[x][y].getHasShip()) {
                System.out.println("hit!");
                showShotResultOnBoard(true,false,x,y);

                numberOfShipSegments--;
                if(numberOfShipSegments == 0){
                    System.out.println("You won!");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("You won!");
                    alert.setHeaderText("Congratulations, you are the winner!");
                    alert.showAndWait();
                }
                //enemyBoard[x][y].setFill(Color.RED);//fixme: color of the tile is changed, but it doesn't appear on screen
            }
            else {
                System.out.println("miss!");
                showShotResultOnBoard(false,false,x,y);
            }
            enemyBoard[x][y].setAlreadyClicked(true);
        }
        else
            System.out.println("You have already shot this tile!");
    }

    private void showShotResultOnBoard(boolean shipSegmentDestroyed, boolean isItFriendlyBoard, int x, int y){
        Rectangle rectangle = new Rectangle(TILE_SIZE,TILE_SIZE);
        rectangle.setStroke(Color.GRAY);
        rectangle.setStrokeWidth(2);
        if(shipSegmentDestroyed)
            rectangle.setFill(Color.RED);
        else
            rectangle.setFill(Color.WHITE);
        if(isItFriendlyBoard)
            rectangle.relocate(x * TILE_SIZE, y * TILE_SIZE);
        else
            rectangle.relocate((x+ Main.getWIDTH()) * Main.TILE_SIZE + Main.GAP_BETWEEN_BOARDS , y * Main.TILE_SIZE);

        shotResultsGroup.getChildren().add(rectangle);

    }
    //TODO: connectToServer; setupStreams; whilePlaying

    private void placeShip(ShipSegment[] ship, int x, int y, boolean isHorizontal){

        for(int i = 0; i < ship.length; i++){
            if(isHorizontal) {
                ship[i] = new ShipSegment(x + i, y ,true);
                board[x+i][y].setHasShip(true);

                setUnavailable(x+i-1, y-1);
                setUnavailable(x+i, y-1);
                setUnavailable(x+i+1, y-1);
                setUnavailable(x+i-1, y);
                setUnavailable(x+i, y);
                setUnavailable(x+i+1, y);
                setUnavailable(x+i-1, y+1);
                setUnavailable(x+i, y+1);
                setUnavailable(x+i+1, y+1);
            }
            else{
                ship[i] = new ShipSegment(x, y + i ,true);
                board[x][y+i].setHasShip(true);
                setUnavailable(x-1, y+i-1);
                setUnavailable(x, y+i-1);
                setUnavailable(x+1, y+i-1);
                setUnavailable(x-1, y+i);
                setUnavailable(x, y+i);
                setUnavailable(x+1, y+i);
                setUnavailable(x-1, y+i+1);
                setUnavailable(x, y+i+1);
                setUnavailable(x+1, y+i+1);
            }
            shipGroup.getChildren().add(ship[i]);
        }
    }
    public static void main(String[] args){
        launch(args);
    }
}