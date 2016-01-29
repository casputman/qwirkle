package qwirkle.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import qwirkle.core.Tile;
import qwirkle.core.Tile.Color;
import qwirkle.core.Tile.Shape;
import qwirkle.player.HumanPlayer;

public class Board {

	public static final /* @ spec_public @ */ String EMPTY = "               ";

	private /* @ spec_public @ */ Map<String, Tile> tiles;

	/**
	 * Constructs a new board object.
	 */
	public Board() {
		tiles = new HashMap<>();
		reset();
	}

	/**
	 * Checks if the move the player wants to do is a valid move and is allowed,
	 * and continues doing this move. It takes a tile from the hand of the
	 * player to do so. If the move is invalid, returns false;
	 * 
	 * @param coords,
	 *            the coordinates that are being used to make a move.
	 * @param tile,
	 *            the tile that is being placed on the board sing the coords.
	 * @param game,
	 *            the game that uses the board.
	 * @return moveMade;
	 */
	// @requires game.current.getHand() != null;
	public boolean makeMove(String coords, Tile tile, Game game) {
		boolean moveMade = true;
		if (game.getRules().isMoveAllowed(coords, tile)) {
			tiles.put(coords, tile);
			int tileNumber = 0;
			for (int i = 0; i < game.current.getHand().size(); i++) {
				if (game.current.getHand().get(i).equals(tile)) {
					tileNumber = i;
					break;
				}
			}
			if (game.current.getClass().equals(HumanPlayer.class)) {
				game.takeTile(game.current, tileNumber);
			}
		} else {
			moveMade = false;
		}
		return moveMade;
	}

	/**
	 * Creates a Tile object from integers passed by the user.
	 * 
	 * @param x,
	 *            the integer corresponding to a color.
	 * @param y,
	 *            the integer corresponding to a shape.
	 * @return tile;
	 */
	// @requires int x != null;
	// @requires int y != null;
	// @ensures \result != null;
	public Tile makeTile(int x, int y) {
		Shape shape = Shape.values()[x];
		Color color = Color.values()[y];
		Tile tile = new Tile(shape, color);
		return tile;
	}

	/**
	 * Clears the board.
	 */
	// @ensures tiles.size() == 0;
	public void reset() {
		tiles.clear();
	}

	/**
	 * Checks what the limits of putting a tile on the board are. Must be done
	 * because the board size is dynamic.
	 * 
	 * @param input,
	 *            map which contains the tile object.
	 * @return limits;
	 */
	// @requires input != null;
	// @ensures \result != null;
	public static int[] getLimits(Map<String, Tile> input) {
		int[] limits = new int[4];
		for (String coords : input.keySet()) {
			int[] coord = splitString(coords);
			// Onderste Y
			limits[0] = Math.min(limits[0], coord[1]);
			// Meest linkse X
			limits[1] = Math.min(limits[1], coord[0]);
			// Bovenste Y
			limits[2] = Math.max(limits[2], coord[1]);
			// Meest rechtse X
			limits[3] = Math.max(limits[3], coord[0]);
		}
		return limits;
	}

	/**
	 * Prints a String representation of the board.
	 */
	public void printBoard() {
		System.out.println(toString());
	}

	/**
	 * Generates a String representation of the board, to be printed by
	 * printBoard();.
	 */
	// @ensures \result != null;
	public String toString() {
		String toReturn = "";
		int[] limits = getLimits(this.tiles);
		for (int i = limits[2]; i >= limits[0]; i--) {
			toReturn += i;
			for (int k = Integer.toString(i).length(); k < 4; k++) {
				toReturn += " ";
			}
			for (int j = limits[1]; j <= limits[3]; j++) {
				String coords = makeString(j, i);
				if (tiles.containsKey(coords)) {
					toReturn = toReturn + tiles.get(coords).toString() + " ";
				} else {
					toReturn = toReturn + EMPTY;
				}
			}
			toReturn = toReturn + "\n";
		}
		toReturn += "    ";
		for (int x = limits[1]; x <= limits[3]; x++) {
			toReturn += " ";
			if (x >= 0) {
				toReturn += " ";
			}
			toReturn += x;
			toReturn += "            ";
			if (x > -10 && x < 10) {
				toReturn += " ";
			}
		}
		return toReturn + "\n-------------------------------------------------------\n";
	}

	/**
	 * Returns tile objects used in various methods.
	 * 
	 * @return tiles;
	 */
	// @pure
	public Map<String, Tile> getTiles() {
		return tiles;
	}

	/**
	 * Creates a String from parameters x and y which can be processed by
	 * makeMove().
	 * 
	 * @param x,
	 *            the x coordinate.
	 * @param y,
	 *            the y coordinate.
	 * @return stringCoords;
	 */
	// @pure
	public static String makeString(int x, int y) {
		return x + "," + y;
	}

	/**
	 * Splits the input string on "," so it can be further processed as
	 * individual objects.
	 * 
	 * @param coords,
	 *            the input that consists of two integers seperated by a comma.
	 * @return toReturn;
	 */
	// @pure
	// @requires coords != null;
	public static int[] splitString(String coords) {
		int[] toReturn = new int[2];
		String[] coord = coords.split(",");
		toReturn[0] = Integer.parseInt(coord[0]);
		toReturn[1] = Integer.parseInt(coord[1]);
		return toReturn;
	}

	/**
	 * Checks whether and which tiles are surrounding the given coordinates.
	 * 
	 * @param tiles,
	 *            the collection of tile objects.
	 * @param coords,
	 *            the collection of String coordinates on the board.
	 * @return surroundings;
	 */
	// @requires tiles != null;
	// @requires coords != null;
	// @ensures \result != null;
	public static Map<String, int[]> surrounding(Map<String, Tile> tiles, Set<String> coords) {
		Map<String, int[]> surroundings = new HashMap<String, int[]>();
		Set<String> boardCoords = tiles.keySet();
		for (String coord : coords) {
			int[] surround = new int[3];
			int surroundX = 0;
			int surroundY = 0;
			int[] xy = Board.splitString(coord);
			for (int i = -1; i < 2; i += 2) {
				if (boardCoords.contains(Board.makeString(xy[0] + i, xy[1]))) {
					surroundX = 1;
				}
			}
			for (int i = -1; i < 2; i += 2) {
				if (boardCoords.contains(Board.makeString(xy[0], xy[1] + i))) {
					surroundY = 1;
				}
			}
			surround[0] = surroundX;
			surround[1] = surroundY;
			surround[2] = surroundX + surroundY;
			surroundings.put(coord, surround);
		}
		return surroundings;
	}

	public static void clear() {
		System.out.println("\n----------------------------------------------------------------------\n");
	}

}
