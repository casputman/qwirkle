package qwirkle.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import qwirkle.player.HumanPlayer;
import qwirkle.player.Player;

public class Game {

	private /* @ spec_public @ */ Board board;
	private /* @ spec_public @ */ Bag bag;
	private /* @ spec_public @ */ int player;
	private /* @ spec_public @ */ ArrayList<Player> players = new ArrayList<Player>();
	public /* @ spec_public @ */ Player current;
	private /* @ spec_public @ */ Rules rules;

	private boolean running;
	public boolean offline;

	/**
	 * Constructs a new game object with 4 players.
	 * 
	 * @param one,
	 *            player 1.
	 * @param two,
	 *            player 2.
	 * @param three
	 *            player 3.
	 * @param four
	 *            player 4.
	 */
	// @ensures players.size() != null;
	// @ensures player = 0;
	public Game(Player one, Player two, Player three, Player four) {
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

	/**
	 * Constructs a new game object with 2 players.
	 * 
	 * @param one,
	 *            player 1.
	 * @param two,
	 *            player 2.
	 */
	// @ensures players.size() != null;
	// @ensures player = 0;
	public Game(Player one, Player two) {
		board = new Board();
		players.add(one);
		players.add(two);
		bag = new Bag();
		player = 0;
		rules = new Rules(this);
		firstTiles();
	}

	/**
	 * Constructs a new game object with 3 players.
	 * 
	 * @param one,
	 *            player 1.
	 * @param two,
	 *            player 2.
	 * @param three
	 *            player 3.
	 */
	// @ensures players.size() != null;
	// @ensures player = 0;
	public Game(Player one, Player two, Player three) {
		board = new Board();
		players.add(one);
		players.add(two);
		players.add(three);
		bag = new Bag();
		player = 0;
		rules = new Rules(this);
		firstTiles();
	}

	/**
	 * Distributes the first tiles to the players once the game has begun.
	 */
	// @ensures players.get(i).getHand() != null;
	private void firstTiles() {
		System.err.println("Distributing tiles");
		for (int i = 0; i < players.size(); i++) {
			giveTiles(players.get(i));
		}
	}

	/**
	 * Starts the game by determining who the first player is, and making it
	 * possible for the player to make a move. Also checks if there is already a
	 * winner and prints out the winner if necessary.
	 */
	public void run() {
		running = true;
		this.nextPlayer();
		makeMoves(current.determineMove(this));
		board.printBoard();

		if (this.offline) {
			while (!this.getRules().hasWinner() && getRunning()) {
				makeMoves(current.determineMove(this));
				board.printBoard();
			}
			if (this.getRules().hasWinner()) {
				for (Player player : players) {
					if (this.getRules().isWinner(player)) {
						System.out.println(player + " wins!");
					}
				}
			}
		}
	}

	/**
	 * Stops the game instance.
	 */
	// @ensures running == false;
	public void stopGame() {
		System.err.println("Ending game");
		running = false;
	}

	/**
	 * Returns the status of the game.
	 */
	// @pure
	public boolean getRunning() {
		return running;
	}

	/**
	 * Sends a move to the board so the move will be placed on the board.
	 * 
	 * @param x,
	 *            the x coordinate of the turn.
	 * @param y,
	 *            the y coordinate of the turn.
	 * @param tile,
	 *            the tile that wants to be placed.
	 * @return the move that's gonna be made.
	 */
	// @requires x != null;
	// @requires y != null;
	// @requires tile != null;
	public boolean takeTurn(int x, int y, Tile tile) {
		String coords = Board.makeString(x, y);
		return getBoard().makeMove(coords, tile, this);
	}

	/**
	 * Returns the board object.
	 * 
	 * @return board;
	 */
	// @pure
	public Board getBoard() {
		return this.board;
	}

	/**
	 * Determines and sets who the next player is going to be. Does this by
	 * first checking who the current player is, and giving him his tiles. Then
	 * checks how many players are in the game, and determines the next (or
	 * first) player using an algorithm
	 */
	// @ensures player == \old player +1
	// @requires current != null;
	public void nextPlayer() {
		if (current != null) {
			giveTiles(current);
		}
		if (current != null || players.get(player).equals(current)) {
			player += 1;
			player %= players.size();
			current = players.get(player);
		} else if (current == null) {
			int random = (int) (Math.random() * players.size());
			current = players.get(random);
		} else {
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).equals(current)) {
					i += 1;
					i %= players.size();
					current = players.get(i);
				}
			}
		}
	}

	/**
	 * Fills the hand of the player with tiles from the bag.
	 * 
	 * @param player,
	 *            the player whose hand is begin filled.
	 */
	// @requires player != null;
	// @ensures player.getHand().size() == 6;
	public void giveTiles(Player player) {
		while (player.getHand().size() < 6) {
			player.addTile(bag.drawTile());
		}
	}

	/**
	 * Resets the board to the beginning state.
	 */
	// @ensures running == true;
	// @ensures current == null;
	public void reset() {
		board.reset();
		running = true;
		current = null;
		board.printBoard();
		nextPlayer();
	}

	/**
	 * Returns the bag object.
	 * 
	 * @return bag;
	 */
	// @pure
	public Bag getBag() {
		return this.bag;
	}

	// @requires player != null;
	// @requires index < player.getHand().size();
	public void takeTile(Player player, int index) {
		player.takeTile(index);
	}

	/**
	 * Returns the rules object.
	 * 
	 * @return rules;
	 */
	// @pure
	public Rules getRules() {
		return rules;
	}

	/**
	 * Calculates the score based on moves made using the rules defined by the
	 * rules class. Updates the score with points earned during this turn.
	 * 
	 * @param moves,
	 *            the moves (being) made.
	 * @return score;
	 */
	// @requires moves != null
	// @ensures score >= 0;
	// @ensures score = \old score + points
	public int calculateScore(Map<String, Tile> moves) {
		int score = 0;
		Set<String> coords = moves.keySet();
		for (String coord : coords) {
			board.makeMove(coord, moves.get(coord), this);
		}
		// Now we have a board with all the moves made
		// We can use this board to compare with the board before the moves were
		// made
		Map<String, Tile> newBoard = board.getTiles();
		Set<String> newBoardCoords = newBoard.keySet();
		Set<String> newCoords = moves.keySet();
		// Now we know which tiles have been put down this turn.
		Map<String, int[]> surroundings = Board.surrounding(newBoard, newBoardCoords);
		for (String coord : newCoords) {
			int[] xy = Board.splitString(coord);
			if (surroundings.get(coord)[0] != 0) {
				for (int i = 1; i < 6; i++) {
					String temp = Board.makeString(xy[0] + i, xy[1]);
					if (!newBoardCoords.contains(temp) || newBoard.get(temp).equals(null)) {
						int points = 0;
						for (int j = 0; j > -6; j--) {
							String temp2 = Board.makeString(Board.splitString(temp)[0] + j, Board.splitString(temp)[1]);
							if (newBoardCoords.contains(temp2) && newBoard.get(temp2) != null
									&& surroundings.get(temp2)[0] != 0) {
								points += 1;
								int[] takeX = surroundings.get(temp2);
								takeX[0] = 0;
								surroundings.put(temp2, takeX);
							}
						}
						if (points == 6) {
							points += 6;
						}
						score += points;
						break;
					}
				}
			}
			if (surroundings.get(coord)[1] != 0) {
				for (int i = 1; i < 6; i++) {
					String temp = Board.makeString(xy[0], xy[1] + i);
					if (!newBoardCoords.contains(temp) || newBoard.get(temp).equals(null)) {
						int points = 0;
						for (int j = 0; j > -6; j--) {
							String temp2 = Board.makeString(Board.splitString(temp)[0], Board.splitString(temp)[1] + j);
							if (newBoardCoords.contains(temp2) && newBoard.get(temp2) != null
									&& surroundings.get(temp2)[1] != 0) {
								points += 1;
								int[] takeY = surroundings.get(temp2);
								takeY[1] = 0;
								surroundings.put(temp2, takeY);
							}
						}
						if (points == 6) {
							points += 6;
						}
						score += points;
						break;
					}
				}
			}
		}
		return score;
	}

	/**
	 * Checks if the player wants to swap a tile. If he wishes to do this, the
	 * selected tile is removed from his hand and a new tile is being drawed
	 * from the bag (if the bag is not empty ofcourse)
	 * 
	 * @param moves,
	 *            the String which contains the SWAP request.
	 * @return success;
	 */
	// @requires moves != null;
	// @ensures \result != null;
	public boolean makeMoves(Map<String, Tile> moves) {
		boolean succes = true;
		Set<String> coords = moves.keySet();
		boolean swap = false;
		while (true) {
			for (String coord : coords) {
				if (coord.contains("SWAP")) {
					current.getHand().remove(moves.get(coord));
					current.getHand().add(bag.swapTile(moves.get(coord)));
					swap = true;
				}
			}
			if (swap) {
				break;
			}
			int score = calculateScore(moves);
			current.addScore(score);
			for (String coord : coords) {
				if (takeTurn(Board.splitString(coord)[0], Board.splitString(coord)[1], moves.get(coord))) {
				} else {
					succes = false;
				}
			}
			break;
		}
		Board.clear();
		nextPlayer();
		if (current.getClass() == HumanPlayer.class) {
			current.printScore();
		}
		return succes;
	}

	/**
	 * Returns the list of player objects.
	 * 
	 * @return players.
	 */
	// @pure
	public ArrayList<Player> getPlayers() {
		return this.players;
	}
}
