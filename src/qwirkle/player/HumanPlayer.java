package qwirkle.player;

import qwirkle.core.Game;
import qwirkle.core.Tile;
import qwirkle.protocol.Protocol;
import qwirkle.core.Board;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;


public class HumanPlayer extends Player {


    public HumanPlayer(String name) {
    	super(name);
    }
   
    @Override
    public Map<String, Tile> determineMove(Game game) {
        Board board = new Board();
        StartTimer timer = new StartTimer(board, this);
        timer.start();
            Map<String, Tile> moveMap = new HashMap<String, Tile>();
            System.out.println(this.name + ", please make your move (x -space- y -space- tile) ");
            System.out.println(getHand());
            String input = "";
    
            try {
                input = new BufferedReader(new InputStreamReader(System.in)).readLine().trim()
                        .toUpperCase();
            } catch (IOException e) {
                System.out.println("shit went down");
            }
            if ((input.startsWith("MOVE") && input.length() > 9) || input.length() == 7) {
                String coordinates = input.replace("MOVE ", "");
                String coordinatesArray[] = coordinates.split(Protocol.MESSAGESEPERATOR);
                int xCoordinate = Integer.parseInt(coordinatesArray[0]);
                int yCoordinate = Integer.parseInt(coordinatesArray[1]);
                int tileSelection = Integer.parseInt(coordinatesArray[2]);
                Tile tile = getHand().get(tileSelection);
                String parsedCoordinates  = Board.makeString(xCoordinate, yCoordinate);
                if (game.getRules().isMoveAllowed(parsedCoordinates, tile)) {
                    timer.done = true;
                    game.getBoard().makeMove(parsedCoordinates, tile, game);
                	getHand().remove(tileSelection);
                } else {
                    System.out.println("Invalid move, please try again\n");
                    this.determineMove(game);
                }
            }
			return moveMap;
        
}
     public class StartTimer extends Thread{
        
        public boolean done = false;
        private Board board;
        private Player player;
        private Game game;
        
        public StartTimer(Board board, Player player) {
            this.board = board;
            this.player = player;
        } 
        
        public void run() {
            
            try {
                Thread.sleep(10000);
                if (!done) {
                    Player hint = new ComputerPlayer("smart");
                    System.out.println("I would suggest this move: " 
                    +  hint.determineMove(game));
                    done = true;
                }
            } catch (InterruptedException e) {
               System.err.println("interrupted");
            }
        
        }
    }
}
