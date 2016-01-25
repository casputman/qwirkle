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
		firstTiles();
	}
	
	private void firstTiles() {
		System.err.println("Distributing tiles");
		giveTiles(players[0]);
		giveTiles(players[1]);
		giveTiles(players[2]);
		giveTiles(players[3]);
	}

	public void run(){
		running = true;
		this.nextPlayer();
		current.determineMove(this);
		board.printBoard();
		this.nextPlayer();
		
		if(this.offline){
			current.determineMove(this);
		}
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
		if(current != null){
			giveTiles(current);
		}
		if(current != null || players[player].equals(current)){
			player += 1;
			player %= 4;
			current = players[player];
		} else if (current == null){
			int random = (int )(Math.random() * 4);
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
			player.addTile(bag.drawTile());
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
		Map<String, int[]> surroundings = Board.surrounding(newBoard, newCoords);
		for(String coord : newCoords){
			int[]xy = Board.splitString(coord);
			if(surroundings.get(coord)[0] != 0){
				for(int i = 1; i < 6; i++){
					String temp = Board.makeString(xy[0]+i, xy[1]);
					if(!newBoardCoords.contains(temp) || newBoard.get(temp).equals(null)){
						int points = 0;
						for(int j = 0; i > -6; i--){
							String temp2 = board.makeString(Board.splitString(temp)[0] + j, Board.splitString(temp)[1]);
							if(newBoardCoords.contains(temp2) && !newBoard.get(temp2).equals(null)){
								points += 1;
								int[] takeX = surroundings.get(temp2);
								takeX[0] = 0;
								surroundings.put(temp2, takeX);
							}
						}
						if(points == 6){
							points += 6;
						}
						score += points;
					}
				}
			if(surroundings.get(coord)[1] != 0){
				for(int i = 1; i < 6; i++){
					String temp = Board.makeString(xy[0], xy[1]+i);
					if(!newBoardCoords.contains(temp) || newBoard.get(temp).equals(null)){
						int points = 0;
						for(int j = 0; i > -6; i--){
							String temp2 = board.makeString(Board.splitString(temp)[0], Board.splitString(temp)[1]+j);
							if(newBoardCoords.contains(temp2) && !newBoard.get(temp2).equals(null)){
								points += 1;
								int[] takeX = surroundings.get(temp2);
								takeX[0] = 0;
								surroundings.put(temp2, takeX);
							}
						}
							if(points == 6){
								points += 6;
							}
							score += points;
						}
					}
				}
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
