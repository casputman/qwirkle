package qwirkle.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import qwirkle.player.Player;

public class Game {
	private Board board;
	
	private Bag bag;
	private int player;
	private Player[] players = new Player[4];
	public Player current;
	private Rules rules;
	
	
	private boolean running;
	public boolean offline;
	
	public Game(Player one, Player two, Player three, Player four){
		board = new Board();
		players[0] = one;
		players[1] = two;
		players[2] = three;
		players[3] = four;
		bag = new Bag();
		player = 0;
		rules = new Rules(this);
	}
	
	public void run(){
		running = true;
		board.printBoard();
		this.nextPlayer();
	}
	
	public void stopGame(){
		running = false;
	}
	
	public boolean getRunning(){
		return running;
	}
	
	public boolean takeTurn(int x, int y, Tile tile){
		String coords =	Board.makeString(x, y);
		return getBoard().makeMove(coords,tile, this);
	}
	
	public Board getBoard(){
		return this.board;
	}
	
	public void nextPlayer(){
		giveTiles(current);
		if(players[player].equals(current) || !current.equals(null)){
			player += 1;
			player %= 4;
			current = players[player];
		} else if (current.equals(null)){
			int random = (int )(Math.random() * 4 + 1);
			current = players[random];
		} else {
			for (int i = 0; i < players.length; i++){
				if(players[i].equals(current)){
					i += 1;
					i %= 4;
					current = players[i];
				}
			}
		}
	}
	
	public void giveTiles(Player player){
		while(player.getHand().size() < 6){
			giveTile(player);
		}
	}
	
	public void reset(){
		board.reset();
		running = true;
		current = null;
		board.printBoard();
		nextPlayer();
	}
	
	public Bag getBag(){
		return this.bag;
	}
	
	public void giveTile(Player player){
		ArrayList<Tile> tempHand = player.getHand();
		for (int i = 0; i < tempHand.size(); i++){
			player.addTile(bag.drawTile());
		}
	}
	
	public void takeTile(Player player, int index){
		player.takeTile(index);
	}
	
	public Rules getRules(){
		return rules;
	}
	
	public int calculateScore(Map<String, Tile> moves){
		int score = 0;
		Board copyBoard = this.getBoard();
		Set<String> coords = moves.keySet();
		for(String coord: coords){
			copyBoard.makeMove(coord, moves.get(coord), this);
		}
		//Now we have a board with all the moves made
		//We can use this board to compare with the board before the moves were made
		Map<String, Tile> newBoard = copyBoard.getTiles();
		Set<String> newBoardCoords = newBoard.keySet();
		Set<String> newCoords = moves.keySet();
		//Now we know which tiles have been put down this turn.
		Map<String, Integer> surroundings = Board.surrounding(newBoard, newCoords);
		for(String coord : newCoords){
			int[]xy = Board.splitString(coord);
			for(int i = 1; i < 6; i++){
				//TODO deze fucking bullshit
			}
		}
		return score;
	}
	
	public boolean makeMoves(Map<String, Tile> moves){
		boolean succes = true;
		current.addScore(calculateScore(moves));
		Set<String> coords = moves.keySet();
		for(String coord: coords){
			if(takeTurn(Board.splitString(coord)[0], Board.splitString(coord)[1], moves.get(coord))){
				
			} else {
				succes = false;
			}
		}
		return succes;
	}
}