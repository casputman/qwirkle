package qwirkle.player;

import java.util.ArrayList;
import java.util.Map;

import qwirkle.core.Board;
import qwirkle.core.Game;
import qwirkle.core.Tile;

public abstract class Player {

	public String name;
	public int score;

	public Player() {
		score = 0;
	}

	public Player(String name) {
		this.name = name;
		score = 0;
	}

	// Sets score to old score + new score
	public void setScore(int oldScore, int scoreAdd) {
		score = oldScore + scoreAdd;
	}

	public void addScore(int scoreAdd) {
		setScore(score, scoreAdd);

	}

	// Creates a new Arraylist called hand,
	// which automatically gets filled when the hand doesn't contain 6 tiles.
	private ArrayList<Tile> hand = new ArrayList<Tile>() {
		public String toString() {
			String toReturn = "";
			for (int i = 0; i < hand.size(); i++) {
				int j = i + 1;
				toReturn += j + ": " + hand.get(i) + "\n";
			}
			return toReturn;
		}

	};

	public void printScore() {
		String score = "Current Score: " + this.score + "|";
		String line = "";
		for (int i = 1; i < score.length(); i++) {
			line += "-";
		}
		line += "|";
		System.out.println(line + "\n" + score + "\n" + line + "\n");
	}

	// Takes a tile from the hand
	public void takeTile(int tile) {
		hand.get(tile);
	}

	// Adds a tile to his hand
	public void addTile(Tile tile) {
		hand.add(tile);
	}

	public ArrayList<Tile> getHand() {
		return hand;
	}

	public abstract Map<String, Tile> determineMove(Game game);

	public String getName() {
		return name;
	}

	public static Player createPlayer(String name) {
		return (new HumanPlayer(name));
	}
}
