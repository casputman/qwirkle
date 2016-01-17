package qwirkle.player;

import java.util.ArrayList;
import java.util.Map;

import qwirkle.core.Game;
import qwirkle.core.Tile;

public abstract class Player {

	public String name;
	public int score;

	public Player(){
		score = 0;
	}
	
	public Player(String name){
		this.name = name;
		score = 0;
	}

	//Sets score to old score + new score
	public void setScore(int score, int scoreAdd){
		this.score = score;
		score = score + scoreAdd;
	}
	
	public void addScore(int scoreAdd){
		setScore(score, scoreAdd);
	}
	
	//Creates a new Arraylist called hand, 
	//which automatically gets filled when the hand doesn't contain 6 tiles.
	private ArrayList<Tile> hand = new ArrayList<Tile>();
	
	//Takes a tile from the hand
	public void takeTile(int tile){
		hand.get(tile);
	}
	//Adds a tile to his hand
	public void addTile(Tile tile){
		hand.add(tile);
	}
	
	public ArrayList<Tile> getHand(){
		return hand;
	}
	
	public abstract Map<String, Tile> determineMove(Game game);
}
