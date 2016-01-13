package qwirkle;

import java.util.ArrayList;

public class Player {

	public String name;
	public int score;

	public Player(String name){
		this.name = name;
		score = 0;
	}

	//Sets score to old score + new score
	public void setScore(int score, int scoreAdd){
		this.score = score;
		score = score + scoreAdd;
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
}
