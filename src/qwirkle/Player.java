package qwirkle;

import java.util.ArrayList;

public class Player {

	public String name;
	public int score;

	public Player(String name, ArrayList<Tile> hand, int score){
		this.name = name;
		this.hand = hand;
		this.score = score;
	}

	//Sets score to old score + new score
	public int setScore(int score, int scoreAdd){
		this.score = score;
		score = score + scoreAdd;
	}
	//Creates a new Arraylist called hand, 
	//which automatically gets filled when the hand doesn't contain 6 tiles.
	private ArrayList<Tile> hand = new ArrayList<Tile>();{
		while (hand.size() < 6){
			hand.add(game.bag.drawTile());
		}
	}
}
