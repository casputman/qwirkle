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
	private ArrayList<Player> players = new ArrayList<Player>();
	public Player current;
	private Rules rules;
	
	
	private boolean running;
	public boolean offline;
	
	public Game(Player one, Player two, Player three, Player four){
		board = new Board();
		players.add(one);
		players.add(two);
		players.add(three);
		players.add(four);
		bag = new Bag();
		player = 0;
		rules = new Rules(this);
		firstTiles();
	}
	
	public Game(Player one, Player two){
		board = new Board();
		players.add(one);
		players.add(two);
		bag = new Bag();
		player = 0;
		rules = new Rules(this);
		firstTiles();
	}

	public Game(Player one, Player two, Player three){
		players.add(one);
		players.add(two);
		players.add(three);
		bag = new Bag();
		player = 0;
		rules = new Rules(this);
		firstTiles();
	}
	
	private void firstTiles() {
		System.err.println("Distributing tiles");
		for (int i = 0; i < players.size(); i++){
		giveTiles(players.get(i));
		}
	}

	public void run(){
		running = true;
		this.nextPlayer();
		makeMoves(current.determineMove(this));
		board.printBoard();
		
		if(this.offline){
			while(!this.getRules().hasWinner() && getRunning()){
				this.nextPlayer();
				makeMoves(current.determineMove(this));
				board.printBoard();
			}
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
		if(current != null || players.get(player).equals(current)){
			player += 1;
			player %= players.size();
			System.err.println("player: " + current);
			//System.err.println("player1: " + players.get(player + 1));
			current = players.get(player);
		} else if (current == null){
			int random = (int )(Math.random() * players.size());
			current = players.get(random);
		} else {
			for (int i = 0; i < players.size(); i++){
				if(players.get(i).equals(current)){
					i += 1;
					i %= players.size();
					current = players.get(i);
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
		Set<String> coords = moves.keySet();
		for(String coord: coords){
			board.makeMove(coord, moves.get(coord), this);
		}
		//Now we have a board with all the moves made
		//We can use this board to compare with the board before the moves were made
		Map<String, Tile> newBoard = board.getTiles();
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
							String temp2 = Board.makeString(Board.splitString(temp)[0] + j, Board.splitString(temp)[1]);
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
							String temp2 = Board.makeString(Board.splitString(temp)[0], Board.splitString(temp)[1]+j);
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
		Board copyBoard = this.getBoard();
		for(String coord: coords){
			if(takeTurn(Board.splitString(coord)[0], Board.splitString(coord)[1], moves.get(coord))){
			} else {
				succes = false;
				board = copyBoard;
			}
		}
		return succes;
	}
	
	public ArrayList<Player> getPlayers(){
		return this.players;
	}
}
